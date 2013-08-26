package ratin.accadius.sabwa.jah;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SMS extends Activity implements Runnable {
    /** Called when the activity is first created. */
	public boolean FirstLoad = true;
	 public byte[] data;
	 public StringBuffer buffer;
	 public InputStream inputStream;
	 public HttpResponse response;
	 public HttpClient httpclient;
	 public HttpPost httppost;
	private EditText source_country, dest_country, volumes, source_place, dest_place ;
	private TextView longi, lati;
	private ProgressDialog progress;
	private String answer,vols, sc,dc,sps,dp, slong, slat, cereal;
	 private LocationManager locmgr = null;
	 double longitude, latitude;
	 String[] grain = 
		{
			 "",
				"White Maize",
				"Chickpeas",
				"Cowpeas",
				"Pigeon Peas",
				"garden/green peas",
				"Lentils",
				"Dry Soybean",
				"Faba Beans",
				"Green grams",
				"Finger Millet",
				"Pearl Millet",
				"Brown Rice",
				"Paddy / Rough rice",
				"Sorghum",
				"Wheat",
			
		};
		
		Spinner sp;
	 
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);
        
        source_country = (EditText) this.findViewById(R.id.source_c);
       dest_country = (EditText) this.findViewById(R.id.dest_c);
        
        source_place = (EditText) this.findViewById(R.id.source_p);
       dest_place = (EditText) this.findViewById(R.id.dest_p);
       volumes = (EditText) this.findViewById(R.id.volume);
        longi = (TextView) findViewById(R.id.xCood);
        lati = (TextView) findViewById(R.id.yCood);
        locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sp = (Spinner)findViewById(R.id.spinner_grain); 
        ArrayAdapter<String> adapter = 
        	new ArrayAdapter<String> (this, 
        			android.R.layout.simple_spinner_item,grain);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				 if(FirstLoad){
	                FirstLoad = false;
	                return;    
				 }
				int item = sp.getSelectedItemPosition();
				 cereal = grain[item];
				Toast.makeText(getBaseContext(), 
						"You have selected the Grain: " + grain[item], 
						Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
        	
        });
    
    
    }
    
    
    LocationListener onLocationChange=new LocationListener() {
        @SuppressWarnings("static-access")
		public void onLocationChanged(Location loc) {
            //sets and displays the lat/long when a location is provided
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
          
            longi.setText( slong.valueOf(longitude) );
            lati.setText(slat.valueOf(latitude));
                      

        }
         
        public void onProviderDisabled(String provider) {
        // required for interface, not used
        }
         
        public void onProviderEnabled(String provider) {
        // required for interface, not used
        }
         
        public void onStatusChanged(String provider, int status,
        Bundle extras) {
        // required for interface, not used
        }
    };
    
    //pauses listener while app is inactive
    @Override
    public void onPause() {
        super.onPause();
        locmgr.removeUpdates(onLocationChange);
    }
    
    //reactivates listener when app is resumed
    @Override
    public void onResume() {
        super.onResume();
        locmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,10000.0f,onLocationChange);
    }
    
    
    
    public void clear (View view)
    {
    	source_place.setText("");
    	source_country.setText("");
    	dest_place.setText("");
    	dest_country.setText("");
    	volumes.setText("");
    	
    }
    public void next (View view)
    {
    	 Intent intent = new Intent(SMS.this, Inc.class);
         startActivity(intent);

    }
    public void end (View view)
    
    {
    	super.finish();
    }
    public void send (View view)
    {
    	sc=source_country.getText().toString();
    	sps=source_place.getText().toString();
    	vols=volumes.getText().toString();
    	dc=dest_country.getText().toString();
    	dp=dest_place.getText().toString();
    	slong = longi.getText().toString();
    	slat = lati.getText().toString();
 if  (sc.equals("") ||  sps.equals("") ||  slong.equals("") || slat.equals(""))
	 {Toast.makeText(SMS.this, "Please enter the blank fields  or make sure your GPS is active and you can see your coodinates to continue....", Toast.LENGTH_SHORT).show();}


else 
    	{progress = ProgressDialog.show(this, "Please Wait..", "Sending Border Data..", true,
				false);

		Thread thread = new Thread(this);
		thread.start();
		return ;}
    }
    public void run() {
		//call the class here for the process to run in the thread
		
		//trying to connect and add data in the database starts here
		
		   //String result = "";
    	

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("volume", volumes.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("source_c", source_country.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("source_p",source_place.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("dest_p",dest_place.getText().toString()));
	   nameValuePairs.add(new BasicNameValuePair("dest_c",dest_country.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("Cereal", cereal));
	    nameValuePairs.add(new BasicNameValuePair("ykood", slat.trim()));   
	    nameValuePairs.add(new BasicNameValuePair("xkood", slong.trim()));
       
	   
	    
	    
        	
		
        	
        	
		    try{
		    	  		       
		    	    HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost("http://ratin.eagc.org/MIS/mobile/404.php");
		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            HttpResponse response = httpclient.execute(httppost);
		            HttpEntity entity = response.getEntity();
		            entity.getContent();
		            
		            //validator
		            response = httpclient.execute(httppost);
		            inputStream = response.getEntity().getContent();

		            data = new byte[256];

		            buffer = new StringBuffer();
		            int len = 0;
		            while (-1 != (len = inputStream.read(data)) )
		            {
		                buffer.append(new String(data, 0, len));
		            }

		            inputStream.close();
		            
		            if(buffer.charAt(0)=='Y')
		            {
		            	
		            	answer="Data not entered. TRY AGAIN!!!";
		            	//finish();
		            }else{
		            //ends validator
		            answer="Data entered succesfully ";
		            finish();
               	// Intent intent = new Intent(Maize.this, Menu.class);
                  //  startActivity(intent);
		    }
		    }
		    catch(Exception e)
		    {
		        //Log.e("log_tag", "Error in http connection "+e.toString());
		    	 answer="Warning: Error in http connection "+e.toString();
		    }           
			
		
		handler.sendEmptyMessage(0);
	}
    
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//After the background process terminates it 
			progress.dismiss();
			//tv.setText(pi_string);
			Toast.makeText(SMS.this, answer , Toast.LENGTH_LONG).show();
			
			

		}
	};

}