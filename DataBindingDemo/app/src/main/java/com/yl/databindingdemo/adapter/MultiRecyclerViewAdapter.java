package com.yl.databindingdemo.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.yl.databindingdemo.R;
import com.yl.databindingdemo.bean.RecyclerViewItem;

import java.util.List;

/**
 * 多布局RecyclerView
 * Created by yangle on 2017/7/21.
 */

public class MultiRecyclerViewAdapter extends RecyclerView.Adapter<MultiRecyclerViewAdapter.BindingHolder> {

    private List<RecyclerViewItem> list;

    public MultiRecyclerViewAdapter(List<RecyclerViewItem> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 5) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public MultiRecyclerViewAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding;

        if (viewType == 0) {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.adapter_recycler_view_other, parent, false);
        } else {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.adapter_recycler_view, parent, false);
        }

        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(MultiRecyclerViewAdapter.BindingHolder holder, int position) {
        holder.getBinding().setVariable(BR.item, list.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }
    }
}
