from django.http import HttpResponse
from similarity.models import Similarity
from authenticate.models import User
from similarity.ed import EditDistance
from similarity.ds import DempsterShafer
from datetime import date
import json, time, math

def process(request):
    start_time = time.clock()
    weightAttribute = {'userFoods' : 0.2, 'userDrinks' : 0.2, 'userBooks' : 0.1, 'userMovies' : 0.1, 'userOccupation' : 0.1, 'userDOB' : 0.2, 'userGender' : 0.1}
    similarityValue = {}
    
    response = ""
    for user1 in User.objects.select_related().all():
        user1Property = {}
        user1Property['userFoods'] = user1.userFoods.split(',')
        user1Property['userDrinks'] = user1.userDrinks.split(',')
        user1Property['userBooks'] = user1.userBooks.split(',')
        user1Property['userMovies'] = user1.userMovies.split(',')
        user1Property['userOccupation'] = user1.userOccupation
        user1Property['userDOB'] = str(user1.userDOB)
        user1Property['userGender'] = user1.userGender
        similarityValue[user1.userId] = {}
        
        for user2 in User.objects.select_related().all():
            simValue = 0
            user1Obj = User()
            user1Obj.userId = user1.userId
            
            user2Obj = User()
            user2Obj.userId = user2.userId
            
            if user1.userId == user2.userId:
                continue
             
            #if Similarity.objects.extra(where=["similarityUser1_id IN (%s, %s) AND similarityUser2_id IN (%s, %s)"], params=[user1.userId,user2.userId,user1.userId,user2.userId]).exists():
            if user1.userId in similarityValue:
                if user2.userId in similarityValue[user1.userId]:
                    continue
            
            if user2.userId in similarityValue:
                if user1.userId in similarityValue[user2.userId]:
                    continue
            
            user2Property = {}
            user2Property['userFoods'] = user2.userFoods.split(',')
            user2Property['userDrinks'] = user2.userDrinks.split(',')
            user2Property['userBooks'] = user2.userBooks.split(',')
            user2Property['userMovies'] = user2.userMovies.split(',')
            user2Property['userOccupation'] = user2.userOccupation
            user2Property['userDOB'] = str(user2.userDOB)
            user2Property['userGender'] = user2.userGender
        
            attributeValue = {}
            start_time = time.clock()
            response += user1.userAlias + " & " + user2.userAlias + "<br />"
            for keyProperty in user1Property:
                if keyProperty == 'userGender':
                    average = 1.0 if user1Property['userGender'] == user2Property['userGender'] else 0.0
                    method = "If-else"
                elif keyProperty == 'userDOB':
                    user1DOB = user1Property['userDOB'].split('-')
                    user2DOB = user2Property['userDOB'].split('-')

                    delta = date(int(user1DOB[0]), int(user1DOB[1]), int(user1DOB[2])) - date(int(user2DOB[0]), int(user2DOB[1]), int(user2DOB[2]))
                    #diff = float((float(delta.days) + 1825.0)/3650.0)
                    diff = float( (1825.0-float(math.fabs(delta.days)))/1825.0 )
                    average = 0.0 if diff < 0.0 else diff
                    method = "Difference"
                elif keyProperty == 'userOccupation':
                    ed = EditDistance(user1Property[keyProperty], user2Property[keyProperty])
                    average = ed.similarity2()
                    method = "EditDistance"
                else:
                    sum = 0
                    numOfItem = 0
                    
                    listTemp = []
                    for p1 in user1Property[keyProperty]:
                        for p2 in user2Property[keyProperty]:
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
                
                #newSim = average    
                #newSim = float( (2 * average * weightAttribute[keyProperty]) / (1 + (average * weightAttribute[keyProperty])) )
                newSim = average * weightAttribute[keyProperty]
                attributeValue[keyProperty] = newSim
                
                #response += keyProperty + " => " + method + " = " + str(newSim) + "<br />"
            
            '''
            ds = DempsterShafer()
            simValue = ds.process(attributeValue)
            #'''
            simValue = reduce(lambda x, y: x + y / float(len(attributeValue.values())), attributeValue.values(), 0)
            response += user1.userName + " & " + user2.userName + " simValue : " + str(simValue) + "<br />" 
            #response += str(time.clock() - start_time) + " seconds<br />"
            response += "<br/>"
            
            similarityValue[user1.userId][user2.userId] = simValue
            
            ''' insert similarity value to db
            sim = Similarity()
            sim.similarityUser1 = user1Obj
            sim.similarityUser2 = user2Obj
            sim.similarityValue = simValue
            sim.save()
            #'''
    response += str(time.clock() - start_time) + " seconds<br />"
    return HttpResponse(response)