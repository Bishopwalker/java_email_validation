package com.example.Login.appuser;

import com.example.Login.dbConfig.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path="api/v1/app_user")
@AllArgsConstructor
public class AppUserController {
    public final AppUserService appUserService;

//    @GetMapping
//    public List<AppUser> getUsers() {
//        return appUserService.getUsers();
//    }

    @GetMapping(path="page")
    private ApiResponse<Page<AppUser>> getUsersPage(@RequestParam Map<String, String> allRequestParams) {
        int amount = Integer.parseInt(allRequestParams.get("amount"));
        int size = allRequestParams.get("size") == null ? 10 : Integer.parseInt(allRequestParams.get("size"));
       Page<AppUser> users =  appUserService.getUsersPage(amount, size);
        return new ApiResponse<>(users.getSize(), users);
    }



        @GetMapping
        private ApiResponse<List<AppUser>> getUsers() {
        List<AppUser> users = appUserService.getUsers();
        return new ApiResponse<>(users.size(), users);
        }

    @GetMapping(path="with_sorting")
    ApiResponse<List<AppUser>> getUsersWithSorting(@RequestParam Map<String,String> params){
        String field = params.get("field");
        String direction = params.get("direction");
       List<AppUser> users = appUserService.findUsersWithSorting(field, direction);
        return new ApiResponse<>(users.size(), users);
    }
}
