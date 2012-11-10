package gdg.androidrccarcontroller;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import gdg.androidrccarcontroller.tool.BluetoothConnector;
import gdg.androidrccarcontroller.tool.ServerCallback;
import gdg.androidrccarcontroller.tool.ServerConnector;
import gdg.androidrccarcontroller.view.CameraSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;

public class ControllerActivity extends Activity implements PreviewCallback, ServerCallback{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout);
        CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(this);
        cameraSurfaceView.setPreviewCallback(this);
        linearLayout.addView(cameraSurfaceView);
        BluetoothConnector.getInstance().open(this);
        ServerConnector.getInstance().setServerCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.controller, menu);
        return true;
    }

    int imageQuality=30;
    
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Camera.Parameters params = camera.getParameters();
		int w = params.getPreviewSize().width;
		int h = params.getPreviewSize().height;
		int format = params.getPreviewFormat();
		
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		YuvImage image = new YuvImage(data, format, w, h, null);
        Rect area = new Rect(0, 0, w, h);

        long start = System.currentTimeMillis();
		image.compressToJpeg(area, imageQuality, out);
        long end = System.currentTimeMillis();
        
        long pingTime = ServerConnector.getInstance().getPingTime()+(end-start);
        if(pingTime>50 && imageQuality>25){
        	imageQuality--;
        }else if(pingTime<50 && imageQuality<100){
        	imageQuality++;
        }
        
		Log.i("image", ""+imageQuality);

		ServerConnector.getInstance().pushImage(out.toByteArray());
	}

	@Override
	public void onServerCallback(JSONObject data) {
		try {
			Log.i("blue", "" + (char)data.getInt("lr") + (char)data.getInt("fb"));
			BluetoothConnector.getInstance().sendSerial((char)data.getInt("lr"), (char)data.getInt("fb"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    
}
