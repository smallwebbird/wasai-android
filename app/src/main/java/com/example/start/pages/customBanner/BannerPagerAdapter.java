package com.example.start.pages.customBanner;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class BannerPagerAdapter<T> extends PagerAdapter {
    private Context mContext;
    private List<T> mData;
    private CustomBanner.ViewCreator<T> mCreator;
    private CustomBanner.OnPageClickListener<T> mOnPageClickListener;
    private SparseArray<View> views = new SparseArray<>();

    public BannerPagerAdapter(Context context, CustomBanner.ViewCreator<T> viewCreator, List<T> data) {
        mContext = context;
        mCreator = viewCreator;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null || mData.isEmpty() ? 0 : mData.size() + 2;
    }

    // 判断初始化返回的Object是不是一个View对象
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    // 初始化显示的条目对象
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final int item = getActualPosition(position);
        View view = views.get(position);

        if (view == null) {
            view = mCreator.createView(mContext, item);
            views.put(position, view);
        }
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }

        final T t = mData.get(item);

        mCreator.updateUI(mContext, view, item, t);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPageClickListener != null) {
                    mOnPageClickListener.onPageClick(item, t);
                }
            }
        });
        container.addView(view);
        return view;

    }
    private int getActualPosition(int position) {
        if (position == 0) {
            return mData.size() - 1;
        } else if (position == getCount() - 1) {
            return 0;
        } else {
            return position - 1;
        }
    }
    // 销毁条目数据
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
    }

    public void setOnPageClickListener(CustomBanner.OnPageClickListener l) {
        mOnPageClickListener = l;
    }
}
