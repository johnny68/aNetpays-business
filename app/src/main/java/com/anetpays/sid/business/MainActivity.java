package com.anetpays.sid.business;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.anetpays.sid.business.Extras.CustomPinActivity;
import com.github.omadahealth.lollipin.lib.PinCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.anetpays.sid.business.Constants.ConstantsApp;
import com.anetpays.sid.business.Extras.CrossfadeWrapper;
import com.anetpays.sid.business.UI.AboutApp;
import com.anetpays.sid.business.UI.AcceptPayment;
import com.anetpays.sid.business.UI.UserHelp;
import com.anetpays.sid.business.UI.UserProfile;
import com.anetpays.sid.business.UI.UserSettings;
import com.anetpays.sid.business.UI.UserTransactions;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.crossfader.util.UIUtils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import static com.anetpays.sid.business.Constants.ConstantsApp.PREF_FIRST_RUN;
import static com.anetpays.sid.business.Constants.ConstantsApp.PREF_LOGIN;
import static com.anetpays.sid.business.Constants.ConstantsApp.PREF_MISC;

/**
 * Created by siddh on 22-01-2018.
 */

public class MainActivity extends PinCompatActivity
{
    private Drawer DrawerResult = null;
    private AccountHeader headerResult = null;
    private MiniDrawer miniDrawer = null;
    private Crossfader crossfader ;
    private SharedPreferences preferences, pref2 ;
    private static FragmentManager fragmentManager;
    public Fragment f;
    private static final int REQUEST_CODE_ENABLE = 11;
    private static final String cameraPerm = Manifest.permission.CAMERA;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fragmentManager = getSupportFragmentManager();
        preferences = getSharedPreferences(PREF_LOGIN, MODE_PRIVATE);
        pref2 = getSharedPreferences(PREF_MISC, MODE_PRIVATE);

        Boolean isFirstRun = getSharedPreferences(PREF_FIRST_RUN, MODE_PRIVATE).getBoolean(ConstantsApp.isFIRSTrun, true);
        if (isFirstRun)
        {
            Intent intent = new Intent(this, CustomPinActivity.class);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            startActivityForResult(intent, REQUEST_CODE_ENABLE);
            getSharedPreferences(PREF_FIRST_RUN, MODE_PRIVATE).edit().putBoolean(ConstantsApp.isFIRSTrun, false).apply();
        }

        if (pref2.getBoolean(ConstantsApp.hasCameraPer, false))
        {
            Log.d("CAMERA PERMISSION IT ", " HAS");
        } else
        {
            RuntimePermissionUtil.requestPermission(MainActivity.this, cameraPerm, 100);
        }

        String tempName = preferences.getString(ConstantsApp.NAME, "");
        String tempEmail = preferences.getString(ConstantsApp.EMAIL, "");
        String tempURL = preferences.getString(ConstantsApp.URLImage, "");
        Log.d(ConstantsApp.TAGMainActivityLogin, tempName);
        Log.d(ConstantsApp.TAGMainActivityLogin, tempEmail);
        Log.d(ConstantsApp.TAGMainActivityLogin, tempURL);



        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);



        final IProfile profile = new ProfileDrawerItem().withName(preferences.getString(ConstantsApp.NAME, "NAME"))
                .withEmail(preferences.getString(ConstantsApp.EMAIL, "EMAIL")).withIcon(tempURL);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withTranslucentStatusBar(false)
                .addProfiles(
                        profile
                )
                .withSelectionListEnabledForSingleProfile(false)
                .withSavedInstance(savedInstanceState)
                .build();

        DrawerResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Accept Payment").withIcon(GoogleMaterial.Icon.gmd_money_off).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Transactions").withIcon(GoogleMaterial.Icon.gmd_money_off).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Profile").withIcon(GoogleMaterial.Icon.gmd_person).withIdentifier(3),
                        new SectionDrawerItem().withName("Extras"),
                        new SecondaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(4),
                        new SecondaryDrawerItem().withName("Help").withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(5),
                        new SecondaryDrawerItem().withName("About").withIcon(GoogleMaterial.Icon.gmd_cached).withIdentifier(6)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable)
                        {
                            if (drawerItem.getIdentifier() == 1)
                            {
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.crossfade_content, new AcceptPayment())
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                            }
                            else if (drawerItem.getIdentifier() == 2)
                            {
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.crossfade_content, new UserTransactions())
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                            }

                            else if (drawerItem.getIdentifier() == 3)
                            {
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.crossfade_content, new UserProfile())
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                            }

                            else if (drawerItem.getIdentifier() == 4)
                            {
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.crossfade_content, new UserSettings())
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                            }
                            else if (drawerItem.getIdentifier() == 5)
                            {
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.crossfade_content, new UserHelp())
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                            }
                            else if(drawerItem.getIdentifier() == 6)
                            {
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.crossfade_content, new AboutApp())
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                            }
                            else
                            {
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.frame_container, new AcceptPayment())
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss();
                            }
                        }
                        return false;
                    }
                })
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                .buildView();
        miniDrawer =  DrawerResult.getMiniDrawer();

        int firstWidth = (int) UIUtils.convertDpToPixel(300, this);
        int SecondWidth = (int) UIUtils.convertDpToPixel(72, this);

        crossfader = new Crossfader()
                .withContent(findViewById(R.id.crossfade_content))
                .withFirst(DrawerResult.getSlider(),firstWidth)
                .withSecond(miniDrawer.build(this), SecondWidth)
                .withSavedInstance(savedInstanceState)
                .build();

        miniDrawer.withCrossFader(new CrossfadeWrapper(crossfader));
        crossfader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState = DrawerResult.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed()
    {
        if (DrawerResult != null && DrawerResult.isDrawerOpen())
        {
            DrawerResult.closeDrawer();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_ENABLE:
                Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted() {
                    if (RuntimePermissionUtil.checkPermissonGranted(MainActivity.this, cameraPerm)) {
                        SharedPreferences.Editor editor = getSharedPreferences(PREF_MISC, MODE_PRIVATE).edit();
                        editor.putBoolean(ConstantsApp.hasCameraPer, true);
                        editor.apply();
                    }
                }

                @Override
                public void onPermissionDenied() {
                    // do nothing
                }
            });
        }
    }

}
