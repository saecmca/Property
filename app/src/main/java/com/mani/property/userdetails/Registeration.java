package com.mani.property.userdetails;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.*;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.home.HomeActivity;
import com.mani.property.webservice.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

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
    /*@BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etMobileLayout)
    TextInputLayout etMobileLayout;*/
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etPasswordLayout)
    TextInputLayout etPasswordLayout;

    @BindView(R.id.etconPwd)
    EditText etconPwd;
    @BindView(R.id.etconPasswordLayout)
    TextInputLayout etconPasswordLayout;


    @BindView(R.id.btnRegister)
    Button btnRegister;
    private String strName, strEmail, strMobile, strPass,strConPwd;
    private Animation animShake;
     private LoginButton loginButton;
    /* Facebook integration*/
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);


        /* Facebook */
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_friends"));

        loginButton.registerCallback(callbackManager, callback);


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(com.facebook.Profile oldProfile, com.facebook.Profile newProfile) {
                displayMessage(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        ButterKnife.bind(this);
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
    }

    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    @OnClick({R.id.btnRegister,R.id.ivMenu, R.id.btnFacebook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                if (validation()) {
                    Dialogbox.keyboard(Registeration.this);
                    if (Dialogbox.isNetworkStatusAvialable(this))
                        webserviceRegister(strName, strEmail, strMobile, strPass);
                }
                break;
            case R.id.ivMenu:
                finish();
                break;
            case R.id.btnFacebook:
                Profile profile = Profile.getCurrentProfile().getCurrentProfile();
                if (profile != null) {
                    // user has logged in
                    LoginManager.getInstance().logOut();

                } else {
                    // user has not logged in
                    loginButton.performClick();
                }
                break;
            default:
        }
    }

    private boolean validation() {
        strName = etUsername.getText().toString();
        strEmail = etEmail.getText().toString();
        strConPwd = etconPwd.getText().toString();
        strMobile = "";
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

        if (strPass.isEmpty()) {
            etPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, 0, 0);
            etPwd.setAnimation(animShake);
            etPasswordLayout.setErrorEnabled(true);
            etPasswordLayout.setError("Please enter password");
            etPwd.requestFocus();
            return false;
        } else {
            etPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_active, 0, 0, 0);
            etPasswordLayout.setErrorEnabled(false);
        }
        if (strConPwd.isEmpty() || !strPass.equals(strConPwd)) {
            etconPwd.setAnimation(animShake);
            etconPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password, 0, 0, 0);
            etconPasswordLayout.setErrorEnabled(true);
            etconPasswordLayout.setError("Please enter confirm password/correct confirm password");
            etconPwd.requestFocus();
            return false;
        } else {
            etconPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_active, 0, 0, 0);
            etconPasswordLayout.setErrorEnabled(false);
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






    /* Facebook Codes */
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
            displayMessage(profile);

            System.out.println("onSuccess");
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    // Get facebook data from login
                    Bundle bFacebookData = getFacebookData(object);

                    String FacebookID = String.valueOf(bFacebookData.get("idFacebook"));
                    String EmailID = String.valueOf(bFacebookData.get("email"));
                    String ProfileIMG = String.valueOf(bFacebookData.get("profile_pic"));
                    String FirstName = String.valueOf(bFacebookData.get("first_name"));
                    String lastName = String.valueOf(bFacebookData.get("last_name"));
                    String gender = String.valueOf(bFacebookData.get("gender"));
                    Log.w("facebook", "onCompleted: " + EmailID);
                    //   Localstorage.saveLoginPref(Login.this, true, "", FirstName, "", EmailID, "");
                    //Toast.makeText(getApplicationContext(), model.getStatus().getDescription(), Toast.LENGTH_SHORT).show();
                  /*  Intent main = new Intent(Login.this, HomeActivity.class);
                    startActivity(main);
                    finish();*/
                    if (Dialogbox.isNetworkStatusAvialable(Registeration.this))
                        webserviceFaceBookLogin(EmailID, "", FirstName);
                    // signinPresenter.callSocailmediaservice(FirstName + " " + lastName, EmailID, "", gender, AppConstants.PLATFORM, ProfileIMG, FacebookID, AppConstants.SocailFacebook, getSavedFirebaseToken);

                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();


        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            e.printStackTrace();
        }
    };

    private void webserviceFaceBookLogin(final String Emailid, final String mobile, final String Username) {
        try {
            SigninRequest signinRequest = new SigninRequest();
            signinRequest.setEmail(Emailid);
            signinRequest.setMobile(mobile);
            signinRequest.setType("facebook");

            Dialogbox.showDialog(Registeration.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<SigninResponse> getcancelresponse = apiInterface.getFacebookSignin(signinRequest);
            getcancelresponse.enqueue(new Callback<SigninResponse>() {
                @Override
                public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                    SigninResponse model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        Localstorage.saveLoginPref(Registeration.this, true, model.getUserId(), Username, model.getMobile(), Emailid, model.getAuth_token());
                        Toast.makeText(getApplicationContext(), model.getStatus().getDescription(), Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(Registeration.this, HomeActivity.class);
                        startActivity(main);
                        finish();
                    } else if (model.getStatus().getId().equals("0") && model.getStatus().getDescription().equals("Mobile can't be blank")) {
                        new MaterialDialog.Builder(Registeration.this)
                                .title("Alert")
                                .cancelable(false)
                                .inputRangeRes(5, 16, R.color.red)
                                .inputType(InputType.TYPE_CLASS_NUMBER)
                                .input("Mobile number", null, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        // Do something
                                        if (input != null && !TextUtils.isEmpty(input) && input.length() < 16) {
                                            dialog.dismiss();
                                            if (Dialogbox.isNetworkStatusAvialable(Registeration.this))
                                                webserviceFaceBookLogin(Emailid, input.toString(), Username);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please Enter valid Mobile number", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).positiveText("OK").positiveColor(Color.BLACK).show();


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

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

                Log.d("facebook email", "getFacebookData: " + object.getString("email"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d("facebook", "Error parsing JSON");
        }
        return null;
    }

    private void displayMessage(com.facebook.Profile profile) {
        if (profile != null) {
            Log.d("Facebook", "Name : " + profile.getName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

    }

    @Override
    public void onResume() {
        super.onResume();
        com.facebook.Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }


}
