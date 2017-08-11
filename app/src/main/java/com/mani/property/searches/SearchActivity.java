package com.mani.property.searches;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mani.property.R;
import com.mani.property.common.AppConstants;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.home.PropertyModel;
import com.mani.property.home.PropertyResp;
import com.mani.property.webservice.RestClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.ivFil)
    ImageView ivFil;
    @BindView(R.id.radoSale)
    RadioButton radoSale;
    @BindView(R.id.rdoRent)
    RadioButton rdoRent;
    @BindView(R.id.rclSearchProperty)
    RecyclerView rclSearchProperty;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.radio_grp)
    RadioGroup radioGrp;

    @BindView(R.id.tvSavetext)
    TextView tvSavetext;

    @BindView(R.id.laySave)
    RelativeLayout laySave;

    private SearchAdapter searchAdapt;
    private ArrayList<PropertyModel> arrProperty = new ArrayList<>();
    private ArrayList<PropertyModel> arrSearchProperty = new ArrayList<>();
    private String selType = "FOR SALE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        try {
            // checked = radoSale.isChecked();
            rclSearchProperty.setHasFixedSize(true);
            rclSearchProperty.setLayoutManager(new LinearLayoutManager(this));
            arrProperty = AppConstants.ARRPROPERTY;

            searchMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radoSale) {
                    selType = "FOR SALE";
                    radoSale.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.colorPrimary));
                    rdoRent.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.gray));
                    selectedMethod(selType);

                    //some code
                } else if (checkedId == R.id.rdoRent) {
                    rdoRent.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.colorPrimary));
                    radoSale.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.gray));

                    selType = "FOR RENT";
                    selectedMethod(selType);

                    //some code
                }
            }
        });
    }

    private void selectedMethod(String rent) {
        try {


            ArrayList<PropertyModel> arrSel = new ArrayList<>();
            if (arrSearchProperty.size() > 0) {
                for (int i = 0; i < arrSearchProperty.size(); i++) {
                    if (arrSearchProperty.get(i).getPosting_type().equals(rent)) {
                        arrSel.add(arrSearchProperty.get(i));
                    }
                }

                searchAdapt = new SearchAdapter(SearchActivity.this, arrSel);
                rclSearchProperty.setAdapter(searchAdapt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchMethod() {
        int searchImgId = searchview.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);

        TextView textView = (TextView) searchview.findViewById(searchImgId);
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        searchview.setActivated(true);
        searchview.setOnQueryTextListener(this);

    }

    @OnClick({R.id.ivBack, R.id.btnSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnSave:
                if (Dialogbox.isNetworkStatusAvialable(this))
                    webserviceSave();
                break;
            default:
        }
    }

    private void webserviceSave() {

        try {

            Dialogbox.showDialog(SearchActivity.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<PropertyResp> getcancelresponse = apiInterface.getSaveSearch(new SaveSearchReq(Localstorage.getSavedUserId(this), tvSavetext.getText().toString().trim()));
            getcancelresponse.enqueue(new Callback<PropertyResp>() {
                @Override
                public void onResponse(Call<PropertyResp> call, Response<PropertyResp> response) {
                    PropertyResp model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        Dialogbox.alerts(SearchActivity.this, model.getStatus().getDescription(), "2");
                    } else {
                        Dialogbox.alerts(SearchActivity.this, model.getStatus().getDescription(), "2");
                    }
                    Dialogbox.dismissDialog();
                }

                @Override
                public void onFailure(Call<PropertyResp> call, Throwable t) {
                    Dialogbox.dismissDialog();
                    Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        arrSearchProperty.clear();
        //adapter.filter(text);
        if (!TextUtils.isEmpty(text)) {
            rclSearchProperty.setVisibility(View.VISIBLE);
            for (PropertyModel searchText : arrProperty) {
               // Log.d("SearchActivity", "onQueryTextChange: " + searchText.getState() + "zip\n" + searchText.getZipcode());
                if (searchText.getCity().toLowerCase().contains(text)||searchText.getState().toLowerCase().contains(text)||searchText.getZipcode().toLowerCase().contains(text)) {
                    laySave.setVisibility(View.VISIBLE);
                    tvSavetext.setText(searchText.getCity());
                    arrSearchProperty.add(searchText);
                }
            }

            selectedMethod(selType);

        } else {
            laySave.setVisibility(View.GONE);
            rclSearchProperty.setVisibility(View.GONE);
            //rclvSearch.setAdapter(adapter);
        }
        return false;
    }
}
