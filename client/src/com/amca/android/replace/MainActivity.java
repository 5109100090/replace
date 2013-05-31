package com.amca.android.replace;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.app.SherlockActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.amca.android.replace.http.HTTPTransfer;
import com.amca.android.replace.user.UserForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends SherlockActivity {

	private ProgressDialog progressDialog;
	private TextView registerScreen;
	private EditText userName, userPassword;
	private Button buttonLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Login to your Account");

		userName = (EditText) findViewById(R.id.userName);
		userPassword = (EditText) findViewById(R.id.userPassword);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (userName.getText().toString().equals("")
						|| userPassword.getText().toString().equals("")) {
					Toast.makeText(MainActivity.this,
							"input your username and password",
							Toast.LENGTH_SHORT).show();
				} else {
					doLogin(userName.getText().toString(), userPassword
							.getText().toString());
				}
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(MainActivity.this, Settings.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void doLogin(String username, String password) {
		progressDialog = ProgressDialog.show(MainActivity.this, "Loading...",
				"Authenticate your data", false, false);
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userName", username);
		data.put("userPassword", password);

		HTTPLogin login = new HTTPLogin();
		login.setContext(MainActivity.this);
		login.setData(data);
		login.execute("authenticate/login/");
	}

	class HTTPLogin extends HTTPTransfer {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("null")) {
				System.out.println("invalid username & password");
				Toast.makeText(this.getContext(),
						"invalid username & password", Toast.LENGTH_SHORT)
						.show();
			} else {
				try {
					JSONArray jArray = new JSONArray(result);
					JSONObject json_data = jArray.getJSONObject(0);

					Intent intent = new Intent(MainActivity.this,
							PlaceType.class);
					intent.putExtra("userId", json_data.getInt("userId"));
					intent.putExtra("userAlias",
							json_data.getString("userAlias"));
					startActivity(intent);
				} catch (JSONException e) {
					Toast.makeText(this.getContext(),
							"Error parsing data " + e.toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
			progressDialog.dismiss();
		}
	}
}
