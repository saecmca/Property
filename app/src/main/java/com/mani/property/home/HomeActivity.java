package com.mani.property.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.userdetails.Login;
import com.mani.property.userdetails.Profile;
import com.mani.property.userdetails.UserRequest;
import com.mani.property.webservice.RestClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity  {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ivMenu)
    ImageView ivMenu;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.ivFav)
    ImageView ivFav;
    @BindView(R.id.bntStart)
    Button bntStart;
    @BindView(R.id.layStart)
    RelativeLayout layStart;
    @BindView(R.id.layEnd)
    LinearLayout layEnd;
    @BindView(R.id.tvMapview)
    TextView tvMapview;
    @BindView(R.id.tvListview)
    TextView tvListview;
    private boolean doubleBackToExitPressedOnce = false;
       private ArrayList<PropertyModel>arrProperty=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setUpNavigationView();
        Log.w("test", "onCreate: test terminal" );
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            ActivityCompat.finishAffinity(this);
            return;
        }
        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;

                    case R.id.nav_logout:
                        Localstorage.clearAllPreferences(HomeActivity.this);
                        Toast.makeText(getApplicationContext(), "Successfully loged out", Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(HomeActivity.this, Login.class);
                        startActivity(main);
                        finish();
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(HomeActivity.this, Profile.class));

                    default:

                }


                return true;
            }
        });
    }

    @OnClick({R.id.ivMenu,R.id.ivSearch, R.id.ivFav, R.id.bntStart, R.id.tvMapview, R.id.tvListview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.ivSearch:
                break;
            case R.id.ivFav:
                break;
            case R.id.bntStart:
                layEnd.setVisibility(View.VISIBLE);
                layStart.setVisibility(View.GONE);
                if(Dialogbox.isNetworkStatusAvialable(this))
                    websercviceProperty();
                break;
            case R.id.tvMapview:
                break;
            case R.id.tvListview:
                break;
        }
    }

    private void websercviceProperty() {

        try {
             UserRequest userRequest = new UserRequest();
            userRequest.setUserId(Localstorage.getSavedUserId(this));
            Dialogbox.showDialog(HomeActivity.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<PropertyResp> getcancelresponse = apiInterface.getPrpertyList(userRequest);
            getcancelresponse.enqueue(new Callback<PropertyResp>() {
                @Override
                public void onResponse(Call<PropertyResp> call, Response<PropertyResp> response) {
                    PropertyResp model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        arrProperty=model.getProperties();
                    } else {
                        Dialogbox.alerts(HomeActivity.this, model.getStatus().getDescription(), "2");
                    }
                    Dialogbox.dismissDialog();
                }

                @Override
                public void onFailure(Call<PropertyResp> call, Throwable t) {
                    Dialogbox.dismissDialog();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
