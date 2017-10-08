package com.naman14.hacktoberfest.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naman14.hacktoberfest.R;
import com.naman14.hacktoberfest.utils.Utils;
import com.naman14.hacktoberfest.network.entity.Issue;
import com.naman14.hacktoberfest.network.entity.Label;
import com.naman14.hacktoberfest.widgets.FlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by naman on 19/9/17.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    private List<Issue> array;
    private Context context;

    private FlowLayout.LayoutParams params;

    public ProjectsAdapter(Context context) {
        this.context = context;
        this.params =  new FlowLayout.LayoutParams(25, 30);
    }

    @Override
    public ProjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_project, parent, false);
        return new ProjectsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectsAdapter.ViewHolder holder, int position) {

        Issue issue = array.get(position);

        holder.tvIssueNumber.setText("#" + issue.getNumber());
        holder.tvIssueTitle.setText(issue.getTitle());
        holder.tvIssueRepo.setText(getRepoFromUrl(issue.getRepository_url()));

        if (Utils.getLanguagePreference(context).equals("All")) {
            holder.tvIssueLanguage.setVisibility(View.GONE);
        } else {
            holder.tvIssueLanguage.setText(Utils.getLanguagePreference(context));
        }

        if (issue.getLabels() != null && issue.getLabels().size() != 0) {
            for (Label label : issue.getLabels()) {

                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_label, null);

                GradientDrawable drawable = (GradientDrawable) textView.getBackground();
                drawable.setColor(Color.parseColor("#" + label.getColor()));

                textView.setText(label.getName());
                textView.setTextColor(Utils.getContrastColor(Color.parseColor("#" + label.getColor())));

                holder.llLabels.addView(textView, params);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (array != null) {
            return array.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_issue_number)
        TextView tvIssueNumber;

        @BindView(R.id.tv_issue_title)
        TextView tvIssueTitle;

        @BindView(R.id.tv_issue_repo)
        TextView tvIssueRepo;

        @BindView(R.id.tv_issue_language)
        TextView tvIssueLanguage;

        @BindView(R.id.ll_labels)
        FlowLayout llLabels;


        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.openChromeCustomTab(context, array.get(getAdapterPosition()).getHtml_url());
                }
            });
        }
    }

    private String getRepoFromUrl(String url) {
        return url.replace("https://api.github.com/repos/", "");
    }

    public void setData(List<Issue> data) {
        this.array = data;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (array != null) {
            this.array.clear();
            notifyDataSetChanged();
        }
    }

    public List<Issue> getData() {
        return array;
    }

}

