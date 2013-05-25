from django.http import HttpResponse
from similarity.models import Similarity
from authenticate.models import User
from similarity.sp import SimilarityProcess
import json, time
from django.db.models import Q

def processAll(request):
    start_time = time.clock()
    similarityValue = {}
    
    response = ""
    for user1 in User.objects.select_related().all():
        similarityValue[user1.userId] = {}
        for user2 in User.objects.select_related().exclude(userId=user1.userId):
             
            #if Similarity.objects.extra(where=["similarityUser1_id IN (%s, %s) AND similarityUser2_id IN (%s, %s)"], params=[user1.userId,user2.userId,user1.userId,user2.userId]).exists():
            if user1.userId in similarityValue:
                if user2.userId in similarityValue[user1.userId]:
                    continue
            
            if user2.userId in similarityValue:
                if user1.userId in similarityValue[user2.userId]:
                    continue
            
            sp = SimilarityProcess()
            simValue = sp.process(user1, user2)
            
            response += user1.userName + " & " + user2.userName + " simValue : " + str(simValue) + "<br />" 
            similarityValue[user1.userId][user2.userId] = simValue
            
            ''' insert similarity value to db
            sim = Similarity()
            sim.similarityUser1 = user1
            sim.similarityUser2 = user2
            sim.similarityValue = simValue
            sim.save()
            #'''

    response += str(time.clock() - start_time) + " seconds<br />"
    return HttpResponse(response)

def processUser(request):
    if request.method == "POST" :
        userId = str(request.POST["userId"])
        sp = SimilarityProcess()
        sp.processUser(userId)
        return HttpResponse("")
    else :
        return HttpResponse("what?")