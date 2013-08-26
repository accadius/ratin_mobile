 package ratin.accadius.sabwa.jah;

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
	import android.os.Bundle;
	import android.os.Handler;
	import android.os.Message;
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
	import android.widget.Button;
	import android.widget.CheckBox;
	import android.widget.EditText;
	import android.widget.TextView;
import android.widget.Toast;
	 
	@SuppressWarnings("unused")
	public class Tdbase extends Activity implements Runnable
	{
	    /** Called when the activity is first created. */
	 
	    public Button login;
	    public  String name="",pass="";
	    public EditText username,password;
	    public TextView tv;
	    public byte[] data;
	    public HttpPost httppost;
	    public StringBuffer buffer;
	    public HttpResponse response;
	    public HttpClient httpclient;
	    public InputStream inputStream;
	    public SharedPreferences app_preferences ;
	    public List<NameValuePair> nameValuePairs;
	    public CheckBox check;
	    private ProgressDialog pd, dialoc;
	    private String Str_check2;
	    public String answer;
	    private String url;
	    private String  slong="", slat="";
	    private LocationManager locmgr = null;
		 double longitude, latitude;
			public AlertDialog.Builder alertbox;
		 private TextView longi, lati;
	    @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
	        username = (EditText) findViewById(R.id.username);
	        password = (EditText) findViewById(R.id.password);
	        login = (Button) findViewById(R.id.login);
	        check = (CheckBox) findViewById(R.id.check);
	        longi = (TextView) findViewById(R.id.xCood);
	        lati = (TextView) findViewById(R.id.yCood);
	        locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        
	        
	        
	        
	        
	        
	        boolean isGPSEnabled = false;
			try
	        {
	            isGPSEnabled = ((LocationManager) locmgr).isProviderEnabled(LocationManager.GPS_PROVIDER);
	            
	        	
	            
	        
	        }
	        catch (Exception ex) {}

	        
	if(isGPSEnabled)
	        {


	     //   locmgr.removeUpdates(onLocationChange);

		
	   
       if(locmgr != null){
    	   dialoc = ProgressDialog.show(this, "Please wait...","Acquiring GPS Location ...", true);
    	   locmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,10000.0f, onLocationChange);
       }else{
    	  // dialoc.dismiss();
    	   Toast.makeText(Tdbase.this, "GPS signal acquired", Toast.LENGTH_LONG).show(); 
       }

	  	        }

    
	        else
	        {
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setTitle("ALERT!")
	            		.setIcon(R.drawable.danger)
	            		.setMessage("GPS Setting is disabled. Kindly Press YES like to enable it ?")
	                   .setCancelable(false)
	                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {

	                             //Sent user to GPS settings screen
	                             final ComponentName toLaunch = new ComponentName("com.android.settings","com.android.settings.SecuritySettings");
	                             final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                             intent.addCategory(Intent.CATEGORY_LAUNCHER);
	                             intent.setComponent(toLaunch);
	                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                             startActivityForResult(intent, 1);

	                             dialog.dismiss();

	                       }
	                   })
	                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();
	                       }
	                   });
	            AlertDialog alert = builder.create();
	            alert.show();
	        }
	        
	        
	        
	        

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        String Str_user = app_preferences.getString("username","" );
	        String Str_pass = app_preferences.getString("password", "");
	        String Str_longi = app_preferences.getString("xCood", "");
	        String Str_lati = app_preferences.getString("yCood", "");
	        String Str_check = app_preferences.getString("checked", "no");
	        
	        
	        // prepare the alert box
	          alertbox = new AlertDialog.Builder(this);
	          alertbox.setTitle("ERROR");
	            // set the message to display
	            alertbox.setMessage("Dear Border Monitor. Looks like there is an error in Internet Connection. Would you like to submit data via SMS? ");
	 
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
	                	
	                	Intent intentz = new Intent(Tdbase.this, MaizeIn.class);
	                    startActivity(intentz);
	                	
	                    Toast.makeText(getApplicationContext(), "Please enter Warehouse name in the field (Warehouse Name)", Toast.LENGTH_SHORT).show();
	                }
	            });
	 
	           

		        // Set the Icon for the Dialog
		        alertbox.setIcon(R.drawable.danger);
		        



	        
	        
	        
	        if(Str_check.equals("yes"))
	        {
	                username.setText(Str_user);
	                password.setText(Str_pass);
	             //   lati.setText(slat.valueOf(latitude));
	               // longi.setText( slong.valueOf(longitude) );
	               //longi.setText(Str_longi);
	               // lati.setText(Str_lati);
	                check.setChecked(true);
	        }
	        login.setOnClickListener(new View.OnClickListener()
	        {
	            public void onClick(View v)
	            {
	                      	
	                   	
	            	//create a new thread to run this process 
	            	
	            	pd = ProgressDialog.show(Tdbase.this, "Please Wait...", "Validating..", true,
	        				false);

	        		Thread thread = new Thread(Tdbase.this);
	        		thread.start();
	        

	            	
	            	/// ends the thread
	        		
	            	
	            	
	            	
	                
	            }
	        });
	        
	        
	        
	        //listening for the check box 
	        check.setOnClickListener(new View.OnClickListener()
	        {
	            public void onClick(View v)
	            {
	                // Perform action on clicks, depending on whether it's now checked
	                SharedPreferences.Editor editor = app_preferences.edit();
	                if (((CheckBox) v).isChecked())
	                {
	 
	                     editor.putString("checked", "yes");
	                     editor.commit();
	                }
	                else
	                {
	                     editor.putString("checked", "no");
	                     editor.commit();
	                }
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
	            if(dialoc.isShowing())
    	        {
    			 Toast.makeText(Tdbase.this, "GPS location acquired", Toast.LENGTH_LONG).show();    
    			 dialoc.dismiss();
    	        }   

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
	    
	    public void end (View view)
	    
	    {
	    	super.finish();
	    }
	        
	        public void run() {
	    		//call the class here for the process to run in the thread
	    		
	        	url="http://ratin.arvixevps.com/~ratin/map_content/mobile/cross.php";
	        	name = username.getText().toString();
	        	pass = password.getText().toString();
	        	slong = longi.getText().toString();
	        	slat = lati.getText().toString();
	        	
	            Str_check2 = app_preferences.getString("checked", "no");
	            
	            if(Str_check2.equals("yes"))
                {
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putString("username", name);
                    editor.putString("password", pass);
                    editor.putString("xCood", slong);
                    editor.putString("yCood", slat);
                     editor.commit();
                }
	            if(slong.equals("") || slat.equals("") )
                {
                    answer="Please wait for GPS to aquire location.."; //Toast.makeText(Tdbase.this, "Blank Field..Please Enter", Toast.LENGTH_LONG).show();
                }
	            //now lets put the code to connect to the database.;
	            //else if
	            
	            
	            else if(name.equals("") )
                {
                    answer="Please Enter Username to continue ..."; //Toast.makeText(Tdbase.this, "Blank Field..Please Enter", Toast.LENGTH_LONG).show();
                }else if(pass.equals("") )
                {
                    answer="Please Enter Password to continue ..."; //Toast.makeText(Tdbase.this, "Blank Field..Please Enter", Toast.LENGTH_LONG).show();
                }else
	            		                {
	            	 
	            	                try {
	            	                	// preparing data to send
	            	                	          	                	        		                   
	            		                               		                   
	            		                   
	            	                       httpclient = new DefaultHttpClient();	            	                      
	            		                   httppost = new HttpPost(url);
	            		                   
	            		                   /* add this code to be able to catch the ConnectionTimeoutExeption and sockettimeoutExeption*/
	            		                   
	            		                   HttpParams params = httpclient.getParams();
		            	                   HttpConnectionParams.setConnectionTimeout(params,3000);
		            		               HttpConnectionParams.setSoTimeout(params, 3000);
		            		                 
	            		                    // Add your data
	            		                    nameValuePairs = new ArrayList<NameValuePair>(2);
	            		                   nameValuePairs.add(new BasicNameValuePair("UserEmail", name.trim()));
	            		                    nameValuePairs.add(new BasicNameValuePair("Password", pass.trim()));
	            		                    nameValuePairs.add(new BasicNameValuePair("xkood", slong.trim()));
	            		                    nameValuePairs.add(new BasicNameValuePair("ykood", slat.trim()));
	            		                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            		 
	            		                    // Execute HTTP Post Request
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
	            		                    	
		            		                	answer="login successfull";
		            		                	finish();
		            		                	 Intent intent = new Intent(Tdbase.this, Premenu.class);
		            		                     startActivity(intent);
	            		                    	
												
		            		                	//setContentView(R.layout.splash);
		            		                	
		            		                    //Toast.makeText(mydatabaseProject.this, "login successfull", Toast.LENGTH_LONG).show();
		            		                }
		            		                else
		            		                {
		            		                	answer="Invalid Username or password Or You are not within your boundary!!!";
		            		                    //Toast.makeText(mydatabaseProject.this, "Invalid Username or password", Toast.LENGTH_LONG).show();
		            	                     }
	            		                            		                    
	            		                    
	            		                } // end try
	            	                
	            	                   catch (ConnectTimeoutException cte ){
	            	                	  //Took too long to connect to remote host
	            	                	   answer="Error: Could not conect to the Host ";//+cte.toString();
	            	                	}
	            	                	catch (SocketTimeoutException ste){
	            	                	  //Remote host didn’t respond in time
	            	                		answer=" Error in internet Connection";//+ste.toString();
	            	                	}
	            		                catch (Exception e)
	            	                    {
	            		                	// General Error when no response at all.
	            		                	answer="Warning: Error in internet connection "+e.toString();
	            		                	handlerz.sendEmptyMessage(1);
	            		                } //end of  try - catches
	            	                
	            		                 	                  
	            		                
	            		       } //end else
	            		                
	            
	            
	            //end of the code connecting to the HTTP request
	                                	
	    		handler.sendEmptyMessage(0);
	    	}

	        
	        //end of the process ,.. the results display
	 private Handler handler = new Handler() {

	    		@Override
	    		public void handleMessage(Message msg) {
	    			//After the background process terminates it 
	    			pd.dismiss();
	    			//tv.setText(pi_string);
	    			//tv.setText(names);
	    			
	    			Toast.makeText(Tdbase.this, answer , Toast.LENGTH_LONG).show();
	              


	    	

	    		}
	    	};
	    	 private Handler handlerz = new Handler() {
	    		   	
	    		 public void handleMessage(Message msg) {
	    				
	    		
	    				 // display box
	    				alertbox.show();
	    				   // show it
	    	           // dialogz.show();
	    			

	    			}
	    			
	    		
	    		
	    	};   
}
	    	