package com.example.start.pages.customHeaderAndContent;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.start.R;
import com.example.start.pages.customBanner.CustomBanner;
import com.example.start.utils.DisplayUtil;
import com.jaeger.library.StatusBarUtil;

import org.w3c.dom.Text;

import java.util.List;

public class CustomHeaderAndContent<T, Y> extends LinearLayout {
    private LinearLayout headerLayout;
    private ViewPager viewPager;
    private Context context;
    private CustomFragmentPagerAdapter<Fragment> customFragmentPagerAdapter;
    private SparseArray<View> views = new SparseArray<>();
    private LinearLayout indicatorLayout;
    private DisplayUtil displayUtil = new DisplayUtil();
    private DisplayUtil.ScreenInfo screenInfo;
    private List<T> tabs;
    private int tabWidth = 200;
    private Activity activity;

    public CustomHeaderAndContent(Context context) {
        super(context);
        init(context);
    }

    public CustomHeaderAndContent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomHeaderAndContent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomHeaderAndContent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context) {
        this.context = context;
        screenInfo = displayUtil.getRealScreenRelatedInformation(context);
        initHeader(context);
        initContent(context);
    }

    private void initHeader(Context context) {
        headerLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerLayout.setLayoutParams(layoutParams);
        headerLayout.setOrientation(HORIZONTAL);
        addView(headerLayout);
    }

    private void initContent(Context context) {
        viewPager = new ViewPager(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(layoutParams);
        viewPager.setId(R.id.home_header_viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicatorLayout.setX(position * tabWidth + (float)tabWidth/screenInfo.widthPixels * positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Tab tab = (Tab)tabs.get(position);
                StatusBarUtil.setColor(activity, tab.color, 0);
                headerLayout.setBackgroundColor(tab.color);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addView(viewPager);
    }
    public void initialize(Activity activity, List<T> headerTabs, List<Y> bodys, FragmentManager fm, CustomHeaderAndContent.ViewCreatorTab<T> viewCreatorTab) {
        this.activity = activity;
        this.tabs = headerTabs;
        Tab tab = (Tab)headerTabs.get(0);
        StatusBarUtil.setColor(activity, tab.color, 0);
        headerLayout.setBackgroundColor(tab.color);
        initHeaderData(context, headerTabs, viewCreatorTab);
        initContentData(context, bodys, fm);
    }
    // 初始化viewPage
    public void initContentData(Context context, List<Y> bodys, FragmentManager fm) {
        System.out.println("hello");
        customFragmentPagerAdapter = new CustomFragmentPagerAdapter(fm, bodys);
        viewPager.setAdapter(customFragmentPagerAdapter);
        System.out.println("world");
    }
    // 初始化顶部tabs
    public void initHeaderData(Context context, List<T> headerTabs, CustomHeaderAndContent.ViewCreatorTab<T> viewCreatorTab) {
        LinearLayout outerScrollViewWrap = new LinearLayout(context);
        LinearLayout.LayoutParams outerScrollViewWrapParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        outerScrollViewWrapParams.weight = 1;
        outerScrollViewWrap.setLayoutParams(outerScrollViewWrapParams);
        outerScrollViewWrap.setOrientation(VERTICAL);
        // 创建左边的headerIcon
        ImageView imageView = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(80, 80);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.splash_icon);
        headerLayout.addView(imageView);
        // 创建一个scrollView
        HorizontalScrollView scrollView = new HorizontalScrollView(context);
        HorizontalScrollView.LayoutParams scrollViewParams = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.MATCH_PARENT, HorizontalScrollView.LayoutParams.WRAP_CONTENT);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setLayoutParams(scrollViewParams);
        // 设置一个wrap布局
        LinearLayout wrapLayout = new LinearLayout(context);
        LinearLayout.LayoutParams wrapLayoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        wrapLayout.setLayoutParams(wrapLayoutParam);
        // 把tab添加到wrapLayout中
        if (headerTabs != null && headerTabs.size() > 0) {
            for(int i = 0; i < headerTabs.size(); i++) {
                View view = viewCreatorTab.createView(context, i);
                viewCreatorTab.updateUI(context, view, i, headerTabs.get(i));
                int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(finalI);
                    }
                });
                wrapLayout.addView(view);
            }
        }
        indicatorLayout = new LinearLayout(context);
        LinearLayout.LayoutParams indicatorLayoutParams = new LinearLayout.LayoutParams(200, 20);
        indicatorLayout.setBackgroundColor(getResources().getColor(R.color.red_500));
        indicatorLayout.setLayoutParams(indicatorLayoutParams);
        scrollView.addView(wrapLayout);
        outerScrollViewWrap.addView(scrollView);
        outerScrollViewWrap.addView(indicatorLayout);
        headerLayout.addView(outerScrollViewWrap);
        // 创建一个顶部导航栏下面的指示器
        ImageView imageView1 = new ImageView(context);
        imageView1.setLayoutParams(layoutParams);
        imageView1.setImageResource(R.drawable.splash_icon);
        headerLayout.addView(imageView1);
    }
    // 提供一个自定义tabView
    public interface ViewCreatorTab<T> {
        View createView(Context context, int position);
        void updateUI(Context context, View view, int position, T data);
    }
    public static class Tab {
        public String name;
        public int color;
        public Tab(String name, int color) {
            this.name = name;
            this.color = color;
        }
    }
}
