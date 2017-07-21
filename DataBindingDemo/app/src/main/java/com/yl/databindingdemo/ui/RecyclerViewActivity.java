package com.yl.databindingdemo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.adapter.RecyclerViewAdapter;
import com.yl.databindingdemo.bean.RecyclerViewItem;
import com.yl.databindingdemo.databinding.ActivityRecyclerViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView
 * Created by yangle on 2017/7/21.
 */

public class RecyclerViewActivity extends AppCompatActivity {

    private List<RecyclerViewItem> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        ActivityRecyclerViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(list);
        binding.recyclerView.setAdapter(recyclerViewAdapter);

        // 更新
        updateData();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new RecyclerViewItem(String.valueOf(i)));
        }
    }

    private void updateData() {
        for (int i = 10; i < 20; i++) {
            list.add(new RecyclerViewItem(String.valueOf(i)));
        }
    }
}
