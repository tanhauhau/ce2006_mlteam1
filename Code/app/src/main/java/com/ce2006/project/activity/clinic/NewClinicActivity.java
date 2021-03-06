package com.ce2006.project.activity.clinic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.model.Clinic;
import com.ce2006.project.model.Credential;
import com.ce2006.project.server.ClinicManager;
import com.example.user.ce2006_project.R;

/**
 * Activity to create new clinic
 */
public class NewClinicActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, OnClickListener {
    private ClinicManager clinicManager;
    private EditText txtClinicName, txtClinicContact, txtClinicAddress, txtPostalCode;
    private Spinner spinnerCity, spinnerCountry;
    private View progressBar;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_clinic_new);

        txtClinicName = (EditText) findViewById(R.id.txtClinicName);
        txtClinicContact = (EditText) findViewById(R.id.txtClinicContact);
        txtClinicAddress = (EditText) findViewById(R.id.txtClinicAddress);
        txtPostalCode = (EditText) findViewById(R.id.txtPostalCode);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        btnSubmit.setOnClickListener(this);
        spinnerCity.setOnItemSelectedListener(this);
        spinnerCountry.setOnItemSelectedListener(this);

        clinicManager = new ClinicManager(Credential.getCredential(PreferenceManager.getManager(this)));
    }

    /**
     * When submit button is clicked,
     * update the clinic, and inform the listeners
     */
    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            boolean emptyField = false;
            TextView focusTo = null;
            if (txtClinicName.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtClinicName;
            }
            if (txtClinicAddress.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtClinicName;
            }
            if (txtClinicContact.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtClinicContact;
            }
            if (txtPostalCode.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtPostalCode;
            }
            if (emptyField) {
                if (focusTo != null) focusTo.requestFocus();
                Toast.makeText(this, "Field should not be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            //check postal code length
            if (txtPostalCode.length() < 5 || txtPostalCode.length() > 6) {
                txtPostalCode.requestFocus();
                Toast.makeText(this, "Please input a valid postal code!", Toast.LENGTH_LONG).show();
                return;
            }
            createClinic();
        }
    }

    /**
     * create clinic via {@link com.ce2006.project.server.ClinicManager#createClinic(String, String, String, String, String, String)}
     * in {@link android.os.AsyncTask}
     */
    private void createClinic() {
        new AsyncTask<Void, Void, Clinic>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Clinic doInBackground(Void... params) {
                return clinicManager.createClinic(
                        (String) spinnerCity.getSelectedItem(),
                        (String) spinnerCountry.getSelectedItem(),
                        txtClinicAddress.getText().toString(),
                        txtPostalCode.getText().toString(),
                        txtClinicName.getText().toString(),
                        txtClinicContact.getText().toString());
            }

            @Override
            protected void onPostExecute(Clinic clinic) {
                clinicCreated(clinic);
            }
        }.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinnerCountry) {
            String country = (String) spinnerCountry.getSelectedItem();
            String[] spinnerArray = new String[]{};
            if (country.equals("Singapore")) {
                spinnerArray = getResources().getStringArray(R.array.city_singapore);
            } else if (country.equals("Malaysia")) {
                spinnerArray = getResources().getStringArray(R.array.city_malaysia);
            } else if (country.equals("Thailand")) {
                spinnerArray = getResources().getStringArray(R.array.city_thailand);
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCity.setAdapter(spinnerArrayAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void clinicCreated(Clinic clinic) {
        Intent intent = new Intent(this, ViewModifyClinicActivity.class);
        intent.putExtra("clinic", clinic);
        startActivity(intent);
        finish();
    }
}