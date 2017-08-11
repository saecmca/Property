package com.mani.property.userdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.home.HomeActivity;
import com.mani.property.webservice.RestClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registeration extends AppCompatActivity {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etUsernameLayout)
    TextInputLayout etUsernameLayout;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etEmailLayout)
    TextInputLayout etEmailLayout;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etMobileLayout)
    TextInputLayout etMobileLayout;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etPasswordLayout)
    TextInputLayout etPasswordLayout;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    private String strName, strEmail, strMobile, strPass;
    private Animation animShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        ButterKnife.bind(this);
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
    }

    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    @OnClick(R.id.btnRegister)
    public void onViewClicked() {
        if (validation()) {
            Dialogbox.keyboard(Registeration.this);
            if(Dialogbox.isNetworkStatusAvialable(this))
            webserviceRegister(strName,strEmail,strMobile,strPass);
        }
    }

    private boolean validation() {
        strName = etUsername.getText().toString();
        strEmail = etEmail.getText().toString();
        strMobile = etMobile.getText().toString();
        strPass = etPwd.getText().toString();
        if (strName.isEmpty()) {
            etUsername.setAnimation(animShake);
            etUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_user_active, 0, 0, 0);
            etUsernameLayout.setErrorEnabled(true);
            etUsernameLayout.setError("Please enter name");
            etUsername.requestFocus();
            return false;
        } else {
            etUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_user_active, 0, 0, 0);
            etUsernameLayout.setErrorEnabled(false);
        }
        if (strEmail.isEmpty()) {
            etEmail.setAnimation(animShake);
            etEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);
            etEmailLayout.setErrorEnabled(true);
            etEmailLayout.setError(getString(R.string.email));
            etEmail.requestFocus();
            return false;
        } else {
            etEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_sel, 0, 0, 0);
            etEmailLayout.setErrorEnabled(false);
        }
        if (strMobile.isEmpty()) {
            etMobile.setAnimation(animShake);
            etMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mobile, 0, 0, 0);
            etMobileLayout.setErrorEnabled(true);
            etMobileLayout.setError("Please enter valid mobile number");
            etMobile.requestFocus();
            return false;
        } else {
            etMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mob_sel, 0, 0, 0);
            etMobileLayout.setErrorEnabled(false);
        }
        if (strPass.isEmpty()) {
            etPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_active, 0, 0, 0);
            etPwd.setAnimation(animShake);
            etPasswordLayout.setErrorEnabled(true);
            etPasswordLayout.setError("Please enter password");
            etPwd.requestFocus();
            return false;
        } else {
            etPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, 0, 0);
            etPasswordLayout.setErrorEnabled(false);
        }
        return true;
    }
    private void webserviceRegister(String Username,String email,String mobile, final String password) {
        try {
            SigninRequest signinRequest = new SigninRequest();
            UserRequest userRequest = new UserRequest();
            signinRequest.setEmail(email);
            signinRequest.setPassword(password);
            signinRequest.setMobile(mobile);
            signinRequest.setUsername(Username);
            userRequest.setUser(signinRequest);
            Dialogbox.showDialog(Registeration.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<SigninResponse> getcancelresponse = apiInterface.getRegister(userRequest);
            getcancelresponse.enqueue(new Callback<SigninResponse>() {
                @Override
                public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                    SigninResponse model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        Localstorage.saveLoginPref(Registeration.this,true,model.getUserId(),model.getUsername(),model.getMobile(),strEmail,model.getAuth_token());
                        Toast.makeText(getApplicationContext(), model.getStatus().getDescription(), Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(Registeration.this, HomeActivity.class);
                        startActivity(main);
                        finish();
                    } else {
                        Dialogbox.alerts(Registeration.this, model.getStatus().getDescription(), "2");
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
