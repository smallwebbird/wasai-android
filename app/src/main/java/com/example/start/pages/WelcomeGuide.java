package com.example.start.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.start.R;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.start.R2;
import com.example.start.pages.login.LoginActivity;
import com.jaeger.library.StatusBarUtil;

public class WelcomeGuide extends AppCompatActivity {
    @BindView(R2.id.activity_welcome_guide)
     RelativeLayout welcomeGuideView;
    @BindView(R2.id.viewpager)
     ViewPager viewPager;
    @BindArray(R2.array.guide_icons)
     TypedArray guideIcons;
    @BindArray(R2.array.guide_descs)
     String[] guideDescs;
    @BindArray(R2.array.guide_bgs)
     int[] guideBgs;

    private int barAlpha = 1;


    private ArgbEvaluator argbEvaluator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.red_050), barAlpha);
        argbEvaluator = new ArgbEvaluator();
        final Adapter adapter = new Adapter(guideIcons, guideDescs);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int color = (int)argbEvaluator.evaluate(positionOffset, guideBgs[position % guideBgs.length],
                        guideBgs[(position + 1) % guideBgs.length]);
                StatusBarUtil.setColor(WelcomeGuide.this, color, barAlpha);
                welcomeGuideView.setBackgroundColor(color);
            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }

        });

    }

    private class Adapter extends PagerAdapter
    {

        private String[] descs;

        private TypedArray icons;

        public Adapter(TypedArray guideIcons, String[] guideDescs)
        {
            descs = guideDescs;
            icons = guideIcons;
        }

        @Override
        public int getCount()
        {
            return icons.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            View itemLayout = getLayoutInflater().inflate(R.layout.guide_viewpager_item, container, false);
            ImageView mImage = (ImageView)itemLayout.findViewById(R.id.guide_viewpager_item_img);
            TextView mTextView = (TextView)itemLayout.findViewById(R.id.guide_viewpager_item_desc);
            Button mButton = (Button)itemLayout.findViewById(R.id.guide_viewpager_item_btn);
            mImage.setImageResource(icons.getResourceId(position, 0));
            mTextView.setText(descs[position]);
            if (position == getCount() - 1)
            {
                mButton.setVisibility(View.VISIBLE);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent loginIntent = new Intent(WelcomeGuide.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });
            }
            else
            {
                mButton.setVisibility(View.GONE);
            }
            container.addView(itemLayout);
            return itemLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View)object);
        }
    }

}
