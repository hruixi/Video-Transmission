package com.rui.mvvm_jetpack.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rui.mvvm_jetpack.R;
import com.rui.mvvm_jetpack.model.bean.RedditBean;

/**
 * @ClassName RedditPagedListAdapter
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/5/11 16:45
 */
public class RedditPagedListAdapter
        extends PagedListAdapter<RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean, RedditPagedListAdapter.Reddit_VH> {
    protected Context mContext;
    private int mLayoutId;

    public RedditPagedListAdapter(Context context) {
        super(DIFF_CALLBACK);

        mContext = context;
    }


    @NonNull
    @Override
    public Reddit_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reddit_post_item, parent, false);
        return new  Reddit_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Reddit_VH holder, int position) {
        holder.score.setText(getItem(position).getScore() + "");

        if (getItem(position).getTitle() != null) {
            holder.title.setText(getItem(position).getTitle());
        } else {
            holder.title.setText("loading...");
        }

        if (getItem(position).getAuthor() != null) {
            holder.subtitle.setText("Submitted by " + getItem(position).getAuthor());
        } else {
            holder.subtitle.setText("unknown");
        }

    }

    public static class Reddit_VH extends RecyclerView.ViewHolder {
        View mView;
        TextView score, title, subtitle;

        public Reddit_VH(View view) {
            super(view);

            mView = view;
            score = view.findViewById(R.id.score);
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
        }
    }

    private static DiffUtil.ItemCallback<RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean oldR, RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean newR) {
                    return true;
                }

                @Override
                public boolean areContentsTheSame(RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean oldR,
                                                  RedditBean.RedditDataBean.ChildrenBean.ChildrenDataBean newR) {
                    return oldR.getUrl().equals(newR.getUrl());
                }
            };

}
