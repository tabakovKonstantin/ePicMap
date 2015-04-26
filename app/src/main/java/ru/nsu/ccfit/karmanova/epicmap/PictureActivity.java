package ru.nsu.ccfit.karmanova.epicmap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Константин on 25.04.2015.
 */
public class PictureActivity extends FragmentActivity {
    private ArrayList<String> pictureUrlSet;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
        initComponent();
        test();
    }

    private void initComponent() {
        pictureUrlSet = (ArrayList<String>) getIntent().getSerializableExtra(MapActivity.PICTURE_URL_SET);
        viewPager = (ViewPager)findViewById(R.id.picasso_viewpager);
    }

    private void test() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())

        {
            @Override
            public Fragment getItem(int position)
            {
                Fragment fragment = new PictureFregment();

                Bundle args = new Bundle();
                args.putString(PictureFregment.IMAGE_URL, pictureUrlSet.get(position));

                fragment.setArguments(args);

                return fragment;
            }

            @Override
            public int getCount()
            {
                return pictureUrlSet.size();
            }
        });
    }
}
