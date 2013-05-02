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