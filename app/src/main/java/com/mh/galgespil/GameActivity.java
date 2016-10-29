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

    Galgelogik spil ;
    TextView synligOrd, statusBar, bogstaverBrugt, point;
    ImageView imageView;
    EditText editText;
    Button nulstil;
    int score = 0, record = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageView = (ImageView) findViewById(R.id.imageView);
        point = (TextView) findViewById(R.id.score);
        synligOrd = (TextView) findViewById(R.id.synligOrd);
        statusBar = (TextView) findViewById(R.id.status);
        bogstaverBrugt = (TextView) findViewById(R.id.bogstaverBrugt);
        editText = (EditText) findViewById(R.id.editText);
        editText.setOnKeyListener(this);
        nulstil = (Button) findViewById(R.id.nulstil);
        spil = new Galgelogik();
        try {
            spil.hentOrdFraDr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        newGame();

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        //filter for key release only
        if (event.getAction() != KeyEvent.ACTION_DOWN)
            return true;
        if (keyCode == KEYCODE_ENTER) {
            tjekInput(editText.getText().toString().toLowerCase());
            bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
            editText.selectAll();
            tjekStatus();
        }
        return false;
    }


    private void newGame() {
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
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newGame();
                    }
                }
        );
    }

    private void tjekStatus() {

        if (spil.erSpilletSlut()) {
            editText.setVisibility(View.INVISIBLE);
            if (spil.erSpilletVundet()) {
                score += (spil.getOrdet().length() * 20) - (spil.getAntalForkerteBogstaver() * 10);
                if(score > record) record = score;
                point.setText("Score: \n" + score + "\nRecord: \n"+record);
                statusBar.setText("ER KORREKT");
                nulstil.setText("nyt ord");
                nulstil.requestFocus();
            } else if (spil.erSpilletTabt()) {
                statusBar.setTextColor(Color.RED);
                statusBar.setText("DU HAR TABT");
                nulstil.setText("nyt spil");
                score = 0;
                point.setText("Score: " + score + "\nRecord: "+record);
            }
            statusBar.setVisibility(View.VISIBLE);
            nulstil.setVisibility(View.VISIBLE);
        }
    }


    private void tjekInput(String s) {
        if (s.length() > 1) {
            if (s.equals(spil.getOrdet())) {
                for(int i = 0; i < s.length(); i++){
                    spil.gætBogstav(s.substring(i, i+1));
                    synligOrd.setText(spil.getSynligtOrd());
                    bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
                }

            }else{
                score -= 50;
                point.setText("Score: \n" + score + "\nRecord: \n"+record);
            }
        } else {
            spil.gætBogstav(s);
            if (spil.erSidsteBogstavKorrekt()) {
                synligOrd.setText(spil.getSynligtOrd());
                bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
            } else if (!spil.erSidsteBogstavKorrekt()) {
                bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
                switch (spil.getAntalForkerteBogstaver()) {
                    case 1:
                        imageView.setImageResource(R.drawable.forkert1);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.forkert2);
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.forkert3);
                        break;
                    case 4:
                        imageView.setImageResource(R.drawable.forkert4);
                        break;
                    case 5:
                        imageView.setImageResource(R.drawable.forkert5);
                        break;
                    case 6:
                        imageView.setImageResource(R.drawable.forkert6);
                        break;
                }
            }
        }
    }
}