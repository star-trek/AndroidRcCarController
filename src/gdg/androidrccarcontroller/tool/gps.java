package gdg.androidrccarcontroller.tool;

import java.util.Locale;

import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class gps extends Activity implements LocationListener {

	LocationManager location = null;
	Location mlocation = null;
	double lat = 0;
	double lng = 0;
	Geocoder geoCorder;
	//TextView lat = (TextView)findViewById(R.id.txt_latitude);
	//TextView lon = (TextView)findViewById(R.id.txt_longitude);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        location = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //GPS위치정보 업데이트
        location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //기지국으로 부터 업데이트
        //location.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        geoCorder = new Geocoder(this, Locale.KOREA);
        //Criteria criteria = new Criteria();
        //criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        //criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        
        //String provider = location.getBestProvider(criteria, true);
        //location.requestLocationUpdates(provider, 1000, 0, this);
        
        /*final Button gpsButton = (Button) findViewById(R.id.btn_getgps);
		gpsButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				GetLocations();
				Log.d("location", "button pressed");
			}
		});*/
        GetLocations();
		
    }
    public void GetLocations(){
    	//TextView txt_latitude = (TextView)findViewById(R.id.txt_latitude);
    	//TextView txt_longitude = (TextView)findViewById(R.id.txt_longitude);
    	
    	if(mlocation != null){
    		lat = mlocation.getLatitude();
    		lng = mlocation.getLongitude();
    		Log.d("gps", "success");
    		Log.d("gps", " "+lat);
    		Log.d("gps", ""+lng);
    		
    	}
    	//TODO 여기에 서버로 push하는 소스
    	
    	//txt_latitude.setText(String.valueOf(lat));
    	//txt_longitude.setText(String.valueOf(lng));
    }


	public void onLocationChanged(Location location) {
		Log.d("location", "location changed");
		mlocation = location;
		
		/*double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		
		Toast.makeText(this, latitude+","+longitude, Toast.LENGTH_SHORT).show();
		//lat.setText(Double.toString(latitude));
		//lon.setText(Double.toString(longitude)); */
	}
	

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}