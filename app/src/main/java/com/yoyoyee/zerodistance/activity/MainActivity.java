package com.yoyoyee.zerodistance.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SlidingTabLayout;
import com.yoyoyee.zerodistance.helper.ViewPagerAdapter;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;

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

        //更新手機資料庫
        ClientFunctions.updateMissions(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                SQLiteHandler db = AppController.getDB();
                String TAG = AppController.class.getSimpleName();
                ArrayList<Mission> missions = db.getMissions();
                if (missions.size() > 0) {
                    Log.d(TAG, "onResponse: " + missions.get(0).getTitle() + " " + missions.get(0).createdAt + " " + missions.get(0).finishedAt);
                }
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });
        //更新手機資料庫

    }


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

    }

}
