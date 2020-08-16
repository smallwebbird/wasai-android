package com.example.start.pages.customBanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.start.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class CustomBanner<T> extends FrameLayout {
    private Context mContext;
    private ViewPager mBannerViewPager;
    private LinearLayout mIndicatorLayout;
    private BannerPagerAdapter bannerPagerAdapter;
    private FragmentBannerPagerAdapter fBannerPagerAdapter;
    private OnPageClickListener mOnPageClickListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private ViewPagerScroller mScroller;
    private IndicatorStyle mIndicatorStyle = IndicatorStyle.ORDINARY;
    private List<T> data;
    private boolean isAutoPlay = true;
    private Handler bannerHandler = new Handler();

    public CustomBanner(Context context) {
        super(context);
        init(context);
    }

    public CustomBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context) {
        mContext = context;
        addBannerViewPager(context);
        addIndicatorLayout(context);
    }

    private void addBannerViewPager(Context context) {
        mBannerViewPager = new ViewPager(context);
        mBannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetP) {
                if (!isMarginal(position) && mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(getActualPosition(position),
                            positionOffset, positionOffsetP);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (!isMarginal(position) && mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(getActualPosition(position));
                }
                updateIndicator();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int position = mBannerViewPager.getCurrentItem();
                if (!isMarginal(position) && mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (position == 0) {
                        mScroller.setSudden(true);
                        mBannerViewPager.setCurrentItem(bannerPagerAdapter.getCount() - 2, true);
                        mScroller.setSudden(false);
                    } else if (position == bannerPagerAdapter.getCount() - 1) {
                        mScroller.setSudden(true);
                        mBannerViewPager.setCurrentItem(1, true);
                        mScroller.setSudden(false);
                    }
                }
            }


        });
        replaceViewPagerScroll();
        addView(mBannerViewPager);
    }

    public void addIndicatorLayout(Context context) {
        mIndicatorLayout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.setMargins(0, 0, 0, 30);
        mIndicatorLayout.setGravity(Gravity.CENTER);
        mIndicatorLayout.setLayoutParams(layoutParams);
        addView(mIndicatorLayout);
    }

    public CustomBanner<T> setPages(ViewCreator<T> viewCreator, List<T> data) {
        this.data = data;
        bannerPagerAdapter = new BannerPagerAdapter(mContext, viewCreator, data);
        if (mOnPageClickListener != null) {
            bannerPagerAdapter.setOnPageClickListener(mOnPageClickListener);
        }
        if (data == null || data.isEmpty()) {

        } else {
            initIndicator(data.size());
        }
        mBannerViewPager.setAdapter(bannerPagerAdapter);
        mBannerViewPager.setCurrentItem(1);
        updateIndicator();
//        bannerHandler.postDelayed(task, 3000);
        return this;
    }
    private void initIndicator(int size) {
        mIndicatorLayout.removeAllViews();
        if (size > 0) {
            for (int i =0; i < size; i++) {
                ImageView imageView = new ImageView(mContext);
                LayoutParams layoutParams = new LayoutParams(30, 30);
                layoutParams.setMargins(5, 5,5, 5);
                imageView.setLayoutParams(layoutParams);
                mIndicatorLayout.addView(imageView);
            }
        }
    }
    private void updateIndicator() {
        if (mIndicatorStyle == IndicatorStyle.ORDINARY) {
            int count = mIndicatorLayout.getChildCount();
            int currentPage = mBannerViewPager.getCurrentItem();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    ImageView imageView = (ImageView) mIndicatorLayout.getChildAt(i);
                    if (i == currentPage - 1) {
                        imageView.setImageResource(R.drawable.indictor_circle_checked);
                    } else {
                        imageView.setImageResource(R.drawable.indictor_circle);
                    }
                }
            }
        } else if(mIndicatorStyle == IndicatorStyle.NUMBER) {

        }
    }
    private boolean isMarginal(int position) {
        return position == 0 || position == getCount() + 1;
    }
    public int getCount() {
        if (bannerPagerAdapter == null || bannerPagerAdapter.getCount() == 0) {
            return 0;
        }
        return bannerPagerAdapter.getCount() - 2;
    }
    private int getActualPosition(int position) {
        if (bannerPagerAdapter == null || bannerPagerAdapter.getCount() == 0) {
            return -1;
        }

        if (position == 0) {
            return getCount() - 1;
        } else if (position == getCount() + 1) {
            return 0;
        } else {
            return position - 1;
        }
    }
    // 创建一个task供handler发送实现自动轮播
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if(isAutoPlay) {
                int currentItem = mBannerViewPager.getCurrentItem() + 1;
                mBannerViewPager.setCurrentItem(currentItem);
                bannerHandler.postDelayed(task, 3000);
            } else {
                bannerHandler.postDelayed(task, 3000);
            }
        }
    };
    public enum IndicatorStyle {
        // 没有指示器
        NONE,
        // 数字指示器
        NUMBER,
        // 普通指示器
        ORDINARY
    }
    // 设置动画样式1
    public CustomBanner<T> setZoomOutPageTransformer() {
        mBannerViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        return this;
    }
    // 设置动画样式2
    public CustomBanner<T> setDepthPageTransformer() {
        mBannerViewPager.setPageTransformer(true, new DepthPageTransformer());
        return this;
    }
//    public CustomBanner<T> setFragmentPages(FragmentManager fm, ViewCreator<T> viewCreator, List<T> data) {
//        fBannerPagerAdapter = new FragmentBannerPagerAdapter(fm,viewCreator, data);
////        if (mOnPageClickListener != null) {
////            bannerPagerAdapter.setOnPageClickListener(mOnPageClickListener);
////        }
//        mBannerViewPager.setAdapter(fBannerPagerAdapter);
//        return this;
//    }

    public CustomBanner<T> setOnPageClickListener(OnPageClickListener l) {
        if (bannerPagerAdapter != null) {
            bannerPagerAdapter.setOnPageClickListener(l);
        }
        mOnPageClickListener = l;
        return this;
    }

    public CustomBanner setOnPageChangeListener(ViewPager.OnPageChangeListener l) {
        mOnPageChangeListener = l;
        return this;
    }

    private void replaceViewPagerScroll() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            mScroller = new ViewPagerScroller(mContext, new AccelerateInterpolator());
            field.set(mBannerViewPager, mScroller);
            mScroller.setScrollDuration(500);
        } catch (Exception e) {

        }
    }
    // 设置轮播图持续持续时间
    public CustomBanner<T> setScrollDuration(int scrollDuration) {
        mScroller.setScrollDuration(scrollDuration);
        return this;
    }

    // 获取持续时间
    public int getScrollDuration() {
        return mScroller.getScrollDuration();
    }

    public interface ViewCreator<T> {
        View createView(Context context, int position);
        void updateUI(Context context, View view, int position, T t);
    }
    public interface OnPageClickListener<T> {
        void onPageClick(int position, T t);
    }
}
