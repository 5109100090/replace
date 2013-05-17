class DempsterShafer():
    def process(self, data, debug = False):
        before = {}
        for key in data:
            if(data[key] == -999):
                continue
            
            if(len(before) == 0):
                before[key] = data[key]
                before['t'] = 1 - data[key]
                continue
            
            newEvidence = {}
            newEvidence['key'] = key
            newEvidence['value'] = data[key]
            beforeCandidate = {}
            for keyBefore in before:
                newEvidenceSplit = newEvidence['key'].split(',')
                oldEvidenceSplit = keyBefore.split(',')
                
                intersection = [val for val in newEvidenceSplit if val in oldEvidenceSplit]
                seen = {}
                intersectionUnique = []
                for item in intersection:
                    if item in seen: continue
                    seen[item] = 1
                    intersectionUnique.append(item)
                
                keyCol1 = newEvidence['key'] if keyBefore == 't' else 'x' if len(intersectionUnique) == 0 else ','.join(intersectionUnique)
                keyCol2 = keyBefore
                valCol1 = before[keyBefore] * newEvidence['value']
                valCol2 = before[keyBefore] * (1 - newEvidence['value'])
                
                if keyCol1 in beforeCandidate:
                    beforeCandidate[keyCol1] += valCol1
                else:
                    beforeCandidate[keyCol1] = valCol1
                    
                if keyCol2 in beforeCandidate:
                    beforeCandidate[keyCol2] += valCol2
                else:
                    beforeCandidate[keyCol2] = valCol2
                
            x = beforeCandidate['x'] if 'x' in beforeCandidate else 0
            
            if x >= 1:
                return -999
            
            if x > 0 and x < 1:
                del beforeCandidate['x']
                #beforeCandidate = {(beforeCandidate[bf] / (1 - x)) for bf in beforeCandidate}
                
                for bf in beforeCandidate:
                    beforeCandidate[bf] = beforeCandidate[bf] / (1 - x)
                    
            if debug:
                print 'newEvidence : ' + str(newEvidence)
                #print 'ds table : '
                print 'beforeCandidate : ' + str(beforeCandidate)
                
            before = beforeCandidate 

        key = max(before, key = lambda x: before.get(x))
        #return (key, before[key])
        return before[key]
    
if __name__ == "__main__":
    data = {}
    #'''
    data['userMovies'] = 0.0732137427272
    data['userOccupation'] = 0.333333333333
    data['userDrinks'] = 0.038853734062
    data['userFoods'] = 0.044262140884
    data['userBooks'] = 0.042398746623
    data['userGender'] = 0.333333333333
    data['userDOB'] = 0.0
    '''
    data["F,D,B"] = 0.8
    data["A,F,D"] = 0.9
    data["A"] = 0.6
    '''
    ds = DempsterShafer()
    print ds.process(data, True)