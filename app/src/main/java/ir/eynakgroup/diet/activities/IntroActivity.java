package ir.eynakgroup.diet.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.lang.ref.WeakReference;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.utils.view.JustifiedTextView;


public class IntroActivity extends BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private Button mButton;

    private Animation animFadeOut;
    private Animation animFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setRadius(mDisplayMetrics.widthPixels / 100);
        mIndicator.setViewPager(mViewPager);

        mButton = (Button) findViewById(R.id.btn_enter);
        mButton.setWidth(mDisplayMetrics.widthPixels / 2);
        mButton.setHeight(mDisplayMetrics.heightPixels / 100);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked!");
                setResult(RESULT_OK);
                finish();
            }
        });

        animFadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        animFadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position + 1 == 3) {
                    mIndicator.setVisibility(View.INVISIBLE);
                    mButton.setAnimation(animFadeIn);
                    mButton.setVisibility(View.VISIBLE);
                } else {
                    mIndicator.setVisibility(View.VISIBLE);
                    mButton.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setVisibility(View.INVISIBLE);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Holder {
        private int drawRes, strResTitle, strResDes;

        Holder(int drawRes, int strResTitle, int strResDes) {
            this.drawRes = drawRes;
            this.strResTitle = strResTitle;
            this.strResDes = strResDes;
        }

        public int getDrawRes() {
            return drawRes;
        }

        public int getStrResTitle() {
            return strResTitle;
        }

        public int getStrResDes() {
            return strResDes;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_DRAWABLE = "drawable_intro";
        private static final String ARG_TITLE = "title_intro";
        private static final String ARG_DESCRIPTION = "description_intro";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment getInstance(WeakReference<Holder> holderWeakReference) {
            Holder holder = holderWeakReference.get();
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_DRAWABLE, holder.getDrawRes());
            args.putInt(ARG_TITLE, holder.getStrResTitle());
            args.putInt(ARG_DESCRIPTION, holder.getStrResDes());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_intro, container, false);
            TextView txtTitleView = (TextView) rootView.findViewById(R.id.title);
//            TextView txtDesView = (TextView) rootView.findViewById(R.id.description);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.intro_img);
            imageView.setImageResource(getArguments().getInt(ARG_DRAWABLE));
            txtTitleView.setText(getString(getArguments().getInt(ARG_TITLE)));
//            txtDesView.setText(getString(getArguments().getInt(ARG_DESCRIPTION)));

            JustifiedTextView mJTv = (JustifiedTextView) rootView.findViewById(R.id.description);
            mJTv.setText(getString(getArguments().getInt(ARG_DESCRIPTION)));
//            mJTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            mJTv.setLineSpacing(15);
            mJTv.setAlignment(Paint.Align.RIGHT);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position + 1) {
                case 1:
                    return PlaceholderFragment.getInstance(new WeakReference<>(new Holder(R.drawable.intro_1, R.string.title_intro1, R.string.des_intro1)));
                case 2:
                    return PlaceholderFragment.getInstance(new WeakReference<>(new Holder(R.drawable.intro_2, R.string.title_intro2, R.string.des_intro2)));
                case 3:
                    return PlaceholderFragment.getInstance(new WeakReference<>(new Holder(R.drawable.intro_3, R.string.title_intro3, R.string.des_intro3)));
                default:
                    return PlaceholderFragment.getInstance(null);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
