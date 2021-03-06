package com.ce2006.project.activity.doctor;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ce2006.project.model.Doctor;
import com.ce2006.project.server.DoctorManager;
import com.example.user.ce2006_project.R;


/**
 * Fragment to modify doctor
 * Created by lhtan on 31/3/15.
 */
public class ModifyDoctorFragment extends Fragment implements View.OnClickListener {
    private Doctor doctor;
    private DoctorManager builder;
    private Spinner spinnerType;
    private Button btnSubmit;
    private Listener listener;
    private View progressBar;

    public static ModifyDoctorFragment getFragment(Doctor doctor, DoctorManager builder) {
        ModifyDoctorFragment fragment = new ModifyDoctorFragment();
        fragment.doctor = doctor;
        fragment.builder = builder;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (listener == null) {
            try {
                listener = (Listener) activity;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_modify, null);
        spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        progressBar = view.findViewById(R.id.progressBar);

        btnSubmit.setOnClickListener(this);

        //load default value
        TextView txtDoctorName = (TextView) view.findViewById(R.id.txtDoctorName);
        txtDoctorName.setText(doctor.getName());
        spinnerType.setSelection(doctor.getType());
        return view;
    }

    /**
     * When submit button is clicked,
     * update the doctor, and inform the listeners
     */
    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            updateDoctor();
            listener.doctorModified(doctor);
        }
    }

    /**
     * call {@link com.ce2006.project.server.DoctorManager#updateDoctor(com.ce2006.project.model.Doctor)}
     * in a {@link android.os.AsyncTask}
     */
    private void updateDoctor() {
        doctor.setType(spinnerType.getSelectedItemPosition());
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return builder.updateDoctor(doctor);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    public static interface Listener {
        void doctorModified(Doctor doctor);
    }
}




