from django.http import HttpResponse
from place.models import Place
from place.ed import EditDistance
from type.models import Type
from authenticate.models import User
from review.models import Review
import json, time
from Levenshtein import *

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
    start_time = time.clock()
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
            
            for keyProperty in activeUserProperty:
                if keyProperty == 'userGender':
                    average = 1.0 if activeUserProperty['userGender'] == currenteUser['userGender'] else 0.0
                    response += keyProperty + " => EditDistance " + activeUser.userName + " ("+activeUserProperty[keyProperty]+") & " + r.reviewUser.userName + " ("+currenteUser[keyProperty]+") : " + str(sum) + "<br />"
                else:
                    if keyProperty in ('userOccupation','userDOB'):
                        numOfItem = 1
                        '''
                        ed = EditDistance(activeUserProperty[keyProperty], currenteUser[keyProperty])
                        sum = ed.similarity()
                        '''
                        ed = distance(activeUserProperty[keyProperty], currenteUser[keyProperty])
                        sum = 1 - (float(ed) / max(len(activeUserProperty[keyProperty]), len(currenteUser[keyProperty])))
                        
                        response += keyProperty + " => EditDistance " + activeUser.userName + " ("+activeUserProperty[keyProperty]+") & " + r.reviewUser.userName + " ("+currenteUser[keyProperty]+") : " + str(sum) + "<br />"
                    else:
                        sum = 0
                        numOfItem = 0
                        
                        listTemp = []
                        for p1 in activeUserProperty[keyProperty]:
                            for p2 in currenteUser[keyProperty]:
                                '''
                                ed = EditDistance(p1, p2)
                                edValue = ed.similarity()                                
                                '''
                                ed = distance(p1, p2)
                                edValue = 1 - (float(ed) / max(len(p1), len(p2)))
                                
                                sum += edValue
                                numOfItem += 1
                                strTemp = keyProperty + " => EditDistance " + activeUser.userName + " ("+p1+") & " + r.reviewUser.userName + " ("+p2+") : " + str(edValue) + "<br />"
                                
                                listTemp.append( (edValue, strTemp) )
                                
                        newList = sorted(listTemp, key=lambda attr: attr[0], reverse=True)
                        for l in range(len(newList)):
                            response += newList[l][1]
                                
                    average = sum / numOfItem
                response += keyProperty + " => Average " + activeUser.userName + " & " + r.reviewUser.userName + " : " + str(average) + "<br />"
        response += str(time.clock() - start_time) + " seconds"
    return HttpResponse(response)
    