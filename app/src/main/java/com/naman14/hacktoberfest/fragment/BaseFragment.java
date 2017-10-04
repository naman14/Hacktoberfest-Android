package com.naman14.hacktoberfest.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.naman14.hacktoberfest.R;

/**
 * Created by naman on 4/10/17.
 */

public class BaseFragment extends Fragment {

    private Toolbar toolbar;

    public void setToolbar(View rootView, String title) {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).setTitle("");
            setToolbarTitle(title);
        }
    }

    protected void setToolbarTitle(String title) {
        if (toolbar != null) {
            TextView titleView = (TextView) toolbar.findViewById(R.id.toolbar_title);
            titleView.setText(title);
        }
    }
}
