package com.ce2006.project.activity.firstaid;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.user.ce2006_project.R;

/**
 * First Aid grid view adapter
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private TypedArray imgs;

    public ImageAdapter(Context c) {
        this.context = c;
        imgs = context.getResources().obtainTypedArray(R.array.first_aid_img);
    }

    @Override
    public int getCount() {
        return imgs.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return imgs.getResourceId(position, -1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imgs.getResourceId(position, -1));
        return imageView;
    }
}
