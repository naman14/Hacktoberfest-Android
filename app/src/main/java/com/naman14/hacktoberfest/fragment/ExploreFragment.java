package com.naman14.hacktoberfest.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.naman14.hacktoberfest.MainActivity;
import com.naman14.hacktoberfest.R;
import com.naman14.hacktoberfest.adapters.PRAdapter;
import com.naman14.hacktoberfest.adapters.ProjectsAdapter;
import com.naman14.hacktoberfest.network.entity.Issue;
import com.naman14.hacktoberfest.network.entity.Label;
import com.naman14.hacktoberfest.utils.AnimUtils;
import com.naman14.hacktoberfest.utils.FabAnimationUtils;
import com.naman14.hacktoberfest.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by naman on 4/10/17.
 */

public class ExploreFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.confirm_save_container)
    ViewGroup confirmSaveContainer;

    @BindView(R.id.results_scrim)
    View resultsScrim;

    @BindView(R.id.save_confirmed)
    Button saveConfirmed;

    private ProjectsAdapter adapter;
    private boolean fabVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        ButterKnife.bind(this, rootView);

        setupFilter();
        setupRecyclerview();


        List<Issue> issues = new ArrayList<>();
        for (int i=0; i<6; i++) {
            Issue issue = new Issue();
            issue.setNumber(319);
            issue.setTitle("Error plotting data in graph");
            issue.setHtml_url("https://github.com/openMF/community-app/pull/19");
            issue.setRepository_url("https://api.github.com/repos/openMF/community-app");

            List<Label> labelList = new ArrayList<>();

            Label label = new Label();
            label.setColor("009688");
            label.setName("Hactoberfest");
            labelList.add(label);

            Label label1 = new Label();
            label1.setColor("ededed");
            label1.setName("Beginner");
            labelList.add(label1);

            issue.setLabels(labelList);
            issues.add(issue);
        }

        adapter.setData(issues);

        return rootView;
    }


    private void setupFilter() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        resultsScrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        saveConfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndhide();
            }
        });
    }


    private void setupRecyclerview() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProjectsAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

    }


    private void show() {
        FabAnimationUtils.scaleOut(fab);
        fab.setVisibility(View.INVISIBLE);
        confirmSaveContainer.setVisibility(View.VISIBLE);
        resultsScrim.setVisibility(View.VISIBLE);


        confirmSaveContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver
                .OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                confirmSaveContainer.getViewTreeObserver().removeOnPreDrawListener(this);

                Animator reveal = ViewAnimationUtils.createCircularReveal(confirmSaveContainer,
                        confirmSaveContainer.getWidth() / 2,
                        confirmSaveContainer.getHeight() / 2,
                        fab.getWidth() / 2,
                        confirmSaveContainer.getWidth() / 2);
                reveal.setDuration(250L);
                reveal.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getActivity()));
                reveal.start();

                int centerX = (fab.getLeft() + fab.getRight()) / 2;
                int centerY = (fab.getTop() + fab.getBottom()) / 2;
                Animator revealScrim = ViewAnimationUtils.createCircularReveal(
                        resultsScrim,
                        centerX,
                        centerY,
                        0,
                        (float) Math.hypot(centerX, centerY));
                revealScrim.setDuration(400L);
                revealScrim.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getActivity()
                ));
                revealScrim.start();
                ObjectAnimator fadeInScrim = ObjectAnimator.ofArgb(resultsScrim,
                        Utils.BACKGROUND_COLOR,
                        Color.TRANSPARENT,
                        ContextCompat.getColor(getActivity(), R.color.scrim));
                fadeInScrim.setDuration(800L);
                fadeInScrim.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getActivity()
                ));
                fadeInScrim.start();

                return false;
            }
        });

        ((MainActivity)getActivity()).getBottomBar().getShySettings().hideBar();

    }

    private void hide() {
        if (confirmSaveContainer.getVisibility() == View.VISIBLE) {
            hideFilterContainer();
        }
    }

    private void saveAndhide() {
        if (confirmSaveContainer.getVisibility() == View.VISIBLE) {
            //do things
            hideFilterContainer();

        }
    }

    private void hideFilterContainer() {

        AnimatorSet hideConfirmation = new AnimatorSet();
        hideConfirmation.playTogether(
                ViewAnimationUtils.createCircularReveal(confirmSaveContainer,
                        confirmSaveContainer.getWidth() / 2,
                        confirmSaveContainer.getHeight() / 2,
                        confirmSaveContainer.getWidth() / 2,
                        fab.getWidth() / 2),
                ObjectAnimator.ofArgb(resultsScrim,
                        Utils.BACKGROUND_COLOR,
                        Color.TRANSPARENT));
        hideConfirmation.setDuration(150L);
        hideConfirmation.setInterpolator(AnimUtils.getFastOutSlowInInterpolator
                (getActivity()));
        hideConfirmation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                confirmSaveContainer.setVisibility(View.GONE);
                resultsScrim.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                FabAnimationUtils.scaleIn(fab);
            }
        });
        hideConfirmation.start();

        ((MainActivity)getActivity()).getBottomBar().getShySettings().showBar();


    }

}
