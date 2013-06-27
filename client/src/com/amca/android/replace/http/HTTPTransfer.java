package com.amca.android.replace.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.amca.android.replace.Setting;
import android.content.Context;
import android.os.AsyncTask;

public class HTTPTransfer extends AsyncTask<String, String, String> {

	private Context context = null;
	private HashMap<String, String> data = null;
	private Integer mode = 0;
	
	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context ctx) {
		this.context = ctx;
	}

	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

	private String send(String serverUrl){
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        Iterator<String> it = getData().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            nameValuePair.add(new BasicNameValuePair(key, getData().get(key)));
        }
        
		try {
			HttpClient client = new DefaultHttpClient();				
			HttpPost post = new HttpPost(serverUrl);
			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			client.execute(post);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String receive(String serverUrl){
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(serverUrl);
			
			HttpResponse httpRespose = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpRespose.getEntity();
			InputStream in = httpEntity.getContent();
			BufferedReader read = new BufferedReader(new InputStreamReader(in)); 
			
			String isi= "";
			String baris= "";
			while((baris = read.readLine())!=null){
				isi+= baris;
			} 

			return isi;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String sendAndReceive(String serverUrl){
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        Iterator<String> it = getData().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            nameValuePair.add(new BasicNameValuePair(key, getData().get(key)));
        }
        
		try {
			HttpClient client = new DefaultHttpClient();				
			HttpPost post = new HttpPost(serverUrl);
			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			HttpResponse responsePost = client.execute(post);
			HttpEntity httpEntity = responsePost.getEntity();
			InputStream in = httpEntity.getContent();
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			
			String isi= "";
			String baris= "";
			while((baris = read.readLine())!=null){
				isi+= baris;
			} 

			return isi;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected String doInBackground(String... params) {
		Setting setting = new Setting();
		String serverUrl = setting.getString(getContext(), "serverUrl") + params[0];
		
		switch(getMode()){
			case 1 :
				return send(serverUrl);
			case 2 :
				return receive(serverUrl);
			default:
				return sendAndReceive(serverUrl);
		}
	}

}
