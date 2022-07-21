package pl.fis.lbd.springsecurityJWT.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @GetMapping
    public String getUser() {
        return "This is user!";
    }

    @PutMapping
    public String updateUser() {
        return "User updated!";
    }
}
