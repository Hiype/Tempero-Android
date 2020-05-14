//package com.example.tempero;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.Spinner;
//import android.widget.Switch;
//import android.widget.Toast;
//
//import androidx.core.content.ContextCompat;
//
//import com.flask.colorpicker.ColorPickerView;
//import com.flask.colorpicker.OnColorChangedListener;
//import com.flask.colorpicker.OnColorSelectedListener;
//import com.flask.colorpicker.builder.ColorPickerClickListener;
//import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//
//
//public class OptionsMenu extends Activity implements AdapterView.OnItemSelectedListener {
//
//    private ArrayList<Module> modules;
//    private ArrayList<Group> groups;
//    private Spinner dropdown;
//    private Spinner dropdown2;
//    private Module module;
//    private EditText input;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.options_activity);
//
//        this.dropdown = (Spinner) findViewById(R.id.spinner1);
//        this.dropdown2 = (Spinner) findViewById(R.id.spinner2);
//        this.input = (EditText) findViewById(R.id.input);
//
//        Intent intent = getIntent();
//        this.modules = (ArrayList<Module>) getIntent().getSerializableExtra("modules");
//        this.groups = (ArrayList<Group>) getIntent().getSerializableExtra("groups");
//        this.module = (Module) getIntent().getSerializableExtra("module");
//
//        SpinAdapter adapter2 = new SpinAdapter(OptionsMenu.this, android.R.layout.simple_spinner_item,
//                modules);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.optionsMenuSelection1, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//        dropdown.setAdapter(adapter);
//        dropdown2.setAdapter(adapter2);
//
//        dropdown.setOnItemSelectedListener(this);
//        dropdown2.setOnItemSelectedListener(this);
//
//    }
//
//    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//
//        Intent intent = new Intent(this, MainActivity.class);
//
//
//        if(parent.getId() == R.id.spinner1)
//        {
//
//            switch (position) {
//                case 0:
//                    //Temperature
//
//                    intent.putExtra("moduleShow", 0);
//                    break;
//                case 1:
//                    //Humidity
//
//                    intent.putExtra("moduleShow", 1);
//                    break;
//                case 2:
//                    //Brightness
//
//                    intent.putExtra("moduleShow", 2);
//                    break;
//
//            }
//
//        }
//        else if(parent.getId() == R.id.spinner2)
//        {
//            switch (position) {
//                case 0:
//                    //Module
//
//
//
//                    switch (position) {
//                        case 0:
//                            //Temperature
//
//                            break;
//                        case 1:
//                            //Humidity
//
//                            break;
//                        case 2:
//                            //Brightness
//
//                            break;
//
//                    }
//                    break;
//                case 1:
//                    //Relay
//
//                    break;
//
//            }
//        }
//
//
//    }
//
//    public void onNothingSelected(AdapterView<?> parent) {
//
//
//    }
//
//    public void applySettings(View view) {
//
//        Module sensor = modules.get(dropdown.getSelectedItemPosition());
//        int dataIndex = dropdown2.getSelectedItemPosition();
//        if(input.getText().toString().equals("") || input.getText().equals(null) || input.getText().toString().equals(" ")) return;
//        int dataValue = Integer.valueOf(input.getText().toString());
//
//
////        groups.add(new Group(module, sensor, dataIndex, dataValue, groups.size()));
//    }
//
//    public void closeActivity(View view) {
//        finish();
//    }
//}
