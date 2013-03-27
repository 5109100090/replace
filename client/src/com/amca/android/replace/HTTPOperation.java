package com.amca.android.replace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import android.os.AsyncTask;
import android.util.Log;

public class HTTPOperation extends AsyncTask<String, Void, JSONObject> {

	private String serverUrl = "http://10.151.36.36/replace/server/";
	private String httpMethod = "post";
	private HashMap<String, String> mData = null;
	
	public HTTPOperation(HashMap<String, String> data){
		mData = data;
	}

	@Override
	protected void onPreExecute() {

	}
	
	@Override
	protected JSONObject doInBackground(String... params) {
		this.httpMethod = params[0];
		this.serverUrl += params[1];
		
		HttpEntity resEntity;
		if(this.httpMethod == "post"){
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = mData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
            }
            
			resEntity = this.postData(nameValuePair);
		}else{
			resEntity = this.getData();
		}
		
		if(resEntity == null){
        	return null;
        }else{
        	String result = null;
			try {
				result = EntityUtils.toString(resEntity);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        	System.out.println(result);
        	if(!result.equals("0") || !result.equals(null))
        	{
	        	Object obj = JSONValue.parse(result);
	        	JSONArray array = (JSONArray) obj;
	        	return (JSONObject) array.get(0);
        	}
        	return null;
        }
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		
	}
	
	private HttpEntity getData(){
		try {
	        HttpClient client = new DefaultHttpClient();
	        HttpGet get = new HttpGet(serverUrl);
	        HttpResponse responseGet = client.execute(get);  
	        return responseGet.getEntity();  
		} catch (Exception e) {
			Log.e("log_tag","error: "+e.toString());
		}
		return null;
	}
	
	private HttpEntity postData(ArrayList<NameValuePair> pair){
        try{
	        HttpClient client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(serverUrl);
	        post.setEntity(new UrlEncodedFormEntity(pair));
	        HttpResponse responsePost = client.execute(post);
	        return responsePost.getEntity();
        }catch(Exception e){
	        e.printStackTrace();
	        Log.e("log_tag","error: "+e.toString());
        }
        return null;
    }
}
