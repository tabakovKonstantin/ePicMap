package ru.nsu.ccfit.karmanova.epicmap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKPhotoArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Константин on 25.04.2015.
 */
public class FriendsActivity extends Activity implements  ListView.OnItemClickListener {
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private ArrayList<UserData> userDatasSet = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        ListView listFriends = (ListView)findViewById(R.id.list_friends);
        friends = (ArrayList<Friend>) getIntent().getSerializableExtra(MainActivity.FRIENDS_LIST);
        Log.e("", friends.get(1).getName());
        FriedsAdapter adapter = new FriedsAdapter(this, friends);
        listFriends.setAdapter(adapter);
        listFriends.setOnItemClickListener(this);
        int id  = 97415655;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("", friends.get(position).getName());
        startLoading(friends.get(position).getId());
    }

    private void startLoading(int id) {
        VKRequest request = new VKRequest("photos.get", VKParameters.from(VKApiConst.PHOTOS, "qw"), VKRequest.HttpMethod.GET, VKPhotoArray.class);
        request.addExtraParameter("owner_id", id);
        request.addExtraParameter("album_id", "wall");
        request.addExtraParameter("extended", 0);
//        request.addExtraParameters();
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject res = response.json.getJSONObject("response");
                    int count = res.getInt("count");
                    JSONArray itemsArray = res.getJSONArray("items");
                    for (int i = 0; i < count; i++) {
                        JSONObject item = itemsArray.getJSONObject(i);
                        JSONArray namesArray = item.names();
                        for (int j = 0; j < namesArray.length(); j++) {
                            if(namesArray.getString(j).equals("lat")) {
                                String url = item.getString("photo_604");
                                double lon = item.getDouble("long");
                                double lan = item.getDouble("lat");
                                Log.i("Photo", "широта долгота " + String.valueOf(lon) + " " + String.valueOf(lan) + " " + url);

                                ArrayList<String> pictureUrlSet = new ArrayList<>();
                                pictureUrlSet.add(url);

                                for (int k = 0; k < userDatasSet.size(); k++) {
                                    if(userDatasSet.get(k).getCoordinate().equals(new LatLng(lan, lon))) {
                                        userDatasSet.get(k).getPictureUrlSet().add(url);
                                    }

                                }
                                userDatasSet.add(new UserData(new LatLng(lan, lon), pictureUrlSet));
                                continue;
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Photo", "ошибка");
                }

                startViewMap();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }

    private void startViewMap() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MainActivity.USER_DATA_SET, userDatasSet);
        startActivity(intent);
    }
}
