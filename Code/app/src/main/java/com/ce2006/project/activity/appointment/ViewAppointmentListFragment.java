package com.ce2006.project.activity.appointment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ce2006.project.adapter.AppointmentArrayAdapter;
import com.ce2006.project.model.Appointment;
import com.ce2006.project.server.AppointmentBuilder;
import com.example.user.ce2006_project.R;

/**
 * fragment showing list of appoitnments
 * Created by lhtan on 31/3/15.
 */
public class ViewAppointmentListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private AppointmentBuilder appointmentBuilder;
    private ListView listView;
    private View progressBar;

    private LoadAppointmentTask task;
    private Listener listener;

    public static ViewAppointmentListFragment getFragment(AppointmentBuilder manager) {
        ViewAppointmentListFragment fragment = new ViewAppointmentListFragment();
        fragment.appointmentBuilder = manager;
        return fragment;
    }

    public static ViewAppointmentListFragment getFragment(AppointmentBuilder manager, Listener listener) {
        ViewAppointmentListFragment fragment = new ViewAppointmentListFragment();
        fragment.appointmentBuilder = manager;
        fragment.listener = listener;
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
        View view = inflater.inflate(R.layout.fragment_view_list, null);
        listView = (ListView) view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progressBar);

        task = new LoadAppointmentTask(this, appointmentBuilder);
        task.execute();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        task.cancel(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.appointmentSelected((Appointment) parent.getAdapter().getItem(position));
    }

    public static interface Listener {
        public void appointmentSelected(Appointment appointment);
    }

    /**
     * calling {@link com.ce2006.project.server.AppointmentBuilder#getAppointments()}
     * in {@link android.os.AsyncTask}
     */
    private class LoadAppointmentTask extends AsyncTask<Void, Void, Appointment[]> {
        private AdapterView.OnItemClickListener listener;
        private AppointmentBuilder appointmentBuilder;

        private LoadAppointmentTask(AdapterView.OnItemClickListener listener, AppointmentBuilder appointmentBuilder) {
            this.listener = listener;
            this.appointmentBuilder = appointmentBuilder;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Appointment[] doInBackground(Void... params) {
            return appointmentBuilder.getAppointments();
        }

        @Override
        protected void onPostExecute(Appointment[] appointments) {
            if (appointments != null) {
                listView.setAdapter(new AppointmentArrayAdapter(getActivity(), appointments));
                listView.setOnItemClickListener(listener);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}