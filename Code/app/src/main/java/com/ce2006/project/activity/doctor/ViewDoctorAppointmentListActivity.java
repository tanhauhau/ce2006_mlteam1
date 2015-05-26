package com.ce2006.project.activity.doctor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ce2006.project.adapter.DoctorAppointmentArrayAdapter;
import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.model.Appointment;
import com.ce2006.project.model.Credential;
import com.ce2006.project.server.DoctorManager;
import com.example.user.ce2006_project.R;


/**
 * View list of appointments by the doctors
 * Created by lhtan on 31/3/15.
 */
public class ViewDoctorAppointmentListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public static final String APPOINTMENT = "appointment";
    private boolean inTask = false;
    private ListView listView;
    private View progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_list);
        listView = (ListView) findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        loadAppointment();
    }

    private void loadAppointment() {
        final DoctorManager manager = new DoctorManager(Credential.getCredential(PreferenceManager.getManager(this)));
        new AsyncTask<Void, Void, Appointment[]>() {
            @Override
            protected void onPreExecute() {
                inTask = true;
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Appointment[] doInBackground(Void... params) {
                return manager.getAppointmentList();
            }

            @Override
            protected void onPostExecute(Appointment[] appointments) {
                progressBar.setVisibility(View.INVISIBLE);
                inTask = false;
                showAppointments(appointments);
            }
        }.execute();

    }

    private void showAppointments(Appointment[] appointments) {
        listView.setAdapter(new DoctorAppointmentArrayAdapter(this, appointments));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //show dialog
        if (parent == listView) {
            Appointment appointment = (Appointment) listView.getAdapter().getItem(position);
            Log.d("Tan", "appointment: " + appointment);
            Intent intent = new Intent(this, ViewDoctorAppointmentActivity.class);
            intent.putExtra(APPOINTMENT, appointment);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (!inTask) super.onBackPressed();
    }
}




