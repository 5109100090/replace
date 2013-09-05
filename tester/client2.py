import httplib,urllib,sys,time

server = 'riset.ajk.if.its.ac.id'
for i in range(10):
    headers = {"Host": "riset.ajk.if.its.ac.id", "Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
    conn = httplib.HTTPConnection(server)
    conn.request('POST', '/replace/review/listReviews/', urllib.urlencode({'userId':1,'placeId':1,'typeId':1}), headers)
    rsp = conn.getresponse()
    print (rsp.status, rsp.reason)
    #time.sleep(1)
    #print rsp.read(1024)