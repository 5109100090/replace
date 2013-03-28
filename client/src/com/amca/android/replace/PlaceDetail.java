package com.amca.android.replace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class PlaceDetail extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		
		Intent intent = getIntent();
		String placeId = intent.getStringExtra("placeId");
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("placeId", placeId);
		
		HTTPPlaceDetail http = new HTTPPlaceDetail(this, data);
		http.execute("place/getDetail");
	}

	class HTTPPlaceDetail extends AsyncTask<String, String, String>{
		
		private Context mContext;
		private HashMap<String, String> mData = null;
		private String serverUrl = "http://10.151.36.36/replace/server/";
		private ProgressDialog progressDialog;
		
		public HTTPPlaceDetail(Context context, HashMap<String, String> data){
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
		protected String doInBackground(String... params) {
			this.serverUrl += params[0];
			
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = mData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
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
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//parse json data
			try{
				List<String> list = new ArrayList<String>();
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = jArray.getJSONObject(0);
				list.add("ID : "+json_data.getString("placeId"));
				list.add("Name :\n"+json_data.getString("placeName"));
				list.add("Description :\n"+json_data.getString("placeDesc"));
				list.add("Latitude :\n"+json_data.getString("placeLat"));
				list.add("Longitude :\n"+json_data.getString("placeLng"));
				list.add("Type :\n"+json_data.getString("typeName"));
				setTitle(json_data.getString("placeName"));
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
				setListAdapter(adapter);
			}catch(JSONException e){
				Toast.makeText(getApplicationContext(), "Error parsing data "+e.toString(), Toast.LENGTH_SHORT).show();
			}
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
