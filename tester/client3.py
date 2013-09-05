import httplib,urllib,sys,threading,time,csv

class myThread (threading.Thread):
    def __init__(self, threadID, name):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
    def run(self):
        print self.name + ' is starting'
        process(self.name)
        print self.name + ' is done'

def process(threadName):
    server = 'riset.ajk.if.its.ac.id'
    reqs = {0:'POST',
    1:'GET',
    2:'POST',
    3:'POST'}
    
    pages = {0:'/authenticate/login/',
    1:'/type/listAll/',
    2:'/place/process/',
    3:'/review/listReviews/'}
            
    params = {0:urllib.urlencode({'userName': 'rizky', 'userPassword': 'a'}),
    1:'',
    2:urllib.urlencode({'userId':1,'typeId':1,'range':1000,'currentLat':'-7.279241','currentLng':'112.790392'}),
    3:urllib.urlencode({'userId':1,'placeId':1,'typeId':1})}
    
    time_result = {0:[],1:[],2:[],3:[]}
    
    process_name = {0:'login',1:'getPlaceType',2:'getPlaces',3:'getReviews'}
    for i in range(5):
        p = 0
        for p in range(4):
            print "#%s %s %s => %s" % (str(i+1), threadName, time.ctime(time.time()), process_name[p])
            st = time.clock()
            headers = {"Host": "riset.ajk.if.its.ac.id", "Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
            conn = httplib.HTTPConnection(server)
            page = pages[p]
            page = '/replace/' + pages[p]
            conn.request(reqs[p], page, params[p], headers)
            rsp = conn.getresponse()
            time_result.setdefault(p,[]).append(time.clock() - st)

    result = [[threadName]]
    for t in time_result:
        s = sum(time_result[t])
        l = len(time_result[t])
        '''
        result += 'test process ' + process_name[t] + '\n'
        result += '\n'.join(map(str, time_result[t]))+'\n'
        result += "avg time : " + str(s/float(l))+'\n\n'
        '''
        lst = [process_name[t]]
        lst += time_result[t]
        lst.append(str(s/float(l)))
        result.append(lst)

    with open('result.csv', 'ab') as fp:
        a = csv.writer(fp, delimiter=',')
        a.writerows(result)

    #f = open('result/' + threadName + '.txt','w')
    #f.write(result)

with open('result.csv', 'ab') as fp:
    a = csv.writer(fp, delimiter=',')
    a.writerows(['user','login','getPlaceType','getPlaces','getReviews','average'])

for i in range(10):
    thread = myThread(i+1, "User-"+str(i+1))
    thread.start()