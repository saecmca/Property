package com.mani.property;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.mani.property.common.Localstorage;
import com.mani.property.home.HomeActivity;
import com.mani.property.userdetails.Login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Localstorage.isAlreadyLoggedIn(MainActivity.this)) {
                    Intent main = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(main);
                    finish();
                } else {
                    Intent main = new Intent(MainActivity.this, Login.class);
                    startActivity(main);
                    finish();
                }


            }
        }, SPLASH_DISPLAY_LENGTH);
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.mani.property", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
