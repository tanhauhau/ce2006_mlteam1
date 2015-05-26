package com.ce2006.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ce2006.project.model.Doctor;
import com.example.user.ce2006_project.R;

/**
 * Created by lhtan on 31/3/15.
 */
public class DoctorListArrayAdapter extends ArrayAdapter<Doctor> {
    public DoctorListArrayAdapter(Context context, Doctor[] doctors) {
        super(context, R.layout.list_item_doctor, doctors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_doctor, null);
            Holder holder = new Holder();
            holder.txtDoctorName = (TextView) convertView.findViewById(R.id.txtDoctorName);
            holder.txtDoctorType = (TextView) convertView.findViewById(R.id.txtDoctorType);
            holder.txtClinicName = (TextView) convertView.findViewById(R.id.txtClinicName);
            convertView.setTag(holder);
        }
        Doctor doctor = getItem(position);
        Holder holder = (Holder) convertView.getTag();
        holder.txtDoctorName.setText(doctor.getName());
        holder.txtDoctorType.setText(getContext().getResources().getStringArray(R.array.clinic_type)[doctor.getType()]);
        if (doctor.getClinic() != null)
            holder.txtClinicName.setText(doctor.getClinic().getName());
        else
            holder.txtClinicName.setText("No assigned clinic");
        return convertView;
    }

    static class Holder {
        private TextView txtDoctorName;
        private TextView txtDoctorType;
        private TextView txtClinicName;
    }
}




