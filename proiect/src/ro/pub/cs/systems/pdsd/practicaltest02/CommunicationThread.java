package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket != null) {
        	 	
            try {
                       	
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (key /value/operatio type )!");
                    String operation = bufferedReader.readLine();
                    HashMap<String, String> data = serverThread.getData();
                    if(operation!=null){
                    	
                    	HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        try {
            				String pageSourceCode = httpClient.execute(httpGet, responseHandler);
            				Log.i(Constants.TAG,"[COMMUNICATION THREAD]"+ pageSourceCode);
                        } catch (ClientProtocolException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			} catch (IOException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}          
                    	
                    	Log.i(Constants.TAG,"[COMMUNICATION THREAD]"+ operation);
                    	String[] tokens = operation.split(",");
                    	String result = null;
                    	for (int i = 0; i < tokens.length; i++)
                    		Log.i(Constants.TAG,"[COMMUNICATION THREAD]"+ tokens[i]);
                    	if(tokens[0].equals("put")){
                    		Log.i(Constants.TAG, "[COMMUNICATION THREAD]"+"adding " + tokens[1] + " " + tokens[2]);	
                    		serverThread.setData(tokens[1], tokens[2]);
                    	}
                    	if(tokens[0].equals("get")){
                    		Log.i(Constants.TAG,"[COMMUNICATION THREAD]"+ "getting" + tokens[1]);
                    		String value = data.get(tokens[1]);
                    		if(value == null)
                    			result = "none\n";
                    		else
                    			result = value;
                    		printWriter.println(value);
                            printWriter.flush();
                    	}
                    	
                    }
                                                             
                                        
                } else {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
                }
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            
        } else {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
        }
    }

}
