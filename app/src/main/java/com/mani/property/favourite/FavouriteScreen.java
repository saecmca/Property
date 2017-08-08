package com.mani.property.favourite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mani.property.R;
import com.mani.property.home.PropertyModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavouriteScreen extends AppCompatActivity {

    @BindView(R.id.rclFavProperty)
    RecyclerView rclFavProperty;
   private ArrayList<PropertyModel>arrProperty=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_screen);
        ButterKnife.bind(this);
     //   arrProperty=EasyPreference.with(FavouriteScreen.this).getObject("af",PropertyModel.class);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rclFavProperty.setLayoutManager(mLayoutManager);
        FavoriteAdapter listMapAdapter = new FavoriteAdapter(this, arrProperty);
        rclFavProperty.setAdapter(listMapAdapter);
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }
}
