from django.http import HttpResponse
from place.models import Place
from place.ed import EditDistance
from type.models import Type
from authenticate.models import User
from review.models import Review
import json, time, math
from datetime import date

def index(request):
    return HttpResponse("Hello")

def process(request):
    if request.method == "POST" :
        currentLat = str(request.POST["currentLat"])
        currentLng = str(request.POST["currentLng"])
        typeId = str(request.POST["typeId"])
        range = str(request.POST["range"])

        data = []
        type = Type()
        type.typeId = typeId
        for p in Place.objects.listInRange(currentLat, currentLng, type, range) :
            dict = {}
            dict['placeId'] = p.placeId
            dict['placeName'] = p.placeName
            dict['placeDesc'] = p.placeDesc
            dict['placeLat'] = p.placeLat
            dict['placeLng'] = p.placeLng
            dict['placeType'] = p.placeType.typeId
            dict['placeDistance'] = p.placeDistance
            data.append(dict)
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")
    
def getDetail(request):
    if request.method == "POST" :
        placeId = int(request.POST["placeId"])        
        place = Place.objects.get(pk=placeId)
        data = [ { 'placeId':str(place.placeId), 'placeName':place.placeName, 'placeDesc':place.placeDesc, 'placeLat':str(place.placeLat), 'placeLng':str(place.placeLng), 'typeId':str(place.placeType.typeId), 'typeName':place.placeType.typeName } ]
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")
    
def process1(request):
    response = ""
    activeUser = User.objects.get(pk=1)
    activeUserProperty = {}
    activeUserProperty['userFoods'] = activeUser.userFoods.split(',')
    activeUserProperty['userDrinks'] = activeUser.userDrinks.split(',')
    activeUserProperty['userBooks'] = activeUser.userBooks.split(',')
    activeUserProperty['userMovies'] = activeUser.userMovies.split(',')
    activeUserProperty['userMusics'] = activeUser.userMusics.split(',')
    activeUserProperty['userOccupation'] = activeUser.userOccupation
    activeUserProperty['userDOB'] = str(activeUser.userDOB)
    activeUserProperty['userGender'] = activeUser.userGender
    
    placeLat =  '-7.27957';
    placeLng =  '112.79751';
    type = Type()
    type.typeId = 1
    
    for p in Place.objects.listInRange(placeLat, placeLng, type, 1000) :
        response += "<h1>"+p.placeName+"</h1>"
        for r in Review.objects.filter(reviewPlace_id = p.placeId) :
            if activeUser.userId == r.reviewUser.userId:
                continue
            
            currenteUser = {}
            currenteUser['userFoods'] = r.reviewUser.userFoods.split(',')
            currenteUser['userDrinks'] = r.reviewUser.userDrinks.split(',')
            currenteUser['userBooks'] = r.reviewUser.userBooks.split(',')
            currenteUser['userMovies'] = r.reviewUser.userMovies.split(',')
            currenteUser['userMusics'] = r.reviewUser.userMusics.split(',')
            currenteUser['userOccupation'] = r.reviewUser.userOccupation
            currenteUser['userDOB'] = str(r.reviewUser.userDOB)
            currenteUser['userGender'] = r.reviewUser.userGender
            
            response += activeUser.userAlias + " & " + r.reviewUser.userAlias + "<br />"
            start_time = time.clock()
            for keyProperty in activeUserProperty:
                if keyProperty == 'userGender':
                    average = 1.0 if activeUserProperty['userGender'] == currenteUser['userGender'] else 0.0
                    method = "If-else"
                elif keyProperty == 'userDOB':
                    user1 = activeUserProperty['userDOB'].split('-')
                    user2 = currenteUser['userDOB'].split('-')

                    delta = date(int(user1[0]), int(user1[1]), int(user1[2])) - date(int(user2[0]), int(user2[1]), int(user2[2]))
                    #diff = float((float(delta.days) + 1825.0)/3650.0)
                    diff = float( (1825.0-float(math.fabs(delta.days)))/1825.0 )
                    average = 0.0 if diff < 0.0 else diff
                    method = "Difference"
                elif keyProperty == 'userOccupation':
                    ed = EditDistance(activeUserProperty[keyProperty], currenteUser[keyProperty])
                    average = ed.similarity2()
                    method = "EditDistance"
                else:
                    sum = 0
                    numOfItem = 0
                    
                    listTemp = []
                    for p1 in activeUserProperty[keyProperty]:
                        for p2 in currenteUser[keyProperty]:
                            ed = EditDistance(p1, p2)
                            edValue = ed.similarity2()
                            
                            sum += edValue
                            numOfItem += 1
                            strTemp = keyProperty + " => [EditDistance]  " + p1 + " & " + p2 + " = " + str(edValue) + "<br />"
                            
                            listTemp.append( (edValue, strTemp) )
                            
                    #for l in sorted(listTemp, key=lambda attr: attr[0], reverse=True):
                    #    response += l[1]
                            
                    average = sum / numOfItem
                    method = "Average EditDistance"
                    
                response += keyProperty + " => " + method + " = " + str(average) + "<br />"
                
            response += str(time.clock() - start_time) + " seconds<br />"
            response += "<br/>"
        
    return HttpResponse(response)
    