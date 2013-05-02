from django.http import HttpResponse
from review.models import Review
from place.models import Place
import json

def index(request):
    return HttpResponse("Hello")
    
def listReviews(request):
    if request.method == "POST" :
        place = Place()
        place.placeId = int(request.POST["placeId"])
        
        data = []       
        for r in Review.objects.filter(reviewPlace_id=place) :
            dict = {}
            dict['userAlias'] = r.reviewUser.userAlias
            dict['reviewPoint'] = str(r.reviewPoint)
            data.append(dict)
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")