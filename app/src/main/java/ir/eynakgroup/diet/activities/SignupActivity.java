package ir.eynakgroup.diet.activities;

import android.accounts.Account;
import android.graphics.Paint;
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
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.blackbox_vision.wheelview.LoopScrollListener;
import io.blackbox_vision.wheelview.view.WheelView;
import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.account.KarafsAccountConfig;
import ir.eynakgroup.diet.account.tasks.CreateAccountTask;
import ir.eynakgroup.diet.network.response_models.User;
import ir.eynakgroup.diet.utils.JDateFormat;
import ir.eynakgroup.diet.utils.view.CustomIndicator;
import ir.eynakgroup.diet.utils.view.CustomTextView;
import ir.eynakgroup.diet.utils.view.CustomViewPager;
import ir.eynakgroup.diet.utils.view.JustifiedTextView;
import ir.eynakgroup.diet.utils.view.ToggleButtonGroupTableLayout;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

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
    private CustomViewPager mViewPager;
    private ImageView mBackView;
    private TextView mNextView;
    private CustomIndicator mIndicator;
    private static String mPhoneNumber;
    private static String mPassword;
    private static int mDay;
    private static int mMonth;
    private static int mYear;

    private final static JDateFormat mDateFormat = new JDateFormat();
    private static final User mUser = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mIndicator = (CustomIndicator) findViewById(R.id.indicator);
        mIndicator.setRadius(mDisplayMetrics.widthPixels / 100);
        mIndicator.setViewPager(mViewPager);

        mNextView = (CustomTextView) findViewById(R.id.next_step);
        mBackView = (ImageView) findViewById(R.id.back_step);

        final View nextParent = (View) mNextView.getParent();  // button: the view you want to enlarge hit area
        nextParent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                mNextView.getHitRect(rect);
                rect.top -= 200;    // increase top hit area
                rect.left -= 200;   // increase left hit area
                rect.bottom += 200; // increase bottom hit area
                rect.right += 200;  // increase right hit area
                nextParent.setTouchDelegate(new TouchDelegate(rect, mNextView));
            }
        });

        final View backParent = (View) mBackView.getParent();  // button: the view you want to enlarge hit area
        backParent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                mBackView.getHitRect(rect);
                rect.top -= 100;    // increase top hit area
                rect.left -= 100;   // increase left hit area
                rect.bottom += 100; // increase bottom hit area
                rect.right += 100;  // increase right hit area
                backParent.setTouchDelegate(new TouchDelegate(rect, mBackView));
            }
        });

        mBackView.setOnClickListener(this);
        mNextView.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mViewPager.getCurrentItem() != 0)
                    mBackView.setVisibility(View.VISIBLE);
                else
                    mBackView.setVisibility(View.GONE);

                if (mViewPager.getCurrentItem() != 7)
                    mNextView.setText(getString(R.string.next_step));
                else
                    mNextView.setText(getString(R.string.sign_up_submit));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }




    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_step:
                if (v.getVisibility() == View.VISIBLE) {
                    if (mViewPager.getCurrentItem() == 0)
                        onBackPressed();
                    else
                        mViewPager.setCurrentItem(getItem(-1), true);
                }
                break;
            case R.id.next_step:
                if (mViewPager.getCurrentItem() == 7) {
                    if (mPhoneNumber == null || !mPhoneNumber.startsWith("09") || mPhoneNumber.length() != 11) {
                        getToast(getString(R.string.reg8_phone_error)).show();
                        break;
                    }
                    if (mPassword == null || mPhoneNumber.length() == 0) {
                        getToast(getString(R.string.reg8_password_error)).show();
                        break;
                    }

                    new CreateAccountTask(this, KarafsAccountConfig.ACCOUNT_TYPE) {
                        @Override
                        protected void onPostExecute(Account result) {
                            super.onPostExecute(result);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }.execute(new CreateAccountTask.AccountInfo(mPhoneNumber, mPassword, mUser));

                } else {
                    boolean goToNext = true;
                    switch (mViewPager.getCurrentItem()) {
                        case 0:
                            if (mUser.getName() == null || mUser.getName().length() == 0) {
                                goToNext = false;
                                getToast(getString(R.string.reg1_error)).show();
                                break;

                            }
                            break;
                        case 1:
                            if (mUser.getGender() == null) {
                                goToNext = false;
                                getToast(getString(R.string.reg2_error)).show();
                                break;

                            }

                            break;
                        case 2:
                            mDateFormat.setIranianDate(mYear, mMonth, mDay);
                            try {
                                mUser.setBirthday(new SimpleDateFormat("yyyy/mm/dd").parse(mDateFormat.getGregorianDate()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (mUser.getBirthday() == null) {
                                goToNext = false;
                                getToast(getString(R.string.reg3_error)).show();
                                break;
                            }


                            break;
                        case 3:
                            if (mUser.getHeight() < 140) {
                                goToNext = false;
                                getToast(getString(R.string.reg4_error)).show();
                                break;

                            }

                            break;
                        case 4:
                            if (mUser.getWeight() < 30) {
                                goToNext = false;
                                getToast(getString(R.string.reg5_error)).show();
                                break;
                            }
                            break;
                        case 5:
                            System.out.println("activity -------------- " + mUser.getActivityLevel() + "");
//                            if(mUser.getActivityLevel() == -1){
//                                goToNext = false;
//                                getToast(getString(R.string.reg6_error)).show();
//                                break;
//                            }

                            break;
                        case 6:

                            break;
                    }
                    if (goToNext)
                        mViewPager.setCurrentItem(getItem(+1), true);
                }


                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
//        return true;
//    }

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


    private static class Holder {
        private int drawRes, strResTitle;

        Holder(int drawRes, int strResTitle) {
            this.drawRes = drawRes;
            this.strResTitle = strResTitle;
        }

        public int getDrawRes() {
            return drawRes;
        }

        public int getStrResTitle() {
            return strResTitle;
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
        private static final String ARG_TITLE_IMAGE = "title_image";
        private static final String ARG_TITLE_TEXT = "title_text";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            switch (sectionNumber) {
                case 1:
                    args.putInt(ARG_TITLE_IMAGE, R.drawable.icn_reg1);
                    args.putInt(ARG_TITLE_TEXT, R.string.reg_1);
                    break;
                case 2:
                    args.putInt(ARG_TITLE_IMAGE, R.drawable.icn_reg2);
                    args.putInt(ARG_TITLE_TEXT, R.string.reg_2);
                    break;
                case 3:
                    args.putInt(ARG_TITLE_IMAGE, R.drawable.icn_reg3);
                    args.putInt(ARG_TITLE_TEXT, R.string.reg_3);
                    break;
                case 4:
                    args.putInt(ARG_TITLE_IMAGE, R.drawable.icn_reg4);
                    args.putInt(ARG_TITLE_TEXT, R.string.reg_4);
                    break;
                case 5:
                    args.putInt(ARG_TITLE_IMAGE, R.drawable.icn_reg5);
                    args.putInt(ARG_TITLE_TEXT, R.string.reg_5);
                    break;
                case 6:
                    args.putInt(ARG_TITLE_IMAGE, R.drawable.icn_reg6);
                    args.putInt(ARG_TITLE_TEXT, R.string.reg_6);
                    break;
                case 7:
                    args.putInt(ARG_TITLE_IMAGE, R.drawable.icn_reg7);
                    args.putInt(ARG_TITLE_TEXT, R.string.reg_7);
                    break;
                case 8:
                    args.putInt(ARG_TITLE_IMAGE, R.drawable.icn_reg8);
                    args.putInt(ARG_TITLE_TEXT, R.string.reg_8);
                    break;
            }
            fragment.setArguments(args);
            return fragment;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    return inflater.inflate(R.layout.fragment_name, container, false);
                case 2:
                    return inflater.inflate(R.layout.fragment_gender, container, false);
                case 3:
                    return inflater.inflate(R.layout.fragment_birthday, container, false);
                case 4:
                    return inflater.inflate(R.layout.fragment_height, container, false);
                case 5:
                    return inflater.inflate(R.layout.fragment_weight, container, false);
                case 6:
                    return inflater.inflate(R.layout.fragment_activity, container, false);
                case 7:
                    return inflater.inflate(R.layout.fragment_sickness, container, false);
                case 8:
                    return inflater.inflate(R.layout.fragment_submit, container, false);
            }

            return null;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    final TextInputEditText editFirstName = (TextInputEditText) view.findViewById(R.id.edit_first_name);
                    final TextInputEditText editLastName = (TextInputEditText) view.findViewById(R.id.edit_last_name);
                    editFirstName.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));
                    editLastName.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));

                    editFirstName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!editFirstName.getText().toString().trim().equals("") && !editLastName.getText().toString().trim().equals(""))
                                mUser.setName(editFirstName.getText().toString().trim() + " " + editLastName.getText().toString().trim());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    editLastName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!editLastName.getText().toString().trim().equals("") && !editFirstName.getText().toString().trim().equals(""))
                                mUser.setName(editFirstName.getText().toString().trim() + " " + editLastName.getText().toString().trim());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    break;
                case 2:
                    final RadioButton radioFemale = ((RadioButton) view.findViewById(R.id.radio_female));
                    final RadioButton radioMale = ((RadioButton) view.findViewById(R.id.radio_male));
                    radioFemale.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));
                    radioMale.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));


                    radioFemale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mUser.setGender(1);
                        }
                    });

                    radioMale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mUser.setGender(0);
                        }
                    });


                    break;
                case 3:
                    final WheelView wheelYear = (WheelView) view.findViewById(R.id.wheel_year);
                    final WheelView wheelMonth = (WheelView) view.findViewById(R.id.wheel_month);
                    final WheelView wheelDay = (WheelView) view.findViewById(R.id.wheel_day);
//                    LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(SignUpActivity.getDisplayMetrics().widthPixels / 3, SignUpActivity.getDisplayMetrics().widthPixels / 5);
//                    wheelDay.setLayoutParams(layoutParams);
//                    wheelMonth.setLayoutParams(layoutParams);
//                    wheelYear.setLayoutParams(layoutParams);
                    mDay = wheelDay.getSelectedItem();
                    mMonth = wheelMonth.getSelectedItem();
                    mYear = wheelYear.getSelectedItem();

                    final List<String> yearList = new ArrayList();
                    for (int i = 1300; i <= 1396; i++)
                        yearList.add(String.valueOf(i));
                    wheelYear.setItems(yearList);
                    wheelYear.setLoopListener(new LoopScrollListener() {
                        @Override
                        public void onItemSelect(int i) {
                            mDay = Integer.valueOf(yearList.get(i));
                        }
                    });

                    wheelMonth.setItems(Arrays.asList(new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"}));
                    wheelMonth.setLoopListener(new LoopScrollListener() {
                        @Override
                        public void onItemSelect(int i) {
                            mMonth = i;
                        }
                    });


                    final List<String> dayList = new ArrayList<>();
                    for (int i = 1; i <= 31; i++)
                        dayList.add(String.valueOf(i));
                    wheelDay.setItems(dayList);
                    wheelDay.setLoopListener(new LoopScrollListener() {
                        @Override
                        public void onItemSelect(int i) {
                            mYear = Integer.valueOf(dayList.get(i));
                        }
                    });

                    break;

                case 4:
                    final EditText editHeight = (EditText) view.findViewById(R.id.edit_height);
                    editHeight.setWidth(mDisplayMetrics.widthPixels / 4);
                    editHeight.setHeight(mDisplayMetrics.widthPixels / 8);

                    editHeight.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() > 3) {
                                editHeight.setText(s.subSequence(0, 3));
                                editHeight.setSelection(3);
                            }

                            if (!editHeight.getText().toString().trim().equals(""))
                                mUser.setHeight(Float.valueOf(editHeight.getText().toString().trim()));


                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    break;
                case 5:
                    final EditText editWeight = (EditText) view.findViewById(R.id.edit_weight);
                    editWeight.setWidth(mDisplayMetrics.widthPixels / 4);
                    editWeight.setHeight(mDisplayMetrics.widthPixels / 8);

                    editWeight.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            if (!editWeight.getText().toString().trim().equals(""))
                                mUser.setWeight(Float.valueOf(editWeight.getText().toString().trim()));

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    break;
                case 6:
                    final ToggleButtonGroupTableLayout radioGroup = (ToggleButtonGroupTableLayout) view.findViewById(R.id.radio_group);
                    final RadioButton radioLow = (RadioButton) view.findViewById(R.id.radio_low);
                    final RadioButton radioVeryLow = (RadioButton) view.findViewById(R.id.radio_very_low);
                    final RadioButton radioNormal = (RadioButton) view.findViewById(R.id.radio_normal);
                    final RadioButton radioHigh = (RadioButton) view.findViewById(R.id.radio_high);
                    final RadioButton radioVeryHigh = (RadioButton) view.findViewById(R.id.radio_very_high);

                    radioVeryLow.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    radioVeryLow.setHeight(mDisplayMetrics.heightPixels / 15);

                    radioLow.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    radioLow.setHeight(mDisplayMetrics.heightPixels / 15);

                    radioNormal.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    radioNormal.setHeight(mDisplayMetrics.heightPixels / 15);

                    radioHigh.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    radioHigh.setHeight(mDisplayMetrics.heightPixels / 15);

                    radioVeryHigh.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    radioVeryHigh.setHeight(mDisplayMetrics.heightPixels / 15);


                    radioGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (((ToggleButtonGroupTableLayout) v).getActiveId()) {
                                case R.id.radio_very_low:
                                    mUser.setActivityLevel(1);
                                    break;
                                case R.id.radio_low:
                                    mUser.setActivityLevel(2);
                                    break;
                                case R.id.radio_normal:
                                    mUser.setActivityLevel(3);
                                    break;
                                case R.id.radio_high:
                                    mUser.setActivityLevel(4);
                                    break;
                                case R.id.radio_very_high:
                                    mUser.setActivityLevel(5);
                                    break;
                            }
                        }
                    });

                    break;

                case 7:
                    final CheckBox checkKidney = (CheckBox) view.findViewById(R.id.check_kidney);
                    final CheckBox checkHeart = (CheckBox) view.findViewById(R.id.check_heart);
                    final CheckBox checkFat = (CheckBox) view.findViewById(R.id.check_fat);
                    final CheckBox checkDiabetes = (CheckBox) view.findViewById(R.id.check_diabetes);

                    checkKidney.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    checkKidney.setHeight(mDisplayMetrics.heightPixels / 15);

                    checkHeart.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    checkHeart.setHeight(mDisplayMetrics.heightPixels / 15);

                    checkFat.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    checkFat.setHeight(mDisplayMetrics.heightPixels / 15);

                    checkDiabetes.setWidth((int) (mDisplayMetrics.widthPixels / 2.5));
                    checkDiabetes.setHeight(mDisplayMetrics.heightPixels / 15);

                    checkKidney.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    checkHeart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    checkFat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    checkDiabetes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });


                    break;
                case 8:
                    final TextInputEditText editPhone = (TextInputEditText) view.findViewById(R.id.edit_phone);
                    final TextInputEditText editPass = (TextInputEditText) view.findViewById(R.id.edit_password);

                    final JustifiedTextView txtPolicy = (JustifiedTextView) view.findViewById(R.id.txt_policy);
                    txtPolicy.setLineSpacing(20);
                    txtPolicy.setAlignment(Paint.Align.RIGHT);
                    txtPolicy.setText(R.string.policy);


                    editPhone.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));
                    editPass.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));
                    editPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_black_18dp, 0, 0, 0);
                    editPass.getCompoundDrawables()[0].setAlpha(45);
                    editPass.setLongClickable(false);

                    final boolean[] showPassword = {false};
                    editPass.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int DRAWABLE_LEFT = 0;
                            final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            final int DRAWABLE_BOTTOM = 3;
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (event.getX() <= (editPass.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()))          // your action here
                                {
                                    // changePasswordVisibility(v);
                                    String temporary_stored_text = editPass.getText().toString().trim();
                                    if (!showPassword[0]) {
                                        editPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_off_black_18dp, 0, 0, 0);
                                        editPass.setTransformationMethod(null);
                                        showPassword[0] = true;
                                    } else {
                                        editPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_black_18dp, 0, 0, 0);
                                        editPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                        showPassword[0] = false;
                                    }
                                    editPass.getCompoundDrawables()[DRAWABLE_LEFT].setAlpha(45);
                                    editPass.setText(temporary_stored_text);

                                    return true;
                                }
                            }
                            return false;
                        }
                    });

                    editPhone.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!editPhone.getText().toString().trim().equals(""))
                                mPhoneNumber = editPhone.getText().toString().trim();
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    editPass.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!editPass.getText().toString().trim().equals(""))
                                mPassword = editPass.getText().toString().trim();
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    break;
            }
            ((TextView) view.findViewById(R.id.txt_title)).setText(getString(getArguments().getInt(ARG_TITLE_TEXT)));
            ((ImageView) view.findViewById(R.id.img_title)).setImageResource(getArguments().getInt(ARG_TITLE_IMAGE));

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int mCount = 8;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 8 total pages.
            return mCount;
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
                case 3:
                    return "SECTION 4";
                case 4:
                    return "SECTION 5";
                case 5:
                    return "SECTION 6";
                case 6:
                    return "SECTION 7";
                case 7:
                    return "SECTION 8";

            }
            return null;
        }
    }
}
