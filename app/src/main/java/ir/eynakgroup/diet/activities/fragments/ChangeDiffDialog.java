package ir.eynakgroup.diet.activities.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.SettingActivity;
import ir.eynakgroup.diet.database.tables.UserInfo;

/**
 * Created by kaveh on 11/16/2017.
 */

public class ActivityDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_setting_picker, null);

        final String[] datas = new String[5];
        datas[0] = "خیلی کم";
        datas[1] = "کم";
        datas[2] = "متوسط";
        datas[3] = "زیاد";
        datas[4] = "خیلی زیاد";

        final NumberPicker picker = (NumberPicker) v.findViewById(R.id.weight);
        picker.setMaxValue(4);
        picker.setMinValue(0);
        picker.setDisplayedValues(datas);
        picker.setValue(actLevel - 1);
        picker.setWrapSelectorWheel(false);

        ((TextView) v.findViewById(R.id.title)).setText(R.string.title_activity);

        builder.setView(v)
                .setPositiveButton("ثبت", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((SettingActivity) getActivity()).changeActivity(picker.getValue() + 1);
                        ActivityDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private int actLevel = 1;
    private UserInfo user;

    public void setActLevel(int w) {
        actLevel = w;
    }

    public void setUser(UserInfo u) {
        user = u;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("وزن خود را وارد کنید");
    }
}