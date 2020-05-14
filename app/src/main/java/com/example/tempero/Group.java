package com.example.tempero;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Group extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;

    private Spinner sensorSpinner;
    private Spinner indicatorSpinner;
    private Button close;
    private Button remove;
    private Button apply;
    private EditText valueInput;

    private ArrayList<Module> sensors;
    private Module module;
    private Module sensor;
    private int dataIndex;
    private int dataValue;

    public Group(Activity a, ArrayList<Module> modules, Module module, Module sensor, int dataIndex, int dataValue) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.sensors = getSensors(modules);
        this.module = module;
        this.sensor = sensor;
        this.dataIndex = dataIndex;
        this.dataValue = dataValue;
    }

    public Group(Activity a, ArrayList<Module> modules, Module module) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.sensors = getSensors(modules);
        this.module = module;
    }

    private int findPosition(ArrayList<Module> modules, Module sensor)
    {
        int i = 0;
        for(Module m : modules) {
            if(m == sensor)
                return i;

            i++;
        }

        return 0;
    }

    private ArrayList<Module> getSensors(ArrayList<Module> modules) {

        ArrayList<Module> sensors = new ArrayList<Module>();

        for(Module m : modules) {
            if(m.getType() == 1) {
                sensors.add(m);
            }
        }
        return sensors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.options_activity);
        this.sensorSpinner = (Spinner) findViewById(R.id.sensorSpinner);
        this.indicatorSpinner = (Spinner) findViewById(R.id.indicatorSpinner);
        this.valueInput = (EditText) findViewById(R.id.valueInput);
        this.close = (Button) findViewById(R.id.close);
        this.remove = (Button) findViewById(R.id.remove);
        this.apply = (Button) findViewById(R.id.apply);

        SpinAdapter adapter = new SpinAdapter(this.c, android.R.layout.simple_spinner_item, sensors);
        sensorSpinner.setAdapter(adapter);

        final String[] str = {"Temperature", "Humidity", "Brightness"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.c, android.R.layout.simple_spinner_item, str);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indicatorSpinner.setAdapter(adapter2);

        indicatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        if(sensor != null)
        {
            sensorSpinner.setSelection(findPosition(sensors, sensor));
            indicatorSpinner.setSelection(dataIndex);
            valueInput.setText(String.valueOf(dataValue));
        }

        close.setOnClickListener(this);
        remove.setOnClickListener(this);
        apply.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                hide();
                break;
            case R.id.remove:
                hide();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            module.destroyGroup();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();                break;
            case R.id.apply:
                if(this.valueInput.getText().length() == 0) return;

                new Thread(new Runnable() {
                    public void run() {
                    try {
                        module.sendGroup(sensors.get(sensorSpinner.getSelectedItemPosition()).getID(),
                                indicatorSpinner.getSelectedItemPosition(),
                                Integer.parseInt(valueInput.getText().toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    }
                }).start();


                hide();
                break;
            default:
                break;
        }
        dismiss();
    }
}