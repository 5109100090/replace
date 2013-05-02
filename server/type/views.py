from django.http import HttpResponse
from type.models import Type
import json

def index(request):
    return HttpResponse("Hello")

def listAll(request):
    if request.method == "GET" :
        query = "SELECT type_type.typeId AS typeId, type_type.typeName AS typeName, COUNT(place_place.placeId) AS typeTotal \
                FROM place_place \
                RIGHT JOIN `type_type` ON type_type.typeId = place_place.placeType_id \
                GROUP BY type_type.typeId \
                ORDER BY type_type.typeId"
        types = Type.objects.raw(query)
        data = []
        for type in types:
            data.append({ 'typeId':type.typeId, 'typeName':type.typeName, 'typeTotal':type.typeTotal })
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")