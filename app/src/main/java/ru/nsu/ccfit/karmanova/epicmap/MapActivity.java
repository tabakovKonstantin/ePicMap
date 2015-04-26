package ru.nsu.ccfit.karmanova.epicmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Константин on 25.04.2015.
 */
public class MapActivity extends Activity implements GoogleMap.OnMarkerClickListener {
    public static final String PICTURE_URL_SET = "pictureUrlSet";
    private ArrayList<UserData> userDatasSet;
    private ArrayList<String> pictureUrlSet;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        initComponent();
        addMarkerToMap(userDatasSet);
    }

    private void initComponent() {
        userDatasSet = (ArrayList<UserData>) getIntent().getSerializableExtra(MainActivity.USER_DATA_SET);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMarkerClickListener(this);
    }


    private void addMarkerToMap (ArrayList<UserData> userDataSet) {
        if (map != null) {
            Log.i("Photo", "count  "+String.valueOf(userDataSet.size()));
            for(UserData userData : userDataSet) {

                Marker marker = map.addMarker(new MarkerOptions()
                        .position(userData.getCoordinate())
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.ic_launcher)));
            }
        } else {
            Log.e("ePicMAP_ERROR", "ERROR add");
        }
    }



    @Override
    public boolean onMarkerClick(Marker marker) {

        int idMarker = Integer.valueOf(marker.getId().substring(1));
        pictureUrlSet = userDatasSet.get(idMarker).getPictureUrlSet();
        startViewPicture();
        return false;
    }

    private void startViewPicture() {
        Intent intent =  new Intent(this, PictureActivity.class);
        intent.putExtra(PICTURE_URL_SET, pictureUrlSet);
        startActivity(intent);
    }

}
