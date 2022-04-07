package com.kits.company.adapter;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

public class myDialog extends AppCompatDialogFragment {
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {

        AlertDialog.Builder AlertDialog=new AlertDialog.Builder(requireActivity())
                .setTitle("title")
                .setMessage("message");
        return AlertDialog.create();
    }
}
