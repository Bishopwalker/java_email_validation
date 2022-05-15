package com.example.Login.registration;

import com.example.Login.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/registration")
@AllArgsConstructor
public class RegistrationController {


    private final RegistrationService registrationService;

    @GetMapping
    public List<AppUser> getUsers() {
        return registrationService.getUsers();
    }

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    }

