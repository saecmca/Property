package com.mani.property.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mani on 31-07-2017.
 */

public class Localstorage {
    public static boolean isAlreadyLoggedIn(Context context) {
        SharedPreferences prefLogin = context.getSharedPreferences("save_pref", Context.MODE_PRIVATE);
        return prefLogin.getBoolean("isLoginSuccessful", false);
    }

    public static void saveLoginPref(Context context, boolean isLoginSuccessful, String userid, String username, String usermobile, String useremail, String authtoken) {
        SharedPreferences prefMember = context.getSharedPreferences("save_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefMember.edit();
        editor.putBoolean("isLoginSuccessful", isLoginSuccessful);
        editor.putString("userid", userid);
        editor.putString("username", username);
        editor.putString("mobile", usermobile);
        editor.putString("email", useremail);
        editor.putString("auth_token", authtoken);
        editor.commit();
    }
}
