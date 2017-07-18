package ir.eynakgroup.diet.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v7.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.List;

import ir.eynakgroup.diet.R;


public class DietFragment extends Fragment {

    final int BREAKFAST = 3;
    final int LUNCH = 2;
    final int SNACK = 1;
    final int DINNER = 0;

    final int TODAY = 0;
    final int TOMORROW = 1;
    final int DAY_AFTER_TOMORROW = 2;

    private static DietFragment mDietFragmentInstance = null;
    private Context mContext;

    public DietFragment(Context context) {
        mContext = context;
    }

    public static DietFragment getInstance(Context context) {
        if (mDietFragmentInstance == null)
            mDietFragmentInstance = new DietFragment(context);

        return mDietFragmentInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * date spinner part
         */
        AppCompatSpinner date_spinner = (AppCompatSpinner) view.findViewById(R.id.date_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.date_spinner_array, R.layout.date_spinner_view);
        adapter.setDropDownViewResource(R.layout.date_spinner_item);
        date_spinner.setAdapter(adapter);
        date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case TODAY:
                        break;
                    case TOMORROW:
                        break;
                    case DAY_AFTER_TOMORROW:
                        break;
                }
                //TODO do somthing, date is changed.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * meal tabs part
         */
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.meals_tab_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.meals_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(DietDishesFragment.newInstance(DINNER+""), getString(R.string.dinner));
        adapter.addFragment(DietDishesFragment.newInstance(SNACK+""), getString(R.string.snack));
        adapter.addFragment(DietDishesFragment.newInstance(LUNCH+""), getString(R.string.lunch));
        adapter.addFragment(DietDishesFragment.newInstance(BREAKFAST+""), getString(R.string.breakfast));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
