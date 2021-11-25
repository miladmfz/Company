package com.kits.company.adapter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
