package com.mh.galgespil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static android.view.KeyEvent.KEYCODE_A;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_Z;


public class GameActivity extends AppCompatActivity implements View.OnKeyListener {

    Galgelogik spil;
    TextView synligOrd, statusBar, bogstaverBrugt, point;
    ImageView imageView;
    EditText editText;
    Button nulstil;
    int score = 0, record = 0;
    private InputMethodManager in;

    boolean ordSat = false;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        score = sharedPref.getInt("score", 0);
        record = sharedPref.getInt("rekord", 0);

        spil = new Galgelogik();

        imageView = (ImageView) findViewById(R.id.imageView);
        point = (TextView) findViewById(R.id.score);
        synligOrd = (TextView) findViewById(R.id.synligOrd);
        statusBar = (TextView) findViewById(R.id.status);
        bogstaverBrugt = (TextView) findViewById(R.id.bogstaverBrugt);
        editText = (EditText) findViewById(R.id.editText);
        editText.setOnKeyListener(this);

        nulstil = (Button) findViewById(R.id.reset_btn);
        nulstil.setVisibility(View.INVISIBLE);

        hentOrdFraDR(); // denne methode laver også nyt spil

    }

    private void newGame() {
        spil.nulstil();
        imageView.setImageResource(R.drawable.galge);
        point.setText(String.format(getString(R.string.score_line), score, record));
        if (!ordSat && getIntent() != null &&  getIntent().getStringExtra("ord") != null){
            spil.setOrdet(getIntent().getStringExtra("ord"));
            synligOrd.setText(spil.getSynligtOrd());
            ordSat = true;
        }
        else synligOrd.setText(spil.getSynligtOrd());

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


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if((keyCode >= KEYCODE_A && keyCode <= KEYCODE_Z) || (keyCode >= 65 && keyCode <= 90)){
            tjekInput(editText.getText().toString().toLowerCase());
            bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
            editText.selectAll();
            tjekStatus();
        }

        else if (keyCode == KEYCODE_ENTER) {
            tjekInput(editText.getText().toString().toLowerCase());
            bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
            editText.selectAll();
            tjekStatus();
        }
        return false;
    }


    private void tjekStatus() {
        in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (spil.erSpilletSlut()) {
            editText.setVisibility(View.INVISIBLE);
            in.hideSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if (spil.erSpilletVundet()) {
                score += (spil.getOrdet().length() * 20) - (spil.getAntalForkerteBogstaver() * 10);
                if (score > record) record = score;
                point.setText(String.format(getString(R.string.score_line), score, record));
                statusBar.setText(R.string.correct_word_message);
                nulstil.setText(R.string.new_word);
            } else if (spil.erSpilletTabt()) {
                statusBar.setTextColor(Color.RED);
                statusBar.setText(R.string.loosing_text);
                synligOrd.setText(getString(R.string.correct_word) + spil.getOrdet() + "\"");
                nulstil.setText(R.string.new_game);
                score = 0;
                point.setText(String.format(getString(R.string.score_line), score, record));
            }
            statusBar.setVisibility(View.VISIBLE);
            nulstil.setVisibility(View.VISIBLE);
        }
    }

    private void tjekInput(String s) {
        if (s.length() > 1) {
            if (s.equals(spil.getOrdet())) {
                for (int i = 0; i < s.length(); i++) {
                    spil.gætBogstav(s.substring(i, i + 1));
                    synligOrd.setText(spil.getSynligtOrd());
                }
            } else {
                if (score > 50) score -= 50;
                else score = 0;
                point.setText(String.format(getString(R.string.score_line), score, record));
                Toast t = Toast.makeText(GameActivity.this, R.string.wrong_word_message,
                        Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }
        } else {
            spil.gætBogstav(s);
            if (spil.erSidsteBogstavKorrekt()) {
                synligOrd.setText(spil.getSynligtOrd());
            } else {
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
                    case 7:
                        imageView.setImageResource(R.drawable.forkert7);
                }
            }
            bogstaverBrugt.setText(spil.getBrugteBogstaver().toString().toUpperCase());
        }
    }


    void hentOrdFraDR(){
        new AsyncTask<String,Void,String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    spil.hentOrdFraDr();
                    return getString(R.string.succesfull_message);
                } catch (Exception e) {
                    e.printStackTrace();
                    return getString(R.string.unsuccesfull_message);
                }
            }
            @Override
            protected void onPostExecute(String s) {
                System.out.println(s.toString());
                newGame();
            }
        }.execute();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPref.edit().putInt("rekord", record).apply();
        sharedPref.edit().putInt("score", score).apply();
    }
}