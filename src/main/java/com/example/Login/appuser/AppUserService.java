package com.example.Login.appuser;

import com.example.Login.registration.token.ConfirmationToken;
import com.example.Login.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final static String USER_NOT_MSG = "User with email %s not found";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_MSG, email)));
    }

    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }

    public String signUpUser(AppUser appUser) {
        appUserRepository.findUserByEmail(appUser.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException(String.format("User with email %s already exists", appUser.getEmail()));
        });
        String encodedPassword= bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().plusMinutes(15),
                LocalDateTime.now(),
                appUser

        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //TODO: send email

        return token;
    }

    public void enableAppUser(String email) {
        appUserRepository.findUserByEmail(email).ifPresent(u -> {
            u.setEnabled(true);
            appUserRepository.save(u);
        });
    }
}
