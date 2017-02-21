package ir.eynakgroup.diet.activities;

import android.graphics.Rect;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.utils.view.JustifiedTextView;

public class ForgotPassActivity extends MainActivity implements View.OnClickListener {

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
    private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mBackView = (ImageView) findViewById(R.id.back_step);
        final View backParent = (View) mBackView.getParent();  // button: the view you want to enlarge hit area
        backParent.post( new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                mBackView.getHitRect(rect);
                rect.top -= 100;    // increase top hit area
                rect.left -= 100;   // increase left hit area
                rect.bottom += 100; // increase bottom hit area
                rect.right += 100;  // increase right hit area
                backParent.setTouchDelegate( new TouchDelegate( rect , mBackView));
            }
        });

        mBackView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_step:
                onBackPressed();
                break;
            default:
                mViewPager.setCurrentItem(getItem(+1), true);
                break;
        }
    }

    public int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    public ViewPager getViewPager(){
        return mViewPager;
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() == 0) {
            finish();
            return;
        }
        mViewPager.setCurrentItem(getItem(-1), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_pass, menu);
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
        private int strResTitle, strResDes, id;

        Holder(int id, int strResTitle, int strResDes) {
            this.strResTitle = strResTitle;
            this.strResDes = strResDes;
            this.id = id;
        }
        public int getID(){return id;}

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
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_TITLE = "title_recovery";
        private static final String ARG_DESCRIPTION = "description_recovery";

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
            args.putInt(ARG_SECTION_NUMBER, holder.getID());
            args.putInt(ARG_TITLE, holder.getStrResTitle());
            args.putInt(ARG_DESCRIPTION, holder.getStrResDes());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final ViewPager viewPager = ((ForgotPassActivity) getActivity()).getViewPager();
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_forgot_pass_phone, container, false);

                    final Button btnNext = (Button) rootView.findViewById(R.id.btn_next);
                    final TextInputEditText editPhone = (TextInputEditText) rootView.findViewById(R.id.edit_phone);
                    btnNext.setWidth(mDisplayMetrics.widthPixels / 2);
                    btnNext.setHeight(mDisplayMetrics.heightPixels / 100);
                    editPhone.setWidth((int)(mDisplayMetrics.widthPixels / 1.25));

                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(((ForgotPassActivity) getActivity()).getItem(+1), true);
                        }
                    });



                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_forgot_pass_recovery_code, container, false);
                    final Button btnCode = (Button) rootView.findViewById(R.id.btn_code);
                    final EditText editCode = (EditText) rootView.findViewById(R.id.edit_code);
                    btnCode.setWidth(mDisplayMetrics.widthPixels / 2);
                    btnCode.setHeight(mDisplayMetrics.heightPixels / 100);
                    editCode.setWidth(mDisplayMetrics.widthPixels / 2);

                    editCode.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s.length() > 6){
                                editCode.setText(s.subSequence(0, 6));
                                editCode.setSelection(6);
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    btnCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(((ForgotPassActivity) getActivity()).getItem(+1), true);
                        }
                    });


                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_forgot_pass_recovery_pass, container, false);
                    final TextInputEditText editPass = (TextInputEditText) rootView.findViewById(R.id.edit_pass);
                    final TextInputEditText editRePass = (TextInputEditText) rootView.findViewById(R.id.edit_re_pass);
                    editPass.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));
                    editRePass.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));

                    final Button btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
                    btnSubmit.setWidth(mDisplayMetrics.widthPixels / 2);
                    btnSubmit.setHeight(mDisplayMetrics.heightPixels / 100);

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    break;
                default:
                    break;
            }

            ((TextView)rootView.findViewById(R.id.title)).setText(getString(getArguments().getInt(ARG_TITLE)));
            ((JustifiedTextView)rootView.findViewById(R.id.description)).setText(getString(getArguments().getInt(ARG_DESCRIPTION)));

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
            int pos = position + 1;
            switch (pos) {
                case 1:
                    return PlaceholderFragment.getInstance(new WeakReference<>(new Holder(pos, R.string.password_forgot, R.string.forgot_pass_description)));
                case 2:
                    return PlaceholderFragment.getInstance(new WeakReference<>(new Holder(pos, R.string.recovery_code, R.string.recovery_code_description)));
                case 3:
                    return PlaceholderFragment.getInstance(new WeakReference<>(new Holder(pos, R.string.recovery_pass, R.string.recovery_pass_description)));
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
