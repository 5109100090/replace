package com.amca.android.replace;

import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
public class MainActivity extends Activity implements OnClickListener {

	private String serverUrl = "http://10.151.36.36/replace/server/login_controller/login/";
	private EditText user_name;
	private EditText user_password;
	private Button button_login; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		user_name = (EditText) findViewById(R.id.user_name);
		user_password = (EditText) findViewById(R.id.user_password);
		button_login = (Button) findViewById(R.id.button_login);
		button_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
        Thread t = new Thread() {
            public void run() {
            	postData(user_name.getText().toString(), user_password.getText().toString());
            }
        };
        t.start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private void postData(String userName, String userPassword){
        try{
	        HttpClient client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(serverUrl);
	        
	        ArrayList<NameValuePair> pair = new ArrayList<NameValuePair>(1);
	        pair.add(new BasicNameValuePair("user_name", userName));
	        pair.add(new BasicNameValuePair("user_password", userPassword));
	        post.setEntity(new UrlEncodedFormEntity(pair));
	        
	        HttpResponse responsePOST = client.execute(post);
	        HttpEntity resEntity = responsePOST.getEntity(); 
	        if(resEntity == null){
	        	System.out.println("send fail");
	        }else{
	        	System.out.println(EntityUtils.toString(resEntity));
	        }
        }catch(Exception e){
	        e.printStackTrace();
	        Log.e("log_tag","error: "+e.toString());
        }
    }
}
