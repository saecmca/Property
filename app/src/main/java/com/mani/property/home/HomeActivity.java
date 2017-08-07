package com.mani.property.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.mani.property.common.ItemClickSupport;
import com.mani.property.common.Localstorage;
import com.mani.property.favourite.FavResp;
import com.mani.property.favourite.FavouriteReq;
import com.mani.property.searches.SearchActivity;
import com.mani.property.userdetails.Login;
import com.mani.property.userdetails.Profile;
import com.mani.property.userdetails.UserRequest;
import com.mani.property.webservice.RestClient;
import com.squareup.picasso.Picasso;

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
    @BindView(R.id.layPopItem)
    LinearLayout layPopItem;
    @BindView(R.id.iv_Favi)
    ImageView iv_Favi;
    @BindView(R.id.tvAmt)
    TextView tvAmt;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tvbed)
    TextView tvbed;
    @BindView(R.id.tvBoth)
    TextView tvBoth;
    @BindView(R.id.tvAddr)
    TextView tvAddr;
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.iv_Image)
    ImageView iv_Image;
    private ListMapAdapter listMapAdapter;
    private int selectPos = 0;
    private MapView mMapView;
    private GoogleMap googleMap;
    private boolean doubleBackToExitPressedOnce = false;
    private ArrayList<PropertyModel> arrProperty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setUpNavigationView();
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
                if (arrProperty.size() > 0)
                    setMArkers(arrProperty);
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
                layPopItem.setVisibility(View.GONE);//selected map marker Item
                tvListview.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                tvListview.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_list_blue, 0, 0, 0);
                tvMapview.setTextColor(ContextCompat.getColor(this, R.color.gray));
                tvMapview.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_map_gray, 0, 0, 0);

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                rclProperty.setLayoutManager(mLayoutManager);
                listMapAdapter = new ListMapAdapter(this, arrProperty);
                rclProperty.setAdapter(listMapAdapter);
              /*  rclProperty.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        final ImageView Imageview = (ImageView)v.findViewById(R.id.iv_Favi);
                        Imageview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return false;
                    }
                });*/
                ItemClickSupport.addTo(rclProperty).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if (Dialogbox.isNetworkStatusAvialable(HomeActivity.this))
                            favouriteMethod(arrProperty.get(position).getZpid(), arrProperty.get(position).isFavorite(), position, "listview");

                    }
                });

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
                        //  setMArkers(arrProperty);
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
        try {
            View markers = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.window_info_layout, null);
            TextView numTxt = (TextView) markers.findViewById(R.id.num_txt);


            for (int i = 0; i < maparray_count.size(); i++) {

                MarkerOptions markerOptions = new MarkerOptions();

                numTxt.setText("$" + maparray_count.get(i).getAmount());
                if (maparray_count.get(i).getPosting_type().contains("FOR RENT")) {
                    numTxt.setBackgroundResource(R.mipmap.location_label_purple);
                    markerOptions.position(new LatLng(Double.valueOf(maparray_count.get(i).getLatitude()), Double.valueOf(maparray_count.get(i).getLongitude()))).
                            icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markers)));
                } else if (maparray_count.get(i).getPosting_type().contains("FOR SALE")) {
                    numTxt.setBackgroundResource(R.mipmap.location_label_red);
                    markerOptions.position(new LatLng(Double.valueOf(maparray_count.get(i).getLatitude()), Double.valueOf(maparray_count.get(i).getLongitude()))).
                            icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markers)));
                }
                Marker marker = googleMap.addMarker(markerOptions);
                marker.showInfoWindow();

            }

            //  googleMap.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);
            // googleMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom - 10.5f));
            // googleMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(maparray_count.get(0).getLatitude()), Double.valueOf(maparray_count.get(0).getLongitude())), 15));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.valueOf(maparray_count.get(0).getLatitude()), Double.valueOf(maparray_count.get(0).getLongitude())))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    LatLng position = arg0.getPosition();
                    propertySelectedItem(position);

                    return true;
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void propertySelectedItem(LatLng position) {
        layPopItem.setVisibility(View.VISIBLE);

        for (int i = 0; i < arrProperty.size(); i++) {
            if (String.valueOf(position.latitude).equals(arrProperty.get(i).getLatitude())) {


                try {
                    final PropertyModel positionMovie = arrProperty.get(i);
                    selectPos = i;
                    tv_type.setText("  " + positionMovie.getPosting_type());
                    if (positionMovie.getPosting_type().contains("FOR RENT"))
                        tv_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_rent, 0, 0, 0);
                    else if (positionMovie.getPosting_type().contains("FOR SALE"))
                        tv_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_sale, 0, 0, 0);
                    tvAmt.setText("$" + positionMovie.getAmount());
                    tvbed.setText(positionMovie.getBedrooms() + " Bed(s)");
                    tvBoth.setText(positionMovie.getBathrooms() + " Bath(s)");
                    tvArea.setText(positionMovie.getSquareaft() + " Sqfts");
                    tvAddr.setText(positionMovie.getStreet() + ", " + positionMovie.getCity());
                    if (positionMovie.getImages().getUrl() != null)
                        Picasso.with(this).load(positionMovie.getImages().getUrl().get(0)).into(iv_Image);
                    else iv_Image.setImageResource(R.drawable.loginbg);
                    if (positionMovie.isFavorite())
                        iv_Favi.setImageResource(R.drawable.icon_favorite_red);
                    else iv_Favi.setImageResource(R.drawable.icon_favorite_trans);
                    iv_Favi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Dialogbox.isNetworkStatusAvialable(HomeActivity.this))
                                favouriteMethod(positionMovie.getZpid(), positionMovie.isFavorite(), selectPos, "mapview");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void favouriteMethod(String zipcode, final boolean fav, final int pos, final String type) {
        try {

            Dialogbox.showDialog(HomeActivity.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<FavResp> getcancelresponse = apiInterface.getSavePropety(new FavouriteReq(Localstorage.getSavedUserId(this), zipcode, fav));
            getcancelresponse.enqueue(new Callback<FavResp>() {
                @Override
                public void onResponse(Call<FavResp> call, Response<FavResp> response) {
                    FavResp model = response.body();
                    if (model != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        Dialogbox.alerts(HomeActivity.this, model.getStatus().getDescription(), "2");
                        if (fav) {
                            arrProperty.get(pos).setFavorite(false);
                            iv_Favi.setImageResource(R.drawable.icon_favorite_trans);
                        } else {
                            arrProperty.get(pos).setFavorite(true);
                            iv_Favi.setImageResource(R.drawable.icon_favorite_red);
                        }
                        if (type.equals("listview")) {
                            listMapAdapter.notifyDataSetChanged();
                        }

                        //  setMArkers(arrProperty);
                    } else {
                        Dialogbox.alerts(HomeActivity.this, model.getStatus().getDescription(), "2");
                    }
                    Dialogbox.dismissDialog();
                }

                @Override
                public void onFailure(Call<FavResp> call, Throwable t) {
                    Dialogbox.dismissDialog();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap = mgoogleMap;
    }
}
