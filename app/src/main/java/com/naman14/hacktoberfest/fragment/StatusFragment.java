package com.naman14.hacktoberfest.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.naman14.hacktoberfest.MainActivity;
import com.naman14.hacktoberfest.R;
import com.naman14.hacktoberfest.adapters.PRAdapter;
import com.naman14.hacktoberfest.network.entity.Issue;
import com.naman14.hacktoberfest.network.repository.GithubRepository;
import com.naman14.hacktoberfest.utils.Utils;
import com.naman14.hacktoberfest.widgets.GridRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by naman on 4/10/17.
 */

public class StatusFragment extends Fragment {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.iv_user_image)
    ImageView ivUserImage;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.tv_placeholder)
    TextView tvPlaceholder;

    @BindView(R.id.tv_pr_count)
    TextView tvPrCount;

    @BindView(R.id.tv_status_message)
    TextView tvStatusMessage;

    @BindView(R.id.iv_check)
    ImageView ivCheck;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.statusView)
    View statusView;

    @BindView(R.id.recyclerView)
    GridRecyclerView recyclerView;

    private PRAdapter adapter;
    private SharedPreferences prefs;

    private static final String SHARED_PREFS = "hacktoberfest-android";
    private static final String USERNAME_KEY = "username";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        ButterKnife.bind(this, rootView);

        setupRecyclerview();

        prefs = getActivity()
                .getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String storedUsername = prefs.getString(USERNAME_KEY, "");
        if(!storedUsername.isEmpty()) {
            etUsername.setText(storedUsername);
        }

        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideKeyboard(getActivity());
                    checkPRStatus();
                    return true;
                }
                return false;
            }
        });


        return rootView;
    }


    @OnClick(R.id.iv_check)
    public void checkClicked() {
        checkPRStatus();
    }

    private void setupRecyclerview() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PRAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);
    }

    private void checkPRStatus() {
        tvPlaceholder.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        prefs
            .edit()
            .putString(USERNAME_KEY, etUsername.getText().toString())
            .apply();

        GithubRepository.getInstance().findValidPRs(etUsername.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Issue>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(scrollView, "Error fetching details", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(List<Issue> response) {
                        progressBar.setVisibility(View.GONE);
                        showStatus(response);

                    }
                });
    }

    private void showStatus(final List<Issue> response) {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.VISIBLE);

        if (response != null && response.size() != 0) {
            Picasso.with(getActivity()).load(response.get(0).getUser().getAvatar_url()).into(ivUserImage);
        } else {
            Picasso.with(getActivity()).load("https://github.com/" +
                    etUsername.getText().toString() + ".png").into(ivUserImage);
        }

        if (response != null) {
            tvPrCount.setText(String.valueOf(response.size()));
            tvStatusMessage.setText(Utils.getStatusMessage(response.size()));
            adapter.setData(response);
        } else {
            tvPrCount.setText("0");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollToView(scrollView, etUsername);

            }
        }, 350);

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).getBottomBar().getShySettings().hideBar();
            }
        }, 450);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setData(response);

            }
        }, 750);

    }


    private void scrollToView(final NestedScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y - 50);
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }
}
