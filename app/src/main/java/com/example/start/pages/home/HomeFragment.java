package com.example.start.pages.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.start.R;
import com.example.start.pages.customBanner.CustomBanner;
import com.example.start.pages.customHeaderAndContent.CustomHeaderAndContent;
import com.example.start.pages.mine.MineFragment;
import com.example.start.pages.test1.Test1Fragment;
import com.example.start.pages.test2.Test2Fragment;
import com.example.start.utils.Status;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private CustomHeaderAndContent<CustomHeaderAndContent.Tab, Fragment> mHeaderAndContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化设置第一个fragment要求的状态栏颜色
        List<CustomHeaderAndContent.Tab> tabs = this.getTabs();
        mHeaderAndContent = (CustomHeaderAndContent) view.findViewById(R.id.home_header);
        List<Fragment> fragments = this.getFragments();
        mHeaderAndContent.initialize(getActivity(), tabs, fragments, getChildFragmentManager(), new CustomHeaderAndContent.ViewCreatorTab<CustomHeaderAndContent.Tab>() {
            @Override
            public View createView(Context context, int position) {
                LinearLayout view = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(layoutParams);
//                view.setPadding(5,5,5,5);
                view.setOrientation(LinearLayout.VERTICAL);
                TextView textView = new TextView(context);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                view.addView(textView);
                return view;
            }

            @Override
            public void updateUI(Context context, View view, int position, CustomHeaderAndContent.Tab data) {
                if (view instanceof  ViewGroup) {
                    ViewGroup vp = (ViewGroup) view;
                    TextView textView = (TextView) vp.getChildAt(0);
                    textView.setText(data.name);
                }
            }
        });
        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


    private List getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Test1Fragment());
        fragments.add(new Test2Fragment());
        return fragments;
    }
    private List<CustomHeaderAndContent.Tab> getTabs() {
        ArrayList<CustomHeaderAndContent.Tab> tabs = new ArrayList<>();
        CustomHeaderAndContent.Tab tab1 = new CustomHeaderAndContent.Tab("生活", getResources().getColor(R.color.colorPrimary));
        CustomHeaderAndContent.Tab tab2 = new CustomHeaderAndContent.Tab("运动", getResources().getColor(R.color.yellow_500));
        tabs.add(tab1);
        tabs.add(tab2);
        return tabs;
    }
}