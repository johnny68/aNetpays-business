package com.anetpays.sid.business.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anetpays.sid.business.R;

/**
 * Created by siddh on 19-02-2018.
 */

public class UserTransactions extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions_activity, container, false);
        getActivity().setTitle("Transactions");

        return view;
    }
}
