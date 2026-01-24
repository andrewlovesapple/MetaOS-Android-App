package com.example.metaos.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.metaos.app.model.ConnectionState;
import com.example.metaos.app.repository.ShellRepository;

public class ShellViewModel extends ViewModel {

    private final ShellRepository repository;
    private final LiveData<ConnectionState> connectionState;
    private final LiveData<String> commandOutput;


    public ShellViewModel(ShellRepository repository) {
        this.repository = repository;
        this.connectionState = repository.getConnectionState();
        this.commandOutput = repository.getCommandOutput();
    }

    public void connect() {
        repository.initiateConnection();
    }

    public void sendCommand(String command) {
        if (command != null && !command.trim().isEmpty()) {
            repository.sendCommand(command);
        }
    }

    public LiveData<ConnectionState> getConnectionState() {
        return connectionState;
    }

    public LiveData<String> getCommandOutput() {
        return commandOutput;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
