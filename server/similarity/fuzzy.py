class Fuzzy():
    def __init__(self, debug=False):
        self.Lns = 0
        self.Mns = 0.1
        self.Rns = 0.3
        
        self.Lll = 0.1
        self.Mll = 0.3
        self.Rll = 0.6
        
        self.Lqs = 0.3
        self.Mqs = 0.6
        self.Rqs = 0.8
        
        self.Ls = 0.6
        self.Ms = 0.8
        self.Rs = 1
        
        self.Lvs = 0.8
        self.Mvs = 1
        self.Rvs = 1
        
        self.debug = debug 
        
    def process(self, value):
        l = []
        d = ""
        
        if self.Lns <= value <= self.Mns:
            Uns = 1
        elif self.Mns <= value <= self.Rns:
            Uns = (self.Rns - value) / (self.Rns - self.Mns)
        else: # value >= Rns
            Uns = 0
        l.append(Uns)
        d += "ns:"+str(Uns)+"\n"
        
        if self.Lll <= value <= self.Mll:
            Ull = (value - self.Lll) / (self.Mll - self.Lll)
        elif self.Mll <= value <= self.Rll:
            Ull = (self.Rll - value) / (self.Rll - self.Mll)
        else: # value >= Rll
            Ull = 0
        l.append(Ull)
        d += "ll:"+str(Ull)+"\n"
        
        if self.Lqs <= value <= self.Mqs:
            Uqs = (value - self.Lqs) / (self.Mqs - self.Lqs)
        elif self.Mqs <= value <= self.Rqs:
            Uqs = (self.Rqs - value) / (self.Rqs - self.Mqs)
        else: # value >= Rqs
            Uqs = 0
        l.append(Uqs)
        d += "qs:"+str(Uqs)+"\n"
        
        if self.Ls <= value <= self.Ms:
            Us = (value - self.Ls) / (self.Ms - self.Ls)
        elif self.Ms <= value <= self.Rs:
            Us = (self.Rs - value) / (self.Rs - self.Ms)
        else: # value <= Ls
            Us = 0
        l.append(Us)
        d += "s:"+str(Us)+"\n"
        
        if self.Lvs <= value <= self.Mvs:
            Uvs = (value - self.Lvs) / (self.Mvs - self.Lvs)
        else: # value <= Lqs
            Uvs = 0
        l.append(Uvs)
        d += "vs:"+str(Uvs)+"\n"
        
        if Uns == 0:
            self.Mns = 0
        if Ull == 0:
            self.Mll = 0
        if Uqs == 0:
            self.Mqs = 0
        if Us == 0:
            self.Ms = 0
        if Uvs == 0:
            self.Mvs = 0
        
        centroid = ( (Uns*self.Mns)+(Ull*self.Mll)+(Uqs*self.Mqs)+(Us*self.Ms)+(Uvs*self.Mvs) ) / (self.Mns+self.Mll+self.Mqs+self.Ms+self.Mvs)
        d += "centroid:"+str(centroid)+"\n"
        
        if self.debug:
            print d
        
        return l.index(max(l))
    
if __name__ == "__main__":
    l = [0.00123, 0.9123]
    for v in l:
        f = Fuzzy(False)
        print "max("+str(v) + ") = " + str(f.process(v))