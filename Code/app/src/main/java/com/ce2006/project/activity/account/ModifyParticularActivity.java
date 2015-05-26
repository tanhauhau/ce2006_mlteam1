package com.ce2006.project.activity.account;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.model.User_Particulars;
import com.ce2006.project.server.Particulars;
import com.example.user.ce2006_project.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity to modify patient particulars
 */
public class ModifyParticularActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button btnSubmit;
    private EditText txtName, txtEmail, txtContact, txtAddress, txtPostal;
    private Spinner spinnerCountry, spinnerCity;
    private Switch switchType;
    private ProgressBar progressBar;
    private UpdateTask task;
    private boolean updateInProgress = false;

    /**
     * Override onCreate
     * load details into the fields
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_modify);

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtContact = (EditText) findViewById(R.id.txtContact);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        txtPostal = (EditText) findViewById(R.id.txtPostal);
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        spinnerCountry.setOnItemSelectedListener(this);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        switchType = (Switch) findViewById(R.id.switchType);
        loadDetails();
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);
    }

    private void loadDetails() {
        FetchParticularTask task = new FetchParticularTask(this);
        task.execute();
    }

    /**
     * Override onClick
     * check field and start {@link com.ce2006.project.activity.account.ModifyParticularActivity.UpdateTask}
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            if (checkField()) {
                String email = txtEmail.getText().toString();
                String contact = txtContact.getText().toString();
                String country = (String) spinnerCountry.getSelectedItem();
                String city = (String) spinnerCity.getSelectedItem();
                String address = txtAddress.getText().toString();
                String postal_code = txtPostal.getText().toString();
                String type = switchType.isChecked() ? "1" : "0";
                task = new UpdateTask(this);
                task.execute(email, contact, address, city, postal_code, country, type);
            }
        }
    }

    /**
     * make sure all fields are filled and
     * filled correctly
     *
     * @return
     */
    private boolean checkField() {
        boolean emptyField = false;
        TextView focusTo = null;
        if (txtEmail.getText().toString().isEmpty()) {
            emptyField = true;
            focusTo = txtEmail;
        }
        if (txtContact.getText().toString().isEmpty()) {
            emptyField = true;
            focusTo = txtContact;
        }
        if (txtAddress.getText().toString().isEmpty()) {
            emptyField = true;
            focusTo = txtAddress;
        }
        if (spinnerCity.getSelectedItem() == null) {
            emptyField = true;
        }
        if (txtPostal.getText().toString().isEmpty()) {
            emptyField = true;
            focusTo = txtPostal;
        }
        if (emptyField) {
            if (focusTo != null) focusTo.requestFocus();
            Toast.makeText(this, "Field should not be empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        //check email address format
        if (!isEmailValid(txtEmail.getText().toString())) {
            txtEmail.requestFocus();
            Toast.makeText(this, "Please input a valid email address!", Toast.LENGTH_LONG).show();
            return false;
        }
        //check phone number length
        int phoneNumLength = txtContact.length();
        if (phoneNumLength < 7 || phoneNumLength > 15) {
            txtContact.requestFocus();
            Toast.makeText(this, "Please input a valid contact number!", Toast.LENGTH_LONG).show();
            return false;
        }
        //check postal code length
        if (txtPostal.length() < 5 || txtPostal.length() > 6) {
            txtPostal.requestFocus();
            Toast.makeText(this, "Please input a valid postal code!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Check if email is valid email
     *
     * @param email email to be checked
     * @return true if email is valid, false otherwise
     */
    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Prevent default back behaviour when update task is in progress
     */
    @Override
    public void onBackPressed() {
        if (!updateInProgress) {
            super.onBackPressed();
        }
    }

    /**
     * load different city name according to country name
     */
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

    /**
     * UpdateTask submits the new particulars to server
     */
    private class UpdateTask extends AsyncTask<String, Void, Boolean> {
        private Activity context;

        private UpdateTask(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length != 7) {
                return false;
            } else {
                PreferenceManager manager = PreferenceManager.getManager(context);
                String name = manager.getUserName();
                String password = manager.getPassword();
                String email = params[0];
                String contact = params[1];
                String address = params[2];
                String city = params[3];
                String postal = params[4];
                String country = params[5];
                String type = params[6];
                return Particulars.updateParticulars(name, password, email, contact, country, city, address, postal, type.equals("1"));
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                txtName.requestFocus();
                Toast.makeText(context, "Update Failed!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Update Successfully!", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
            btnSubmit.setEnabled(true);
            finish();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);
        }
    }

    /**
     * FetchParticularTask fetch particulars from server and fill in the textfields
     */
    private class FetchParticularTask extends AsyncTask<Void, Void, User_Particulars> {
        private Activity context;

        private FetchParticularTask(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);
            updateInProgress = true;
        }

        @Override
        protected User_Particulars doInBackground(Void... params) {
            PreferenceManager manager = PreferenceManager.getManager(context);
            return Particulars.getParticulars(manager.getUserName(), manager.getPassword());
        }

        @Override
        protected void onPostExecute(User_Particulars particulars) {
            updateInProgress = false;
            progressBar.setVisibility(View.INVISIBLE);
            btnSubmit.setEnabled(true);

            txtName.setText(particulars.getName());
            txtEmail.setText(particulars.getEmail());
            txtContact.setText(particulars.getContactNumber());
            txtAddress.setText(particulars.getAddress());
            txtPostal.setText(particulars.getPostal());
            switchType.setChecked(particulars.isContactType());

            SpinnerAdapter adapter = spinnerCountry.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (((String) adapter.getItem(i)).equals(particulars.getCountry())) {
                    spinnerCountry.setSelection(i);
                    break;
                }
            }
            adapter = spinnerCountry.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (((String) adapter.getItem(i)).equals(particulars.getCity())) {
                    spinnerCity.setSelection(i);
                    break;
                }
            }
        }
    }
}