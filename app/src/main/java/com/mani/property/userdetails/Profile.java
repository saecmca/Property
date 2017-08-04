package com.mani.property.userdetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.webservice.RestClient;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {


    @BindView(R.id.ivprof)
    CircularImageView ivprof;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.tvUsermail)
    TextView tvUsermail;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etMob)
    EditText etMob;

    @BindView(R.id.ivMenu)
    ImageView ivMenu;
    @BindView(R.id.tvsave)
    TextView tvsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        if (Dialogbox.isNetworkStatusAvialable(this))
            tvUser.setText(Localstorage.getUsrName(this));
        etUsername.setText(Localstorage.getUsrName(this));
        etEmail.setText(Localstorage.getUsrEmail(this));
        etMob.setText(Localstorage.getUsrMob(this));
        tvUsermail.setText(Localstorage.getUsrEmail(this));
    }


    @OnClick({R.id.ivMenu, R.id.tvsave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                finish();
                break;
            case R.id.tvsave:
                if(etUsername.getText().toString().trim().equals("")){
                    etUsername.setError("Please enter username");
                }else if(etMob.getText().toString().trim().equals("")){
                    etMob.setError("Please enter mobile number");
                }else{
                    if(Dialogbox.isNetworkStatusAvialable(this))
                        webserviceProfile(etUsername.getText().toString().trim(),etMob.getText().toString().trim());
                }
                break;
        }
    }

    private void webserviceProfile(String Username, final String mobile) {
        try {
            SigninRequest signinRequest = new SigninRequest();
            UserRequest userRequest = new UserRequest();
            signinRequest.setMobile(mobile);
            signinRequest.setUsername(Username);
            userRequest.setUserId(Localstorage.getSavedUserId(this));
            userRequest.setUser(signinRequest);
            Dialogbox.showDialog(Profile.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<SigninResponse> getcancelresponse = apiInterface.getProfileUpdate(userRequest);
            getcancelresponse.enqueue(new Callback<SigninResponse>() {
                @Override
                public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                    SigninResponse model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        String useremail=Localstorage.getUsrEmail(Profile.this);
                        Localstorage.saveLoginPref(Profile.this, true, model.getUserId(), model.getUsername(), model.getMobile(), useremail, model.getAuth_token());

                        finish();
                    } else {
                        Dialogbox.alerts(Profile.this, model.getStatus().getDescription(), "2");
                    }
                    Dialogbox.dismissDialog();
                }

                @Override
                public void onFailure(Call<SigninResponse> call, Throwable t) {
                    Dialogbox.dismissDialog();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
