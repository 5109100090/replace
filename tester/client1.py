import httplib,urllib,sys,time

server = '10.151.36.36:9090'
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

for i in range(5):
    #start_time = time.clock()
    p = 0
    for p in range(4):
        st = time.clock()
        headers = {"Host": "riset.ajk.if.its.ac.id", "Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
        #headers = {"Host": "riset.ajk.if.its.ac.id", "Accept": "text/plain"}
        conn = httplib.HTTPConnection(server)
        page = pages[p]
        page = '/replace/' + pages[p]
        conn.request(reqs[p], page, params[p], headers)
        rsp = conn.getresponse()
        time_result.setdefault(p,[]).append(time.clock() - st)
        #time.sleep(2)
    #print (i+1, str(time.clock() - start_time) + " second")

# ""
result = ''
for t in time_result:
    s = sum(time_result[t])
    l = len(time_result[t])
    result += 'test process #'+str(t)+'\n'
    result += '\n'.join(map(str, time_result[t]))+'\n'
    result += "avg time : " + str(s/float(l))+'\n\n'

print result
f = open('result','w')
f.write(result)