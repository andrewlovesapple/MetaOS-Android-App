package com.example.metaos.app.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.metaos.app.repository.ShellRepository;

public class ShellViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ShellViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ShellViewModel.class)) {

            ShellRepository repository = new ShellRepository(context);

            @SuppressWarnings("unchecked")
            T viewModel = (T) new ShellViewModel(repository);

            return viewModel;

        }
        throw new IllegalArgumentException("Unknown ViewModel class.");
    }
}
