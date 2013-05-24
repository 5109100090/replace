package com.amca.android.replace;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amca.android.replace.http.HTTPTransfer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity {

	private TextView registerScreen;
	private EditText userName, userPassword;
	private Button buttonLogin, quickButtonLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Login to your Account");
		//this.prepareConfiguration();
		
		userName = (EditText) findViewById(R.id.userName);
		userPassword = (EditText) findViewById(R.id.userPassword);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	doLogin(userName.getText().toString(), userPassword.getText().toString());
            }
        });
        registerScreen = (TextView) findViewById(R.id.linkToRegister);
        registerScreen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
            	Intent intent = new Intent(MainActivity.this, UserForm.class);
            	intent.putExtra("mode", "register");
		    	startActivity(intent);
			}
		});
		/*/
		quickButtonLogin = (Button) findViewById(R.id.quickButtonLogin);
		quickButtonLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	doLogin("rizky", "123qwe");
            }
        });
        //*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intent = new Intent(MainActivity.this, Settings.class);
		    	startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }
	
	private void doLogin(String username, String password){
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userName", username);
		data.put("userPassword", password);
		HTTPLogin login = new HTTPLogin();
		login.setCtx(MainActivity.this);
		login.setData(data);
		login.execute("authenticate/login/");
	}
	
	private void prepareConfiguration(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		editor.putString("serverUrl", "http://10.151.36.36:8080/");
		editor.putString("geolocation", "static");
		editor.commit();
	}
	
	class HTTPLogin extends HTTPTransfer{
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equals("null")){
				System.out.println("invalid username & password");
	        	Toast.makeText(getApplicationContext(), "invalid username & password", Toast.LENGTH_SHORT).show();
			}else{
				try{
					JSONArray jArray = new JSONArray(result);
					JSONObject json_data = jArray.getJSONObject(0);
					
					Intent intent = new Intent(MainActivity.this, PlaceType.class);
		        	intent.putExtra("userId", json_data.getInt("userId"));
		        	intent.putExtra("userAlias", json_data.getString("userAlias"));
		        	startActivity(intent);
				}catch(JSONException e){
					Toast.makeText(getApplicationContext(), "Error parsing data "+e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}

