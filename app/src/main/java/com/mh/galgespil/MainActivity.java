package com.mh.galgespil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button startGame;
    Button seOrdListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = (Button) findViewById(R.id.startGameButton);
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
        seOrdListe.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, OrdListe.class);
                        startActivity(intent);
                    }
                }
        );
    }

}