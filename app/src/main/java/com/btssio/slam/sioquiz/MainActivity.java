package com.btssio.slam.sioquiz;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button go;
    EditText prenom;
    Intent intentJeu;
    String leprenom;
    ArrayList items;

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("filmoiid.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        //Log.i("tag1", json);
        return json;
    }

    public ArrayList getPersonnages() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());

            // Creation du tableau general a partir d'un JSONObject
            JSONArray jsonArray = obj.getJSONArray("personnages");
            // Pour chaque element du tableau
            for (int i = 0; i < jsonArray.length(); i++) {
                // Creation d'un tableau element a partir d'un JSONObject
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                // Recuperation de l'item qui nous interesse
                String nom = jsonObj.getString("nom");
                String prenom = jsonObj.getString("prenom");
                String acteur = jsonObj.getString("acteur");
                String film = jsonObj.getString("film");
                String genre = jsonObj.getString("genre");
                currentFilms = new Films(nom, prenom, acteur, film, genre);
                items.add(currentFilms);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go = (Button) findViewById(R.id.btngo);
        prenom = (EditText) findViewById(R.id.editprenom);
        Log.i("Json", "" + loadJSONFromAsset());
        go.setOnClickListener(this);
    }

    public void onClick(View arg0) {
        intentJeu = new Intent(this, JeuActivity.class);
        leprenom = prenom.getText().toString();
        if (leprenom.equals("")) {
            Toast.makeText(this, "Vous n'avez pas mis de prénom !", Toast.LENGTH_LONG).show();
        } else {
            lancementJeu(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode > 1) {
            // Si l'on veut que l'application s'arrête...
            // finish();
        } else {
            lancementJeu(requestCode + 1);
        }
    }

    public void lancementJeu(int num) {
        intentJeu.putExtra("Joueur", leprenom);
        intentJeu.putExtra("Numero", num);
        this.startActivityForResult(intentJeu, num);
    }
}
