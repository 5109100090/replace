class DempsterShafer():
    before = {}
    def process(self, data):
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
                
                keyCol1 = newEvidence['key'] if keyBefore == 't' else 'x' if len(intersectionUnique) == 0 else intersectionUnique.join(',')
                #keyCol1 = (keyBefore == 't' ? newEvidence['key'] : (len(intersectionUnique) == 0 ? 'x' : intersectionUnique.join(',')))
                #keyCol2
                #valCol1
                #valCol2