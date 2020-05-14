package com.example.tempero;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class UDPReceiver extends Thread {
    private  ArrayList<Module> modules;
    private DatagramSocket serverSocket = null;
    private int port = 1338;
    private ListAdapter adapter;
    private boolean findServer = false;
    private Date lastUpdate = new Date(new Date().getTime() - 60*1000);
    private Date socketDate = new Date();
    private MainActivity m;

    UDPReceiver(ArrayList<Module> modules, ListAdapter adapter, MainActivity m)
    {
        this.modules = modules;
        this.adapter = adapter;
        this.m = m;

        adapter.clear();
    }

    public void run()
    {
        try {
            serverSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[255];

            Log.e("TEMPERO", "Listening on udp:" + InetAddress.getLocalHost()+":"+ port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            Log.e("TEMPERO:", "Server started!");


            new Thread(new Runnable() {
                public void run() {
                    while(true) {
                        if (findServer == false) {
                            try {
                                if((new  Date()).getTime() - socketDate.getTime() >= 2000)
                                {
                                    socketDate = new Date();
                                    String data = "1,0";
                                    DatagramPacket sendPacket = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getByName("255.255.255.255"), 1337);
                                    serverSocket.send(sendPacket);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        findServer = checkLastUpdate();
                    }
                }
            }).start();

            while(true)
            {
                serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData(), 0, receivePacket.getLength() );

                InetAddress IPAddress = InetAddress.getByName(receivePacket.getAddress().toString().substring(receivePacket.getAddress().toString().indexOf("/")+1).trim());

                Log.e("TEMPERO", sentence);

                onReceive(split(sentence), IPAddress);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private boolean  checkLastUpdate()
    {
        if((new  Date()).getTime() - this.lastUpdate.getTime() >= 10*1000)
        {
            if(findServer == true) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        m.setLoading(true);
                    }
                });
            }
            return false;
        }

        if(findServer == false) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    m.setLoading(false);
                }
            });
        }
        return true;
    }


    private int[] split(String input)
    {
        String[] parts = input.split(",");
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            numbers[i] = (int)Integer.parseInt(parts[i]);
        }

        return numbers;
    }

    private void onReceive(int[] received, InetAddress serverIP) throws IOException {

        this.lastUpdate = new Date();

        if(received[0] == 0 && getModule(received[1]) == null)
            modules.add(new Module(received[1], received[2], Arrays.copyOfRange(received, 3, received.length), serverSocket, serverIP));
        else if(received[0] == 0 && getModule(received[1]) != null)
            getModule(received[1]).update(Arrays.copyOfRange(received, 3, received.length));
        else if(received[0] == 1 &&  getModule(received[2]) != null)
            getModule(received[1]).createGroup(m, modules, getModule(received[2]), received[3], received[4]);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

    private Module getModule(int id)
    {
        for (Module module : modules) {
            if (module.getID() == id) {
                return module;
            }
        }

        return null;
    }

}
