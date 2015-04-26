package ru.nsu.ccfit.karmanova.epicmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKUsersArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.vk.sdk.api.VKRequest.VKRequestListener;

/**
 * Created by Константин on 25.04.2015.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private Button buttonLogin;
    private Button buttonMap;
    private Button buttonFriends;
    private ArrayList<UserData> userDatasSet;
    public static final String USER_DATA_SET = "userDataSet";
    public static final String FRIENDS_LIST = "friendsList";

    private static final String VK_APP_ID = "4892881";
    private final VKSdkListener sdkListener = new VKSdkListener() {


        @Override
        public void onCaptchaError(VKError vkError) {

        }

        @Override
        public void onTokenExpired(VKAccessToken vkAccessToken) {

        }

        @Override
        public void onAccessDenied(VKError vkError) {

        }
    };

    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private VKRequest currentRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initComponent();
        setListener(this);

        VKSdk.initialize(sdkListener, VK_APP_ID);
        VKUIHelper.onCreate(this);


//        if (VKSdk.wakeUpSession()) {
//            buttonLogin.setVisibility(View.INVISIBLE);
//        } else {
//            buttonLogin.setVisibility(View.VISIBLE);
//            buttonMap.setVisibility(View.INVISIBLE);
//            buttonFriends.setVisibility(View.INVISIBLE);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login: {
                VKSdk.authorize(VKScope.FRIENDS, VKScope.PHOTOS);
                break;
            }
            case R.id.button_map: {
                ArrayList<String> pictureUrlSet = new ArrayList<String>(Arrays.asList("http://i.imgur.com/DvpvklR.png",
                        "http://www.pablo-ruiz-picasso.com/images/works/2.jpg",
                        "http://picassolive.ru/wp-content/uploads/2012/01/Pablo-Picasso_La-repasseuse_19011.jpg",
                        "http://uploads7.wikiart.org/images/pablo-picasso/bullfight-1934.jpg",
                        "http://www.themost10.com/wp-content/uploads/2012/03/Girl-Before-A-Mirror-By-Pablo-Picasso.jpg?5b7486",
                "http://cdn2.hubspot.net/hub/40667/file-14025462-jpeg/images/picasso-las-meninas-group-resized-600.jpeg"));

                ArrayList<String> pictureUrlSet1 = new ArrayList<String>(Arrays.asList(
                        "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRs__i5Tba9N3rBeLd8wec2e-AiwblYyR4FJJQnzwnPqgYFHtC-dg",
                        "http://apikabu.ru/img_n/2011-06_4/1defdd.jpg"));

                LatLng HAMBURG = new LatLng(53.558, 9.927);
                LatLng KIEL = new LatLng(43.551, 8.993);

                ArrayList<UserData> userDatasSet = new ArrayList<>();

                userDatasSet.add(new UserData(HAMBURG, pictureUrlSet));
                userDatasSet.add(new UserData(KIEL, pictureUrlSet1));

                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra(USER_DATA_SET, userDatasSet);
                startActivity(intent);

                break;
            }
            case R.id.button_friends: {
                startLoading();
                break;
            }
        }

    }

    private void initComponent() {
        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonMap = (Button) findViewById(R.id.button_map);
        buttonFriends = (Button) findViewById(R.id.button_friends);
    }

    private void setListener(View.OnClickListener listener) {
        buttonLogin.setOnClickListener(listener);
        buttonMap.setOnClickListener(listener);
        buttonFriends.setOnClickListener(listener);
    }

    private void startLoading() {
        if (currentRequest != null) {
            currentRequest.cancel();
        }

        VKRequest request = new VKRequest("photos.get", VKParameters.from(VKApiConst.PHOTOS, "qw"), VKRequest.HttpMethod.GET, VKPhotoArray.class);
        request.addExtraParameter("owner_id", 97415655);
        request.addExtraParameter("album_id", "wall");
        request.addExtraParameter("extended", 0);
//        request.addExtraParameters();
        request.executeWithListener(new VKRequestListener() {
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
                                continue;
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Photo", "ошибка");
                }


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


        currentRequest = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name"));
        currentRequest.executeWithListener(new VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKUsersArray usersArray = (VKUsersArray) response.parsedModel;
                for (VKApiUserFull userFull : usersArray) {
                    friends.add(new Friend(userFull.last_name, userFull.id));
                    Log.i("Friends", String.valueOf(userFull.id) + " "+ userFull.last_name + " " + userFull.first_name);
                }
                startViewFriendsList();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
//                Log.e("ER",error.errorMessage);
            }
        });
    }

    private void startViewFriendsList() {
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra(FRIENDS_LIST, friends);
        startActivity(intent);

    }
}
