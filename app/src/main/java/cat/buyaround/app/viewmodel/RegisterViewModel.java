package cat.buyaround.app.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.ViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import cat.buyaround.app.callback.RegisterCallback;
import cat.buyaround.app.model.User;
import cat.buyaround.app.network.BuyAroundRepository;

public class RegisterViewModel extends ViewModel {

    private BuyAroundRepository buyAroundRepository;

    @Inject
    public RegisterViewModel(BuyAroundRepository buyAroundRepository) {
        this.buyAroundRepository = buyAroundRepository;
    }

    public void register(String name, String surnames, String birthday, String email, String phone, String password, RegisterCallback registerCallback) {
        try {
            Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthday);
            User user = new User(name, surnames, birthDate, email, phone, password);
            buyAroundRepository.register(user, registerCallback);
        } catch (ParseException e) {
            registerCallback.onFailure(RegisterCallback.RegisterError.INVALID_BIRTHDAY);
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isValidPassword(String text) {
        return text.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$");
    }

    public boolean isValidBirthday(String birthday) {
        try {
            Calendar birthdayDate = Calendar.getInstance();
            birthdayDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(birthday));
            Calendar currentDate = Calendar.getInstance();

            int age = currentDate.get(Calendar.YEAR) - birthdayDate.get(Calendar.YEAR);
            if (currentDate.get(Calendar.MONTH) < birthdayDate.get(Calendar.MONTH)) {
                age--;
            } else if (currentDate.get(Calendar.MONTH) == birthdayDate.get(Calendar.MONTH)
                    && currentDate.get(Calendar.DAY_OF_MONTH) < birthdayDate.get(Calendar.DAY_OF_MONTH)) {
                age--;
            }

            return age >= 18;
        } catch (ParseException e) {
            return false;
        }
    }
}
