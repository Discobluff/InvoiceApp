package com.sica.android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Map;


public class ProduitsActivity extends AppCompatActivity {
    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    ArrayList<EditText> editTextProduits=new ArrayList<>();
    private LinearLayout parentLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);

        SharedPreferences pref = getSharedPreferences(PREFERENCES_FILE_NAME, 0);

        Button new_Button=findViewById(R.id.new_button);

        ArrayList<TextView> textViewProduits=new ArrayList<>();
        ArrayList<EditText> editTextPrix=new ArrayList<>();
        ArrayList<ImageButton> imageButtonDelete=new ArrayList<>();
        ArrayList<TextView> textViewPrix=new ArrayList<>();

        parentLayout = findViewById(R.id.parentLayout);

        ScrollView scrollview = findViewById(R.id.scroll_view);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int heightScreen = size.y;

        scrollview.getLayoutParams().height=heightScreen-450;

        Button backButton = findViewById(R.id.back_button_produits);
        backButton.setOnClickListener(z -> {

            Intent gameActivityIntent = new Intent(ProduitsActivity.this, MainActivity.class);
            startActivity(gameActivityIntent);
            Toast.makeText(ProduitsActivity.this,"Modifications non enregistrées",Toast.LENGTH_SHORT).show();
        });
        Map<String, ?> allEntries = pref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            if (!entry.getValue().toString().equals("")) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 20, 0, 0);
                LinearLayout childLayout = new LinearLayout(this);
                editTextProduits.add(new EditText(this));
                editTextProduits.get(editTextProduits.size() - 1).setWidth(600);

                editTextProduits.get(editTextProduits.size() - 1).setText(entry.getValue().toString());
                editTextProduits.get(editTextProduits.size() - 1).setHint("Entrez un produit");
                editTextProduits.get(editTextProduits.size() - 1).setMaxLines(1);
                editTextProduits.get(editTextProduits.size() - 1).setTextSize(16);
                editTextProduits.get(editTextProduits.size() - 1).setInputType(InputType.TYPE_CLASS_TEXT);
                editTextProduits.get(editTextProduits.size() - 1).setLayoutParams(params);
                editTextProduits.get(editTextProduits.size() - 1).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        new_Button.setEnabled(!editTextProduits.get(editTextProduits.size() - 1).getText().toString().equals("") && !editTextPrix.get(editTextPrix.size() - 1).getText().toString().equals(""));
                    }
                });

                final View activityRootView = findViewById(R.id.root);
                activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    Rect r = new Rect();
                    activityRootView.getWindowVisibleDisplayFrame(r);
                    int screenHeight = activityRootView.getRootView().getHeight();
                    int keypadHeight = screenHeight - r.bottom;
                    if (keypadHeight > screenHeight * 0.15) {
                        scrollview.setMinimumHeight(heightScreen - 1250);
                        scrollview.getLayoutParams().height = heightScreen - 1250;
                    } else {
                        scrollview.setMinimumHeight(heightScreen - 450);
                        scrollview.getLayoutParams().height = heightScreen - 450;

                    }
                });

                textViewProduits.add(new TextView(this));
                textViewProduits.get(textViewProduits.size() - 1).setText(" = ");
                textViewProduits.get(textViewProduits.size() - 1).setTextSize(20);
                textViewProduits.get(textViewProduits.size() - 1).setTextColor(Color.parseColor("#009b55"));
                textViewProduits.get(textViewProduits.size() - 1).setLayoutParams(params);

                editTextPrix.add(new EditText(this));
                editTextPrix.get(editTextPrix.size() - 1).setHint("Prix");
                editTextPrix.get(editTextPrix.size() - 1).setTextSize(16);
                editTextPrix.get(editTextPrix.size() - 1).setText(entry.getKey().split("_")[1]);
                editTextPrix.get(editTextPrix.size() - 1).setLayoutParams(params);
                editTextPrix.get(editTextPrix.size() - 1).setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                editTextPrix.get(editTextPrix.size() - 1).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        new_Button.setEnabled(!editTextProduits.get(editTextProduits.size() - 1).getText().toString().equals("") && !editTextPrix.get(editTextPrix.size() - 1).getText().toString().equals(""));
                    }
                });
                new_Button.setEnabled(!editTextProduits.get(editTextProduits.size() - 1).getText().toString().equals("") && !editTextPrix.get(editTextPrix.size() - 1).getText().toString().equals(""));
                textViewPrix.add(new TextView(this));
                textViewPrix.get(textViewPrix.size() - 1).setText("€ HT /u");
                textViewPrix.get(textViewPrix.size() - 1).setTextSize(17);
                textViewPrix.get(textViewPrix.size() - 1).setTextColor(Color.parseColor("#009b55"));
                textViewPrix.get(textViewPrix.size() - 1).setLayoutParams(params);
                imageButtonDelete.add(new ImageButton(this));
                imageButtonDelete.get(imageButtonDelete.size() - 1).setImageResource(R.drawable.trash);
                imageButtonDelete.get(imageButtonDelete.size() - 1).setLayoutParams(new LinearLayout.LayoutParams(100, 150));
                imageButtonDelete.get(imageButtonDelete.size() - 1).setScaleType(ImageView.ScaleType.FIT_XY);
                imageButtonDelete.get(imageButtonDelete.size() - 1).setBackgroundColor(0);
                imageButtonDelete.get(imageButtonDelete.size() - 1).setOnClickListener(b -> {
                    childLayout.removeAllViews();
                    parentLayout.removeView(childLayout);

                    for (ImageButton widget : imageButtonDelete) {
                        if (!widget.isShown()) {
                            int index = imageButtonDelete.indexOf(widget);
                            pref.edit().remove(editTextProduits.get(index).getText() + "_" + editTextPrix.get(index).getText()).apply();
                            editTextProduits.remove(index);
                            editTextPrix.remove(index);
                            textViewProduits.remove(index);
                            imageButtonDelete.remove(index);
                            break;
                        }
                    }
                    new_Button.setEnabled(parentLayout.getChildCount() == 0);
                    if (editTextProduits.size() > 0) {
                        new_Button.setEnabled(!editTextProduits.get(editTextProduits.size() - 1).getText().toString().equals("") && !editTextPrix.get(editTextPrix.size() - 1).getText().toString().equals(""));
                    }
                });

                childLayout.addView(editTextProduits.get(editTextProduits.size() - 1));
                childLayout.addView(textViewProduits.get(textViewProduits.size() - 1));
                childLayout.addView(editTextPrix.get(editTextPrix.size() - 1));
                childLayout.addView(textViewPrix.get(textViewPrix.size() - 1));
                childLayout.addView(imageButtonDelete.get(imageButtonDelete.size() - 1));

                parentLayout.addView(childLayout);

            }

        }
        Button validateButton = findViewById(R.id.validate_button);
        validateButton.setOnClickListener(z -> {

            for (EditText widget : editTextProduits) {
                widget.clearFocus();
            }
            for (EditText widget : editTextPrix) {
                widget.clearFocus();
            }
            Map<String, ?> allEntriesBis = pref.getAll();

            for (Map.Entry<String, ?> entry : allEntriesBis.entrySet()) {
                pref.edit().remove(entry.getKey()).apply();

            }
            SharedPreferences.Editor editor = pref.edit();
            for (EditText widget : editTextProduits) {
                if (!widget.getText().toString().equals("") && !editTextPrix.get(editTextProduits.indexOf(widget)).getText().toString().equals("")){
                    editor.putString( widget.getText().toString()+"_"+ editTextPrix.get(editTextProduits.indexOf(widget)).getText(), widget.getText().toString()); // Storing string
                }}
            editor.apply();
            Intent gameActivityIntent = new Intent(ProduitsActivity.this, MainActivity.class);
            startActivity(gameActivityIntent);
            Toast.makeText(ProduitsActivity.this,"Modifications enregistrées",Toast.LENGTH_SHORT).show();
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 0);
        new_Button.setOnClickListener(v -> {
            LinearLayout childLayout = new LinearLayout(this);
            editTextProduits.add(new EditText(this));
            editTextProduits.get(editTextProduits.size() - 1).setWidth(600);

            editTextProduits.get(editTextProduits.size() - 1).setHint("Entrez un produit");
            editTextProduits.get(editTextProduits.size() - 1).setMaxLines(1);
            editTextProduits.get(editTextProduits.size() - 1).setTextSize(16);
            editTextProduits.get(editTextProduits.size() - 1).setInputType(InputType.TYPE_CLASS_TEXT);
            editTextProduits.get(editTextProduits.size() - 1).setLayoutParams(params);
            editTextProduits.get(editTextProduits.size() - 1).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    new_Button.setEnabled(!editTextProduits.get(editTextProduits.size() - 1).getText().toString().equals("") && !editTextPrix.get(editTextPrix.size() - 1).getText().toString().equals(""));
                }
            });
            new_Button.setEnabled(!editTextProduits.get(editTextProduits.size() - 1).getText().toString().equals("") && !editTextPrix.get(editTextPrix.size() - 1).getText().toString().equals(""));
            textViewProduits.add(new TextView(this));
            textViewProduits.get(textViewProduits.size() - 1).setText(" = ");
            textViewProduits.get(textViewProduits.size() - 1).setTextSize(20);
            textViewProduits.get(textViewProduits.size() - 1).setTextColor(Color.parseColor("#009b55"));
            textViewProduits.get(textViewProduits.size() - 1).setLayoutParams(params);

            editTextPrix.add(new EditText(this));
            editTextPrix.get(editTextPrix.size() - 1).setHint("Prix");
            editTextPrix.get(editTextPrix.size() - 1).setTextSize(16);

            editTextPrix.get(editTextPrix.size() - 1).setLayoutParams(params);
            editTextPrix.get(editTextPrix.size() - 1).setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            editTextPrix.get(editTextPrix.size() - 1).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    new_Button.setEnabled(!editTextProduits.get(editTextProduits.size() - 1).getText().toString().equals("") && !editTextPrix.get(editTextPrix.size() - 1).getText().toString().equals(""));
                }
            });
            textViewPrix.add(new TextView(this));
            textViewPrix.get(textViewPrix.size() - 1).setText("€ HT /u");
            textViewPrix.get(textViewPrix.size() - 1).setTextSize(17);
            textViewPrix.get(textViewPrix.size() - 1).setTextColor(Color.parseColor("#009b55"));
            textViewPrix.get(textViewPrix.size() - 1).setLayoutParams(params);
            imageButtonDelete.add(new ImageButton(this));
            imageButtonDelete.get(imageButtonDelete.size() - 1).setImageResource(R.drawable.trash);
            imageButtonDelete.get(imageButtonDelete.size() - 1).setLayoutParams(new LinearLayout.LayoutParams(100, 150));
            imageButtonDelete.get(imageButtonDelete.size() - 1).setScaleType(ImageView.ScaleType.FIT_XY);
            imageButtonDelete.get(imageButtonDelete.size() - 1).setBackgroundColor(0);
            imageButtonDelete.get(imageButtonDelete.size() - 1).setOnClickListener(b -> {
                childLayout.removeAllViews();
                parentLayout.removeView(childLayout);

                for (ImageButton widget : imageButtonDelete) {
                    if (!widget.isShown()) {
                        int index = imageButtonDelete.indexOf(widget);
                        pref.edit().remove(editTextProduits.get(index).getText() + "_" + editTextPrix.get(index).getText()).apply();
                        editTextProduits.remove(index);
                        editTextPrix.remove(index);
                        textViewProduits.remove(index);
                        imageButtonDelete.remove(index);
                        break;
                    }
                }
                new_Button.setEnabled(parentLayout.getChildCount() == 0);
                if (editTextProduits.size() > 0) {
                    new_Button.setEnabled(!editTextProduits.get(editTextProduits.size() - 1).getText().toString().equals("") && !editTextPrix.get(editTextPrix.size() - 1).getText().toString().equals(""));
                }
            });
            childLayout.addView(editTextProduits.get(editTextProduits.size() - 1));
            childLayout.addView(textViewProduits.get(textViewProduits.size() - 1));
            childLayout.addView(editTextPrix.get(editTextPrix.size() - 1));
            childLayout.addView(textViewPrix.get(textViewPrix.size() - 1));
            childLayout.addView(imageButtonDelete.get(imageButtonDelete.size() - 1));

            parentLayout.addView(childLayout);
            editTextProduits.get(editTextProduits.size() - 1).requestFocus();

        });

    }

}

