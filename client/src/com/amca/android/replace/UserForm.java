package com.amca.android.replace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.amca.android.replace.http.HTTPTransfer;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UserForm extends ListActivity {

	private String mode = null;
	private List<String> dataList = new ArrayList<String>();
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_form);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.VISIBLE);
		
		Intent intent = getIntent();
		mode = intent.getStringExtra("mode");
		
		if(mode.equals("profile")){
			String userId = intent.getStringExtra("userId");
			progressBar = (ProgressBar) findViewById(R.id.progressBar1);
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("userId", userId);
			HTTPUserForm http = new HTTPUserForm();
			http.setCtx(UserForm.this);
			http.setData(data);
			http.execute("authenticate/detail/");
		}else{
			progressBar.setVisibility(View.INVISIBLE);
		}
		
		List<String> list = new ArrayList<String>();
		list.add("Username");
		list.add("Password");
		list.add("Alias");
		list.add("Favourite Foods");
		list.add("Favourite Drinks");
		list.add("Favourite Books");
		list.add("Favourite Movies");
		list.add("Favourite Musics");
		list.add("Gender");
		list.add("Occupation");
		list.add("Date of Birth");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(UserForm.this, UserFormDetail.class);
		if(mode.equals("profile")){
			intent.putExtra("data", dataList.get(position).toString());
		}
		intent.putExtra("mode", this.mode);
    	startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_form, menu);
		return true;
	}
	
	class HTTPUserForm extends HTTPTransfer{
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try{
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = jArray.getJSONObject(0);
				dataList.add(json_data.getString("userName"));
				dataList.add(json_data.getString("userPassword"));
				dataList.add(json_data.getString("userAlias"));
				dataList.add(json_data.getString("userFoods"));
				dataList.add(json_data.getString("userDrinks"));
				dataList.add(json_data.getString("userBooks"));
				dataList.add(json_data.getString("userMovies"));
				dataList.add(json_data.getString("userMusics"));
				dataList.add(json_data.getString("userGender"));
				dataList.add(json_data.getString("userOccupation"));
				dataList.add(json_data.getString("userDOB"));
			}catch(JSONException e){
				Toast.makeText(getApplicationContext(), "Error parsing data "+e.toString(), Toast.LENGTH_SHORT).show();
			}
			
			progressBar.setVisibility(View.INVISIBLE);
		}
	}  

}
