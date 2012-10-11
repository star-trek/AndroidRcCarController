package gdg.androidrccarcontroller.tool;

import java.io.BufferedOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

public class ServerConnector implements Runnable{
	private static ServerConnector instance = null;
	private Socket socket = null;
	private BufferedOutputStream bufferedOutputStream = null;
	
	public ServerConnector() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					socket = new Socket("rc.lkl.kr", 1024);
					bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
					new Thread(ServerConnector.this).start();
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
					final byte[] image = this.image;
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
