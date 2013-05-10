from django.http import HttpResponse
from authenticate.models import User
import json, hashlib

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

