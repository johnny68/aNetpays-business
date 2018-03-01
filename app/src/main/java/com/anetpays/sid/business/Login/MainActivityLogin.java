package com.anetpays.sid.business.Login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.anetpays.sid.business.Constants.ConstantsApp;
import com.anetpays.sid.business.Login.Extras.Utils;
import com.anetpays.sid.business.MainActivity;
import com.anetpays.sid.business.R;


import static com.anetpays.sid.business.Constants.ConstantsApp.PREF_LOGIN;


/**
 * Created by siddh on 22-01-2018.
 */

public class MainActivityLogin extends AppCompatActivity
{
    private static FragmentManager fragmentManager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity_login);
        fragmentManager = this.getSupportFragmentManager();
        preferences = getSharedPreferences(PREF_LOGIN, MODE_PRIVATE);


        if (preferences.getBoolean(ConstantsApp.IS_LOGGED_IN, false))
        {
            goToProfile();
        }
        else
        {
            if (savedInstanceState == null)
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frame, new LoginFragment(),
                            Utils.Login_Fragment).commit();
        }

        findViewById(R.id.close_activity).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );
    }

    public void replaceFragment()
    {

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frame, new LoginFragment(),
                        Utils.Login_Fragment).commit();

    }

    @Override
    public void onBackPressed()
    {
        Fragment ForgotPassword = fragmentManager.findFragmentByTag(Utils.Forgot_Password);
        Fragment SignupFragment = fragmentManager.findFragmentByTag(Utils.SignUp_Fragment);

        if (ForgotPassword != null)
        {
            replaceFragment();
        }
        else if (SignupFragment != null)
        {
            replaceFragment();
        }
        else
            super.onBackPressed();
    }

    public void goToProfile()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
