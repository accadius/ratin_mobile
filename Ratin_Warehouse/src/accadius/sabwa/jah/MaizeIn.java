package accadius.sabwa.jah;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

@SuppressWarnings("unused")
public class MaizeIn extends Activity implements Runnable{
	
	public boolean FirstLoad = true;
	public byte[] data;
	 public StringBuffer buffer;
	 public InputStream inputStream;
	 public HttpResponse response;
	 public HttpClient httpclient;
	 public HttpPost httppost;
	 public Button end, clear, send;
	private EditText GrainVol, GrainPrice, GrainStock,whouse_jina;
	private ProgressDialog progress;
	private String answer, mzVol,jina, mzPrice, mzStock, slong, slat, mzFlo ,cereal, cereal1;
	 private LocationManager locmgr = null;
	 double longitude, latitude;
	 public AlertDialog.Builder alertbox;
	public RadioButton mzIn, mzOut;
	public Uri uri;
	public Intent it;
	 private TextView longi, lati;
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
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.maize_in);
	        longi = (TextView) findViewById(R.id.xCood);
	        lati = (TextView) findViewById(R.id.yCood);
	        GrainVol=(EditText) this.findViewById(R.id.W_Maize_in_volume);
	        //GrainPrice=(EditText) this.findViewById(R.id.W_Maize_in_price);
	        whouse_jina=(EditText) this.findViewById(R.id.Whouse_name);
	         mzIn = (RadioButton) this.findViewById(R.id.in);
	         mzOut = (RadioButton) this.findViewById(R.id.out);
	         send = (Button) findViewById(R.id.btnNext);
	         clear = (Button) findViewById(R.id.btnClear);
	         end = (Button) findViewById(R.id.btnExit);
	
	         
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
	        	         
	         locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        
	         

	         // prepare the alert box
	       alertbox = new AlertDialog.Builder(this);
	       alertbox.setTitle("ALERT");
	         // set the message to display
	         alertbox.setMessage("Dear Warehouse Monitor. Once again Looks like there is an error in Internet Connection. Would you like to submit data via SMS? ");

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
	    	GrainVol.setText("");
	    	//GrainPrice.setText("");
	    //	GrainStock.setText("");
	    	
	    }
	    public void end(View view)
	    {
	    	super.finish();
	    }
	    public void send (View view)
	    {
	    	if (mzIn.isChecked()== true)
	    		{
	    		mzFlo=mzIn.getText().toString();
	    		}
	    	else if (mzOut.isChecked()== true){
    		mzFlo=mzOut.getText().toString();
    		}
	    	else 
	    		{
	    		{Toast.makeText(MaizeIn.this, "Please Select Direction of Grain flow", Toast.LENGTH_SHORT).show();}
	    		}
	    	//slong = Double.toString(longitude);
	     //   slat = Double.toString(latitude);
	        slong = longi.getText().toString();
        	slat = lati.getText().toString();
	    	mzVol=GrainVol.getText().toString();
	    	jina=whouse_jina.getText().toString();
	    //	mzStock=GrainStock.getText().toString();
	    		
	    	
	 if  (mzVol.equals("")  || slong.equals("") || slat.equals(""))
		 {Toast.makeText(MaizeIn.this, "Please enter the blank fields  or make sure your GPS is active and you can see your coodinates to continue....", Toast.LENGTH_SHORT).show();}


	else 
	    	{progress = ProgressDialog.show(this, "Please Wait..", "Sending Warehouse Data..", true,
					false);

			Thread thread = new Thread(this);
			thread.start();
			return ;}
	    }
	public void run() {
		// TODO Auto-generated method stub

		//call the class here for the process to run in the thread
		
		//trying to connect and add data in the database starts here
		
		   //String result = "";
    	

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("Cereal", cereal));
		nameValuePairs.add(new BasicNameValuePair("GrainFlow", mzFlo));
		nameValuePairs.add(new BasicNameValuePair("GrainVolume", mzVol));
	   // nameValuePairs.add(new BasicNameValuePair("GrainPrice",mzPrice));
	   // nameValuePairs.add(new BasicNameValuePair("GrainStock",mzStock));
	    nameValuePairs.add(new BasicNameValuePair("ykood", slat.trim()));   
	    nameValuePairs.add(new BasicNameValuePair("xkood", slong.trim()));
       
	    uri = Uri.parse("smsto:+254708021448");
  	  it = new Intent(Intent.ACTION_SENDTO, uri);
  	 it.putExtra("sms_body", "W#"+jina+","+cereal+","+mzFlo+","+mzVol+"");
	    
	    try{
		    	  		       
		    	    HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost("http://ratin.arvixevps.com/~ratin/map_content/mobile/whouse_flow.php");
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
		            int len = 1;
		            while (-1 != (len = inputStream.read(data)) )
		            {
		                buffer.append(new String(data, 0, len));
		            }

		            inputStream.close();
		            
		            if(buffer.charAt(0)=='Y')
		            {
		            	
		            	
		            }else{
		            //ends validator
		            answer="Data Not Sent. TRY AGAIN PLEASE";
		           // finish();
               //	 Intent intent = new Intent(MaizeIn.this, Menu.class);
                   // startActivity(intent);
		    }*/
		    answer="Data  entered succesfully";
		            	finish();
		    }
		    catch(Exception e)
		    {
		        //Log.e("log_tag", "Error in http connection "+e.toString());
		    	 answer="Warning:Error in internet Connection "+ e.toString();
		    
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
			Toast.makeText(MaizeIn.this, answer , Toast.LENGTH_LONG).show();
			
			

		}
	};
	private Handler handlerz = new Handler() {
	   	
		 public void handleMessage(Message msg) {
				
		
				 // display box
				alertbox.show();
	            
			

			}
			
		
		
	};   

}
