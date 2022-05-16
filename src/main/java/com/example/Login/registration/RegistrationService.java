package com.example.Login.registration;

import com.example.Login.appuser.AppUser;
import com.example.Login.appuser.AppUserRole;
import com.example.Login.appuser.AppUserService;
import com.example.Login.registration.token.ConfirmationToken;
import com.example.Login.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;

    public List<AppUser> getUsers() {
        return appUserService.getUsers();
    }

    public String register(RegistrationRequest request) {
        boolean isEmailValid = emailValidator.test(request.getEmail());
        if (!isEmailValid) {
            throw new IllegalArgumentException(format("Email is not valid %s", request.getEmail()));
        }
        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }

    public String confirmToken(String token) {
 ConfirmationToken confirmationToken = confirmationTokenService
         .getToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Token not found"));
    if(confirmationToken.getConfirmedAt() != null) {
        throw new IllegalArgumentException("Token already confirmed");
    }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if(LocalDateTime.now().isAfter(expiredAt)) {
            throw new IllegalArgumentException("Token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );
        return "Token confirmed";
    }
}
