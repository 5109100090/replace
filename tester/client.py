import httplib,urllib,sys,threading,time,csv

class myThread (threading.Thread):
    def __init__(self, threadID, name):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
    def run(self):
        print self.name + ' is starting'
        process(self.threadID,self.name)
        print self.name + ' is done'

def process(threadID,threadName):
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
    
    time_result = []
    process_name = {0:'login',1:'getPlaceType',2:'getPlaces',3:'getReviews'}
    lst = [threadID,threadName,time.ctime(time.time())]
	
    for p in range(4):
        print "%s %s => %s" % (threadName, time.ctime(time.time()), process_name[p])
        flag = True
        while flag:
            st = time.clock()
            headers = {"Host": "riset.ajk.if.its.ac.id", "Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
            conn = httplib.HTTPConnection(server)
            page = '/replace/' + pages[p]
            conn.request(reqs[p], page, params[p], headers)
            rsp = conn.getresponse()
            if rsp.status == 200:
                flag = False
                time_result.append(time.clock() - st)

    result = [lst + time_result]

    with open('result.csv', 'ab') as fp:
        a = csv.writer(fp, delimiter=',')
        a.writerows(result)

with open('result.csv', 'wb') as fp:
    a = csv.writer(fp, delimiter=',')
    a.writerows([['no','user','time','login','getPlaceType','getPlaces','getReviews']])

for i in range(40):
    thread = myThread(i+1, "User-"+str(i+1))
    thread.start()