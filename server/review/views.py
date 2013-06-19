from django.http import HttpResponse
from review.models import Review
from authenticate.models import User
from place.models import Place
from similarity.sp import SimilarityProcess
from similarity.fuzzy import Fuzzy
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
        for r in Review.objects.filter(reviewPlace_id=place).exclude(reviewUser_id=user) :
            dict = {}
            dict['userId'] = r.reviewUser.userId
            dict['userAlias'] = r.reviewUser.userAlias
            dict['reviewPointPrice'] = r.reviewPointPrice
            dict['reviewPointService'] = r.reviewPointService
            dict['reviewPointLocation'] = r.reviewPointLocation
            dict['reviewPointCondition'] = r.reviewPointCondition
            dict['reviewPointComfort'] = r.reviewPointComfort
            dict['reviewText'] = r.reviewText
            average = float(r.reviewPointPrice+r.reviewPointService+r.reviewPointLocation+r.reviewPointCondition+r.reviewPointComfort) / 5
            dict['averagePoint'] = average
            sp = SimilarityProcess()
            dict['similarityValue'] = sp.process(User.objects.get(userId=user.userId), r.reviewUser, typeId)
            dict['newSimilarityValue'] = float(dict['similarityValue']) * average
            data.append(dict)
        
        #''' normalizing similarity on same place
        minV = min(data, key=lambda x:x['similarityValue'])
        maxV = max(data, key=lambda x:x['similarityValue'])
        minValue = minV['similarityValue']
        maxValue = maxV['similarityValue']    
        for dict in data:
            dict["similarityValue"] = float((dict["similarityValue"] - minValue)/(maxValue - minValue))
            dict['newSimilarityValue'] = dict['similarityValue'] * dict['averagePoint']
            dict['averagePoint'] = str(dict['averagePoint'])
            fuzzy = Fuzzy()
            dict['similarityFlag'] = str(fuzzy.process(dict['similarityValue']))
        #'''
            
        data = sorted(data, key=lambda rev: rev['newSimilarityValue'], reverse=True)
        return HttpResponse(json.dumps(data))
    else :
        return HttpResponse("what?")
    
def write(request):
    if request.method == "POST" :
        user = User()
        user.userId = int(request.POST["userId"])
        
        place = Place()
        place.placeId = int(request.POST["placeId"])
        
        review = Review()
        review.reviewUser = user
        review.reviewPlace = place
        review.reviewPointPrice = int(request.POST["reviewPointPrice"])
        review.reviewPointService = int(request.POST["reviewPointService"])
        review.reviewPointLocation = int(request.POST["reviewPointLocation"])
        review.reviewPointCondition = int(request.POST["reviewPointCondition"])
        review.reviewPointComfort = int(request.POST["reviewPointComfort"])
        review.reviewText = request.POST["reviewText"]
        review.save()

        return HttpResponse("oke")
    else :
        return HttpResponse("what?")
