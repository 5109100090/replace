from django.http import HttpResponse
from review.models import Review
from authenticate.models import User
from place.models import Place
import json

def index(request):
    return HttpResponse("Hello")
    
def listReviews(request):
    if request.method == "POST" :
        user = User()
        user.userId = int(request.POST["userId"])
        
        place = Place()
        place.placeId = int(request.POST["placeId"])
        
        data = []       
        for r in Review.objects.listReviews(user, place) :
            dict = {}
            dict['userAlias'] = r.reviewUser.userAlias
            dict['reviewPoint'] = str(r.reviewPoint)
            dict['similarityValue'] = str(r.similarityValue)
            dict['newSimilarityValue'] = str(r.newSimilarityValue)
            data.append(dict)
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")