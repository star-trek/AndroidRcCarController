package gdg.androidrccarcontroller.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

public class BluetoothConnector {
	static BluetoothConnector instance = null;
	
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothDevice bluetoothDevice = null;
    private BluetoothSocket bluetoothSocket = null;
    private OutputStream bluetoothOutputStream = null;
    private InputStream bluetoothInputStream = null;
	
	public BluetoothConnector() {
		// TODO Auto-generated constructor stub
	}
	
	public static BluetoothConnector getInstance() {
		if(instance==null){
			instance = new BluetoothConnector();
		} else if (instance!=null) {
			//TODO
		}
		return instance;
	}
	
	public void open(Activity activity) {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null)
        {
        	Log.i("find", "No bluetooth adapter available");
        }

        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
        	Log.i("find", "Bluetooth Device List");
            for(BluetoothDevice device : pairedDevices)
            {
            	Log.i("find", device.getName());
                if(device.getName().equals("RCCAR")) 
                {
                	bluetoothDevice = device;
                    break;
                }
            }
        }
        
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID
        try {
        	bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        	bluetoothSocket.connect();
        	bluetoothOutputStream = bluetoothSocket.getOutputStream();
        	bluetoothInputStream = bluetoothSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    void close() throws IOException
    {
    	bluetoothOutputStream.close();
    	bluetoothInputStream.close();
    	bluetoothSocket.close();
    }
	
    public void sendSerial(char lr, char fb){
        byte[] buf = new byte[4];
        buf[0] = (byte) 0xFF;
        buf[1] = (byte) lr;
        buf[2] = (byte) fb;
        buf[3] = (byte) (buf[0]^buf[1]^buf[2]);
        try {
        	bluetoothOutputStream.write(buf);
        } catch (IOException ex) {
        }
    }
}
