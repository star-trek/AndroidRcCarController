package gdg.androidrccarcontroller.view;

import java.io.BufferedOutputStream;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    private SurfaceHolder holder;
    private Camera camera = null;
    BufferedOutputStream bufferedOutputStream = null;
    boolean stream=false;
    PreviewCallback callback = null;

    public CameraSurfaceView(Context context) 
    {
        super(context);

        this.holder = this.getHolder();
        this.holder.addCallback(this);
        this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

	@Override
    public void surfaceCreated(SurfaceHolder holder) 
    {
		this.camera = Camera.open();
		this.camera.getParameters().getSupportedPreviewSizes();
		try {
			this.camera.setPreviewDisplay(this.getHolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.camera.setPreviewCallback(callback);
    }
	
	public void setPreviewCallback(PreviewCallback callback){
		this.callback = callback;
        if(camera!=null){
    		this.camera.setPreviewCallback(callback);
        }
	}
	
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
    {
        this.camera.startPreview();
    }    

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) 
    {
		this.camera.setPreviewCallback(null);
        this.camera.stopPreview();
        this.camera.release();
        this.camera = null;
    }

    public Camera getCamera()
    {
        return this.camera;
    }
}