package ru.nsu.ccfit.karmanova.epicmap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Константин on 25.04.2015.
 */
public class PictureFregment extends Fragment {
    public static final String IMAGE_URL = "imageUrl";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture_fragment, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.pictureImageView);
        String url = getArguments().getString(IMAGE_URL);
        Picasso.with(getActivity()).load(url).into(imageView);
        return view;
    }
}
