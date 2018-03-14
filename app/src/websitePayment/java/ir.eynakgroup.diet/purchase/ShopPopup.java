package ir.eynakgroup.caloriemeter.payment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import ir.eynakgroup.caloriemeter.R;
import ir.eynakgroup.caloriemeter.shop.AbstractPurchase;
import ir.eynakgroup.caloriemeter.shop.BaseShopActivity;
import ir.eynakgroup.caloriemeter.util.StaticMethods;

/**
 * Created by eynak on 5/28/16.
 */
public class ShopPopup extends DialogFragment {

    Button monthPurchase;
    Button seasonPurchase;
    Button twoSeasonPurchase;
    Button lifetimePurchase;

    private AbstractPurchase mPurchaser;
    View.OnClickListener onPurchaseButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPurchaser.launchPurchaseFlow(BaseShopActivity.skuMap.get(v.getTag().toString()), null);
            dismiss();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.popup_shop, null);
        monthPurchase = v.findViewById(R.id.purchase_month_btn);
        seasonPurchase = v.findViewById(R.id.purchase_season_btn);
        twoSeasonPurchase = v.findViewById(R.id.purchase_two_season_btn);
        lifetimePurchase = v.findViewById(R.id.purchase_lifetime_btn);

        mPurchaser = new PaymentImpl(getActivity());

        monthPurchase.setOnClickListener(onPurchaseButtonClicked);
        seasonPurchase.setOnClickListener(onPurchaseButtonClicked);
        twoSeasonPurchase.setOnClickListener(onPurchaseButtonClicked);
        lifetimePurchase.setOnClickListener(onPurchaseButtonClicked);

        StaticMethods.overrideFonts(v, StaticMethods.getTypeface(getActivity(), StaticMethods.fontsYekan));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        float widthPec = .75f;
        p.width = (int) (width * widthPec);
        getDialog().getWindow().setAttributes(p);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public interface ShopPopupClicks {
        void onPopupPurchaseClick(String sku);
    }
}
