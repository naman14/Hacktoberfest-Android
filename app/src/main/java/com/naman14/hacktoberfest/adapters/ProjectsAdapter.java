package com.naman14.hacktoberfest.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by naman on 19/9/17.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    private List<Issue> array;
    private Context context;

    public ProjectsAdapter(Context context) {
        this.context = context;
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

        holder.tvIssueNumber.setText("#"+ issue.getNumber());
        holder.tvIssueTitle.setText(issue.getTitle());
        holder.tvIssueRepo.setText(getRepoFromUrl(issue.getRepository_url()));
        holder.tvIssueLanguage.setText(issue.getLanguage());

        if (issue.getLabels() != null && issue.getLabels().size() != 0) {
            for (Label label : issue.getLabels()) {
//                TextView textView = new TextView(context);
//                textView.setBackgroundResource(R.drawable.item_label_bg);

                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_label, null);
                GradientDrawable drawable = (GradientDrawable) textView.getBackground();

                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 0, 0, 0);

                drawable.setColor(Color.parseColor("#" + label.getColor()));
                textView.setText(label.getName());
                textView.setTextColor(Utils.getContrastColor(Color.parseColor("#" + label.getColor())));

                holder.llLabels.addView(textView, layoutParams);
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
        LinearLayout llLabels;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            tvIssueNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(array.get(getAdapterPosition()).getHtml_url()));
                    context.startActivity(intent);
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
        this.array.clear();
        notifyDataSetChanged();
    }

    public List<Issue> getData() {
        return array;
    }
}
