package ru.nsu.ccfit.karmanova.epicmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Константин on 26.04.2015.
 */
public class FriedsAdapter extends ArrayAdapter<Friend> {

    public FriedsAdapter(Context context, ArrayList<Friend> friend) {
        super(context, 0, friend);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friend friend = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_friend, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
//        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        tvName.setText(friend.getName());
//        tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return convertView;
    }
}
