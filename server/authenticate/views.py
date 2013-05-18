from django.http import HttpResponse
from authenticate.models import User
import json, hashlib, datetime

def index(request):
    return HttpResponse("Hello")

def login(request):
    if request.method == "POST" :
        userName = str(request.POST["userName"])
        userPassword = hashlib.md5(str(request.POST["userPassword"])).hexdigest()
        u = User.objects.get(userName=userName,userPassword=userPassword)
        data = [ { 'userId':u.userId, 'userAlias':u.userAlias } ]
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")
    
def detail(request):
    if request.method == "POST" :
        userId = str(request.POST["userId"])
        u = User.objects.get(userId=userId)
        data = [ { 'userName':u.userName, 'userPassword':u.userPassword, 'userAlias':u.userAlias, 'userFoods':u.userFoods, 'userDrinks':u.userDrinks, 'userBooks':u.userBooks, 'userMovies':u.userMovies, 'userMusics':u.userMusics, 'userGender':u.userGender, 'userOccupation':u.userOccupation, 'userDOB':str(u.userDOB)} ]
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")
    
def users(request):
    response = ""
    for user in User.objects.all():
        response += "<table border='1' width='100%'><tr><td colspan='2'>" + user.userAlias + "</td></tr>" \
        + "<tr><td>userFoods</td><td>" + user.userFoods + "</td></tr>" \
        + "<tr><td>userDrinks</td><td>" + user.userDrinks + "</td></tr>" \
        + "<tr><td>userBooks</td><td>" + user.userBooks + "</td></tr>" \
        + "<tr><td>userMovies</td><td>" + user.userMovies + "</td></tr>" \
        + "<tr><td>userOccupation</td><td>" + user.userOccupation + "</td></tr></table><br />"

    return HttpResponse(response)

def update(request):
    if request.method == "POST" :
        req = request.POST
        u = User.objects.get(userName=req['userName'])
        u.userPassword = hashlib.md5(str(req['userPassword'])).hexdigest()
        u.userFoods = req['userFoods']
        u.userDrinks = req['userDrinks']
        u.userBooks = req['userBooks']
        u.userMovies = req['userMovies']
        u.userGender = req['userGender']
        u.userOccupation = req['userOccupation']
        dob = req['userDOB'].split('-')
        u.userDOB = datetime.date(int(dob[0]), int(dob[1]), int(dob[2]))
        u.save()
        
        sp = SimilarityProcess()
        sp.processUser(u.userId)
        return HttpResponse("oke")
    else :
        return HttpResponse("what?")

def register(request):
    if request.method == "POST" :
        req = request.POST
        dob = req['userDOB'].split('-')
        u = User(userName=req['userName'], userAlias=req['userAlias'], userPassword=hashlib.md5(str(req['userPassword'])).hexdigest(), userFoods=req['userFoods'], userDrinks=req['userDrinks'], userBooks=req['userBooks'], userMovies=req['userMovies'], userGender=req['userGender'], userOccupation=req['userOccupation'], userDOB=datetime.date(int(dob[0]), int(dob[1]), int(dob[2])))
        u.save()
        
        user = User.objects.latest('userId')
        sp = SimilarityProcess()
        sp.processUser(user.userId)
        return HttpResponse("oke")
    else :
        return HttpResponse("what?")