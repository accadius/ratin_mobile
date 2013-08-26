package accadius.ben.sabwa.jah;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
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

public class Cereals extends Activity implements Runnable {
    /** Called when the activity is first created. */
	public boolean FirstLoad = true;
	 public byte[] data;
	 public StringBuffer buffer;
	 public InputStream inputStream;
	 public HttpResponse response;
	 public HttpClient httpclient;
	 public HttpPost httppost;
	private EditText jina, size,  mahali;
	private TextView longi, lati;
	private ProgressDialog progress;
	private String answer, jin, siz,  maha, slong, slat, cereal, cro;
	 private LocationManager locmgr = null;
	 double longitude, latitude;
	 String[] grain = 
		{
			 "",
			 "Maize",
			 "Beans",
			 "Wheat",
			 "Millet",
			 "Sorghum",
			 "Rice",
			 "Lentils",
			 "Cowpeas",
			 "Chickpeas",
			 "Pegion peas",
			 "Green Peas",
			 "Soybean",
			 "Green Grams",
			
		};
		
	 
		Spinner sp;
	 
	 
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        jina = (EditText) this.findViewById(R.id.jina);
        size = (EditText) this.findViewById(R.id.size);
        
       // crop = (EditText) this.findViewById(R.id.crop);
        mahali = (EditText) this.findViewById(R.id.mahali);
        longi = (TextView) findViewById(R.id.xCood);
        lati = (TextView) findViewById(R.id.yCood);
        
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
				 cro=cereal.toString();
				Toast.makeText(getBaseContext(), 
						"You have selected the Grain: " + grain[item], 
						Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
        	
        });
        
        
        locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
    	jina.setText("");
    	size.setText("");
    	//crop.setText("");
    	mahali.setText("");
    	
    	
    }
    public void next (View view)
    {
    	 Intent intent = new Intent(Cereals.this, Splash.class);
         startActivity(intent);

    }
    public void end (View view)
    
    {
    	super.finish();
    }
    public void send (View view)
    {
    	jin=jina.getText().toString();
    	siz=size.getText().toString();
    	
    	
    	maha=mahali.getText().toString();
    	slong = longi.getText().toString();
    	slat = lati.getText().toString();
 if  (jin.equals("") || maha.equals("") || siz.equals(""))
	 {Toast.makeText(Cereals.this, "Please enter the blank fields ....", Toast.LENGTH_SHORT).show();}

 else if
 ( slong.equals("") || slat.equals(""))
 {Toast.makeText(Cereals.this, " make sure your GPS is active and you can see your coodinates to continue....", Toast.LENGTH_SHORT).show();}

else 
    	{progress = ProgressDialog.show(this, "Please Wait..", "Registering...", true,
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
		nameValuePairs.add(new BasicNameValuePair("crop", cereal));
		nameValuePairs.add(new BasicNameValuePair("jina", jina.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("size",size.getText().toString()));
	    //nameValuePairs.add(new BasicNameValuePair("crop",crop.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("mahali",mahali.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("ykood", slat.trim()));   
	    nameValuePairs.add(new BasicNameValuePair("xkood", slong.trim()));
       
	   
	    
	    
        	
		
        	
        	
		    try{
		    	  		       
		    	    HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost("http://ratin.arvixevps.com/~ratin/map_content/mobile/yield.php");
		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            HttpResponse response = httpclient.execute(httppost);
		            HttpEntity entity = response.getEntity();
		            entity.getContent();
		           /* 
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
		            	
		            	
		            }else{
		            //ends validator
		            answer="Data not entered succesfully. Try Again.";
		            finish();
               	 Intent intent = new Intent(Cereals.this, Splash.class);
                    startActivity(intent);
		    }*/
		            
		            answer="Data entered succesfully";
		            	finish();
		    }
		    catch(Exception e)
		    {
		        //Log.e("log_tag", "Error in http connection "+e.toString());
		    	 answer="Warning: Error in http connection "+e.toString();
		    
		    	 
		    /*	 Uri uri = Uri.parse("smsto:12346556");
			    	 Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			    	 it.putExtra("sms_body", "RATIN#YE "+ cro +", "+jin+", "+siz+", "+maha+","+slat+","+slong);
			    	 startActivity(it);
		    	 
		    */
		    	

		    }           
			
		
		handler.sendEmptyMessage(0);
	}
    
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//After the background process terminates it 
			progress.dismiss();
			//tv.setText(pi_string);
			Toast.makeText(Cereals.this, answer , Toast.LENGTH_LONG).show();
			
			

		}
	};

}