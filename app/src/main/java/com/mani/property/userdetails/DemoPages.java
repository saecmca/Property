package com.mani.property.userdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.webservice.RestClient;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if(Dialogbox.isNetworkStatusAvialable(this))
            webserviceDemo();



    }

    private void webserviceDemo() {
        try {

            Dialogbox.showDialog(DemoPages.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<DemoResp> getcancelresponse = apiInterface.getDemo();
            getcancelresponse.enqueue(new Callback<DemoResp>() {
                @Override
                public void onResponse(Call<DemoResp> call, Response<DemoResp> response) {
                    DemoResp model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        arrView= model.getImages();
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
                    } else {
                        Dialogbox.alerts(DemoPages.this, model.getStatus().getDescription(), "2");
                    }
                    Dialogbox.dismissDialog();
                }

                @Override
                public void onFailure(Call<DemoResp> call, Throwable t) {
                    Dialogbox.dismissDialog();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void onBackPressed(){
    ActivityCompat.finishAffinity(this);
}
}
