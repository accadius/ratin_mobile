package accadius.sabwa.jah;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Inc extends Activity implements Runnable {
    private static final int TAKE_PICTURE = 0;
	/** Called when the activity is first created. */
	 public byte[] data;
	 public StringBuffer buffer;
	 public InputStream inputStream;
	 public HttpResponse response;
	 public HttpClient httpclient;
	 public HttpPost httppost;
	private EditText title, place, details;
	private TextView longi, lati;
	private ProgressDialog progress;
	private String answer, tit, mahali, incd,  slong, slat, deviceId, imeiz;
	 private LocationManager locmgr = null;
	 double longitude, latitude;
	 public Uri outputFileUri;
	 public Intent intentax;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incident);
        
        title = (EditText) this.findViewById(R.id.txtTitle);
        place = (EditText) this.findViewById(R.id.txtPlace);
        
        details = (EditText) this.findViewById(R.id.txtdata);
       
        longi = (TextView) findViewById(R.id.xCood);
        lati = (TextView) findViewById(R.id.yCood);
        locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        deviceId = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
    	imeiz=deviceId.trim();//String.valueOf(deviceId);
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
    	title.setText("");
    	place.setText("");
    	details.setText("");
    	
    	
    }
    public void photo (View view)
    {
    	 intentax = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), ""+deviceId.trim()+"_W.jpg");
         outputFileUri = Uri.fromFile(file);
        intentax.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intentax, TAKE_PICTURE);
       
    }
        protected void onActivityResult(int file, int outputFileUri)
        {
        if (outputFileUri == RESULT_OK)
        {
            Uri imageUri=intentax.getData();
            List<NameValuePair> params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("image", imageUri.getPath()));
            post("http://ratin.arvixevps.com/~ratin/media/db.php",params);
        }
    
    }

	private void post(String string, List<NameValuePair> params) {
			// TODO Auto-generated method stub
			
		}

	public void next (View view)
    {
    	 Intent intent = new Intent(Inc.this, MaizeIn.class);
         startActivity(intent);

    }
    public void end (View view)
    
    {
    	super.finish();
    }
    public void send (View view)
    {
    	tit=title.getText().toString();
    	mahali=place.getText().toString();
    	
    	incd=details.getText().toString();
    	
    	slong = longi.getText().toString();
    	slat = lati.getText().toString();//|| slong.equals("") || slat.equals(""))
 if  (tit.equals("") || mahali.equals("") || incd.equals("") )
	 {Toast.makeText(Inc.this, "Please enter the blank fields  or make sure your GPS is active and you can see your coodinates to continue....", Toast.LENGTH_SHORT).show();}


else 
    	{progress = ProgressDialog.show(this, "Please Wait..", "Submitting incident..", true,
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
		nameValuePairs.add(new BasicNameValuePair("title", title.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("mahali",place.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("incd",details.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("ykood", slat.trim()));   
	    nameValuePairs.add(new BasicNameValuePair("xkood", slong.trim()));
	    nameValuePairs.add(new BasicNameValuePair("imei", deviceId.trim()));
       
	   
	    
	    
        	
		
        	
        	
		    try{
		    	  		       
		    	    HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost("http://ratin.arvixevps.com/~ratin/map_content/mobile/incident.php");
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
		            	
		            	answer="Data not entered succesfully. TYR AGAIN!";
		            	//finish();
		            }else{
		            //ends validator
		            answer="Data Sent";
		            finish();
		            //Intent intentpic = new Intent(Inc.this, Upload.class);
		           // startActivity(intentpic);
               	// Intent intent = new Intent(Cereals.this, Menu.class);
                  //  startActivity(intent);
		    }*/
		            answer="Incident submitted successfully";
		            finish();
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
			Toast.makeText(Inc.this, answer , Toast.LENGTH_LONG).show();
			
			

		}
	};

}