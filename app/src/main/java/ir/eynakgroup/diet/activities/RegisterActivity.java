package ir.eynakgroup.diet.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.account.KarafsAccountConfig;
import ir.eynakgroup.diet.account.tasks.CreateAccountTask;
import ir.eynakgroup.diet.activities.fragments.DietFragment;
import ir.eynakgroup.diet.activities.fragments.ProfileFragment;
import ir.eynakgroup.diet.network.response_models.LoginResponse;
import ir.eynakgroup.diet.services.AuthenticationService;
import ir.eynakgroup.diet.utils.view.CustomViewPager;
import ir.eynakgroup.karafs.account.IAuthentication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Shayan on 2/7/2017.
 */
public class RegisterActivity extends BaseActivity {


    private static ImageView mBackImg;

    private ServiceConnection mConnection;
    private IAuthentication mService;

    private boolean boundService = false;

    private static final int INTRO_REQUEST_CODE = 123;
    private static final int SIGN_UP_REQUEST_CODE = 124;

    private static final String FRAGMENT_REGISTER = "register";
    private static final String FRAGMENT_LOGIN = "login";


    private PagerAdapter pagerAdapter;
    private static CustomViewPager viewPager;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = (CustomViewPager) findViewById(R.id.container_fragment);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);


        mBackImg = (ImageView) findViewById(R.id.back_step);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackFragment();
            }
        });

        int waiting;
        if (mAppPreferences.getFirstTime())
            waiting = 3000;
        else
            waiting = 2150;

        new CountDownTimer(waiting, waiting) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                viewPager.setAdapter(pagerAdapter);
                new BindServiceTask().execute();
                cancel();
            }
        }.start();

    }

    private void onBackFragment() {
//        if (mEnterBtn.getVisibility() == View.GONE) {
//            onBackPressed();
//            return;
//        }
//        mPassLayout.setVisibility(View.GONE);
//        mPhoneLayout.setVisibility(View.GONE);
//        mTxtForgot.setVisibility(View.GONE);
//        mEnterBtn.setVisibility(View.GONE);
//        mSignInBtn.setVisibility(View.VISIBLE);
//        mSignUpBtn.setVisibility(View.VISIBLE);
        mBackImg.setVisibility(View.GONE);
        viewPager.setCurrentItem(0, false);
    }

    private class BindServiceTask extends AsyncTask<Void, Void, Boolean> {
        protected void onPreExecute() {
            super.onPreExecute();

            if (mConnection == null) {
                mConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        if (mService == null)
                            mService = IAuthentication.Stub.asInterface(service);

                        Map accountProperty = null;
                        try {
                            accountProperty = mService.getAccountProperties();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        if (accountProperty != null && accountProperty.size() != 0) {
                            if (mAppPreferences.getFirstTime())
                                startActivityForResult(new Intent(RegisterActivity.this, IntroActivity.class), INTRO_REQUEST_CODE);
                            else{
                                System.out.println(accountProperty.get(KarafsAccountConfig.ACCOUNT_NAME));
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            }


                        } else {
                            if (mAppPreferences.getFirstTime())
                                startActivityForResult(new Intent(RegisterActivity.this, IntroActivity.class), INTRO_REQUEST_CODE);

                            else {

//                                if (mSignInBtn.getVisibility() == View.GONE) {
//                                    mSignInBtn.setVisibility(View.VISIBLE);
//                                    mSignUpBtn.setVisibility(View.VISIBLE);
//                                }
                                viewPager.setCurrentItem(0, false);

                            }


                        }
//                        releaseService();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        mService = null;
                        boundService = false;
                    }
                };
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return authenticate();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.d("bind", "initService() bound with " + result.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTRO_REQUEST_CODE:
                    mAppPreferences.setFirstTime(false);
//                    if(mSignInBtn.getVisibility() == View.GONE){
//                        mSignInBtn.setVisibility(View.VISIBLE);
//                        mSignUpBtn.setVisibility(View.VISIBLE);
//                    }
                    viewPager.setCurrentItem(0, false);

                    break;
                case SIGN_UP_REQUEST_CODE:
                    System.out.println("Account created!");
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                    break;

                default:

            }
        } else {
            switch (requestCode) {
                case INTRO_REQUEST_CODE:
                    finish();
                    break;
                case SIGN_UP_REQUEST_CODE:

                    break;

                default:
            }

        }

    }

    private boolean authenticate() {
        Intent serviceIntent = new Intent(getString(R.string.action_authentication));
        serviceIntent.setClass(this, AuthenticationService.class);
        List<ResolveInfo> intentServices = getPackageManager().queryIntentServices(serviceIntent, 0);
        if (intentServices != null && !intentServices.isEmpty())
            return boundService = bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        return false;
    }



    @Override
    protected void onPause() {
        super.onPause();
        releaseService();
    }


    /**
     * Unbinds this activity from the service.
     */
    private void releaseService() {
        if (mConnection != null && boundService) {
            unbindService(mConnection);
            mConnection = null;
//            mService = null;
//            boundService = false;
            Log.d("unbind", "releaseService() unbound.");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();

    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position + 1) {
                case 1:
                    return RegisterFragment.getInstance(mContext);

                case 2:
                    return LoginFragment.getInstance(mContext);

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return FRAGMENT_LOGIN;
                case 1:
                    return FRAGMENT_REGISTER;
            }
            return null;
        }
    }

    public static class RegisterFragment extends Fragment implements View.OnClickListener {

        private Button mSignUpBtn;
        private Button mSignInBtn;
        private Context mContext;


        private static RegisterFragment mRegisterFragmentInstance = null;

        private RegisterFragment(Context context) {
            mContext = context;
        }

        public static RegisterFragment getInstance(Context context) {
            if (mRegisterFragmentInstance == null)
                mRegisterFragmentInstance = new RegisterFragment(context);

            return mRegisterFragmentInstance;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_register, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mSignUpBtn = (Button) view.findViewById(R.id.btn_sign_up);
            mSignInBtn = (Button) view.findViewById(R.id.btn_sign_in_karafs);

            mSignUpBtn.setWidth(mDisplayMetrics.widthPixels / 2);
            mSignUpBtn.setHeight(mDisplayMetrics.heightPixels / 100);

            mSignInBtn.setWidth(mDisplayMetrics.widthPixels / 2);
            mSignInBtn.setHeight(mDisplayMetrics.heightPixels / 100);

            mSignInBtn.setOnClickListener(this);
            mSignUpBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sign_up:
                    startActivityForResult(new Intent(mContext, SignUpActivity.class), SIGN_UP_REQUEST_CODE);
                    break;
                case R.id.btn_sign_in_karafs:

                    RegisterActivity.viewPager.setCurrentItem(1, false);

                    mBackImg.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    }

//    private static class CustomFragment extends Fragment {
//        @Override
//        public void onDetach() {
//            super.onDetach();
//
//            try {
//                Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//                childFragmentManager.setAccessible(true);
//                childFragmentManager.set(this, null);
//
//            } catch (NoSuchFieldException e) {
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        @Override
//        public void onAttach(Context context) {
//            if(context != null)
//                super.onAttach(context);
//        }
//    }

    public static class LoginFragment extends Fragment implements View.OnClickListener {

        private Button mEnterBtn;

        private TextInputEditText mPassEdit;
        private TextInputEditText mPhoneEdit;
        private TextView mTxtForgot;

        private boolean showPassword = false;
        private Context mContext;

        private LoginFragment(Context context) {
            mContext = context;
        }

        private static LoginFragment mLoginFragmentInstance;

        public static LoginFragment getInstance(Context context) {
            if (mLoginFragmentInstance == null)
                mLoginFragmentInstance = new LoginFragment(context);

            return mLoginFragmentInstance;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_login, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            mPassEdit = (TextInputEditText) view.findViewById(R.id.edit_pass);
            mPassEdit.setLongClickable(false);
            mPassEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_black_18dp, 0, 0, 0);
            mPassEdit.getCompoundDrawables()[0].setAlpha(45);

            mPassEdit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getX() <= (mPassEdit.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()))          // your action here
                        {
                            // changePasswordVisibility(v);
                            String temporary_stored_text = mPassEdit.getText().toString().trim();
                            if (!showPassword) {
                                mPassEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_off_black_18dp, 0, 0, 0);
                                mPassEdit.setTransformationMethod(null);
                                showPassword = true;
                            } else {
                                mPassEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_black_18dp, 0, 0, 0);
                                mPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                showPassword = false;
                            }
                            mPassEdit.setText(temporary_stored_text);
                            mPassEdit.getCompoundDrawables()[DRAWABLE_LEFT].setAlpha(45);
                            return true;
                        }
                    }
                    return false;
                }
            });

            mPhoneEdit = (TextInputEditText) view.findViewById(R.id.edit_phone);
            mTxtForgot = (TextView) view.findViewById(R.id.txt_password_forgot);

            mEnterBtn = (Button) view.findViewById(R.id.btn_enter);


            mEnterBtn.setWidth(mDisplayMetrics.widthPixels / 2);
            mEnterBtn.setHeight(mDisplayMetrics.heightPixels / 100);

            mPhoneEdit.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));
            mPassEdit.setWidth((int) (mDisplayMetrics.widthPixels / 1.25));

            mTxtForgot.setOnClickListener(this);
            mEnterBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_enter:
                    final String phone = mPhoneEdit.getText().toString().trim();
                    final String pass = mPassEdit.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        mPhoneEdit.setError(getString(R.string.phone_input));
                        mPhoneEdit.requestFocus();
                        return;
                    }

                    if (!(phone.startsWith("09") && phone.length() == 11)) {
                        mPhoneEdit.setError(getString(R.string.phone_error));
                        mPhoneEdit.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(pass)) {
                        mPassEdit.setError(getString(R.string.pass_input));
                        mPassEdit.requestFocus();
                        return;
                    }

//                    if (pass.length() < 4 || (
//                            !pass.matches("^(.*[A-Za-z]+.*[0-9]+.*)$") && !pass.matches("^(.*[0-9]+.*[A-Za-z]+.*)$"))) {
//                        mPassEdit.setError(getString(R.string.pass_error));
//                        mPassEdit.requestFocus();
//                        return;
//                    }

                    Call<LoginResponse> call = mRequestMethod.login(phone, pass, "true");
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            new CreateAccountTask(mContext, getString(R.string.type_account)) {
                                @Override
                                protected void onPostExecute(Account result) {
                                    super.onPostExecute(result);
                                    System.out.println("Account created!");
                                    startActivity(new Intent(mContext, MainActivity.class));
                                    ((Activity) mContext).finish();

                                }
                            }
                                    .execute(new CreateAccountTask.AccountInfo(phone, pass, response.body().getUser()));

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            System.out.println("failed -----------");
                        }
                    });

                    break;
                case R.id.txt_password_forgot:
                    startActivity(new Intent(mContext, ForgotPassActivity.class));
                    break;
                default:
                    break;
            }
        }
    }

}
