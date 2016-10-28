package com.mh.galgespil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class OrdListe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ord_liste);

        //ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item , liste);

        GridView gridView = new GridView(this);
        gridView.setNumColumns(GridView.AUTO_FIT);
       // gridView.setAdapter(adapter);

        setContentView(gridView);
    }
}
