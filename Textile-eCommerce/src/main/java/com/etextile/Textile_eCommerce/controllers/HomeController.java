package com.etextile.Textile_eCommerce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etextile.Textile_eCommerce.Entities.Users;
import com.etextile.Textile_eCommerce.repositories.UserRepository;
import com.etextile.Textile_eCommerce.service.UserService;

import jakarta.validation.constraints.NotNull;

@RestController
public class HomeController {
    @Autowired
    private UserRepository ur;

    @Autowired
    private UserService us;

    @RequestMapping("home")
    public String home() {
        System.out.println("Thikeee");
        return "<h1> Hello Budddy </h1>";
    }

    @GetMapping("/test/{id}")
    public String testPost(@PathVariable @NotNull Integer id) {
        return "Hello World!" + id;
    }

    @PostMapping("/adduser")
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        ResponseEntity<?> rs = us.adduser(user);
        if (rs.getStatusCodeValue() == 200) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("Already exist");
        }
    }

    @GetMapping("/test")
    public String home(@RequestParam(name = "name") String name) {

        return "<h1>" + name + "</h1>";
    }

    @GetMapping("/find")
    public List<Users> findunser() {
        List<Users> li = (List<Users>) ur.findAllByUsername("Rama");
        return li;

    }

    @GetMapping("/findu")
    public Users finduser(@RequestParam(name = "uname") String name) {

        Users u = ur.findByUsername(name);
        return u;

    }

    @GetMapping("/findid")
    public Optional<Users> finduserid(@RequestParam(name = "id") String id) {

        Optional<Users> u = ur.findById(id);
        return u;

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password) {

        Users user = ur.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            String userId = user.getId();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

}
