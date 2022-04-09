package com.example.lighttouch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwitchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwitchFragment extends Fragment {

    public static final int SERVERPORT = 5040;

    public static final String SERVER_IP = "192.168.142.108";
    private SwitchFragment.ClientThread clientThread;
    private Thread thread;


    TextView text;
    Button on;
    Button off;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SwitchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwitchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwitchFragment newInstance(String param1, String param2) {
        SwitchFragment fragment = new SwitchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_switch, container, false);

        text = view.findViewById(R.id.label);
        on = view.findViewById(R.id.buttonOn);
        off = view.findViewById(R.id.buttonOff);

        clientThread = new SwitchFragment.ClientThread();
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
        return view;
    }

    class ClientThread implements Runnable{
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
    public void onDestroy(){
        super.onDestroy();
        if(null != clientThread){
            clientThread.sendMessage("!D");
            clientThread = null;
        }
    }
}