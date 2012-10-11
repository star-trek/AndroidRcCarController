package gdg.androidrccarcontroller;

import gdg.androidrccarcontroller.view.CameraSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.view.Menu;
import android.widget.LinearLayout;

public class ControllerActivity extends Activity implements PreviewCallback{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout);
        CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(this);
        cameraSurfaceView.setPreviewCallback(this);
        linearLayout.addView(cameraSurfaceView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.controller, menu);
        return true;
    }

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		
	}

    
}
