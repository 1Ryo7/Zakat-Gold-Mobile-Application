package com.example.zakatgoldapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spThemes;
    EditText etWeight, etGoldValue;
    Button btnCalculation, btnClear;
    TextView tvOutput1, tvOutput2, tvOutput3;
    RadioButton radio_keep;
    RadioButton radio_wear;

    SharedPreferences sharedPref;

    float totalG = 0;
    float zakatP = 0;
    float totalZ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);

        //create a shared preferences object with the specified name
        sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);

        setupSpinnerItemSelection();

        etWeight = (EditText) findViewById(R.id.textWeight);
        radio_keep = (RadioButton) findViewById(R.id.radio_keep);
        radio_wear = (RadioButton) findViewById(R.id.radio_wear);
        etGoldValue = (EditText) findViewById(R.id.textGoldValue);
        btnCalculation = (Button) findViewById(R.id.btnCalculate);
        btnClear = (Button) findViewById(R.id.btnClear);
        tvOutput1 = (TextView) findViewById(R.id.tvOutput1);
        tvOutput2 = (TextView) findViewById(R.id.tvOutput2);
        tvOutput3 = (TextView) findViewById(R.id.tvOutput3);

        btnCalculation.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        //load the data
        //retrieve the saved values from shared preferences
        String weight = sharedPref.getString("weight", "");
        String goldValue = sharedPref.getString("gold_value", "");
        String totalValueOfGold = sharedPref.getString("total_value_of_gold", "");
        String zakatPayableAmount = sharedPref.getString("zakat_payable_amount", "");
        String totalZakat = sharedPref.getString("total_zakat", "");
        String selectedRadioButton = sharedPref.getString("selected_radio_button", "");

        if (selectedRadioButton.equals("keep")) {
            radio_keep.setChecked(true);
        } else if (selectedRadioButton.equals("wear")) {
            radio_wear.setChecked(true);
        }


        //populate the input views with the saved values
        etWeight.setText(weight);
        etGoldValue.setText(goldValue);

        //populate the output views with the saved values
        tvOutput1.setText(totalValueOfGold);
        tvOutput2.setText(zakatPayableAmount);
        tvOutput3.setText(totalZakat);

    }

    //Setting function clicking button
    @Override
    public void onClick(View v) {

        //Setting format for decimal
        DecimalFormat precision = new DecimalFormat("0.00");

        switch (v.getId()) {

            //Calculate
            case R.id.btnCalculate:
                //buat pengiraan
                performCalculation();

                SharedPreferences.Editor editor = sharedPref.edit();

                //Save checked radio button
                if (radio_keep.isChecked()) {
                    editor.putString("selected_radio_button", "keep");
                } else if (radio_wear.isChecked()) {
                    editor.putString("selected_radio_button", "wear");
                }

                //save the input values in shared preferences
                editor.putString("weight", etWeight.getText().toString());
                editor.putString("gold_value", etGoldValue.getText().toString());

                //save the output values in shared preferences
                editor.putString("total_value_of_gold", tvOutput1.getText().toString());
                editor.putString("zakat_payable_amount", tvOutput2.getText().toString());
                editor.putString("total_zakat", tvOutput3.getText().toString());
                editor.apply();

                break;

            case R.id.btnClear:
                etWeight.getText().clear();
                etGoldValue.getText().clear();
                tvOutput1.setText("");
                tvOutput2.setText("");
                tvOutput3.setText("");

                radio_keep.setChecked(false);
                radio_wear.setChecked(false);

                //clear the saved values in shared preferences
                sharedPref.edit().clear().commit();

                break;
        }
    }

    //performing Calculation
    private void performCalculation() {

        float xGram = 0;

        if (radio_keep.isChecked()) {
            xGram = 85;
        } else if (radio_wear.isChecked()) {
            xGram = 200;
        }

        try {
            float weight = Float.parseFloat(etWeight.getText().toString());
            float goldV = Float.parseFloat(etGoldValue.getText().toString());

            // Check if either of the RadioButton instances are checked
            if (!radio_keep.isChecked() && !radio_wear.isChecked()) {
                Toast.makeText(this, "Please select a radio button", Toast.LENGTH_SHORT).show();
                return;
            }

            totalG = weight * goldV;

            zakatP = (weight - xGram) * goldV;

            totalZ =  zakatP * (float) 0.025;

            if (totalZ <= 0) {
                totalZ = 0;
            }

            tvOutput1.setText("Total value of gold is RM" + totalG);
            tvOutput2.setText("Zakat payable amount is RM" + zakatP);
            tvOutput3.setText("Total zakat is RM" + totalZ);


        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
            //set error
            etWeight.setError("Please enter a valid number");
            etGoldValue.setError("Please enter a valid number");
        }
    }

    //setting Spinner
    private void setupSpinnerItemSelection() {
        spThemes = (Spinner) findViewById(R.id.spThemes);
        spThemes.setSelection(ThemeApplication.currentPosition);
        ThemeApplication.currentPosition = spThemes.getSelectedItemPosition();

        spThemes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            //parameter AdapterView is to refer where selection happened and in this case the spinner.
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ThemeApplication.currentPosition != position) {
                    Utils.changeToTheme(MainActivity.this, position);
                }
                ThemeApplication.currentPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and add the "about" menu item
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.about) {
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.goldRate) {
            onGoldRateClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onGoldRateClicked() {
        openUrl("https://goldrate.com/gold-price-malaysia/");
    }

    public void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}
