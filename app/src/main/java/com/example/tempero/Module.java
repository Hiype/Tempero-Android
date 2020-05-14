package com.example.tempero;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class Module implements Parcelable, Serializable {
    private int hwid;
    private int type;
    private int[] data;
    private DatagramSocket socket;
    private InetAddress ip;
    private Group group;

    Module(int hwid, int type, int[] data, DatagramSocket socket, InetAddress ip)
    {
        this.hwid = hwid;

        this.type = type;
        this.socket = socket;
        this.data = data;
        this.ip = ip;

        Log.e("TEMPERO", "module id:"+this.hwid + ", type: " + this.type);
    }

    protected Module(Parcel in) {
        hwid = in.readInt();
        type = in.readInt();
        data = in.createIntArray();
    }

    public boolean checkGroup()
    {
        if(this.type != 1 && group == null)
            return true;

        return false;
    }

    public void sendGroup(int sensor, int dataIndex, int dataValue) throws IOException {
        String ss = ("3,"+this.hwid+","+sensor+","+dataIndex+","+dataValue);

        DatagramPacket sendPacket = new DatagramPacket(ss.getBytes(), ss.getBytes().length, this.ip, 1337);
        socket.send(sendPacket);
    }

    public void createGroup(MainActivity m, ArrayList<Module> modules)
    {
        this.group = new Group(m, modules, this);
        this.group.show();
    }

    public void createGroup(MainActivity m, ArrayList<Module> modules, Module sensor, int dataIndex, int dataValue)
    {
        this.group = new Group(m, modules, this, sensor, dataIndex, dataValue);
    }

    public void destroyGroup() throws IOException {
        String ss = ("3,"+this.hwid+",-1");

        DatagramPacket sendPacket = new DatagramPacket(ss.getBytes(), ss.getBytes().length, this.ip, 1337);
        socket.send(sendPacket);

        this.group = null;
    }

    public Group getGroup()
    {
        return this.group;
    }

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    public int getID()
    {
        return hwid;
    }

    public int[] getData()
    {
        return this.data;
    }

    public void update(int[] data) {
        if(data[0] != -1)
            this.data = data;

        Log.e("TEMPERO", "module updated: "+ Arrays.toString(this.data));
    }

    public void send() throws IOException {
        String ss = ("1,"+this.hwid+dataToString());

        DatagramPacket sendPacket = new DatagramPacket(ss.getBytes(), ss.getBytes().length, this.ip, 1337);
        socket.send(sendPacket);
        Log.e("TEMPERO", "Data sent");
    }

    private String dataToString()
    {
        String r = "";

        for(int d : this.data)
        {
            r += ","+d;
        }

        return r;
    }

    public int getType()
    {
        return this.type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hwid);
        dest.writeInt(type);
        dest.writeIntArray(data);
    }
}
