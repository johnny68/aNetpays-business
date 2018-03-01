package com.anetpays.sid.business.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anetpays.sid.business.Constants.*;
import com.anetpays.sid.business.Constants.models.*;
import com.anetpays.sid.business.Login.Extras.*;
import com.anetpays.sid.business.Login.Extras.Facebook.FacebookHelper;
import com.anetpays.sid.business.Login.Extras.Facebook.FacebookResponse;
import com.anetpays.sid.business.Login.Extras.Facebook.FacebookUser;
import com.anetpays.sid.business.Login.Extras.Google.GoogleAuthResponse;
import com.anetpays.sid.business.Login.Extras.Google.GoogleAuthUser;
import com.anetpays.sid.business.Login.Extras.Google.GooglePlusSignInHelper;
import com.anetpays.sid.business.Login.Extras.Google.GoogleResponseListener;
import com.anetpays.sid.business.Login.Extras.Google.GoogleSignInHelper;
import com.anetpays.sid.business.MainActivity;
import com.anetpays.sid.business.R;
import com.google.android.gms.plus.model.people.Person;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.anetpays.sid.business.Constants.ConstantsApp.PREF_LOGIN;

/**
 * Created by siddh on 18-02-2018.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, FacebookResponse, GoogleAuthResponse, GoogleResponseListener
{
    private static View view;

    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static RelativeLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    private static ProgressDialog progress;
    private static SharedPreferences preferences;
    private static FancyButton gpButton, fbButton;
    private String email, photo, name;


    private FacebookHelper mFbHelper;
    private GoogleSignInHelper mGAuthHelper;
    private GooglePlusSignInHelper mGHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.login_layout, null, false);
        initViews();
        setListeners();

        mGHelper = new GooglePlusSignInHelper(getActivity(), this);
        mGAuthHelper = new GoogleSignInHelper(getActivity(), null, this);
        mFbHelper = new FacebookHelper(this,"id,name,email,gender,birthday,picture,cover", getActivity());

        return view;
    }


    private void initViews()
    {

        fragmentManager = this.getFragmentManager();
        emailid = (EditText)view.findViewById(R.id.login_emailid);
        password = (EditText)view.findViewById(R.id.login_password);
        loginButton = (Button)view.findViewById(R.id.loginBtn);
        forgotPassword = (TextView)view.findViewById(R.id.forgot_password);
        signUp = (TextView) view.findViewById(R.id.createAccount);
        show_hide_password = (CheckBox)view.findViewById(R.id.show_hide_password);
        loginLayout = (RelativeLayout) view.findViewById(R.id.login_layout);
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
        gpButton = (FancyButton) view.findViewById(R.id.btn_gplus);
        fbButton = (FancyButton) view.findViewById(R.id.btn_facebook);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try
        {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),xrp);
            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            show_hide_password.setTextColor(csl);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void setListeners()
    {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        gpButton.setOnClickListener(this);
        fbButton.setOnClickListener(this);
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                {
                    show_hide_password.setText(R.string.hide_pwd);
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                    {
                    show_hide_password.setText(R.string.show_pwd);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

            }
        });
    }

    @Override
    public void onClick (View v)
    {
        switch (v.getId())
        {
            case R.id.loginBtn:
                                        CheckValidation();
                                        break;
            case R.id.forgot_password:
                                        fragmentManager
                                                .beginTransaction()
                                                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                                                .replace(R.id.frame,
                                                        new ForgotPassword(),
                                                        Utils.Forgot_Password).commit();
                                        break;
            case R.id.createAccount:
                                        fragmentManager
                                                .beginTransaction()
                                                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                                                .replace(R.id.frame, new SignUpFragment(),
                                                        Utils.SignUp_Fragment).commit();
                                        break;
            case R.id.btn_facebook:
                                        mFbHelper.performSignIn(this);
                                        break;

            case R.id.btn_gplus:
                                        mGHelper.performSignIn();
                                        break;

        }
    }

    private void CheckValidation()
    {
        String emailID = emailid.getText().toString();
        String Password = password.getText().toString();

        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(emailID);

        if (emailID.equals("") || emailID.length() == 0 || Password.equals("") || Password.length() == 0)
        {
            loginLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view, "Enter Both Credentials");
        }
        else if (!m.find())
        {
            new CustomToast().Show_Toast(getActivity(), view, "EMAIL ID is invalid");
        }
        else
        {
            progress.setMessage("Logging you in ......");
            showDialog();
            LoginProcess(emailID, Password);
        }

    }

    private void LoginProcess(String email, String pwd)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsApp.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        User user = new User();
        user.setEmail(email);
        user.setPassword(pwd);
        ServerRequest request = new ServerRequest();
        request.setOperation(ConstantsApp.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                if (resp.getResult().equals(ConstantsApp.SUCCESS))
                {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREF_LOGIN, MODE_PRIVATE).edit();
                    editor.putBoolean(ConstantsApp.IS_LOGGED_IN, true);
                    editor.putString(ConstantsApp.EMAIL, resp.getUser().getEmail());
                    editor.putString(ConstantsApp.NAME, resp.getUser().getName());
                    editor.putString(ConstantsApp.UNIQUE_ID, resp.getUser().getUnique_id());
                    editor.apply();
                    goToProfile();
                }
                hideDialog();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                hideDialog();
                Log.d(ConstantsApp.TAGMainActivityLogin, "Failed at logging in " + t.getLocalizedMessage());
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG);


            }
        });
    }

    private void showDialog()
    {
        if (!progress.isShowing())
            progress.show();
    }

    private void hideDialog()
    {
        if (progress.isShowing())
            progress.hide();
    }

    private void goToProfile()
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);


        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGHelper.disconnectApiClient();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        //handle permissions
        mGHelper.onPermissionResult(requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle results
        mFbHelper.onActivityResult(requestCode, resultCode, data);
        mGHelper.onActivityResult(requestCode, resultCode, data);
        mGAuthHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFbSignInFail() {
        Toast.makeText(getActivity(), "Facebook sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbSignInSuccess() {
        Toast.makeText(getActivity(), "Facebook sign in success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {
        Toast.makeText(getActivity(), "Facebook user data: name= " + facebookUser.name + " email= " + facebookUser.email, Toast.LENGTH_SHORT).show();

        //finish();
        Log.d("Person name: ", facebookUser.name + "");
        Log.d("Person gender: ", facebookUser.gender + "");
        Log.d("Person email: ", facebookUser.email + "");
        Log.d("Person image: ", facebookUser.facebookID + "");

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREF_LOGIN, MODE_PRIVATE).edit();
        editor.putBoolean(ConstantsApp.IS_LOGGED_IN, true);
        editor.putString(ConstantsApp.NAME, facebookUser.name);
        editor.putString(ConstantsApp.EMAIL, facebookUser.email);
        editor.putString(ConstantsApp.URLImage,facebookUser.profilePic);
        Log.d("URL" , facebookUser.profilePic + "");
        editor.apply();

        goToProfile();
    }

    @Override
    public void onFBSignOut() {
        Toast.makeText(getActivity(), "Facebook sign out success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGSignInFail() {
        Toast.makeText(getActivity(), "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGSignInSuccess(Person person) {
        Toast.makeText(getActivity(), "Google sign in success", Toast.LENGTH_SHORT).show();

        Log.d("Person display name: ", person.getDisplayName() + "");
        Log.d("Person birth date: ", person.getBirthday() + "");
        Log.d("Person gender: ", person.getGender() + "");
        Log.d("Person name: ", person.getName() + "");
        Log.d("Person id: ", person.getImage() + "");


        String URL = person.getImage().getUrl();
        Log.d("URL", URL);

            SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREF_LOGIN, MODE_PRIVATE).edit();
            editor.putBoolean(ConstantsApp.IS_LOGGED_IN, true);
            editor.putString(ConstantsApp.NAME, person.getDisplayName());
            editor.putString(ConstantsApp.EMAIL, person.getAboutMe());
            editor.putString(ConstantsApp.URLImage, URL);
        Log.d("Person id: ", ConstantsApp.URLImage + "");
            editor.apply();
            goToProfile();
        }
        //finish();

    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        Toast.makeText(getActivity(), "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(getActivity(), "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignOut(boolean isSuccess) {
        Toast.makeText(getActivity(), isSuccess ? "Sign out success" : "Sign out failed", Toast.LENGTH_SHORT).show();
    }

}
