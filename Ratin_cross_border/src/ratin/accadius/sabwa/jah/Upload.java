package ratin.accadius.sabwa.jah;

import java.io.DataInputStream; 
import java.io.DataOutputStream; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.IOException; 
import java.net.HttpURLConnection; 
import java.net.MalformedURLException; 
import java.net.URL; 
import android.app.Activity; 
import android.os.Bundle; 
import android.telephony.TelephonyManager;
import android.util.Log; 
@SuppressWarnings("unused")
public class Upload extends Activity { 
    /** Called when the activity is first created. */ 
	public String deviceId, imeiz;
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.incident); 
        doFileUpload(); 
    } 
    private void doFileUpload() 
    { 
    	deviceId = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
    	imeiz=String.valueOf(deviceId);
        HttpURLConnection connection = null; 
        DataOutputStream outputStream = null; 
        DataInputStream inputStream = null; 
        String pathToOurFile = "/sdcard/"+deviceId.trim()+"_CB.jpg"; 
        String urlServer = "http://ratin.arvixevps.com/~ratin/ratin/media/db.php"; 
        String lineEnd = "\r\n"; 
        String twoHyphens = "--"; 
        String boundary =  "*****"; 
        int bytesRead, bytesAvailable, bufferSize; 
        byte[] buffer; 
        int maxBufferSize = 1*1024*1024; 
        try 
        { 
        FileInputStream fileInputStream = new FileInputStream(new 
File(pathToOurFile) ); 
        URL url = new URL(urlServer); 
        connection = (HttpURLConnection) url.openConnection(); 
        // Allow Inputs & Outputs 
        connection.setDoInput(true); 
        connection.setDoOutput(true); 
        connection.setUseCaches(false); 
        // Enable POST method 
        connection.setRequestMethod("POST"); 
        connection.setRequestProperty("Connection", "Keep-Alive"); 
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary); 
        outputStream = new 
DataOutputStream( connection.getOutputStream() ); 
        outputStream.writeBytes(twoHyphens + boundary + lineEnd); 
        outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd); 
        outputStream.writeBytes(lineEnd); 
        bytesAvailable = fileInputStream.available(); 
        bufferSize = Math.min(bytesAvailable, maxBufferSize); 
        buffer = new byte[bufferSize]; 
        // Read file 
        bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
        while (bytesRead > 0) 
        { 
        outputStream.write(buffer, 0, bufferSize); 
        bytesAvailable = fileInputStream.available(); 
        bufferSize = Math.min(bytesAvailable, maxBufferSize); 
        bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
        } 
        outputStream.writeBytes(lineEnd); 
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + 
lineEnd); 
        // Responses from the server (code and message) 
        int serverResponseCode = connection.getResponseCode(); 
        String serverResponseMessage = connection.getResponseMessage(); 
        fileInputStream.close(); 
        outputStream.flush(); 
        outputStream.close(); 
        } 
        catch (Exception ex) 
        { 
        //Exception handling 
        } 
    } 
} 
