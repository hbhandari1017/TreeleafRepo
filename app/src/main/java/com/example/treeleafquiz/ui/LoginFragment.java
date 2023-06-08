package com.example.treeleafquiz.ui;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.treeleafquiz.R;
import com.example.treeleafquiz.databinding.FragmentLoginBinding;
import com.example.treeleafquiz.util.QuizPreference;
import com.example.treeleafquiz.util.SoftKeyboardUtil;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {


    private FragmentLoginBinding loginBinding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        String userName = QuizPreference.getName();

        loginBinding.submitButton.setEnabled(false);
        if(!userName.isEmpty()){
            loginBinding.whatShouldTextView.setText(String.format("%s! we missed you", userName));
            loginBinding.submitButton.setText(R.string.let_s_play);
            loginBinding.nameEditText.setVisibility(View.INVISIBLE);
            loginBinding.submitButton.setEnabled(true);
        }
        return loginBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListeners();

    }

    private void initView() {
        loginBinding.userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputText = s.toString();
                if (inputText.length() >= 2) {
                    loginBinding.submitButton.setEnabled(true);
                } else {
                    loginBinding.submitButton.setEnabled(false);
                }
            }
        });

    }

    private void initListeners() {
        loginBinding.submitButton.setOnClickListener(v -> {
            SoftKeyboardUtil.hideSoftKeyboard(requireContext(), loginBinding.userNameEditText);
            String userName = loginBinding.userNameEditText.getText().toString();
            QuizPreference.setName(userName);
            showWelcomeDialog();
        });
    }
    private void showWelcomeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogCustom);
        builder.setTitle(R.string.quiz_title);
        builder.setMessage(R.string.quiz_description);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Activity activity = getActivity();

                // Check if the activity reference is not null and of the correct type
                if (activity instanceof MainActivity) {
                    MainActivity hostingActivity = (MainActivity) activity;
                     hostingActivity.moveToQuestionFragment();

                }
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requireActivity().finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}