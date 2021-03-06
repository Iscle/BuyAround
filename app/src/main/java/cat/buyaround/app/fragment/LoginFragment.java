package cat.buyaround.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import javax.inject.Inject;

import cat.buyaround.app.R;
import cat.buyaround.app.callback.LoginCallback;
import cat.buyaround.app.databinding.FragmentLoginBinding;
import cat.buyaround.app.factory.ViewModelFactory;
import cat.buyaround.app.viewmodel.LoginViewModel;
import dagger.android.support.DaggerFragment;

public class LoginFragment extends DaggerFragment implements LoginCallback {

    @Inject
    protected ViewModelFactory viewModelFactory;
    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        binding.loginToRegister.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections
                    .actionLoginFragmentToRegisterFragment();
            Navigation.findNavController(v).navigate(action);
        });

        binding.loginBtn.setOnClickListener(v -> {
            String username = binding.loginMail.getText().toString().trim();
            String password = binding.loginPassword.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(getContext(), R.string.register_empty_email, Toast.LENGTH_LONG).show();
                binding.loginMail.requestFocus();
            } else if (password.isEmpty()) {
                Toast.makeText(getContext(), R.string.register_empty_password, Toast.LENGTH_LONG).show();
                binding.loginPassword.requestFocus();
            } else if (!loginViewModel.isValidEmail(username)) {
                Toast.makeText(getContext(), R.string.register_empty_email, Toast.LENGTH_LONG).show();
                binding.loginMail.requestFocus();
            } else if (!loginViewModel.isValidPassword(password)) {
                Toast.makeText(getContext(), R.string.wrong_password, Toast.LENGTH_LONG).show();
                binding.loginPassword.requestFocus();
            } else {
                loginViewModel.login(username, password, this);
            }
        });

        binding.forgotPasswordTv.setOnClickListener(v -> {
            // TODO: GO TO FORGOT PASSWORD FRAGMENT, WHICH REQUESTS MAIL AND MAKES API CALL
        });
    }

    @Override
    public void onSuccess() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onFailure(LoginError loginError) {
        switch (loginError) {
            case NETWORK_ERROR:
                Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG).show();
                break;
            case INTERNAL_ERROR:
                Toast.makeText(getContext(), R.string.internal_error, Toast.LENGTH_LONG).show();
                break;
            case WRONG_PASSWORD:
                Toast.makeText(getContext(), R.string.wrong_password, Toast.LENGTH_LONG).show();
                break;
            case MISSING_PARAMETERS:
                Toast.makeText(getContext(), R.string.missing_parameters, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
