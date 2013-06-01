from django.http import HttpResponse
from review.models import Review
from authenticate.models import User
from place.models import Place
from similarity.sp import SimilarityProcess
import json

def index(request):
    return HttpResponse("Hello")
    
def listReviews(request):
    if request.method == "POST" :
        user = User()
        user.userId = int(request.POST["userId"])
        
        place = Place()
        place.placeId = int(request.POST["placeId"])
        
        typeId = int(request.POST["typeId"])
        
        data = []       
        # for r in Review.objects.listReviews(user, place) :
        for r in Review.objects.filter(reviewPlace_id=place).exclude(reviewUser_id=user) :
            dict = {}
            dict['userAlias'] = r.reviewUser.userAlias
            dict['reviewPointPrice'] = r.reviewPointPrice
            dict['reviewPointService'] = r.reviewPointService
            dict['reviewPointLocation'] = r.reviewPointLocation
            dict['reviewPointCondition'] = r.reviewPointCondition
            dict['reviewPointComfort'] = r.reviewPointComfort
            dict['reviewText'] = r.reviewText
            average = float(r.reviewPointPrice+r.reviewPointService+r.reviewPointLocation+r.reviewPointCondition+r.reviewPointComfort) / 5
            dict['averagePoint'] = str(average)
            sp = SimilarityProcess()
            dict['similarityValue'] = sp.process(User.objects.get(userId=user.userId), r.reviewUser, typeId)
            dict['newSimilarityValue'] = float(dict['similarityValue']) * average
            data.append(dict)
        data = sorted(data, key=lambda rev: rev['newSimilarityValue'], reverse=True)
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")
