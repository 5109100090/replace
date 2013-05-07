from Levenshtein import *

class EditDistance():
    def __init__(self, str1, str2):
        self.str1 = str1
        self.str2 = str2
        self.length1 = len(str1)
        self.length2 = len(str2)
        
    def similarity1(self):
        d = float(self.process())
        x = 1 - (d / max(self.length1, self.length2))
        return x
    
    def similarity2(self):
        d = float(distance(self.str1, self.str2))
        x = 1 - (d / max(self.length1, self.length2))
        return x
    
    def process(self):
        m = [[0 for i in range(self.length2 + 1)] for j in range(self.length1 + 1)]
        for i in range(0, self.length1 + 1):
            for j in range(0, self.length2 + 1):
                if i == 0:
                    m[i][j] = j
                elif j == 0:
                    m[i][j] = i
                else:
                    if self.str1[i - 1] == self.str2[j - 1]:
                        m[i][j] = m[i - 1][j - 1]
                    else:
                        m[i][j] = 1 + min( m[i][j - 1], m[i - 1][j], m[i - 1][j - 1] )

        return m[self.length1][self.length2]