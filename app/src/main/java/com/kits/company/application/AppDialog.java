package com.kits.company.application;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AppDialog extends AppCompatDialogFragment {
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {

        return  new AlertDialog.Builder(requireContext())
                .setTitle("توجه")
                .setMessage("ورژن نسخه مورد استفاده قدیمی می باشد")
                .setNegativeButton("رد کردن", (dialogInterface, i) -> { })
                .create();
    }
















}
