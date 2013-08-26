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
	import android.app.Activity;
import android.content.Context;
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
	public class Login extends Activity implements Runnable
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
	    private ProgressDialog pd;
	    private String Str_check2;
	    public String answer;
	    private String url;
	    private String  slong="", slat="";
	    private LocationManager locmgr = null;
		 double longitude, latitude;
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
	        
	        String Str_user = app_preferences.getString("username","0" );
	        String Str_pass = app_preferences.getString("password", "0");
	        String Str_longi = app_preferences.getString("xCood", "");
	        String Str_lati = app_preferences.getString("yCood", "");
	        String Str_check = app_preferences.getString("checked", "no");
	        
	        
	        

	        
	        
	        
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
	            	
	            	pd = ProgressDialog.show(Login.this, "Please Wait...", "Validating..", true,
	        				false);

	        		Thread thread = new Thread(Login.this);
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
	    
	       
	        
	        public void run() {
	    		//call the class here for the process to run in the thread
	    		
	        	url="http://ratin.eagc.org/MIS/mobile/index.php";
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
		            		                	 Intent intent = new Intent(Login.this, Premenu.class);
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
	            	                
	            	                /*   catch (ConnectTimeoutException cte ){
	            	                	  //Took too long to connect to remote host
	            	                	   answer="Error: Could not conect to the Host ";//+cte.toString();
	            	                	}
	            	                	catch (SocketTimeoutException ste){
	            	                	  //Remote host didn’t respond in time
	            	                		answer=" Error in internet Connection";//+ste.toString();
	            	                	}*/
	            		                catch (Exception e)
	            	                    {
	            		                	// General Error when no response at all.
	            		                	answer="Warning: Error in internet connection "+e.toString();
	            		                	
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
	    			
	    			Toast.makeText(Login.this, answer , Toast.LENGTH_LONG).show();
	              


	    	

	    		}
	    	};
}
	    	