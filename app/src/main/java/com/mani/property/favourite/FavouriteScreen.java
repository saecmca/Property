package com.mani.property.favourite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mani.property.R;
import com.mani.property.common.AppConstants;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.home.PropertyModel;
import com.mani.property.home.PropertyResp;
import com.mani.property.userdetails.UserRequest;
import com.mani.property.webservice.RestClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteScreen extends AppCompatActivity {

    @BindView(R.id.rclFavProperty)
    RecyclerView rclFavProperty;
    @BindView(R.id.tvNoproperty)
    TextView tvNoproperty;

    private ArrayList<PropertyModel> arrProperty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_screen);
        ButterKnife.bind(this);
        //   arrProperty=EasyPreference.with(FavouriteScreen.this).getObject("af",PropertyModel.class);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rclFavProperty.setLayoutManager(mLayoutManager);
        if (Dialogbox.isNetworkStatusAvialable(this)) {
            if (AppConstants.ARRPROPERTY.size() > 0) {
                for (int i = 0; i < AppConstants.ARRPROPERTY.size(); i++) {
                    if (AppConstants.ARRPROPERTY.get(i).isFavorite()) {
                        arrProperty.add(AppConstants.ARRPROPERTY.get(i));
                    }
                }
                if (arrProperty.size() > 0) {
                    FavoriteAdapter listMapAdapter = new FavoriteAdapter(FavouriteScreen.this, arrProperty);
                    rclFavProperty.setAdapter(listMapAdapter);
                } else {
                    tvNoproperty.setVisibility(View.VISIBLE);
                }
            } else {

                websercviceProperty();
            }
        }


    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    private void websercviceProperty() {

        try {
            UserRequest userRequest = new UserRequest();
            userRequest.setUserId(Localstorage.getSavedUserId(this));
            Dialogbox.showDialog(FavouriteScreen.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<PropertyResp> getcancelresponse = apiInterface.getFavPrpertyList(userRequest);
            getcancelresponse.enqueue(new Callback<PropertyResp>() {
                @Override
                public void onResponse(Call<PropertyResp> call, Response<PropertyResp> response) {
                    PropertyResp model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        arrProperty = model.getFavorite_properties();
                        if (arrProperty.size() > 0) {
                            FavoriteAdapter listMapAdapter = new FavoriteAdapter(FavouriteScreen.this, arrProperty);
                            rclFavProperty.setAdapter(listMapAdapter);
                        } else tvNoproperty.setVisibility(View.VISIBLE);

                    } else {
                        tvNoproperty.setVisibility(View.VISIBLE);
                        Dialogbox.alerts(FavouriteScreen.this, model.getStatus().getDescription(), "2");
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
