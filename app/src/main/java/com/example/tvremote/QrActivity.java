package com.example.tvremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tvremote.utils.QrCodeGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private ExecutorService service = Executors.newFixedThreadPool(2);
    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private String request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        qrCodeImageView = findViewById(R.id.qr_code_image_view);

        // Example string to encode as QR code
        String qrCodeText = getIpAddress();
        int width = 500;
        int height = 500;

        Bitmap qrCodeBitmap = QrCodeGenerator.generateQRCode(qrCodeText, width, height);
        if (qrCodeBitmap != null) {
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        }
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(3005);
                    while (isRunning) {
                        Socket clientSocket = serverSocket.accept();
                        handleClient(clientSocket);
                    }
                }catch (Exception e){
                    Toast.makeText(QrActivity.this, "server did not start", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            StringBuilder requestBuilder = new StringBuilder();
            char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = in.read(buffer)) != -1) {
                requestBuilder.append(buffer, 0, charsRead);
                if (charsRead < buffer.length) break;
            }
            request = requestBuilder.toString();
            System.out.println("Received: " + request);
            String response = "OK\n";
            out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                Intent intent = new Intent(QrActivity.this, GalleryActivity.class);
                intent.putExtra("IP", request);
                startActivity(intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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