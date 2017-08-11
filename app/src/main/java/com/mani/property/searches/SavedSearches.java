package com.mani.property.searches;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.userdetails.UserRequest;
import com.mani.property.webservice.RestClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedSearches extends AppCompatActivity {

    @BindView(R.id.rclSaved)
    RecyclerView rclSaved;
    // private ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_searches);
        ButterKnife.bind(this);
        rclSaved.setHasFixedSize(true);
        rclSaved.setLayoutManager(new LinearLayoutManager(this));
        if (Dialogbox.isNetworkStatusAvialable(this))
            websercviceSavedSearch();
    }

    private void websercviceSavedSearch() {
        try {
            UserRequest userRequest = new UserRequest();
            userRequest.setUserId(Localstorage.getSavedUserId(this));
            Dialogbox.showDialog(SavedSearches.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<SavedResp> getcancelresponse = apiInterface.getSavedSearch(userRequest);
            getcancelresponse.enqueue(new Callback<SavedResp>() {
                @Override
                public void onResponse(Call<SavedResp> call, Response<SavedResp> response) {
                    SavedResp model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        SavedSearchAdapter savedSearchAdapter = new SavedSearchAdapter(SavedSearches.this, model.getSave_searches());
                        rclSaved.setAdapter(savedSearchAdapter);

                    } else {
                        Dialogbox.alerts(SavedSearches.this, model.getStatus().getDescription(), "2");
                    }
                    Dialogbox.dismissDialog();
                }

                @Override
                public void onFailure(Call<SavedResp> call, Throwable t) {
                    Dialogbox.dismissDialog();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivMenu)
    public void onViewClicked() {
        finish();
    }
}
