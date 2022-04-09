package com.example.lighttouch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LTHome extends AppCompatActivity {

    /*public static final int SERVERPORT = 5040;

    public static final String SERVER_IP = "192.168.77.108";
    private LTHome.ClientThread clientThread;
    private Thread thread;


    TextView text;
    Button on;
    Button off;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lthome);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText("Tab " + (position + 1));
                    }
                }).attach();

        /*
        text = findViewById(R.id.label);
        on = findViewById(R.id.buttonOn);
        off = findViewById(R.id.buttonOff);

        clientThread = new LTHome.ClientThread();
        thread = new Thread(clientThread);
        thread.start();


        on.setOnClickListener((v) ->{
            text.setText("Lamp is on");
            String on_msg = "on";
            clientThread.sendMessage(on_msg.trim());
        });

        off.setOnClickListener((v) ->{
            text.setText("Lamp is off");
            String on_msg = "off";
            clientThread.sendMessage(on_msg.trim());
        });
        */
    }

    /*class ClientThread implements Runnable{
        private Socket socket;
        private BufferedReader input;

        @Override
        public void run(){
            try{
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);

                while(!Thread.currentThread().isInterrupted()){
                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if (null == message || "!D".contentEquals(message)) {
                        Thread.interrupted();
                        message = "The server has disconnected";
                        text.setText(message);
                        break;
                    }
                }
            }catch (UnknownHostException e1){
                e1.printStackTrace();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }

        void sendMessage(final String message){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        if(null != socket){
                            PrintWriter out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream()))
                                    ,true);
                            out.println(message);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(null != clientThread){
            clientThread.sendMessage("!D");
            clientThread = null;
        }
    }*/
}