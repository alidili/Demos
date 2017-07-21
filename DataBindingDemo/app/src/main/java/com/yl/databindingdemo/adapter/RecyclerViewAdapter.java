package com.yl.databindingdemo.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.yl.databindingdemo.R;
import com.yl.databindingdemo.bean.RecyclerViewItem;
import com.yl.databindingdemo.databinding.AdapterRecyclerViewBinding;

import java.util.List;

/**
 * RecyclerView
 * Created by yangle on 2017/7/21.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BindingHolder> {

    private List<RecyclerViewItem> list;

    public RecyclerViewAdapter(List<RecyclerViewItem> list) {
        this.list = list;
    }

    @Override
    public RecyclerViewAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterRecyclerViewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.adapter_recycler_view, parent, false);

        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.BindingHolder holder, int position) {
        holder.getBinding().setVariable(BR.item, list.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private AdapterRecyclerViewBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
        }

        public AdapterRecyclerViewBinding getBinding() {
            return binding;
        }

        public void setBinding(AdapterRecyclerViewBinding binding) {
            this.binding = binding;
        }
    }
}
