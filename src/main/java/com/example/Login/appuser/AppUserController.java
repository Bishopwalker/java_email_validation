package com.example.Login.appuser;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/app_user")
@AllArgsConstructor
public class AppUserController {
    public final AppUserService appUserService;

    @GetMapping
    public List<AppUser> getUsers() {
        return appUserService.getUsers();
    }

    @GetMapping(path="with_sorting")
    public List<AppUser> getUsersWithSorting(@RequestParam("field") String field) {
        return appUserService.findUsersWithSorting(field);
    }
}
