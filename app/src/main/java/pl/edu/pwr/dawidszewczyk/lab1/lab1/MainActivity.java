package pl.edu.pwr.dawidszewczyk.lab1.lab1;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidParameterException;

public class MainActivity extends AppCompatActivity {

    Spinner sUnits;
    EditText etMass;
    EditText etHeight;
    TextView tvMassUnit;
    TextView tvHeightUnit;
    TextView tvBMIResult;
    SharedPreferences sharedPreferences;
    ICountBMI BMICalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sUnits = (Spinner) findViewById(R.id.sUnits);
        etMass = (EditText) findViewById(R.id.etMass);
        etHeight = (EditText) findViewById(R.id.etHeight);
        tvMassUnit = (TextView) findViewById(R.id.tvMassUnit);
        tvHeightUnit = (TextView) findViewById(R.id.tvHeightUnit);
        tvBMIResult = (TextView) findViewById(R.id.tvBMIResult);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        BMICalculator = new CountBMIForKgM();
        setSpinnerContent();
        setSpinnerListener();
        restorePreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                openAboutActivity();
            case R.id.action_save:
                savePreferences();
                return true;
            case R.id.action_share:
                try {
                    float BMI = Float.parseFloat(tvBMIResult.getText().toString());
                    shareByEmail(BMI);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.BMI_no_calculated_error,
                            Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if(!tvBMIResult.getText().toString().isEmpty()) {
            menu.findItem(R.id.action_share).setEnabled(true);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(getString(R.string.mass_key), etMass.getText().toString());
        savedInstanceState.putString(getString(R.string.height_key), etHeight.getText().toString());
        savedInstanceState.putInt(getString(R.string.units_key), sUnits.getSelectedItemPosition());
        savedInstanceState.putString(getString(R.string.BMI_key), tvBMIResult.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        etMass.setText(savedInstanceState.getString(getString(R.string.mass_key)));
        etHeight.setText(savedInstanceState.getString(getString(R.string.height_key)));
        sUnits.setSelection(savedInstanceState.getInt(getString(R.string.units_key)));
        try {
            float BMI = Float.parseFloat(savedInstanceState.getString(getString(R.string.BMI_key)));
            setBMIResult(BMI);
        } catch (Exception e) {}
        invalidateOptionsMenu();
    }

    public void savePreferences() {
        float mass = getMass();
        float height = getHeight();
        if (areValuesValid(mass, height)) {
            int sUnitsPosition = sUnits.getSelectedItemPosition();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(getString(R.string.mass_key), mass);
            editor.putFloat(getString(R.string.height_key), height);
            editor.putInt(getString(R.string.units_key), sUnitsPosition);
            editor.commit();
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_save_error,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void restorePreferences() {
        float mass = sharedPreferences.getFloat(getString(R.string.mass_key), 0f);
        float height = sharedPreferences.getFloat(getString(R.string.height_key), 0f);
        int sUnitsPosition = sharedPreferences.getInt(getString(R.string.units_key), 0);
        etMass.setText(Float.toString(mass));
        etHeight.setText(Float.toString(height));
        sUnits.setSelection(sUnitsPosition);
    }

    private void setSpinnerContent() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.units,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUnits.setAdapter(adapter);
    }

    private void setSpinnerListener() {
        sUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitsType = (String) parent.getItemAtPosition(position);
                updateUnits(unitsType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void updateUnits(String unitsType) {
        if (unitsType.equals(getString(R.string.metric_units))) {
            tvMassUnit.setText(R.string.kilograms);
            tvHeightUnit.setText(R.string.meters);
            BMICalculator = new CountBMIForKgM();
        } else if (unitsType.equals(getString(R.string.imperial_units))) {
            tvMassUnit.setText(R.string.pounds);
            tvHeightUnit.setText(R.string.feet);
            BMICalculator = new CountBMIForLbFt();
        } else {
            throw new InvalidParameterException();
        }
    }

    public void onBtnCountClick(View view) {
        float mass = getMass();
        float height = getHeight();
        countBMI(mass, height);
        invalidateOptionsMenu();
    }

    private void countBMI(float mass, float height) {
        if (areValuesValid(mass, height)) {
            float BMI = BMICalculator.countBMI(mass, height);
            setBMIResult(BMI);
        } else {
            showError();
        }
    }

    private void setBMIResult(float BMI) {
        tvBMIResult.setText(Float.toString(BMI));
        changeBMIColor(BMI);
    }

    private float getMass() {
        try {
            return Float.parseFloat(etMass.getText().toString());
        } catch (Exception e) {
            return -1f;
        }
    }

    private float getHeight() {
        try {
            return Float.parseFloat(etHeight.getText().toString());
        } catch (Exception e) {
            return -1f;
        }
    }

    private boolean areValuesValid(float mass, float height) {
        return BMICalculator.isMassValid(mass) && BMICalculator.isHeightValid(height);
    }

    private void showError() {
        Toast.makeText(getApplicationContext(), R.string.invalid_params_error,
                Toast.LENGTH_SHORT).show();
    }

    private void changeBMIColor(float BMI) {
        if (BMI < 18.5f || (BMI > 25f && BMI < 30f)) {
            tvBMIResult.setTextColor(ContextCompat.getColor(this, R.color.orange));
        } else if (BMI > 30f) {
            tvBMIResult.setTextColor(Color.RED);
        } else {
            tvBMIResult.setTextColor(ContextCompat.getColor(this, R.color.green));
        }
    }

    private void shareByEmail(float BMI) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_recipient));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text) + " " + Float.toString(BMI));
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), R.string.toast_no_email_clients,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void openAboutActivity() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
