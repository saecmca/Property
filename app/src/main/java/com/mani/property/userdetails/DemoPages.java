package com.mani.property.userdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mani.property.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by Ramesh on 09-Jun-16.
 */
public class DemoPages extends AppCompatActivity {

    ViewPager viewPager;

    ArrayList<String> arrView = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        arrView.add("https://s27.postimg.org/d6uymm89v/Intro1.png");
        arrView.add("https://s27.postimg.org/oz3bdn3sz/Intro2.png");
        arrView.add("https://s17.postimg.org/q5o05uopb/Intro3.png");
        final DemoPagerAdapter book_adapt = new DemoPagerAdapter(DemoPages.this, arrView);
        viewPager.setAdapter(book_adapt);
        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator1);
        circleIndicator.setViewPager(viewPager);

        ViewPager.OnPageChangeListener pagerListener = new ViewPager.OnPageChangeListener() {
            boolean lastPageChange = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int lastIdx = book_adapt.getCount() - 1;


                if (lastPageChange && position == lastIdx) {
                    Intent main = new Intent(DemoPages.this, Login.class);
                    startActivity(main);
                    finish();
                }

            }

            @Override
            public void onPageSelected(int position) {
                // pgText.setCurrentItem(i/2);
                //viewPagerSet(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int lastIdx = book_adapt.getCount() - 1;

                int curItem = viewPager.getCurrentItem();
                if (curItem == lastIdx /*&& lastPos==lastIdx*/ && state == 1)
                    lastPageChange = true;
                else lastPageChange = false;
            }
        };

        viewPager.addOnPageChangeListener(pagerListener);

    }

public  void onBackPressed(){
    ActivityCompat.finishAffinity(this);
}
}
