package com.anetpays.sid.business.UI;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anetpays.sid.business.Constants.ConstantsApp;
import com.anetpays.sid.business.R;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


/**
 * Created by siddh on 19-02-2018.
 */

public class AcceptPayment extends Fragment
{
/*    private static final String cameraPerm = Manifest.permission.CAMERA;*/
    private TextView textView;
    private SurfaceView surfaceView;
    private QREader qrEader;
    private SharedPreferences preferences;

/*    boolean hasCameraPermission = false;*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_activity, container, false);
        getActivity().setTitle("Payment");

        textView = (TextView)view.findViewById(R.id.code_info);

        final Button stateBtn = (Button)view.findViewById(R.id.btn_start_stop);
        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getBoolean(ConstantsApp.hasCameraPer, false))

                {
                    if (qrEader.isCameraRunning())
                    {
                        stateBtn.setText("Start Payment");
                        qrEader.stop();
                    }
                    else
                        {
                            stateBtn.setText("Stop Scanning");
                            qrEader.start();
                        }
                }
            }
        });
        stateBtn.setVisibility(View.VISIBLE);
        Button restartbtn = (Button)view.findViewById(R.id.btn_restart_activity);
        restartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartActivity();
            }
        });

        surfaceView = (SurfaceView)view.findViewById(R.id.camera_view);
        setUpQReader();

        return view;
    }

    public void restartActivity()
    {
        Fragment CurrentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (CurrentFragment instanceof AcceptPayment)
        {
            FragmentTransaction fragmentTransaction = (getActivity()).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(CurrentFragment);
            fragmentTransaction.attach(CurrentFragment);
            fragmentTransaction.commit();
        }
    }
    void setUpQReader()
    {
        qrEader = new QREader.Builder(getActivity(), surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QR READER", "Value : " + data);
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(data);
                    }
                });
            }
        })
                .facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(surfaceView.getHeight())
                .width(surfaceView.getWidth())
                .build();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        qrEader.releaseAndCleanup();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        qrEader.initAndStart(surfaceView);

    }
}
