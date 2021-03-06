package com.ce2006.project.activity.doctor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ce2006.project.activity.clinic.ChooseClinicActivity;
import com.ce2006.project.activity.clinic.ViewModifyClinicActivity;
import com.ce2006.project.model.Clinic;
import com.ce2006.project.model.Doctor;
import com.ce2006.project.server.DoctorManager;
import com.example.user.ce2006_project.R;

/**
 * View details of the doctor
 * Created by lhtan on 4/4/15.
 */
public class ViewDoctorFragment extends Fragment implements View.OnClickListener {
    private final static int REQUEST_CLINIC = 1;
    private Doctor doctor;
    private DoctorManager manager;
    private Button btnClinic, btnChangeClinic;
    private TextView txtClinicName;
    private boolean inTask = false;

    public static ViewDoctorFragment getFragment(Doctor doctor, DoctorManager manager) {
        ViewDoctorFragment fragment = new ViewDoctorFragment();
        fragment.doctor = doctor;
        fragment.manager = manager;
        return fragment;
    }

    @Override
    public void onStop() {
        if (!inTask) {
            super.onStop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_view, null);
        TextView txtDoctorName = (TextView) view.findViewById(R.id.txtDoctorName);
        TextView txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        TextView txtType = (TextView) view.findViewById(R.id.txtDoctorType);
        txtClinicName = (TextView) view.findViewById(R.id.txtClinicName);
        btnClinic = (Button) view.findViewById(R.id.btnClinic);
        btnChangeClinic = (Button) view.findViewById(R.id.btnChangeClinic);
        //
        btnChangeClinic.setOnClickListener(this);
        btnClinic.setOnClickListener(this);
        //
        txtDoctorName.setText(doctor.getName());
        txtEmail.setText(doctor.getEmail());
        txtType.setText(getResources().getStringArray(R.array.clinic_type)[doctor.getType()]);
        if (doctor.getClinic() != null) {
            txtClinicName.setText(doctor.getClinic().getName());
            txtClinicName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ViewModifyClinicActivity.class);
                    intent.putExtra("clinic", doctor.getClinic());
                    startActivity(intent);
                }
            });
        } else {
            txtClinicName.setVisibility(View.GONE);
            btnClinic.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == btnClinic) {
            startActivityForResult(new Intent(getActivity(), ChooseClinicActivity.class), REQUEST_CLINIC);
        } else if (v == btnChangeClinic) {
            startActivityForResult(new Intent(getActivity(), ChooseClinicActivity.class), REQUEST_CLINIC);
        }
    }

    /**
     * allow assignment of doctors to clinic
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CLINIC) {
            if (resultCode == Activity.RESULT_OK) {
                final Clinic clinic = (Clinic) data.getSerializableExtra(ChooseClinicActivity.CLINIC_EXTRA);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        inTask = true;
                        txtClinicName.setText(getString(R.string.loading));
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        manager.linkDoctor(doctor, clinic);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        txtClinicName.setText(clinic.getName());
                        inTask = false;
                    }
                }.execute();
                btnClinic.setVisibility(View.GONE);
                txtClinicName.setVisibility(View.VISIBLE);
            }
        }
    }
}
