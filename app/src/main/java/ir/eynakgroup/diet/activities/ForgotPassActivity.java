package ir.eynakgroup.diet.activities;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.network.response_models.CommonResponse;
import ir.eynakgroup.diet.utils.view.JustifiedTextView;
import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPassActivity extends BaseActivity implements View.OnClickListener {

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

    private static String phoneNumber;
    private static String code;

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


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(mViewPager.getCurrentItem() != 0)
                    mBackView.setVisibility(View.VISIBLE);
                else
                    mBackView.setVisibility(View.GONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_step:
                if(v.getVisibility() == View.VISIBLE)
                    onBackFragment();
                break;
            default:

                break;
        }
    }

    public int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    public ViewPager getViewPager(){
        return mViewPager;
    }

    private void onBackFragment(){
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

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    return inflater.inflate(R.layout.fragment_forgot_pass_phone, container, false);
                case 2:
                    return inflater.inflate(R.layout.fragment_forgot_pass_recovery_code, container, false);
                case 3:
                    return inflater.inflate(R.layout.fragment_forgot_pass_recovery_pass, container, false);
                default:
                    return null;
            }
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            final ViewPager viewPager = ((ForgotPassActivity) getActivity()).getViewPager();
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    final Button btnNext = (Button) view.findViewById(R.id.btn_next);
                    final TextInputEditText editPhone = (TextInputEditText) view.findViewById(R.id.edit_phone);
                    btnNext.setWidth(mDisplayMetrics.widthPixels / 2);
                    btnNext.setHeight(mDisplayMetrics.heightPixels / 100);
                    editPhone.setWidth((int)(mDisplayMetrics.widthPixels / 1.25));

                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!TextUtils.isEmpty(editPhone.getText().toString().trim()) && editPhone.getText().toString().trim().length() == 11){
                                phoneNumber = editPhone.getText().toString().trim();
                                Call<Void> call = mRequestMethod.forgotPass(phoneNumber);
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                        System.out.println(response.raw().body().toString());
                                        viewPager.setCurrentItem(((ForgotPassActivity) getActivity()).getItem(+1), true);
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });

                            }else{
                                editPhone.setError(getString(R.string.reg8_phone_error));
                                editPhone.requestFocus();
                            }


                        }
                    });
                    break;
                case 2:

                    final Button btnCode = (Button) view.findViewById(R.id.btn_code);
                    final EditText editCode = (EditText) view.findViewById(R.id.edit_code);
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
                            if(!TextUtils.isEmpty(editCode.getText().toString().trim()) && editCode.getText().toString().trim().length() == 11){
                                code = editCode.getText().toString().trim();
                                Call<CommonResponse> call = mRequestMethod.forgotPassCode(phoneNumber, code);
                                call.enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {

                                        if(response.body().getStatus() !=  null && response.body().getStatus().equalsIgnoreCase("success"))
                                            viewPager.setCurrentItem(((ForgotPassActivity) getActivity()).getItem(+1), true);

                                        else if(response.body().getError() != null){
                                            editCode.setError(getString(R.string.code_error));
                                            editCode.requestFocus();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                                    }
                                });

                            }else{
                                editCode.setError(getString(R.string.code_error));
                                editCode.requestFocus();
                            }


                        }
                    });
                    break;
                case 3:
                    final TextInputEditText editPass = (TextInputEditText) view.findViewById(R.id.edit_pass);
                    final TextInputEditText editRePass = (TextInputEditText) view.findViewById(R.id.edit_re_pass);
                    editPass.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));
                    editRePass.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));

                    final Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
                    btnSubmit.setWidth(mDisplayMetrics.widthPixels / 2);
                    btnSubmit.setHeight(mDisplayMetrics.heightPixels / 100);

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!TextUtils.isEmpty(editPass.getText().toString().trim()) && !TextUtils.isEmpty(editRePass.getText().toString().trim()) && editPass.getText().toString().trim().equals(editRePass.getText().toString().trim())){
                                Call<CommonResponse> call = mRequestMethod.resetPass(phoneNumber, code, editPass.getText().toString().trim());
                                call.enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {

                                        if(response.body().getStatus() != null && response.body().getStatus().equalsIgnoreCase("success"))
                                            viewPager.setCurrentItem(((ForgotPassActivity) getActivity()).getItem(+1), true);

                                        else if(response.body().getError() != null)
                                            getToast(getString(R.string.reg8_password_error)).show();

                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                                    }
                                });

                            }else
                                getToast(getString(R.string.reg8_password_error)).show();

                        }
                    });
                    break;

                default:
            }

            ((TextView)view.findViewById(R.id.title)).setText(getString(getArguments().getInt(ARG_TITLE)));
            ((JustifiedTextView)view.findViewById(R.id.description)).setText(getString(getArguments().getInt(ARG_DESCRIPTION)));
        }

        private Toast getToast(String message) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_custom,
                    (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(message);
            Toast toast = new Toast(getContext());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            return toast;
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
