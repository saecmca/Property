package com.mani.property.userdetails;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.common.SwipeTouchListener;
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

/**
 * Created by Mani on 29-07-2017.
 */

public class Login extends AppCompatActivity {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.btnSignin)
    Button btnSignin;
    @BindView(R.id.btnFacebook)
    Button btnFacebook;
    @BindView(R.id.tvForgot)
    TextView tvForgot;
    @BindView(R.id.tvCreate)
    TextView tvCreate;
    @BindView(R.id.etUsernameLayout)
    TextInputLayout etUsernameLayout;
    @BindView(R.id.etPasswordLayout)
    TextInputLayout etPasswordLayout;

    @BindView(R.id.scroll)
    ScrollView scroll;


    LoginButton loginButton;
    private String strUsername, strPassword;
    private Vibrator vib;
    private Animation animShake;
    /* Facebook integration*/
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
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
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        ButterKnife.bind(this);


        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        scroll.setOnTouchListener(new SwipeTouchListener(Login.this) {
            public void onSwipeRight() {
                onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        Intent main=new Intent(this,DemoPages.class);
        startActivity(main);
        finish();
    }

    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @OnClick({R.id.btnSignin, R.id.btnFacebook, R.id.tvForgot, R.id.tvCreate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSignin:
                if (!checkValidation()) {
                    etUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_user_active, 0, 0, 0);
                    etUsername.setAnimation(animShake);
                    return;
                }
                if (!validPass()) {
                    etPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_active, 0, 0, 0);
                    etPwd.startAnimation(animShake);
                    return;
                }
                etUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_user_active, 0, 0, 0);
                etPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_password_active, 0, 0, 0);
                etUsernameLayout.setErrorEnabled(false);
                etPasswordLayout.setErrorEnabled(false);
                Dialogbox.keyboard(Login.this);
                if (Dialogbox.isNetworkStatusAvialable(this))
                    webserviceLogin(strUsername, strPassword);


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
            case R.id.tvForgot:
                try {
                    final Dialog dialog = new Dialog(Login.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Window window = dialog.getWindow();
                    dialog.setContentView(R.layout.forgot_dialog);
                    window.setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    final EditText editText = (EditText) dialog.findViewById(R.id.tvEmail);
                    ((TextView) dialog.findViewById(R.id.tvOk)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isValidEmail(editText.getText().toString().trim())) {
                                Toast.makeText(getApplicationContext(), "Please enter your valid Email Id", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                Dialogbox.keyboard(Login.this);
                                if (Dialogbox.isNetworkStatusAvialable(Login.this))
                                    webserviceForgot(editText.getText().toString().trim());
                            }
                        }
                    });

                    ((TextView) dialog.findViewById(R.id.tvCancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tvCreate:
                startActivity(new Intent(this, Registeration.class));
                break;
        }
    }

    private boolean checkValidation() {
        strUsername = etUsername.getText().toString();
        if (strUsername.isEmpty()) {
            etUsernameLayout.setErrorEnabled(true);
            etUsernameLayout.setError("Please enter your Email Id");
            etUsername.requestFocus();
            return false;
        } else if (!isValidEmail(strUsername)) {
            etUsernameLayout.setErrorEnabled(true);
            etUsernameLayout.setError(getString(R.string.validemail));
            etUsername.requestFocus();
            return false;
        }
        etUsernameLayout.setErrorEnabled(false);

        return true;
    }

    private boolean validPass() {
        strPassword = etPwd.getText().toString();
        if (strPassword.isEmpty()) {
            etPasswordLayout.setErrorEnabled(true);
            etPasswordLayout.setError(getString(R.string.validpasee));
            etPwd.requestFocus();
            return false;
        }
        etPasswordLayout.setErrorEnabled(false);
        return true;

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void webserviceLogin(String Username, final String password) {
        try {
            SigninRequest signinRequest = new SigninRequest();
            UserRequest userRequest = new UserRequest();
            signinRequest.setEmail(Username);
            signinRequest.setPassword(password);
            userRequest.setUser(signinRequest);
            Dialogbox.showDialog(Login.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<SigninResponse> getcancelresponse = apiInterface.getSignin(userRequest);
            getcancelresponse.enqueue(new Callback<SigninResponse>() {
                @Override
                public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                    SigninResponse model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        Localstorage.saveLoginPref(Login.this, true, model.getUserId(), model.getUsername(), model.getMobile(), strUsername, model.getAuth_token());
                        Toast.makeText(getApplicationContext(), model.getStatus().getDescription(), Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(Login.this, HomeActivity.class);
                        startActivity(main);
                        finish();
                    } else {
                        Dialogbox.alerts(Login.this, model.getStatus().getDescription(), "2");
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
            Profile profile = Profile.getCurrentProfile();
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
                    if (Dialogbox.isNetworkStatusAvialable(Login.this))
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
            if (e instanceof FacebookAuthorizationException) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
            }
        }
    };

    private void webserviceFaceBookLogin(final String Emailid, final String mobile, final String Username) {
        try {
            SigninRequest signinRequest = new SigninRequest();
            signinRequest.setEmail(Emailid);
            signinRequest.setMobile(mobile);
            signinRequest.setType("facebook");

            Dialogbox.showDialog(Login.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<SigninResponse> getcancelresponse = apiInterface.getFacebookSignin(signinRequest);
            getcancelresponse.enqueue(new Callback<SigninResponse>() {
                @Override
                public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                    SigninResponse model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        Localstorage.saveLoginPref(Login.this, true, model.getUserId(), Username, model.getMobile(), Emailid, model.getAuth_token());
                        Toast.makeText(getApplicationContext(), model.getStatus().getDescription(), Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(Login.this, HomeActivity.class);
                        startActivity(main);
                        finish();
                    } else if (model.getStatus().getId().equals("0") && model.getStatus().getDescription().equals("Mobile can't be blank")) {
                        new MaterialDialog.Builder(Login.this)
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
                                            if (Dialogbox.isNetworkStatusAvialable(Login.this))
                                                webserviceFaceBookLogin(Emailid, input.toString(), Username);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please Enter valid Mobile number", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).positiveText("OK").positiveColor(Color.BLACK).show();


                    } else {
                        Dialogbox.alerts(Login.this, model.getStatus().getDescription(), "2");
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

    private void displayMessage(Profile profile) {
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
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    private void webserviceForgot(String Username) {
        try {
            SigninRequest signinRequest = new SigninRequest();
            UserRequest userRequest = new UserRequest();
            signinRequest.setEmail(Username);

            userRequest.setUser(signinRequest);
            Dialogbox.showDialog(Login.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<SigninResponse> getcancelresponse = apiInterface.getForgot(userRequest);
            getcancelresponse.enqueue(new Callback<SigninResponse>() {
                @Override
                public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                    SigninResponse model = response.body();
                    if (model.getStatus().getId().equalsIgnoreCase("1")) {
                        Dialogbox.alerts(Login.this, model.getStatus().getDescription(), "2");
                    } else {
                        Dialogbox.alerts(Login.this, model.getStatus().getDescription(), "2");
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
