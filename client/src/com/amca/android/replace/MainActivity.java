package com.amca.android.replace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private EditText userName;
	private EditText userPassword;
	private Button buttonLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		userName = (EditText) findViewById(R.id.userName);
		userPassword = (EditText) findViewById(R.id.userPassword);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userName", userName.getText().toString());
		data.put("userPassword", userPassword.getText().toString());
		HTTPLogin login = new HTTPLogin(this, data);
		login.execute("user/login");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class HTTPLogin extends AsyncTask<String, String, JSONObject>{

		private Context mContext;
		private HashMap<String, String> mData = null;
		private String serverUrl = "http://10.151.36.36/replace/server/";
		private ProgressDialog progressDialog;
		
		public HTTPLogin(Context context, HashMap<String, String> data){
			this.mContext = context;
			mData = data;
		}
		
		@Override
	    protected void onPreExecute() {
			progressDialog = new ProgressDialog(mContext);
		    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		    progressDialog.setMessage("Loading...");
		    progressDialog.setCancelable(false);
		    progressDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			this.serverUrl += params[0];
			
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = mData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
            }
            
			try{
		        HttpClient client = new DefaultHttpClient();
		        HttpPost post = new HttpPost(serverUrl);
		        post.setEntity(new UrlEncodedFormEntity(nameValuePair));
		        HttpResponse responsePost = client.execute(post);
		        HttpEntity resEntity = responsePost.getEntity();
		        if(resEntity == null){
		        	return null;
		        }else{
		        	String result = null;
					try {
						result = EntityUtils.toString(resEntity);
					}catch (IOException e) {
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
	        }catch(Exception e){
		        e.printStackTrace();
		        Log.e("log_tag","error: "+e.toString());
	        }
	        return null;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if(result == null){
				System.out.println("invalid username & password");
	        	Toast.makeText(getApplicationContext(), "invalid username & password", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(MainActivity.this, PlaceType.class);
	        	intent.putExtra("userId", result.get("userId").toString());
	        	startActivity(intent);
			}
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}

