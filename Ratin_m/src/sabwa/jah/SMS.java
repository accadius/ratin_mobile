package sabwa.jah;


import java.io.IOException;
	import java.io.InputStream;
import java.net.SocketTimeoutException;
	import java.util.ArrayList;
	import java.util.List;
	import android.location.Location;
	import android.location.LocationListener;
	import android.location.LocationManager;

	import android.widget.Toast;

import org.apache.http.HttpEntity;
	import org.apache.http.HttpResponse;
	import org.apache.http.NameValuePair;
	import org.apache.http.client.ClientProtocolException;
	import org.apache.http.client.HttpClient;
	import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
	import org.apache.http.client.methods.HttpPost;
	import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
	import org.apache.http.impl.client.DefaultHttpClient;
	import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;


	import android.app.Activity;
import android.app.AlertDialog;
	import android.app.ProgressDialog;
import android.location.LocationManager;
import android.net.Uri;
	import android.os.Bundle;
	import android.os.Handler;
	import android.os.Message;
import android.view.Gravity;
	import android.view.KeyEvent;
	import android.widget.TextView;

	import android.app.ProgressDialog;
	import android.preference.PreferenceActivity;
	import android.preference.PreferenceManager;
import android.provider.Settings;
	import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
	import android.content.Intent;
	import android.content.SharedPreferences;
	import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
	import android.util.Log;
	import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
	import android.widget.Button;
	import android.widget.CheckBox;
	import android.widget.EditText;
import android.widget.Spinner;
	import android.widget.TextView;
	import android.widget.Toast;
	//import yesno.dialog.R;
	import android.app.AlertDialog;
import android.content.DialogInterface;


public class SMS extends Activity implements Runnable {
    /** Called when the activity is first created. */
	public boolean FirstLoad = true;
	 public byte[] data;
	 public StringBuffer buffer;
	 public InputStream inputStream;
	 public HttpResponse response;
	 public HttpClient httpclient;
	 public HttpPost httppost;
	private EditText wsale_bp, retail_bp, mako;
	private TextView longi, lati;
	private ProgressDialog progress;
	private String answer, mz, bn, mk, ric, whe, slong, slat, cereal;
	 private LocationManager locmgr = null;
	 public Uri uri;
	 public AlertDialog.Builder builder;
		public AlertDialog alertDialog, jahz;
		public AlertDialog.Builder alertbox;
	 double longitude, latitude;
	 public Intent it;
	 String[] grain = 
		{
			"",
			"Maize",
			"Beans",
			"Rice",
			"Wheat",
			"Sorghum",
			"Millet",
			
		};
		
		Spinner sp;
	 
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);
        /*SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        wsale_bp = (EditText) findViewById(R.id.w_bp);
        retail_bp = (EditText) findViewById(R.id.r_bp);
        cereal = (Button) findViewById(R.id.login);
        longi = (TextView) findViewById(R.id.xCood);
        lati = (TextView) findViewById(R.id.yCood);
        locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        */
        
        

        // prepare the alert box
      alertbox = new AlertDialog.Builder(this);
      alertbox.setTitle("ALERT");
        // set the message to display
        alertbox.setMessage("Dear Market Monitor. Once again Looks like there is an error in Internet Connection. Would you like to submit data via SMS? ");

        // set a positive/yes button and create a listener
        alertbox.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {

            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(getApplicationContext(), "'Yes' button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // set a negative/no button and create a listener
        alertbox.setNegativeButton("SMS", new DialogInterface.OnClickListener() {

            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
            	
            	startActivity(it);
            	
                Toast.makeText(getApplicationContext(), "PLEASE do not alter this SMS whatsoever.  ", Toast.LENGTH_SHORT).show();
            }
        });

       

        // Set the Icon for the Dialog
        alertbox.setIcon(R.drawable.danger);
       

    	 

        
        
        
        
        wsale_bp = (EditText) this.findViewById(R.id.w_bp);
        mako = (EditText) this.findViewById(R.id.m_name);
        
        retail_bp = (EditText) this.findViewById(R.id.r_bp);
       // retail_sp = (EditText) this.findViewById(R.id.r_sp);
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
    	wsale_bp.setText("");
    	//wsale_sp.setText("");
    	retail_bp.setText("");
    	//retail_sp.setText("");
   
    	
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
    	mz=wsale_bp.getText().toString();
    	mk=mako.getText().toString();
    	
    	ric=retail_bp.getText().toString();
    	//whe=retail_sp.getText().toString();
    	slong = longi.getText().toString();
    	slat = lati.getText().toString();
 if  (mz.equals("") ||  ric.equals("") ||  slong.equals("") || slat.equals(""))
	 {Toast.makeText(SMS.this, "Please enter the blank fields  or make sure your GPS is active and you can see your coodinates to continue....", Toast.LENGTH_SHORT).show();}


else 
    	{progress = ProgressDialog.show(this, "Please Wait..", "Sending Market Data..", true,
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
		nameValuePairs.add(new BasicNameValuePair("W_BP", wsale_bp.getText().toString()));
	   // nameValuePairs.add(new BasicNameValuePair("W_SP",wsale_sp.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("R_BP",retail_bp.getText().toString()));
	  //  nameValuePairs.add(new BasicNameValuePair("R_SP",retail_sp.getText().toString()));
	    nameValuePairs.add(new BasicNameValuePair("Cereal", cereal));
	    nameValuePairs.add(new BasicNameValuePair("ykood", slat.trim()));   
	    nameValuePairs.add(new BasicNameValuePair("xkood", slong.trim()));
       
	   
	    
	    
        	
		 uri = Uri.parse("smsto:0708021448");
    	  it = new Intent(Intent.ACTION_SENDTO, uri);
    	 it.putExtra("sms_body", "DATA#"+mk+","+cereal+","+mz+","+ric+"");
    	// startActivity(it);
        	
        	
		    try{
		    	  		       
		    	    HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost("http://ratin.eagc.org/MIS/mobile/cereals.php");
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
		            	
		            	answer="Data Sent Successful";
		            	finish();
		            }else
		            
		            if(buffer.charAt(0)=='Z')
		            {
		            	
		            	answer="Data is out of range, Please Give reason ";
		            	finish();
		            }else{
		            //ends validator
		            answer="Data not entered succesfully ";
		        //    finish();
               	// Intent intent = new Intent(Maize.this, Menu.class);
                  //  startActivity(intent);
		    }
		    }
		    catch(Exception e)
		    {
		        //Log.e("log_tag", "Error in http connection "+e.toString());
		    	 answer="Warning: Error in http connection "+e.toString();
		    	 handlerz.sendEmptyMessage(1);
		    
		    	 
		    	 
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
	private Handler handlerz = new Handler() {
	   	
		 public void handleMessage(Message msg) {
				
		
				 // display box
				alertbox.show();
	            
			

			}
			
		
		
	};   
		 


}