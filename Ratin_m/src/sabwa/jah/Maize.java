package sabwa.jah;

/*------------------------------------------------------*
 * --------RATIN MARKET DATA CAPTURE--------------------*
 * ------------DEVELOPED BY-----------------------------* 
 * -----MOBILE: SABWA ACCADIUS BEN ---------------------*
 * --------CMS: PAUL NGUMII-----------------------------*
 * --------SMS: DANIEL MUSYOKA--------------------------*
 * ----MANAGER: DAVID MUTHAMI---------------------------*
 * ----COPYRIGHT: ESRI EASTERN AFRICA-------------------*
 * ---------------3RD FLOOR, KUSSCO CENTER--------------*
 * ---------------KILIMANJARO AVENUE, UPPER HILL--------*
 * ---------------P.O.BOX 57783 - 00200-----------------*
 * ---------------NAIROBI, KENYA------------------------*
 * ---------------WWW.ESRIEA.CO.KE----------------------*
 * -----------------------------------------------------*
 * */

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
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Maize extends Activity implements Runnable {
    /** Called when the activity is first created. */
	public boolean FirstLoad = true;
	 public byte[] data;
	 public StringBuffer buffer;
	 public InputStream inputStream;
	 public HttpResponse response;
	 public HttpClient httpclient;
	 public HttpPost httppost;
	private EditText wsale_bp, retail_bp, maketi;
	private TextView longi, lati;
	private ProgressDialog progress;
	private String answer, mz, bn, ric, mk,whe, slong, slat, cereal,deviceId;
	 private LocationManager locmgr = null;
	 double longitude, latitude;
	 public AlertDialog.Builder alertbox , alertbox_send;
	 public Intent it;
	 public Uri uri;
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
        setContentView(R.layout.maize);
        
      
        wsale_bp = (EditText) this.findViewById(R.id.w_bp);
        maketi = (EditText) this.findViewById(R.id.m_name);
        
        retail_bp = (EditText) this.findViewById(R.id.r_bp);
       // retail_sp = (EditText) this.findViewById(R.id.r_sp);
        longi = (TextView) findViewById(R.id.xCood);
        lati = (TextView) findViewById(R.id.yCood);
        deviceId = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
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
				Toast.makeText(getBaseContext(), 
						"Please select Product! ", 
						Toast.LENGTH_SHORT).show();
				
			}
        	
        });
    
    
   
    
    
    
    
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
    	 Intent intent = new Intent(Maize.this, Inc.class);
         startActivity(intent);

    }
    public void end (View view)
    
    {
    	super.finish();
    }
    public void send (View view)
    {
    	mz=wsale_bp.getText().toString();
    	mk=maketi.getText().toString();
    	
    	ric=retail_bp.getText().toString();
    	//whe=retail_sp.getText().toString();
    	slong = longi.getText().toString();
    	slat = lati.getText().toString();
 if  (mz.equals("") ||  ric.equals("") ||  slong.equals("") || slat.equals(""))
	 {Toast.makeText(Maize.this, "Please enter the blank fields  or make sure your GPS is active and you can see your coodinates to continue....", Toast.LENGTH_SHORT).show();}


else 
    	{
	
	handlerz_send.sendEmptyMessage(1);
	
}
    
    
    
    
    // prepare the alert box
    alertbox_send = new AlertDialog.Builder(this);
    alertbox_send.setTitle("ALERT");
      // set the message to display
    alertbox_send.setMessage("Dear Market Monitor.  \n Would you like to submit \n the following data? \n"+" Product: "+ cereal+ " \n W/Sale Price : "+ wsale_bp.getText().toString()+ "\n Retail Price : "+ retail_bp.getText().toString());
  

      // set a positive/yes button and create a listener
      alertbox_send.setPositiveButton("SEND", new DialogInterface.OnClickListener() {

          // do something when the button is clicked
          public void onClick(DialogInterface arg0, int arg1) {
              //Toast.makeText(getApplicationContext(), "'Yes' button clicked", Toast.LENGTH_SHORT).show();
          	progress = ProgressDialog.show(Maize.this, "Please Wait..", "Sending Market Data..", true,
      				false);
      		Thread thread = new Thread(Maize.this);
      		thread.start();
      		return ;
          }
      });

      // set a negative/no button and create a listener
      alertbox_send.setNegativeButton("CORRECT", new DialogInterface.OnClickListener() {

          // do something when the button is clicked
          public void onClick(DialogInterface arg0, int arg1) {
          	
           }
      });

     

      // Set the Icon for the Dialog
      alertbox_send.setIcon(R.drawable.danger);
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
	    nameValuePairs.add(new BasicNameValuePair("imei", deviceId.trim()));
       
	   
	    
	    
	    uri = Uri.parse("smsto:+254708021448");
  	  it = new Intent(Intent.ACTION_SENDTO, uri);
  	 it.putExtra("sms_body", "DATA#"+mk+","+cereal+","+mz+","+ric+"");
		
        	
        	
		    try{
		    	  		       
		    	    HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost("http://ratin.arvixevps.com/~ratin/map_content/mobile/cereals.php");
		           // HttpPost httppost = new HttpPost("http://ratin.eagc.org/MIS/mobile/cereals.php");
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
		            	
		            	answer="Data not entered. TRY AGAIN!!!";
		            	//finish();
		            }
		            
		            
		            
		            else
		            {
				            	 
		            //ends validator
		            
		            finish();
               	// Intent intent = new Intent(Maize.this, Menu.class);
                  //  startActivity(intent);
		    
		            }*/
		            answer="Data entered succesfully ";
		            finish();
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
			Toast.makeText(Maize.this, answer , Toast.LENGTH_LONG).show();
			
			

		}
	};
	private Handler handlerz = new Handler() {
	   	
		 public void handleMessage(Message msg) {
				
		
				 // display box
				alertbox.show();
	            
			

			}
			
	
		
	};   
	
	private Handler handlerz_send = new Handler() {
	   	
		 public void handleMessage(Message msg) {
				
		
				 // display box
				alertbox_send.show();
	            
			

			}
			
		
		
	};
	

}