package ir.eynakgroup.diet.activities.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.eynakgroup.diet.R;


public class DietDishesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEAL_ID = "meal_id";
    private static final String RESOURCE_ID = "res_id";


    // TODO: Rename and change types of parameters
    private int mMealId;
    private int mLayoutRes;


    public DietDishesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mealId Parameter 1.
     * @return A new instance of fragment DietDishesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DietDishesFragment newInstance(int mealId, @NonNull @LayoutRes int layout) {
        DietDishesFragment fragment = new DietDishesFragment();
        Bundle args = new Bundle();
        args.putInt(MEAL_ID, mealId);
        args.putInt(RESOURCE_ID, layout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMealId = getArguments().getInt(MEAL_ID);
            mLayoutRes = getArguments().getInt(RESOURCE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(mLayoutRes, container, false);
    }

}
