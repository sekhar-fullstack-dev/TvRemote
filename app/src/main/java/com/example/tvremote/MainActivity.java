package com.example.tvremote;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// MainActivity.java
public class MainActivity extends FragmentActivity {

    private static final int LISTENING_PORT = 12345;
    private Socket socket;
    private static final int SERVER_PORT = 8000;
    private String TAG="MainActivity";
    private ImageView pointer;
    private TextView tvIpAddress;
    private List<String> messages = new ArrayList<>();
    private int REQUEST_CODE = 1000;
    private WindowManager windowManager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pointer = findViewById(R.id.pointer);
        tvIpAddress = findViewById(R.id.ipAddresss);

        tvIpAddress.setText(getIpAddress());

        new Thread(new ClientThread()).start();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(this, "permission", Toast.LENGTH_SHORT).show();
                    initializeOverlay();
                }
            }
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // Permission granted, set up the overlay
                    initializeOverlay();
                } else {
                    // Permission not granted
                    Toast.makeText(this, "Overlay permission is required for the pointer to display over other apps.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void initializeOverlay() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // Define the position of the overlay
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        windowManager.addView(pointer, params);
    }



    class ClientThread implements Runnable {
        @Override
        public void run() {
            /*try {
                socket = new Socket(etIPAddress.getText().toString(), SERVER_PORT);
                Log.d(TAG, "run: socket connected");
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("Hi");
                while (!Thread.currentThread().isInterrupted()) {
                    //out.println("Hi");
                    String s = input.readLine();
                    if (s.contains(":")){
                        String[] co_or = s.split(":");
                        updatePointerPosition(Integer.parseInt(co_or[0]),Integer.parseInt(co_or[1]));
                        Log.d(TAG, "readfromServer: "+s);
                    }
                }
            } catch (IOException e) {
                Log.e("ClientThread", "Error connecting to server", e);
            }*/
            try {
                DatagramSocket socket = new DatagramSocket(LISTENING_PORT);
                byte[] buffer = new byte[1024]; // Adjust size based on expected data
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                int count=0;

                while (true) {
                    socket.receive(packet);
                    count++;
                    String received = new String(packet.getData(), 0, packet.getLength());
                    //Toast.makeText(MainActivity.this, ""+received, Toast.LENGTH_SHORT).show();
                    int finalCount = count;
                    if (received.contains(":")){
                        String[] co_or = received.split(":");
                        updatePointerPosition(Integer.parseInt(co_or[0]),Integer.parseInt(co_or[1]));
                        Log.d(TAG, "readfromServer: "+received);
                    }
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messages.add(received);
                            adapter.notifyDataSetChanged();
                        }
                    });*/
                }
            } catch (SocketException e) {
                System.err.println("SocketException: " + e.getMessage());
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                System.err.println("Exception: " + e.getMessage());
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatePointerPosition(int x, int y) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)pointer.getLayoutParams();

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int screenWidth = size.x;
                int screenHeight = size.y;

                // Update the constraints to move the pointer
                layoutParams.leftMargin = (int) ((float)x/1920*screenWidth);
                layoutParams.topMargin = (int) ((float)y/1080*screenHeight);

                // Apply the new layout parameters
                pointer.setLayoutParams(layoutParams);*/


                // You can update the position of the pointer later by modifying params.x and params.y and then calling:
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int screenWidth = size.x;
                int screenHeight = size.y;

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

                // Define the position of the overlay
                params.gravity = Gravity.TOP | Gravity.LEFT;
                params.x = (int) ((float)x/1920*screenWidth);
                params.y = (int) ((float)y/1080*screenHeight);
                windowManager.updateViewLayout(pointer, params);
            }
        });
    }


    private String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }


}
