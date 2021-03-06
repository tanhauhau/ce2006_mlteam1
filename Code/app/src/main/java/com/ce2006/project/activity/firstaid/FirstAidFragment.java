package com.ce2006.project.activity.firstaid;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.ce2006_project.R;

/**
 * Fragment showing details of the first aid
 */
public class FirstAidFragment extends Fragment {
    private Integer img;
    private CharSequence text;
    private String title;

    public static FirstAidFragment getFragment(Integer img, CharSequence text, String title) {
        FirstAidFragment fragment = new FirstAidFragment();
        fragment.img = img;
        fragment.text = text;
        fragment.title = title;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_aid, null);
        ImageView imgView = (ImageView) view.findViewById(R.id.img);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtView = (TextView) view.findViewById(R.id.txtDesc);
        imgView.setImageResource(img);
        txtView.setText(text);
        txtTitle.setText(title);
        return view;
    }
}
