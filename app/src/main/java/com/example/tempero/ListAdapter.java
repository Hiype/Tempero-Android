package com.example.tempero;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ListAdapter extends ArrayAdapter<Module> {

    private ArrayList<Module> modules;

    public ListAdapter(Context context, ArrayList<Module> modules) {
        super(context, 0, modules);
        this.modules = modules;
    }


        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Module m = modules.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_type_one, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        Switch b = (Switch) convertView.findViewById(R.id.switchb);
        ProgressBar temp = (ProgressBar) convertView.findViewById(R.id.temp);
        TextView tempText = (TextView) convertView.findViewById(R.id.tempText);
        ProgressBar hum = (ProgressBar) convertView.findViewById(R.id.hum);
        TextView humText = (TextView) convertView.findViewById(R.id.humText);
        ProgressBar br = (ProgressBar) convertView.findViewById(R.id.br);
        TextView brText = (TextView) convertView.findViewById(R.id.brText);
        Button rgb = (Button) convertView.findViewById(R.id.rgbButton);

        FrameLayout tempL = (FrameLayout) convertView.findViewById(R.id.tempL);
        FrameLayout humL = (FrameLayout) convertView.findViewById(R.id.humL);
        FrameLayout brL = (FrameLayout) convertView.findViewById(R.id.brL);

        Log.e("TEMPERO", "ListAdapter Type: "+m.getType());

        if(m.getType() == 1) {
            title.setText(Integer.toString(m.getID()));
            ObjectAnimator.ofInt(temp, "progress", m.getData()[0])
                    .setDuration(1000)
                    .start();
            tempText.setText(Integer.toString(m.getData()[0])+"°C");

            ObjectAnimator.ofInt(hum, "progress", m.getData()[1])
                    .setDuration(1000)
                    .start();
            humText.setText(Integer.toString(m.getData()[1])+"%");

            ObjectAnimator.ofInt(br, "progress", m.getData()[2])
                    .setDuration(1000)
                    .start();
            brText.setText(Integer.toString(m.getData()[2])+"%");

            rgb.setVisibility(View.GONE);
            b.setVisibility(View.GONE);
        }
        else if(m.getType() == 2 || m.getType() == 3)
        {
            title.setText(Integer.toString(m.getID()));
            b.setVisibility(View.VISIBLE);
            b.setChecked((m.getData()[0] != 0));

            tempL.setVisibility(View.GONE);
            humL.setVisibility(View.GONE);
            brL.setVisibility(View.GONE);
            rgb.setVisibility(View.GONE);

            ColorStateList thumbStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Color.BLUE,
                            Color.argb(255, 2, 165, 0),
                            Color.argb(255, 199, 0, 0)
                    }
            );
            b.setThumbTintList(thumbStates);
            if (Build.VERSION.SDK_INT >= 24) {
                ColorStateList trackStates = new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_enabled},
                                new int[]{}
                        },
                        new int[]{
                                Color.GRAY,
                                Color.LTGRAY
                        }
                );
                b.setTrackTintList(trackStates);
            }

            b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                        m.getData()[0] = 1;
                    else
                        m.getData()[0] = 0;

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                m.send();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }

            });

        } else if(m.getType() == 4)
        {
            title.setText(Integer.toString(m.getID()));

            b.setVisibility(View.GONE);
            tempL.setVisibility(View.GONE);
            humL.setVisibility(View.GONE);
            brL.setVisibility(View.GONE);

            rgb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ColorPickerDialogBuilder
                            .with(getContext())
                            .setTitle("Choose color")
                            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                            .density(12)
                            .showLightnessSlider(true)
                            .showAlphaSlider(false)
                            .setOnColorSelectedListener(new OnColorSelectedListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onColorSelected(int selectedColor) {

                                    int color[] = {
                                        Color.red(selectedColor),
                                        Color.green(selectedColor),
                                        Color.blue(selectedColor),
                                        Color.alpha(selectedColor)
                                    };

                                    Log.e("TEMPERO", "Color: "+ Arrays.toString(color));

                                    m.update(color);

                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                m.send();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            })
                            .setPositiveButton("Close", new ColorPickerClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                                }
                            })
                            .build()
                            .show();                }
            });

        }

        // Return the completed view to render on screen
        return convertView;
    }


}

