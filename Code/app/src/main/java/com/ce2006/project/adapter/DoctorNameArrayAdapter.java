package com.ce2006.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ce2006.project.model.Doctor;

/**
 * Created by lhtan on 31/3/15.
 */
public class DoctorNameArrayAdapter extends ArrayAdapter<Doctor> {
    public DoctorNameArrayAdapter(Context context, Doctor[] doctors) {
        super(context, android.R.layout.simple_list_item_1, doctors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            Holder holder = new Holder();
            holder.txtName = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }
        Doctor doctor = getItem(position);
        Holder holder = (Holder) convertView.getTag();
        holder.txtName.setText(doctor.getName());
        return convertView;
    }

    static class Holder {
        private TextView txtName;
    }
}
