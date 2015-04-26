package ru.nsu.ccfit.karmanova.epicmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Константин on 25.04.2015.
 */
public class UserData implements Serializable{
    private double latitude;
    private double longitude;
    private ArrayList<String> pictureUrlSet;

    public UserData(LatLng coordinate, ArrayList<String> pictureUrlSet) {
        this.latitude = coordinate.latitude;
        this.longitude = coordinate.longitude;
        this.pictureUrlSet = pictureUrlSet;
    }

    public LatLng getCoordinate() {

        return new LatLng(latitude, longitude);
    }

    public ArrayList<String> getPictureUrlSet() {
        return pictureUrlSet;
    }
}
