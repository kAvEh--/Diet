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

public class HeightDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_setting_picker, null);

        final NumberPicker picker = (NumberPicker) v.findViewById(R.id.weight);
        picker.setMaxValue(250);
        picker.setMinValue(100);
        picker.setValue(height);
        picker.setWrapSelectorWheel(true);

        ((TextView) v.findViewById(R.id.title)).setText(R.string.title_height);

        builder.setView(v)
                .setPositiveButton("ثبت", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((SettingActivity) getActivity()).changeHight(String.valueOf(picker.getValue()));
                        HeightDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HeightDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private int height = 60;
    private UserInfo user;

    public void setHeight(int h) {
        height = h;
    }

    public void setUser(UserInfo u) {
        user = u;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("قد خود را وارد کنید");
    }
}