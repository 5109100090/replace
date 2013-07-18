from django.http import HttpResponse
from place.models import Place
import json

def index(request):
    return HttpResponse("Hello")

def process(request):
    if request.method == "POST" :
        currentLat = str(request.POST["currentLat"])
        currentLng = str(request.POST["currentLng"])
        typeId = str(request.POST["typeId"])
        range = str(request.POST["range"])
        placeTag = str(request.POST["placeTag"])

        data = []
        for p in Place.objects.listInRange(currentLat, currentLng, typeId, range, placeTag) :
            dict = {}
            dict['placeId'] = p.placeId
            dict['placeName'] = p.placeName
            dict['placeDesc'] = p.placeDesc
            dict['placeLat'] = p.placeLat
            dict['placeLng'] = p.placeLng
            dict['placeType'] = p.placeType.typeId
            dict['placeDistance'] = p.placeDistance
            dict['placeReviews'] = p.placeReviews
            dict['placeAddress'] = p.placeAddress
            dict['averagePoint'] = str(p.averagePoint)
            data.append(dict)
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")