package com.example.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//tài liệu tham khảo: https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
public class UserAdapter extends ArrayAdapter<SV> {

    public UserAdapter(Context context, ArrayList<SV> sv) {
        super(context, 0, sv);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SV sv = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvID);
        // Populate the data into the template view using the data object
        tvName.setText(sv.hoten);
        tvHome.setText(sv.mssv);
        // Return the completed view to render on screen
        return convertView;
    }
}