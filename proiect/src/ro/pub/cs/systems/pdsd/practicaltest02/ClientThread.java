package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.EditText;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String command;
    EditText value;
    

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String command,
            EditText value) {
        this.address = address;
        this.port = port;
        this.command = command;
        this.value = value;
        
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(command);
                printWriter.flush();
                String result;
                while ((result = bufferedReader.readLine()) != null){
                	Log.e(Constants.TAG, "[CLIENT THREAD] result" + result);
                	final String info = result;
                    value.post(new Runnable() {
                        @Override
                        public void run() {
                            value.setText(info + "\n");
                        }
                    });
                	
                }
                
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
