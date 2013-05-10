package com.amca.android.replace;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;

public class UserFormDetail extends Activity {

	private String mode = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_form_detail);
		
		String[] dataSplit = null;
		int n = 3;
		
		Intent intent = getIntent();
		mode = intent.getStringExtra("mode");
		
		if(mode.equals("profile")){
			String data = intent.getStringExtra("data");
			System.out.println(data);
			dataSplit = data.split(",");
			n = dataSplit.length;
		}
		
		LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayout);
		for(int i=0; i<n; i++){
			EditText ed = new EditText(this);
			ed.setInputType(InputType.TYPE_CLASS_TEXT);
			
			String text = null;
			
			if(mode.equals("profile")){
				text = dataSplit[i];
			}else{
				text = "Hello this is the edittext demo";
			}
			ed.setText(text);
			ll.addView(ed);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_form_detail, menu);
		return true;
	}

}
