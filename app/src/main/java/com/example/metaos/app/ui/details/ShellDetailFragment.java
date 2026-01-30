package com.example.metaos.app.ui.details;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.metaos.app.model.ConnectionState;
import com.example.metaos.app.viewmodel.ShellViewModel;
import com.example.metaos.app.R;
import com.example.metaos.app.viewmodel.ShellViewModelFactory;

public class ShellDetailFragment extends Fragment {
    private ShellViewModel viewModel;
    private TextView statusText;
    private View statusIndicator;
    private TextView consoleOutput;
    private ScrollView consoleScrollView;
    private EditText inputCommand;

    public ShellDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shell_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statusText = view.findViewById(R.id.text_connection_status);
        statusIndicator = view.findViewById(R.id.indicator_status);
        consoleOutput = view.findViewById(R.id.text_console_output);
        consoleScrollView = view.findViewById(R.id.scroll_console);
        inputCommand = view.findViewById(R.id.input_command);

        ShellViewModelFactory factory = new ShellViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(ShellViewModel.class);

        setupObservers();
        setupInputListener();

        viewModel.connect();
    }

    private void setupObservers() {
        viewModel.getConnectionState().observe(getViewLifecycleOwner(), state -> {
            updateConnectionUI(state);
        });

        viewModel.getCommandOutput().observe(getViewLifecycleOwner(), newText -> {
            if (newText != null && !newText.isEmpty()) {
                consoleOutput.append(newText);

                consoleScrollView.post(() ->
                        consoleScrollView.fullScroll(View.FOCUS_DOWN));
            }
        });
    }

    private void updateConnectionUI(ConnectionState state) {
        switch (state) {
            case CONNECTED:
                statusText.setText("Connected");
                statusIndicator.setBackgroundColor(Color.parseColor("#A3BE8C"));
                break;
            case CONNECTING:
                statusText.setText("Connecting...");
                statusIndicator.setBackgroundColor(Color.parseColor("#EBCB8B"));
                break;
            case ERROR:
                statusText.setText("Connection Failed");
                statusIndicator.setBackgroundColor(Color.parseColor("#BF616A"));
                break;
            default:
                statusText.setText("Offline");
                statusIndicator.setBackgroundColor(Color.parseColor("#D8DEE9"));
                break;
        }
    }

    private void setupInputListener() {
        inputCommand.setOnEditorActionListener((view, actionId, event) -> {
            boolean isEnterKey = (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN);

            if (actionId == EditorInfo.IME_ACTION_SEND || isEnterKey || actionId == EditorInfo.IME_ACTION_DONE) {
                String command = inputCommand.getText().toString();

                if (!command.trim().isEmpty()) {
                    viewModel.sendCommand(command);
                    consoleOutput.append("\n$ " + command + "\n");

                    inputCommand.setText("");
                }
                return true;
            }
            return false;
        });
    }
}
