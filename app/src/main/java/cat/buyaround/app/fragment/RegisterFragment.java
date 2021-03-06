package cat.buyaround.app.fragment;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import javax.inject.Inject;

import cat.buyaround.app.R;
import cat.buyaround.app.callback.RegisterCallback;
import cat.buyaround.app.databinding.FragmentRegisterBinding;
import cat.buyaround.app.factory.ViewModelFactory;
import cat.buyaround.app.viewmodel.RegisterViewModel;
import dagger.android.support.DaggerFragment;

public class RegisterFragment extends DaggerFragment implements RegisterCallback {

    @Inject
    protected ViewModelFactory viewModelFactory;
    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerViewModel = new ViewModelProvider(this, viewModelFactory).get(RegisterViewModel.class);

        initView();
    }

    private void initView() {
        binding.registerToLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });

        binding.registerBirthday.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        binding.registerBtn.setOnClickListener(v -> {
            registerUser();
        });

        binding.registerPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("ES"));

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            // +1 because January is zero
            binding.registerBirthday.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
        });

        newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
    }

    private void registerUser() {
        String name = binding.registerName.getText().toString().trim();
        String email = binding.registerMail.getText().toString().trim();
        String surnames = binding.registerSurname.getText().toString().trim();
        String phone = binding.registerPhone.getText().toString().trim();
        String birthday = binding.registerBirthday.getText().toString().trim();
        String password = binding.registerPassword.getText().toString();
        String passwordVerification = binding.registerPasswordVerification.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), R.string.register_empty_name, Toast.LENGTH_LONG).show();
            binding.registerName.requestFocus();

        } else if (surnames.isEmpty()) {
            Toast.makeText(getContext(), R.string.register_empty_surname, Toast.LENGTH_LONG).show();
            binding.registerSurname.requestFocus();

        } else if (birthday.isEmpty()) {
            Toast.makeText(getContext(), R.string.register_empty_birthday, Toast.LENGTH_LONG).show();

        } else if (email.isEmpty()) {
            Toast.makeText(getContext(), R.string.register_empty_email, Toast.LENGTH_LONG).show();
            binding.registerMail.requestFocus();

        } else if (phone.length() != 12) {
            Toast.makeText(getContext(), R.string.register_phone_not_valid, Toast.LENGTH_LONG).show();
            binding.registerPhone.requestFocus();

        } else if (password.isEmpty()) {
            Toast.makeText(getContext(), R.string.register_empty_password, Toast.LENGTH_LONG).show();
            binding.registerPassword.requestFocus();

        } else if (passwordVerification.isEmpty()) {
            Toast.makeText(getContext(), R.string.register_empty_password_verification, Toast.LENGTH_LONG).show();
            binding.registerMail.requestFocus();

        } else if (!password.equals(passwordVerification)) {
            Toast.makeText(getContext(), R.string.register_passwords_dont_match, Toast.LENGTH_LONG).show();
            binding.registerPassword.requestFocus();

        } else if (!registerViewModel.isValidEmail(email)) {
            Toast.makeText(getContext(), R.string.register_empty_email, Toast.LENGTH_LONG).show();
            binding.registerMail.requestFocus();

        } else if (!registerViewModel.isValidPassword(password)) {
            Toast.makeText(getContext(), R.string.register_password_not_secure, Toast.LENGTH_LONG).show();
            binding.registerPassword.requestFocus();

        } else if (!registerViewModel.isValidBirthday(birthday)) {
            Toast.makeText(getContext(), R.string.register_birthday_not_valid, Toast.LENGTH_LONG).show();

        } else {
            // TODO: THERE ARE MISSING PARAMETERS
            registerViewModel.register(name, surnames, birthday, email, phone, password, this);

        }
    }

    @Override
    public void onSuccess() {
        NavHostFragment.findNavController(this).popBackStack();
        Toast.makeText(getContext(), R.string.register_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(RegisterError registerError) {
        switch (registerError) {
            case NETWORK_ERROR:
                Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG).show();
                break;
            case INTERNAL_ERROR:
                Toast.makeText(getContext(), R.string.internal_error, Toast.LENGTH_LONG).show();
                break;
            case EXISTING_EMAIL:
                Toast.makeText(getContext(), R.string.existing_email, Toast.LENGTH_LONG).show();
                break;
            case WEAK_PASSWORD:
                Toast.makeText(getContext(), R.string.weak_password, Toast.LENGTH_LONG).show();
                break;
            case MISSING_PARAMETERS:
                Toast.makeText(getContext(), R.string.missing_parameters, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
