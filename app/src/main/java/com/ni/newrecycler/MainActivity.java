package com.ni.newrecycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ni.newrecycler.adapter.MyAdapter;
import com.ni.newrecycler.bean.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 *   首页
 */
public class MainActivity extends AppCompatActivity implements MyAdapter.OnChildClickListener {

    private RecyclerView recycler;
    private ImageView imageView;
    private MyAdapter adapter;
    int  flag = 0;
    private List<Bean.ApkBeanHeadline> apk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
        getData();
    }
    /**
     *  获取id
     */
    private void intiView(){
        recycler = (RecyclerView) findViewById(R.id.recycler);
        imageView = (ImageView) findViewById(R.id.imageView);
        //监听切换事件

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0){
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
                    recycler.setLayoutManager(gridLayoutManager);
                    //添加下划线
                    MyDecoration decoration = new MyDecoration(MainActivity.this, LinearLayoutManager.VERTICAL);
                    recycler.addItemDecoration(decoration);
                    adapter.notifyDataSetChanged();
                    flag = 1;
                }else if(flag == 1){
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
                    recycler.setLayoutManager(manager);
                    //添加下划线
                    MyDecoration decoration = new MyDecoration(MainActivity.this, LinearLayoutManager.HORIZONTAL);
                    recycler.addItemDecoration(decoration);
                    adapter.notifyDataSetChanged();
                    flag = 0;
                }

            }
        });
    }
    /**
     *  获取资源  网络请求
     */
    private void getData(){
        try {
            InputStream open = getAssets().open("data.json");
            //读取资源
            Gson gson = new Gson();

            Bean bean = gson.fromJson(new InputStreamReader(open), Bean.class);

            apk = bean.getApk();
            Log.i("TAG", "getData:++++ "+ apk.size());
            //进行适配
            adapter = new MyAdapter(this, apk);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
            recycler.setLayoutManager(manager);
            //设置下划线
            MyDecoration decoration = new MyDecoration(this, LinearLayoutManager.VERTICAL);
            recycler.addItemDecoration(decoration);
            recycler.setAdapter(adapter);
            adapter.setOnChildClickListener(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //点击
    @Override
    public void onChildClick(int position) {
        //弹出popupwindow
        setPopu(apk.get(position).getName());
    }

    /**
     *    popupwindow 生成
     */
    private void setPopu(String name){
        View loginPopView = View.inflate(this, R.layout.popu_view, null);
        TextView name_popu = (TextView) loginPopView.findViewById(R.id.name_popu);
        name_popu.setText(name);
        //构建登录按钮弹出的PopupWindow实例
        final PopupWindow loginPopWindow = new PopupWindow(loginPopView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //指定显示在某个参照物View的左下方,可以指定向右，向下偏移量
        //设置背景色透明
        //loginPopWindow.showAsDropDown(recycler, 200, 200);
        loginPopWindow.showAtLocation(recycler, Gravity.CENTER,100,100);
    }
  //长按
    @Override
    public void onChildLongClick(int position) {
        adapter.remove(position);
    }
}
