package com.sica.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class NouvelleFactureActivity extends AppCompatActivity {
    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    public static final String PREFERENCES_FILE_NAME_SET = "MySettingsPreferences";

    //Log.i("Test",getSharedPreferences(PREFERENCES_FILE_NAME_SET, 0));
    private ArrayList<Button> scanText;
    Bitmap bitmap;
    private int index;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_facture);
        Button back_button = findViewById(R.id.back_button_facture);
        Button new_button = findViewById(R.id.new_button);
        Button print_button = findViewById(R.id.print_button);

        ArrayList<LinearLayout> childLayout = new ArrayList();
        ArrayList<View> viewLine = new ArrayList();
        ArrayList<EditText> editText = new ArrayList();

        EditText nom_client = findViewById(R.id.edit_text_name);
        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        ArrayList<Spinner> spinner = new ArrayList();
        ArrayList<LinearLayout> scanLayout = new ArrayList();
        scanText = new ArrayList();
        ScrollView scrollView = findViewById(R.id.scroll_view);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int heightScreen = size.y;


        final View activityRootView = findViewById(R.id.root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            activityRootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = activityRootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;
            if (keypadHeight > screenHeight * 0.15) {
                scrollView.setMinimumHeight(heightScreen - 1250);
                scrollView.getLayoutParams().height = heightScreen - 1250;
            } else {
                scrollView.setMinimumHeight(heightScreen - 630);
                scrollView.getLayoutParams().height = heightScreen - 630;

            }
        });





        print_button.setOnClickListener(v -> {
            for (int i = 0;i<spinner.size();i++){
                if (spinner.get(i).getSelectedItem().toString().equals("Choisir un produit")){
                    parentLayout.removeView(childLayout.get(i));
                    parentLayout.removeView(viewLine.get(i));
                    childLayout.remove(i);
                    viewLine.remove(i);
                    spinner.remove(i);
                    editText.remove(i);
                    for (int butt=0;butt<scanLayout.get(i).getChildCount();butt++){
                        scanText.remove(butt + i);
                    }
                    scanLayout.remove(i);


                    new_button.setEnabled(true);
                }
            }
            boolean continu = true;
            if (spinner.size()==0){
                Toast.makeText(NouvelleFactureActivity.this,"Pas de produits à imprimer",Toast.LENGTH_SHORT).show();
                continu = false;
            }
            int compte = 0;
            for (int i = 0; i<scanLayout.size();i++){
                if (scanText.get(compte).getText().equals("Scanner un sac")){
                    Toast.makeText(NouvelleFactureActivity.this,"Au moins un produit n'a pas de numéro de lot en premier bouton de scan",Toast.LENGTH_SHORT).show();
                    continu = false;
                }
                compte = compte + scanLayout.get(i).getChildCount();
            }
            SharedPreferences prefSet = getSharedPreferences(PREFERENCES_FILE_NAME_SET, 0);
            String fromEmail = prefSet.getAll().get("exp").toString();
            String fromPassword = prefSet.getAll().get("mdp").toString();
            String toEmails = prefSet.getAll().get("dest").toString();
            List<String> toEmailList = Arrays.asList(toEmails
                    .split("\\s*,\\s*"));
            String emailSubject = prefSet.getAll().get("obj").toString();
            String emailBody = "@name:" + nom_client.getText() + "%##%";
            if ((fromEmail.equals(""))) {
                Toast.makeText(NouvelleFactureActivity.this, "Adresse mail du expéditeur manquante", Toast.LENGTH_SHORT).show();
                continu = false;
            }
            if ((fromPassword.equals(""))) {
                Toast.makeText(NouvelleFactureActivity.this, "Mot de passe manquant", Toast.LENGTH_SHORT).show();
                continu = false;
            }
            if ((toEmails.equals(""))) {
                Toast.makeText(NouvelleFactureActivity.this, "Adresse mail destinataire manquante", Toast.LENGTH_SHORT).show();
                continu = false;
            }
            if ((emailSubject.equals(""))) {
                Toast.makeText(NouvelleFactureActivity.this, "Objet du mail manquant", Toast.LENGTH_SHORT).show();
                continu = false;
            }
            if (continu) {

                int compteur = 0;
                for (int i=0;i<spinner.size();i++){
                    for (int lay=0 ; lay<scanLayout.get(i).getChildCount();lay++){
                        //if (scanText.get(lay+compteur).getText().toString()=="Scanner un sac"){
                            scanText.get(lay+compteur).setText(scanText.get(compteur).getText().toString());
                        //}
                        emailBody = emailBody+spinner.get(i).getSelectedItem()+":"+scanText.get(lay+compteur).getText().toString().replace(" ","")+"_";

                    }
                    compteur=compteur+scanLayout.get(i).getChildCount();

                }
                 /*for (int i = 0; i < spinner.size(); i++) {
                    for (int lay = 0; lay < scanLayout.get(i).getChildCount(); lay++) {
                        if (!(lay == 0) && scanText.get(lay + compteur).getText().toString().equals("Scanner un sac")) {
                            scanText.get(lay + compteur).setText(scanText.get(lay + compteur - 1).getText().toString());
                        }
                        emailBody = emailBody + spinner.get(i).getSelectedItem() + ":" + scanText.get(lay + compteur).getText().toString().replace(" ", "") + "_";
                    }
                    compteur = compteur + scanLayout.get(i).getChildCount();

                }*/
                new SendMailTask(NouvelleFactureActivity.this).execute(fromEmail,
                        fromPassword, toEmailList, emailSubject, emailBody);
//                Toast.makeText(NouvelleFactureActivity.this,"Mail envoyé",Toast.LENGTH_SHORT).show();
            }}
        );

        TextView prix = findViewById(R.id.prix_textview);




        RelativeLayout.LayoutParams paramsLinearLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams paramsViewLine = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        SharedPreferences pref = getSharedPreferences(PREFERENCES_FILE_NAME, 0);




        int wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        prix.measure(wrapSpec, wrapSpec);
        int width = prix.getMeasuredWidth();

        int widthScreen = size.x;

        //scrollView.getLayoutParams().height = heightScreen * 1350 / 2016;
        scrollView.getLayoutParams().height=heightScreen-450;

        nom_client.getLayoutParams().width = widthScreen - width;

        back_button.setOnClickListener(k -> {
            Intent gameActivityIntent = new Intent(NouvelleFactureActivity.this, MainActivity.class);
            startActivity(gameActivityIntent);
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);
        RelativeLayout.LayoutParams paramsBis = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsBis.setMargins(0, 10, 0, 10);

        new_button.setOnClickListener(b -> {

            childLayout.add(new LinearLayout(this));


            scanLayout.add(new LinearLayout(this));
            scanLayout.get(scanLayout.size() - 1).setOrientation(LinearLayout.VERTICAL);
            scanLayout.get(scanLayout.size() - 1).setLayoutParams(paramsLinearLayout);
            scanLayout.get(scanLayout.size() - 1).getLayoutParams().width = 500;
            ArrayList<String> items = new ArrayList();
            items.add("Choisir un produit");
            Map<String, ?> allEntries = pref.getAll();


            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                items.add(entry.getValue().toString());
            }

           // if (spinner.size() >= items.size() - 2) {
             //   new_button.setEnabled(false);
            //}
            int nbProduits = items.size() - 1;
         //   for (Spinner widget : spinner) {
           //     if (!widget.getSelectedItem().toString().equals("Choisir un produit")) {
             //       if (widget.isShown()) {
               //         items.remove(widget.getSelectedItem().toString());
                 //   }
               // }
           // }
            spinner.add(new Spinner(this));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(NouvelleFactureActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.get(spinner.size() - 1).setLayoutParams(params);
            spinner.get(spinner.size() - 1).getLayoutParams().width = 500;
            spinner.get(spinner.size() - 1).setAdapter(adapter);
            spinner.get(spinner.size() - 1).addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {

                new_button.setEnabled(!spinner.get(spinner.size() - 1).getSelectedItem().toString().equals("Choisir un produit"));
                //if (spinner.size() >= nbProduits) {
                  //  new_button.setEnabled(false);
                //}
                double prixTotal = 0;
                for (Spinner spin : spinner) {
                    if (spin.getSelectedItem() != "Choisir un produit") {
                        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                            if (entry.getValue() == spin.getSelectedItem()) {
                                if (!editText.get(spinner.indexOf(spin)).getText().toString().equals("")) {
                                    prixTotal += Double.parseDouble(entry.getKey().split("_")[1]) * Integer.parseInt(String.valueOf(editText.get(spinner.indexOf(spin)).getText()));

                                }
                            }
                        }
                        if (Double.toString(prixTotal).split("\\.")[1].length() < 2) {
                            prix.setText(prixTotal + "0 € HT");
                        } else if (Double.toString(prixTotal).split("\\.")[1].length() > 2) {
                            if (String.valueOf(round(prixTotal, 2)).split("\\.")[1].length() < 2) {
                                prix.setText(round(prixTotal, 2) + "0 € HT");
                            } else {
                                prix.setText(round(prixTotal, 2) + " € HT");
                            }

                        } else {
                            prix.setText(prixTotal + " € HT");
                        }
                        if (prixTotal == 0) {
                            prix.setText("0.00 € HT");
                        }
                        prix.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        nom_client.getLayoutParams().width = widthScreen - prix.getMeasuredWidth();
                    }
                }

            });

            editText.add(new EditText(this));
            editText.get(editText.size() - 1).setHint("Nb");
            editText.get(editText.size() - 1).setLayoutParams(paramsBis);
            editText.get(editText.size() - 1).setInputType(InputType.TYPE_CLASS_NUMBER);


            editText.get(editText.size() - 1).addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {



                    int indexEditText = 0;
                    for (EditText edit : editText) {
                        if (editable.getFilters() == edit.getEditableText().getFilters()) {
                            indexEditText = editText.indexOf(edit);
                            break;
                        }
                    }
                    if (!editText.get(indexEditText).getText().toString().equals("")) {
                        Integer quantity = Integer.valueOf(String.valueOf(editText.get(indexEditText).getText()));

                        RelativeLayout.LayoutParams paramsScanText = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        paramsScanText.setMargins(0, 5, 0, 5);
                        Integer nb = scanLayout.get(indexEditText).getChildCount();

                        if (quantity == 0) {


                            parentLayout.removeView(childLayout.get(indexEditText));
                            parentLayout.removeView(viewLine.get(indexEditText));
                            childLayout.remove(childLayout.get(indexEditText));
                            editText.remove(editText.get(indexEditText));
                            scanLayout.remove(scanLayout.get(indexEditText));
                            spinner.remove(spinner.get(indexEditText));
                            viewLine.remove(viewLine.get(indexEditText));

                        } else if (quantity > nb && !spinner.get(indexEditText).getSelectedItem().toString().equals("Choisir un produit")) {

                            for (int i = 0; i < quantity - nb; i++) {

                                scanText.add(new Button(scanLayout.get(scanLayout.size() - 1).getContext()));

                                scanText.get(scanText.size() - 1).setText("Scanner un sac");
                                scanText.get(scanText.size() - 1).setLayoutParams(paramsScanText);
                                scanText.get(scanText.size() - 1).getLayoutParams().width = 400;
                                scanText.get(scanText.size() - 1).setBackgroundColor(Color.parseColor("#009b55"));
                                scanText.get(scanText.size() - 1).setTextColor(Color.WHITE);

                                scanText.get(scanText.size() - 1).setOnClickListener(m -> {

                                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(NouvelleFactureActivity.this);
                                    index = scanText.indexOf(m);


                                });

                                scanLayout.get(indexEditText).addView(scanText.get(scanText.size() - 1));


                            }
                        } else if (quantity < nb) {
                            for (int i = 0; i < nb - quantity; i++) {
                                scanLayout.get(indexEditText).removeView(scanText.get(scanText.size() - 1));
                                scanText.remove(scanText.size() - 1);

                            }
                        }
                        if (quantity != 0) {
                            childLayout.get(indexEditText).removeView(scanLayout.get(indexEditText));
                            childLayout.get(indexEditText).addView(scanLayout.get(indexEditText));
                        }

                    }
                    double prixTotal = 0;
                    for (Spinner spin : spinner) {
                        if (spin.getSelectedItem() != "Choisir un produit") {
                            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

                                if (entry.getValue() == spin.getSelectedItem()) {
                                    if (!editText.get(spinner.indexOf(spin)).getText().toString().equals("")) {
                                        prixTotal += Double.parseDouble(entry.getKey().split("_")[1]) * Integer.parseInt(String.valueOf(editText.get(spinner.indexOf(spin)).getText()));

                                    }
                                }
                            }
                            if (Double.toString(prixTotal).split("\\.")[1].length() < 2) {
                                prix.setText(prixTotal + "0 € HT");
                            } else if (Double.toString(prixTotal).split("\\.")[1].length() > 2) {
                                if (String.valueOf(round(prixTotal, 2)).split("\\.")[1].length() < 2) {
                                    prix.setText(round(prixTotal, 2) + "0 € HT");
                                } else {
                                    prix.setText(round(prixTotal, 2) + " € HT");
                                }

                            } else {
                                prix.setText(prixTotal + " € HT");
                            }
                            if (prixTotal == 0) {
                                prix.setText("0.00 € HT");
                            }
                            prix.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            nom_client.getLayoutParams().width = widthScreen - prix.getMeasuredWidth();
                        }
                    }
                    if (parentLayout.getChildCount() == 0) {
                        new_button.setEnabled(true);
                        nom_client.clearFocus();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(nom_client.getWindowToken(), 0);
                    } else if (items.size()-1<spinner.size()){
                        new_button.setEnabled(true);
                        for (Spinner spin : spinner) {
                            if (spin.getSelectedItem().toString().equals("Choisir un produit"))
                                new_button.setEnabled(false);
                            break;
                        }
                    }
                    else {
                        new_button.setEnabled(false);
                    }

                }
            });

            childLayout.get(childLayout.size() - 1).addView(spinner.get(spinner.size() - 1));
            childLayout.get(childLayout.size() - 1).addView(editText.get(editText.size() - 1));

            viewLine.add(new View(this));
            viewLine.get(viewLine.size() - 1).setBackgroundColor(Color.parseColor("#009b55"));
            paramsViewLine.setMargins(100, 30, 100, 30);
            viewLine.get(viewLine.size() - 1).setLayoutParams(paramsViewLine);

            viewLine.get(viewLine.size() - 1).getLayoutParams().height = 5;

            parentLayout.addView(childLayout.get(childLayout.size() - 1));
            parentLayout.addView(viewLine.get(viewLine.size() - 1));
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                assert result != null;
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void getTextFromImage(Bitmap bitmap){

        com.google.android.gms.vision.text.TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()){
            Toast.makeText(NouvelleFactureActivity.this,"Error Occurred!!!",Toast.LENGTH_SHORT).show();
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0;i<textBlockSparseArray.size();i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());

            }

            scanText.get(index).setText(stringBuilder.toString());


        }
    }







}