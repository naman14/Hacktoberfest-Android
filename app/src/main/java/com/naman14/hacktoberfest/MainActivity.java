package com.naman14.hacktoberfest;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.naman14.hacktoberfest.fragment.AboutFragment;
import com.naman14.hacktoberfest.fragment.ExploreFragment;
import com.naman14.hacktoberfest.fragment.StatusFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        replaceFragment(new StatusFragment());

        OnTabSelectListener onTabSelectListener = new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {
                    case R.id.tab_status:
                        replaceFragment(new StatusFragment());
                        break;
                    case R.id.tab_explore:
                        replaceFragment(new ExploreFragment());
                        break;
                    case R.id.tab_about:
                        replaceFragment(new AboutFragment());
                        break;
                }
            }
        };

        bottomBar.setOnTabSelectListener(onTabSelectListener);

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
