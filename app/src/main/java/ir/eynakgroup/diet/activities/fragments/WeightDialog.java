package ir.eynakgroup.diet.activities.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.SettingActivity;
import ir.eynakgroup.diet.database.tables.UserInfo;

/**
 * Created by Eynak_PC2 on 11/11/2017.
 */

public class WeightDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_weight, null);

        final EditText weight = (EditText) v.findViewById(R.id.input);
        weight.setText(String.valueOf(_w));
        weight.setSelection(weight.getText().length());

        ((TextView) v.findViewById(R.id.title)).setText(R.string.title_weight);

        builder.setView(v)
                .setPositiveButton("ثبت", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((SettingActivity) getActivity()).changeWeight(weight.getText().toString());
                        WeightDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WeightDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private float _w = 60;
    private UserInfo user;

    public void set_w(float w) {
        _w = w;
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