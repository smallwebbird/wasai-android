package com.example.start.pages.customRefresh;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.start.R;

public class Refresh extends LinearLayout implements View.OnTouchListener {
    private Context cContext;
    private LinearLayout refreshHeader;
    private ScrollView scrollView;
    private int hideRefreshHeaderHeight; // 隐藏下拉刷新的头部高度
    private MarginLayoutParams refreshLayoutParams;
    private boolean loadOnce = false;
    private boolean isScrollIntoHeader = true;
    private float yDown;  //手指按下时屏幕的纵坐标
    private int touchSlop;
    // 代表当前的状态
    private Status currentStatus = Status.STATUS_PULL_TO_REFRESH;
    // 下拉头部回滚的速度
    public static final int SCROLL_SPEED = -5;
    private PullToRefreshListener pullToRefreshListener;
    private ImageView arrow;
    private Status lastStatus;
    private ProgressBar progressBar;


    public Refresh(Context context) {
        super(context);
        init(context);
    }

    public Refresh(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Refresh(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Refresh(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init (Context context) {
        cContext = context;
        initRefreshHeader();
    }

    private void initRefreshHeader() {
        refreshHeader = (LinearLayout) LayoutInflater.from(cContext).inflate(R.layout.custom_refresh_header, null, true);
        addView(refreshHeader, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            hideRefreshHeaderHeight = -refreshHeader.getHeight();
            refreshLayoutParams = (MarginLayoutParams) refreshHeader.getLayoutParams();
            refreshLayoutParams.topMargin = hideRefreshHeaderHeight;
            scrollView = (ScrollView) getChildAt(1);
            scrollView.setOnTouchListener(this);
            arrow = (ImageView) refreshHeader.findViewById(R.id.custom_refresh_header_arrow);
            progressBar = (ProgressBar) refreshHeader.findViewById(R.id.custom_refresh_header_loading);
            /*
                getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件，如viewpager
                就是用这个距离来判断用户是否翻页。
            */
            touchSlop = ViewConfiguration.get(cContext).getScaledTouchSlop();
            loadOnce = true;
        }
    }
    public class RefreshingTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int topMargin = refreshLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= 0) {
                    topMargin = 0;
                    break;
                }
                publishProgress(topMargin);
                sleep(10);
            }
            currentStatus = Refresh.Status.STATUS_REFRESHING;
            publishProgress(0);
            if (pullToRefreshListener != null) {
                pullToRefreshListener.onRefresh();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            updateHeaderView();
            refreshLayoutParams.topMargin = topMargin[0];
            refreshHeader.setLayoutParams(refreshLayoutParams);
        }
    }

    // 影藏头部
    public class HideRefreshHeaderTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int topMargin = refreshLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= hideRefreshHeaderHeight) {
                    topMargin = hideRefreshHeaderHeight;
                    break;
                }
                // 更新ui
                publishProgress(topMargin);
                // 阻塞线程
                sleep(10);
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            // 更新ui的操作
            refreshLayoutParams.topMargin = topMargin[0];
            refreshHeader.setLayoutParams(refreshLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer topMargin) {
            // 接收线程执行结果, 也就是
            refreshLayoutParams.topMargin = topMargin;
            refreshHeader.setLayoutParams(refreshLayoutParams);
            currentStatus = Refresh.Status.STATUS_REFRESH_FINISHED;
        }
    }

    // 完成下拉刷新之后
    public void finishRefresh() {
        currentStatus = Status.STATUS_REFRESH_FINISHED;
        // 完成下拉刷新之后，回弹到首页
        new HideRefreshHeaderTask().execute();
    }

    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isAblePullDown(event);
        if (isScrollIntoHeader) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    yDown = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float yMove = event.getRawY();
                    int distance = (int) (yMove - yDown);
                    if (distance <= 0 && refreshLayoutParams.topMargin <= hideRefreshHeaderHeight) {
                        return false;
                    }
                    //
                    if (distance < touchSlop) {
                        return false;
                    }
                    // 如果正在刷新，那么就不响应下拉刷新
                    if (currentStatus != Status.STATUS_REFRESHING) {
                        if (refreshLayoutParams.topMargin > 0) {
                            currentStatus = Status.STATUS_RELEASE_TO_REFRESH;
                        } else {
                            currentStatus = Status.STATUS_PULL_TO_REFRESH;
                        }
                        refreshLayoutParams.topMargin = (distance / 2) + hideRefreshHeaderHeight;
                        refreshHeader.setLayoutParams(refreshLayoutParams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if (currentStatus == Status.STATUS_RELEASE_TO_REFRESH) {
                        new RefreshingTask().execute();
                    } else if (currentStatus == Status.STATUS_PULL_TO_REFRESH) {
                        new HideRefreshHeaderTask().execute();
                    }
                    break;
            }
            if (currentStatus == Status.STATUS_PULL_TO_REFRESH
                        || currentStatus == Status.STATUS_RELEASE_TO_REFRESH) {
                updateHeaderView();
                lastStatus = currentStatus;
                return true;
            }
        }
        return false;
    }

    private void updateHeaderView() {
        if (currentStatus != lastStatus) {
            if (currentStatus == Status.STATUS_RELEASE_TO_REFRESH) {
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == Status .STATUS_PULL_TO_REFRESH) {
                progressBar.setVisibility(View.GONE);
                arrow.setVisibility(View.VISIBLE);
                rotateArrow();
            } else if (currentStatus == Status.STATUS_REFRESHING) {
                progressBar.setVisibility(View.VISIBLE);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
            }
        }
    }

    // 箭头饭庄
    private void rotateArrow() {
        float pivotX = arrow.getWidth() / 2f;
        float pivotY = arrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (currentStatus == Status.STATUS_PULL_TO_REFRESH) {
            fromDegrees = 0f;
            toDegrees = 0f;
        } else if (currentStatus == Status.STATUS_RELEASE_TO_REFRESH) {
            System.out.println("zzzzzzz");
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        rotateAnimation.setDuration(100);
        rotateAnimation.setFillAfter(true);
        arrow.startAnimation(rotateAnimation);
    }

    public interface PullToRefreshListener {
        void onRefresh();
    }

    public Refresh setOnRefreshListener(PullToRefreshListener pullToRefreshListener) {
        this.pullToRefreshListener = pullToRefreshListener;
        return this;
    }

    // 下拉过程中的几个状态
    public enum Status {
        STATUS_PULL_TO_REFRESH, // 下拉状态
        STATUS_RELEASE_TO_REFRESH, // 释放立即刷新
        STATUS_REFRESHING, // 正在刷新
        STATUS_REFRESH_FINISHED  // 刷新完成或未刷新状态
    }
    private void isAblePullDown(MotionEvent event) {
        if (scrollView.getScrollY() == 0) {
            if (!isScrollIntoHeader) {
                yDown = event.getRawY();
            }
            isScrollIntoHeader = true;
        } else {
//            if (refreshLayoutParams.topMargin != hideRefreshHeaderHeight) {
//                System.out.println("执行这个方法了");
//                refreshLayoutParams.topMargin = hideRefreshHeaderHeight;
//                refreshHeader.setLayoutParams(refreshLayoutParams);
//            }
            isScrollIntoHeader = false;
        }
    }
}
