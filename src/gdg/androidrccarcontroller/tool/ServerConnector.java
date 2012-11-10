package gdg.androidrccarcontroller.tool;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.json.JSONObject;

import android.util.Log;

public class ServerConnector{
	private static ServerConnector instance = null;
	private Socket socket = null;
	private BufferedOutputStream bufferedOutputStream = null;
	private ServerCallback callback = null;
	
	public ServerConnector() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					socket = new Socket("rc.lkl.kr", 1024);
					bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
					new Thread(inputThread).start();
					new Thread(outputThread).start();
				} catch (Exception e) {
					e.printStackTrace();
					close();
				}
			}
		}).start();
	}
	
	public static ServerConnector getInstance() {
		if(instance==null){
			instance = new ServerConnector();
		}
		return instance;
	}

	byte[] image;
	int imageNo=0;
	public void pushImage(byte[] data){
		image = data;
		imageNo++;
	}
	
	public boolean isWriting() {
		return streamWriting;
	}

	public long getPingTime() {
		return pingTime;
	}
	
	long pingTime = 1;
	boolean streamWriting = false;

	public void setServerCallback(ServerCallback callback) {
		this.callback = callback;
	}
	
	private Runnable outputThread = new Runnable() {
		@Override
		public void run() {
			int no=0;
			try {
				while(instance!=null) {
					if(image==null||imageNo==no){
						Thread.sleep(10);
					}else{
						streamWriting = true;
						no = imageNo;
						final byte[] image = ServerConnector.this.image;
						Log.i("push", "size="+image.length);
						long start = System.currentTimeMillis();
						bufferedOutputStream.write(intToByteArray(image.length));
						bufferedOutputStream.flush();
						bufferedOutputStream.write(image);
						bufferedOutputStream.flush();
						long end = System.currentTimeMillis();
						pingTime = end-start+1;
						streamWriting = false;
					}
				}
			} catch (Exception e) {
				close();
			}
		}
	};
	
	private Runnable inputThread = new Runnable() {
		
		@Override
		public void run() {
			try{
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				while(socket!=null){
					try {
						String line = bufferedReader.readLine();
						Log.i("socket", line);
						try {
							JSONObject data = new JSONObject(line);
							if(callback!=null){
								callback.onServerCallback(data);
							}
						} catch (Exception e) {
							Log.i("socket", "Exception "+line);
						}
					} catch (IOException e) {
						e.printStackTrace();
						close();
					}
				}
			}catch(Exception e){}
		}
	};
	
	public void close(){
		instance=null;
		try {
			socket.close();
		} catch (Exception e) {}
		socket = null;
		bufferedOutputStream = null;
	}
	
	private static byte[] intToByteArray(final int integer) {
	    ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE / 8);
	    buff.putInt(integer);
	    buff.order(ByteOrder.BIG_ENDIAN);
	    return buff.array();
	}
}
