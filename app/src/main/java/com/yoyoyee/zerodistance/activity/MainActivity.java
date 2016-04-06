package com.yoyoyee.zerodistance.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.group.Group;
import com.yoyoyee.zerodistance.helper.SlidingTabLayout;
import com.yoyoyee.zerodistance.helper.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

//    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static Context context;
    ViewPagerAdapter adapter;
    Toolbar toolbar;
    ViewPager pager;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"任務","揪團","未完成任務","已完成揪團","成就","設定"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //原本的開始

        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
        // 原本的結束

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Titles.length);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = null;
//            int a =getArguments().getInt(ARG_SECTION_NUMBER);
//            switch(a){
//                case 1:
//                    rootView= inflater.inflate(R.layout.fragment_mission, container, false);
//                    CreateMissionView(rootView);
//                    break;
//                case 2:
//                    rootView = inflater.inflate(R.layout.fragment_team, container, false);
//                    CreateTeamView(rootView);
//                    break;
//                case 3:
//                    rootView = inflater.inflate(R.layout.fragment_havebeen, container, false);
//                    CreateHaveBeenView(rootView);
//                    break;
//                case 4:
//                    rootView = inflater.inflate(R.layout.fragment_notbeen, container, false);
//                    CreateNotBeenView(rootView);
//                    break;
//                case 5:
//                    rootView = inflater.inflate(R.layout.fragment_achievement, container, false);
//                    CreateAchieventView(rootView);
//                    break;
//                case 6:
//                    rootView = inflater.inflate(R.layout.fragment_setting, container, false);
//                    CreateSettingView(rootView);
//                    break;
//            }
////            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
////            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//
//        public void CreateMissionView(View rootView){
//            String[] myDataset={"你說把愛漸漸放下會走更遠,或許命運的謙讓我遇見","你好阿","xx","你好阿","xx","你好阿"} , missionName ={"我難過","打屁屁","878787","打屁屁","878787","打屁屁"};
//            Drawable[] missiondangerous={Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"),Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"),Drawable.createFromPath("@android:drawable/star_big_on")} ;
//
//            try {
//
//                MyAdapter myAdapter = new MyAdapter(myDataset , missionName , missiondangerous);
//                RecyclerView mList = (RecyclerView) rootView.findViewById(R.id.listView);
//
//                LinearLayoutManager layoutManager;
//                layoutManager = new LinearLayoutManager(getActivity());
//                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                mList.setLayoutManager(layoutManager);
//                mList.setAdapter(myAdapter);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //漂浮
//            FloatingActionButton fab=  (FloatingActionButton) rootView.findViewById(R.id.fab);
//                fab.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//    //                        .setAction("Action", null).show();
//                        Intent in = new Intent(context, MissionActivity.class);
//
//                            startActivity(in);
//
//
//
//                    }
//                });
//
//            //漂浮
//        }
//        public void CreateTeamView(View rootView){
//            //漂浮
//            FloatingActionButton fab=  (FloatingActionButton) rootView.findViewById(R.id.fab);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    //                        .setAction("Action", null).show();
//                    Intent in = new Intent(context, GroupActivity.class);
//
//                    startActivity(in);
//                }
//            });
//
//            //漂浮
//        }
//        public void CreateHaveBeenView(View rootView){
//
//        }
//        public void CreateNotBeenView(View rootView){
//
//        }
//        public void CreateAchieventView(View rootView){
//
//        }
//        public void CreateSettingView(final View rootView){
//          SeekBar  seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
//            final TextView  textView17 = (TextView) rootView.findViewById(R.id.textView17);
//            seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() { int progress = 0;
//
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) { progress = progresValue; }
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//                    // Do something here,
//
//                    //if you want to do anything at the start of
//                    // touching the seekbar
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                    // Display the value in textview
//                    textView17.setText(progress + "/" + seekBar.getMax());
//                }
//            });
//        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//        boolean s = true;
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 6;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "任務";
//                case 1:
//                    return "揪團";
//                case 2:
//                    return "已完成任務";
//                case 3:
//                    return "未完成任務";
//                case 4:
//                    return "成就";
//                case 5:
//                    return "設定";
//            }
//            return null;
//        }
//    }
}
