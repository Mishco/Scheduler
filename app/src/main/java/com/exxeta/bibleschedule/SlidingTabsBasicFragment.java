package com.exxeta.bibleschedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exxeta.bibleschedule.View.SlidingTabLayout;

@SuppressLint("ValidFragment")
class SlidingTabsBasicFragment extends Fragment {
    static final String LOG_TAG = "SlidingTabsFragment";

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private static final int NUMBER_OF_PAGES = 52;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        mViewPager.setAdapter(new SamplePagerAdapter());
//        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
//        mSlidingTabLayout.setViewPager(mViewPager);
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);

            container.addView(view);

            //TextView title = (TextView) view.findViewById(R.id.item_title);
            //title.setText(String.valueOf(position + 1));

            Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }
    }
}
