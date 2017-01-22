package com.mh.galgespil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;


public class OrdList extends AppCompatActivity implements AdapterView.OnItemLongClickListener{


    AutoCompleteTextView textView;
    ListView listView;
    ArrayAdapter<String> adapter;
    final Galgelogik gl = new Galgelogik();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ord_list);

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, gl.getMuligeOrd());
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemLongClickListener(this);

        textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
        textView.setHint(R.string.auto_complete_text);
        textView.setDropDownHeight(0);
        updateWords();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        listView.setSelected(true);
        Intent intent = new Intent(OrdList.this, GameActivity.class);
        intent.putExtra("ord", adapter.getItem(position));
        startActivity(intent);
        return true;
    }


    void updateWords(){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... params) {
                try{
                    gl.hentOrdFraDr();
                    SystemClock.sleep(2000);
                    return "OrdListen er updateret!";
                }catch (Exception e){
                    e.printStackTrace();
                    return "ERROR! \nUpdateringen fejlede";
                }
            }
            @Override
            protected void onPreExecute() {
                Toast.makeText(getApplicationContext(), "Henter ord fra DR" , Toast.LENGTH_SHORT).show();
            }
            @Override
            protected void onPostExecute(String o) {
                listView.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), ""+o , Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }





}
