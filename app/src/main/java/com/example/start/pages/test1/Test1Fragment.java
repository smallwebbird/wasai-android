package com.example.start.pages.test1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.start.R;
import com.example.start.pages.customBanner.CustomBanner;
import com.example.start.pages.customRefresh.Refresh;

import java.util.ArrayList;

public class Test1Fragment extends Fragment {
    private CustomBanner<String> mBanner;
    private Refresh refreshView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test1_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshView = (Refresh) view.findViewById(R.id.test1_fragment_refresh);
        mBanner = (CustomBanner) view.findViewById(R.id.test1_fragment_banner);
        ArrayList<String> images = new ArrayList<>();
        images.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3778456200,3076998411&fm=23&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3535338527,4000198595&fm=23&gp=0.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1017904219,2460650030&fm=23&gp=0.jpg");
        images.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3513269361,2662598514&fm=23&gp=0.jpg");
        mBanner.setPages(new CustomBanner.ViewCreator<String>() {

            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, images)
        .setDepthPageTransformer()
        .setOnPageClickListener(new CustomBanner.OnPageClickListener() {
            @Override
            public void onPageClick(int position, Object o) {
                Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
            }
        });
        // 匿名内部类
        refreshView.setOnRefreshListener(new Refresh.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshView.finishRefresh();
            }
        });
    }
}
