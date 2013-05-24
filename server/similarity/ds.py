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
    
    def assignThreshold(self, data):
        #data['A'] = round(data['A'], 2)

        a = data['A'] - (data['A'] * 0.1)
        b = 1 - data['A']
        ab = 1 - (a + b)
        
        '''
        data['A'] = round(a, 2)
        data['B'] = round(b, 2)
        data['AB'] = round(ab, 2)
        #'''
        data['A'] = a
        data['B'] = b
        data['AB'] = ab
        #print data
        #'''
        return data
    
    def process2(self, data, debug = False):
        m1 = {}
        m2 = {}
        for key in data:
            if(len(m1) == 0):
                m1['A'] = data[key]
                continue
            m1 = self.assignThreshold(m1)
            
            m2['A'] = data[key]
            m2 = self.assignThreshold(m2)
            
            q = m1['A'] * m2['A'] + m1['A'] * m2['AB'] + m1['AB'] * m2['A']
            w = m1['A'] * m2['B'] + m1['B'] * m2['A']
            m3 = q / (1 - w)
            m1['A'] = m3
        
        #return {'A':round(m1['A'], 2), 'B':round(1 - m1['A'], 2) }
        #return {'A':m1['A'], 'B':1 - m1['A'] }
        return m3

if __name__ == "__main__":
    data = {}
    #'''
    data['userMovies'] =  0.0510703743887
    data['userOccupation'] = 0.181818181818
    data['userDrinks'] = 0.133615580761
    data['userFoods'] = 0.144806087205
    data['userBooks'] = 0.0328666641286
    data['userGender'] = 0.181818181818
    data['userDOB'] = 0.290398126464
    '''
    data["1"] = 0.8
    data["2"] = 0
    #'''
    ds = DempsterShafer()
    res = ds.process2(data, True)
    print res
    print res['A'] if res['A'] >= 0.5 else res['B']