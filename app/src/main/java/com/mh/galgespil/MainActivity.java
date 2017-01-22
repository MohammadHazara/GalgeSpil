package com.mh.galgespil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    Button startGame;
    Button seOrdListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = (Button) findViewById(R.id.startGameButton);
        startGame.setText(R.string.Start_Game_string);
        startGame.setOnClickListener(
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intent);
                }
            }
        );

        seOrdListe = (Button) findViewById(R.id.seOrdListeButton);
        seOrdListe.setText(R.string.word_list_btn_text);
        seOrdListe.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, OrdList.class);
                        startActivity(intent);
                    }
                }
        );
    }
}


