package com.sica.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class ParametresActivity extends AppCompatActivity {
    public static final String PREFERENCES_FILE_NAME_SET = "MySettingsPreferences";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);
        Button back_button = findViewById(R.id.back_button_parametres);
        Button validate_button = findViewById(R.id.save_button);
        EditText expediteur_edit = findViewById(R.id.edit_text_exp);
        EditText mdp_edit = findViewById(R.id.edit_text_mdp);
        mdp_edit.setTransformationMethod(new PasswordTransformationMethod());
        EditText destinataire_edit = findViewById(R.id.edit_text_dest);
        EditText objet_edit = findViewById(R.id.edit_text_objet);



        back_button.setOnClickListener(k -> {
            Intent gameActivityIntent = new Intent(ParametresActivity.this, MainActivity.class);
            startActivity(gameActivityIntent);
            Toast.makeText(ParametresActivity.this,"Modifications non enregistrées",Toast.LENGTH_SHORT).show();
        });
        SharedPreferences prefSet = getSharedPreferences(PREFERENCES_FILE_NAME_SET, 0);
        Map<String, ?> allEntries = prefSet.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey() == "mdp") {
                mdp_edit.setText(entry.getValue().toString());
            }
            if (entry.getKey() == "dest") {
                destinataire_edit.setText(entry.getValue().toString());
            }
            if (entry.getKey() == "exp") {
               expediteur_edit.setText(entry.getValue().toString());
            }
            if (entry.getKey() == "obj") {
                objet_edit.setText(entry.getValue().toString());
            }
        }
        validate_button.setOnClickListener(f -> {
            Map<String, ?> allEntriesBis = prefSet.getAll();
            for (Map.Entry<String, ?> entry : allEntriesBis.entrySet()) {
                prefSet.edit().remove(entry.getKey()).apply();
            }
            SharedPreferences.Editor editor = prefSet.edit();
            editor.putString("exp",expediteur_edit.getText().toString());
            editor.putString("mdp",mdp_edit.getText().toString());
            editor.putString("dest",destinataire_edit.getText().toString());
            editor.putString("obj",objet_edit.getText().toString());
            editor.apply();
            Intent gameActivityIntent = new Intent(ParametresActivity.this, MainActivity.class);
            startActivity(gameActivityIntent);
            Toast.makeText(ParametresActivity.this,"Modifications enregistrées",Toast.LENGTH_SHORT).show();
        });
        }
}