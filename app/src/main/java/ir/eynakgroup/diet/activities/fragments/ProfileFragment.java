package ir.eynakgroup.diet.activities.fragments;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import java.sql.SQLException;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.database.DatabaseHelper;
import ir.eynakgroup.diet.database.tables.UserInfo;
import ir.eynakgroup.diet.network.response_models.User;
import ir.eynakgroup.diet.utils.view.CircularImageView;
import ir.eynakgroup.diet.utils.view.CustomTextView;

public class ProfileFragment extends Fragment {

    private Context mContext;
    private static ProfileFragment mProfileFragmentInstance = null;
    public final static String TAG = "FRAGMENT_PROFILE";

    private ProfileFragment(Context context) {
        mContext = context;
    }

    public static ProfileFragment newInstance(Context context) {
        if (mProfileFragmentInstance == null)
            mProfileFragmentInstance = new ProfileFragment(context);

        return mProfileFragmentInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final CircularImageView imageProfile = (CircularImageView) view.findViewById(R.id.img_profile);
        final TextView textName = (CustomTextView) view.findViewById(R.id.txt_name);
        final TextView textList = (CustomTextView) view.findViewById(R.id.txt_list);
        final TextView textCredit = (CustomTextView) view.findViewById(R.id.txt_credit);
        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.diet_picker);
        picker.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans_light.ttf"));

        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        try {
            UserInfo user = databaseHelper.getUserDao().queryForAll().get(0);
            textName.setText(user.getName());
            textCredit.setText(user.getCredit()+"");
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
//                    // Collapsed
//
//
//                } else if (verticalOffset == 0) {
//                    // Expanded
//
//
//                }
//
//            }
//        });

        ColorFilter filter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            filter = new LightingColorFilter(getResources().getColor(R.color.colorWhite, getActivity().getTheme()), getResources().getColor(R.color.colorWhite, getActivity().getTheme()));

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            filter = new LightingColorFilter(getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorWhite));
        }
        textList.getCompoundDrawables()[0].setColorFilter(filter);


    }
}
