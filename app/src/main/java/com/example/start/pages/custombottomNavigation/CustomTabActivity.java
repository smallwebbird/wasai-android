package com.example.start.pages.custombottomNavigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.start.pages.home.HomeFragment;
import com.example.start.pages.mine.MineFragment;

import com.example.start.R;

public class CustomTabActivity extends AppCompatActivity implements CustomTabView.OnTabCheckListener {
    private CustomTabView mCustomTabView;
    private Fragment[] mFragmensts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_tab);
        mFragmensts = this.getFragments();
        initView();

    }

    private Fragment[] getFragments() {
        Fragment [] fragments = new Fragment[2];
        fragments[0] = new HomeFragment();
        fragments[1] = new MineFragment();
        return fragments;
    }

    private void initView() {
        mCustomTabView = (CustomTabView) findViewById(R.id.custom_tab_container);
        CustomTabView.Tab tabHome = new CustomTabView.Tab().setText("首页")
                .setColor(getResources().getColor(android.R.color.darker_gray))
                .setCheckedColor(getResources().getColor(R.color.tabCheckedColor))
                .setNormalIcon(R.drawable.tab_home_icon)
                .setPressedIcon(R.drawable.tab_home_checked_icon);
        mCustomTabView.addTab(tabHome);
        CustomTabView.Tab tabMine = new CustomTabView.Tab().setText("我的")
                .setColor(getResources().getColor(android.R.color.darker_gray))
                .setCheckedColor(getResources().getColor(R.color.tabCheckedColor))
                .setNormalIcon(R.drawable.tab_mine_icon)
                .setPressedIcon(R.drawable.tab_mine_checked_icon);
        mCustomTabView.addTab(tabMine);

        mCustomTabView.setOnTabCheckListener(this);

        mCustomTabView.setCurrentItem(0);

    }
    @Override
    public void onTabSelected(View v, int position) {
        Log.e("zhouwei","position:"+position);
        onTabItemSelected(position);
    }
    private void onTabItemSelected(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = mFragmensts[0];
                break;
            case 1:
                fragment = mFragmensts[1];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
        }
    }

}
