package com.mh.galgespil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.KeyEvent.KEYCODE_DEL;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class GameActivity extends AppCompatActivity implements View.OnKeyListener {

    Galgelogik spil;
    TextView synligOrd, statusBar, bogstaverBrugt;
    ImageView imageView;
    EditText editText;
    Button nulstil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageView = (ImageView) findViewById(R.id.imageView);
        synligOrd = (TextView) findViewById(R.id.synligOrd);
        statusBar = (TextView) findViewById(R.id.status);
        bogstaverBrugt = (TextView) findViewById(R.id.bogstaverBrugt);
        editText = (EditText) findViewById(R.id.editText);
        editText.setOnKeyListener(this);
        nulstil = (Button) findViewById(R.id.nulstil);

        spil = new Galgelogik();
        hentOrdliste();
        newGame();

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        //filter for key release only
        if (event.getAction() != KeyEvent.ACTION_DOWN)
            return true;
        if(keyCode == KEYCODE_ENTER){
            tjekBogstave(editText.getText().toString().toLowerCase());
            bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
            synligOrd.setText(spil.getSynligtOrd());
            editText.selectAll();
            tjekStatus();
        }if(keyCode == KEYCODE_DEL || keyCode == KEYCODE_BACK){
            editText.clearFocus();
        }

        return false;
    }


    private void newGame(){
        spil.nulstil();
        imageView.setImageResource(R.drawable.galge);
        synligOrd.setText(spil.getSynligtOrd());
        statusBar.setVisibility(View.INVISIBLE);
        statusBar.setTextColor(Color.GREEN);
        nulstil.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.VISIBLE);
        editText.setText("");
        bogstaverBrugt.setText("");
        nulstil.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        newGame();
                    }
                }
        );
    }

    private void tjekStatus(){

        if(spil.erSpilletSlut()){
            editText.setVisibility(View.INVISIBLE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            if(spil.erSpilletVundet()) statusBar.setText("DU HAR VUNDET");
            else if(spil.erSpilletTabt()){
                statusBar.setTextColor(Color.RED);
                statusBar.setText("DU HAR TABT");
            }
            statusBar.setVisibility(View.VISIBLE);
            nulstil.setVisibility(View.VISIBLE);
        }
    }


    private void tjekBogstave(String s){
        spil.gætBogstav(s);
        if(spil.erSidsteBogstavKorrekt()){
            synligOrd.setText(spil.getSynligtOrd());
        }else if(s.toLowerCase().equals(spil.getOrdet().toLowerCase())){
            statusBar.setText("DU HAR VUNDET");
            editText.setVisibility(View.INVISIBLE);
            statusBar.setVisibility(View.VISIBLE);
            nulstil.setVisibility(View.VISIBLE);
        } else if(!spil.erSidsteBogstavKorrekt()){
            bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
            int antalForkerte = spil.getAntalForkerteBogstaver();
            switch (antalForkerte){
                case 1: imageView.setImageResource(R.drawable.forkert1);
                    break;
                case 2: imageView.setImageResource(R.drawable.forkert2);
                    break;
                case 3: imageView.setImageResource(R.drawable.forkert3);
                    break;
                case 4: imageView.setImageResource(R.drawable.forkert4);
                    break;
                case 5: imageView.setImageResource(R.drawable.forkert5);
                    break;
                case 6: imageView.setImageResource(R.drawable.forkert6);
                    break;
            }
        }
    }


    public void hentOrdliste(){
        try {
            spil.hentOrdFraDr();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
