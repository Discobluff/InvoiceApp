package com.sica.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button produitsButton = findViewById(R.id.les_produits_button);
        Button nouvelleFactureButton = findViewById(R.id.nouvelle_facture_button);
        Button settingsButton = findViewById(R.id.settings_button);
        produitsButton.setOnClickListener(v -> {
            Intent gameActivityIntent = new Intent(MainActivity.this, ProduitsActivity.class);
            startActivity(gameActivityIntent);
        });
        nouvelleFactureButton.setOnClickListener(v -> {
            Intent gameActivityIntent = new Intent(MainActivity.this, NouvelleFactureActivity.class);
            startActivity(gameActivityIntent);
        });
        settingsButton.setOnClickListener(v -> {
            Intent gameActivityIntent = new Intent(MainActivity.this, ParametresActivity.class);
            startActivity(gameActivityIntent);
        });
    }
}