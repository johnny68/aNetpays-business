package com.anetpays.sid.business.Extras;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.anetpays.sid.business.R;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;

import uk.me.lewisdeane.ldialogs.BaseDialog;
import uk.me.lewisdeane.ldialogs.CustomDialog;

/**
 * Created by siddh on 28-02-2018.
 */

public class CustomPinActivity extends AppLockActivity
{
    @Override
    public void showForgotDialog()
    {
        Resources resources = getResources();
        CustomDialog.Builder builder = new CustomDialog.Builder(this, resources.getString(R.string.activity_dialog_title), resources.getString(R.string.activity_dialog_accept));
        builder.content(resources.getString(R.string.activity_dialog_content));
        builder.negativeText(resources.getString(R.string.activity_dialog_decline));

        builder.darkTheme(false);
        builder.typeface(Typeface.SANS_SERIF);
        builder.positiveColor(resources.getColor(R.color.light_blue_500));
        builder.negativeColor(resources.getColor(R.color.light_blue_500));
        builder.rightToLeft(false);
        builder.titleAlignment(BaseDialog.Alignment.CENTER);
        builder.buttonAlignment(BaseDialog.Alignment.CENTER);

        builder.titleTextSize((int) resources.getDimension(R.dimen.activity_dialog_title_size));
        builder.contentTextSize((int) resources.getDimension(R.dimen.activity_dialog_content_size));
        builder.positiveButtonTextSize((int) resources.getDimension(R.dimen.activity_dialog_positive_button_size));
        builder.negativeButtonTextSize((int) resources.getDimension(R.dimen.activity_dialog_negative_button_size));

        //Build the dialog.
        CustomDialog customDialog = builder.build();
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setClickListener(new CustomDialog.ClickListener() {
            @Override
            public void onConfirmClick() {
                Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelClick() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog.
        customDialog.show();
    }

    @Override
    public void onPinFailure(int attempts) {

    }

    @Override
    public void onPinSuccess(int attempts) {

    }

    @Override
    public int getPinLength() {
        return super.getPinLength();
    }
}
