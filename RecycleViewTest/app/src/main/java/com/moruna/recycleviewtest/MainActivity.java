package com.moruna.recycleviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        ViewAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        viewAdapter = new ViewAdapter(this, list);
        //设置滑动的方向,默认VERTICAL,支持横向、纵向
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //网格布局
        //recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        //瀑布就式布
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
        //StaggeredGridLayoutManager.VERTICAL));
        //设置动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));
        //使用StaggeredGridLayoutManager需设置，不然没有竖直方向的分割线
        //recyclerView.addItemDecoration(new DividerItemDecoration(
        // this,DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(viewAdapter);
        viewAdapter.setOnItemClickListener(this);
        //设置动画
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        for (int i = 0; i < 30; i++) {
            list.add("Test RecyclerView");
        }
    }

    public void add(View view) {
        list.add(2, "Insert Data");
        //只更新该item部分
        viewAdapter.notifyItemInserted(2);
    }

    public void remove(View view) {
        list.remove(2);
        //只更新该item部分
        viewAdapter.notifyItemRemoved(2);
    }

    @Override
    public void onItemClick(View v, int position) {
        Log.e(TAG, "onItemClick: position=" + position);
    }
}
