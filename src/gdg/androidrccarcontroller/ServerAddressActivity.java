package gdg.androidrccarcontroller;

import gdg.androidrccarcontroller.tool.ServerConnector;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.EditText;


public class ServerAddressActivity extends Activity{
	
	TextView serverText;
	Button mainBtn;
	EditText serverEdit;
	 
	//Intent RequestCode
	final static int EDIT_ACT = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serveraddress);
        
        serverText = (TextView)findViewById(R.id.servertext);
        serverEdit = (EditText)findViewById(R.id.serveredit);
        mainBtn = (Button)findViewById(R.id.okbtn);
        mainBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ServerConnector.setServerAddress(serverEdit.getText().toString());
				// TODO Auto-generated method stub
				Intent intent = new Intent(ServerAddressActivity.this, ControllerActivity.class);
				startActivity(intent);
			    finish();
			}
		});
	}
}
