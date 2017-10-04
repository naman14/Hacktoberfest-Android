package com.naman14.hacktoberfest.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naman14.hacktoberfest.R;
import com.naman14.hacktoberfest.network.entity.Issue;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by naman on 19/9/17.
 */

public class PRAdapter extends RecyclerView.Adapter<PRAdapter.ViewHolder> {

    private List<Issue> array;
    private Context context;

    public PRAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PRAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_status_pr, parent, false);
        return new PRAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PRAdapter.ViewHolder holder, int position) {

        Issue issue = array.get(position);

        holder.tvPrNumber.setText("#"+ issue.getNumber());
        holder.tvPrTitle.setText(issue.getTitle());
        holder.tvPrRepo.setText(getRepoFromUrl(issue.getRepository_url()));

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

        @BindView(R.id.tv_pr_number)
        TextView tvPrNumber;

        @BindView(R.id.tv_pr_title)
        TextView tvPrTitle;

        @BindView(R.id.tv_pr_repo)
        TextView tvPrRepo;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            tvPrNumber.setOnClickListener(new View.OnClickListener() {
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

