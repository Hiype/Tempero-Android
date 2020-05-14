package com.example.tempero;

import android.app.ActionBar;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<Module> modules = new ArrayList<Module>();
    private ArrayList<Group> groups = new ArrayList<Group>();
    private ListView list;
    private ListAdapter adapter;
    private LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = (LinearLayout) findViewById(R.id.loadingLayout);
        loading.setVisibility(View.VISIBLE);

        list = (ListView) this.findViewById(R.id.list);

        adapter = new ListAdapter(this, modules);

        list.setAdapter(adapter);

        adapter.addAll(modules);

        Thread t = new Thread(new UDPReceiver(modules, adapter, this));
        t.start();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                if(modules.get(position).getType() != 1) {

                    if(modules.get(position).checkGroup()) {
                        modules.get(position).createGroup(MainActivity.this, modules);
                    }
                    else {
                        modules.get(position).getGroup().show();
                    }
                }
            }
        });

    }

    public void setLoading(boolean s)
    {
        if(s)
            this.loading.setVisibility(View.VISIBLE);
        else
            this.loading.setVisibility(View.GONE);
    }

}

