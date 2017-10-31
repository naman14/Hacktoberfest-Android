package com.naman14.hacktoberfest.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.naman14.hacktoberfest.MainActivity;
import com.naman14.hacktoberfest.R;
import com.naman14.hacktoberfest.adapters.ProjectsAdapter;
import com.naman14.hacktoberfest.network.entity.Issue;
import com.naman14.hacktoberfest.network.repository.GithubRepository;
import com.naman14.hacktoberfest.utils.AnimUtils;
import com.naman14.hacktoberfest.utils.FabAnimationUtils;
import com.naman14.hacktoberfest.utils.Utils;

import java.util.Arrays;
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

    @BindView(R.id.tv_language)
    TextView tvLanguage;

    @BindView(R.id.tv_tags)
    TextView tvTags;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ProjectsAdapter adapter;
    private int page = 1;
    private Boolean loading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        ButterKnife.bind(this, rootView);

        setupFilter();
        setupRecyclerview();

        setupSwipeRefreshLayout();

        fetchIssues();
        return rootView;
    }

    private void setupSwipeRefreshLayout() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                fetchIssues();
            }
        });
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

        tvLanguage.setText(Utils.getLanguagePreference(getActivity()));
        tvTags.setText(Utils.getTagsPreferenceString(getActivity()));

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    if (fab.getVisibility() == View.VISIBLE) {
                        FabAnimationUtils.scaleOut(fab);
                    }
                }
                if (scrollY < oldScrollY) {
                    if (fab.getVisibility() != View.VISIBLE) {
                        fab.setVisibility(View.VISIBLE);
                        FabAnimationUtils.scaleIn(fab);
                    }
                }
                if(view.getChildAt(view.getChildCount() - 1) != null) {
                    if ((scrollY >= (view.getChildAt(view.getChildCount() - 1).getMeasuredHeight() - view.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        if (!loading) {
                            page++;
                            fetchIssues();
                        }
                    }
                }

            }
        });

    }

    @OnClick(R.id.tv_language)
    public void showLanguageDialog() {
        final String[] array = Utils.getLanguagesArray();
        new MaterialDialog.Builder(getActivity())
                .title("Select language")
                .items(Utils.getLanguagesArray())
                .itemsCallbackSingleChoice(Arrays.asList(array).indexOf(Utils.getLanguagePreference(getActivity())), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tvLanguage.setText(array[which]);
                        Utils.setLanguagePreference(getActivity(), array[which]);
                        return true;
                    }
                })
                .show();
    }

    @OnClick(R.id.tv_tags)
    public void showTagsDialog() {
        final List<String> tagsList = Arrays.asList(Utils.getTagsArray());
        String[] tagsPreference = Utils.getTagsPreference(getActivity());
        Integer[] tagsPreferenceIndex = {};

        if (tagsPreference != null) {
            tagsPreferenceIndex = new Integer[tagsPreference.length];

            int index = 0;
            for (String tagPreference: tagsPreference) {
                tagsPreferenceIndex[index++] = tagsList.indexOf(tagPreference);
            }
        }

        new MaterialDialog.Builder(getActivity())
                .title("Select Tags")
                .items(Utils.getTagsArray())
                .itemsCallbackMultiChoice(tagsPreferenceIndex, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            String[] selectedTags = new String[text.length];
                            int index = 0;

                            for (CharSequence charSequence: text) {
                                selectedTags[index++] = charSequence.toString();
                            }

                            Utils.setTagsPreference(getActivity(), selectedTags);
                            tvTags.setText(Utils.getTagsPreferenceString(getActivity()));
                            return true;
                        };
                    }
                ).positiveText("Select Tags")
                .show();
    }

    private void setupRecyclerview() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProjectsAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

    }

    private void fetchIssues() {
        loading = true;
        swipeRefreshLayout.setEnabled(false);
        if(page == 1) {
            progressBar.setVisibility(View.VISIBLE);
            adapter.clearData();
        }
        else
            swipeRefreshLayout.setRefreshing(true);
        String language = Utils.getLanguagePreference(getActivity());

        String[] tags = Utils.getTagsPreference(getActivity());
        GithubRepository.getInstance().findIssues(language, tags, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Issue>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setEnabled(true);
                        loading = false;
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Error fetching projects", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(List<Issue> response) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setEnabled(true);
                        loading = false;
                        if(page == 1)
                            adapter.setData(response);
                        else
                            adapter.addData(response);
                    }
                });
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
            page=1;
            fetchIssues();
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
