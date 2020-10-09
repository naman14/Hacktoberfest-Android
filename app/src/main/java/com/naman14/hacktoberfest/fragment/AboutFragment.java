package com.naman14.hacktoberfest.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naman14.hacktoberfest.BuildConfig;
import com.naman14.hacktoberfest.R;
import com.naman14.hacktoberfest.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by naman on 4/10/17.
 */

public class AboutFragment extends Fragment {

    @BindView(R.id.tv_website)
    TextView tvWebsite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ButterKnife.bind(this, rootView);

        tvWebsite.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        return rootView;
    }

    @OnClick(R.id.tv_website)
    public void websiteClicked() {
        Utils.openChromeCustomTab(getActivity(), "https://hacktoberfest.digitalocean.com");
    }

    @OnClick(R.id.tv_view_github)
    public void viewGithub() {
        Utils.openChromeCustomTab(getActivity(), "https://github.com/naman14/Hacktoberfest-Android");
    }

    @OnClick(R.id.tv_sign_up)
    public void signUp() {
        Utils.openChromeCustomTab(getActivity(), "https://hacktoberfest.digitalocean.com/sign_up/register");
    }

    @OnClick(R.id.cv_rate_us)
    public void rateUs(){
        Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        }
    }
}
