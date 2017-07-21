package ir.eynakgroup.diet.activities.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.MainActivity;
import ir.eynakgroup.diet.activities.SetupDietActivity;
import ir.eynakgroup.diet.utils.view.JustifiedTextView;

/**
 * Created by Shayan on 5/1/2017.
 */

public class PreDietFragment extends Fragment {

    private Context mContext;
    private static PreDietFragment mPreDietFragmentInstance = null;

    private PreDietFragment(Context context) {
        mContext = context;
    }

    //    public static PreDietFragment getInstance(Context context) {
//        if (mPreDietFragmentInstance == null)
//            mPreDietFragmentInstance = new PreDietFragment(context);
//
//        return mPreDietFragmentInstance;
//  }
    public static PreDietFragment newInstance(Context context) {
        Bundle args = new Bundle();
        if (mPreDietFragmentInstance == null)
            mPreDietFragmentInstance = new PreDietFragment(context);

        mPreDietFragmentInstance.setArguments(args);
        return mPreDietFragmentInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prediet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button mButton = (Button) view.findViewById(R.id.btn_enter);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mButton.setWidth(displayMetrics.widthPixels / 2);
        mButton.setHeight(displayMetrics.heightPixels / 100);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, SetupDietActivity.class));
                ((Activity)mContext).startActivityForResult(new Intent(mContext, SetupDietActivity.class), MainActivity.SETUP_REQUEST_CODE);
            }
        });

        JustifiedTextView mJTv = (JustifiedTextView) view.findViewById(R.id.description);
        mJTv.setText(getString(R.string.trial_description));
        mJTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mJTv.setLineSpacing(15);
        mJTv.setAlignment(Paint.Align.RIGHT);

    }
}
