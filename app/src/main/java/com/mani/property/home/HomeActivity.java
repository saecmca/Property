package com.mani.property.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.searches.SearchActivity;
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

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {
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
    @BindView(R.id.rclProperty)
    RecyclerView rclProperty;
    private MapView mMapView;
    private GoogleMap googleMap;
    private boolean doubleBackToExitPressedOnce = false;
    private ArrayList<PropertyModel> arrProperty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        if (Dialogbox.isNetworkStatusAvialable(this))
            websercviceProperty();
        mMapView = (MapView) findViewById(R.id.near_by_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);


            }
        });
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

    @OnClick({R.id.ivMenu, R.id.ivSearch, R.id.ivFav, R.id.bntStart, R.id.tvMapview, R.id.tvListview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.ivSearch:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.ivFav:
                break;
            case R.id.bntStart:
                layEnd.setVisibility(View.VISIBLE);
                layStart.setVisibility(View.GONE);
                break;
            case R.id.tvMapview:
                mMapView.setVisibility(View.VISIBLE);
                rclProperty.setVisibility(View.GONE);
                tvListview.setTextColor(ContextCompat.getColor(this, R.color.gray));
                tvListview.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_list_gray, 0, 0, 0);
                tvMapview.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                tvMapview.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_map_blue, 0, 0, 0);
                if (arrProperty.size() > 0)
                    setMArkers(arrProperty);
                break;
            case R.id.tvListview:
                mMapView.setVisibility(View.GONE);
                rclProperty.setVisibility(View.VISIBLE);
                tvListview.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                tvListview.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_list_blue, 0, 0, 0);
                tvMapview.setTextColor(ContextCompat.getColor(this, R.color.gray));
                tvMapview.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_map_gray, 0, 0, 0);

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                rclProperty.setLayoutManager(mLayoutManager);
                ListMapAdapter listMapAdapter = new ListMapAdapter(this, arrProperty);
                rclProperty.setAdapter(listMapAdapter);
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
                        arrProperty = model.getProperties();
                        setMArkers(arrProperty);
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

    private void setMArkers(final ArrayList<PropertyModel> maparray_count) {
        googleMap.clear();
        for (int i = 0; i < maparray_count.size(); i++) {

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("hi").snippet("asdf");

            if (maparray_count.get(i).getPosting_type().contains("FOR RENT")) {
                markerOptions.position(new LatLng(Double.valueOf(maparray_count.get(i).getLatitude()), Double.valueOf(maparray_count.get(i).getLongitude()))).
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_label_red));
            } else if (maparray_count.get(i).getPosting_type().contains("FOR SALE")) {
                markerOptions.position(new LatLng(Double.valueOf(maparray_count.get(i).getLatitude()), Double.valueOf(maparray_count.get(i).getLongitude()))).
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_label_purple));
            }
            Marker marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();

        }


        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(maparray_count.get(0).getLatitude()), Double.valueOf(maparray_count.get(0).getLongitude())), 10));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.valueOf(maparray_count.get(0).getLatitude()), Double.valueOf(maparray_count.get(0).getLongitude())))      // Sets the center of the map to location user
                .zoom(10)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {


                View v = getLayoutInflater().inflate(R.layout.window_info_layout, null);
                LatLng position = arg0.getPosition();

                TextView locname = (TextView) v.findViewById(R.id.loc_name);
                TextView locdist = (TextView) v.findViewById(R.id.loc_dist);
                TextView locadd = (TextView) v.findViewById(R.id.loc_address);
                ImageView marker_image = (ImageView) v.findViewById(R.id.marker_image);
                for (int i = 0; i < maparray_count.size(); i++) {
                    if (String.valueOf(position.latitude) == maparray_count.get(i).getLatitude()) {
                        locname.setText(maparray_count.get(i).getCity());
                        locdist.setText(maparray_count.get(i).getState());
                        locadd.setText(maparray_count.get(i).getStreet());

                    }
                }

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                return v;

            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(HomeActivity.this, "Hai", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap = mgoogleMap;
    }
}
