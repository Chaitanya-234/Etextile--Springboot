package com.etextile.Textile_eCommerce.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etextile.Textile_eCommerce.Entities.TempUser;
import com.etextile.Textile_eCommerce.Entities.Users;
import com.etextile.Textile_eCommerce.repositories.TempUserRepo;
import com.etextile.Textile_eCommerce.repositories.UserRepository;
import com.etextile.Textile_eCommerce.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService us;
    @Autowired
    UserRepository ur;
    @Autowired
    TempUserRepo tur;

    @PostMapping("/adduser")
    public ResponseEntity<?> adduser(@Valid @RequestBody Users u) {
        if (!us.checkuser(u)) {
            ur.save(u);
            return ResponseEntity.ok("User Created sucessfully");

        } else {
            return ResponseEntity.badRequest().body("Unable to createUser already Exist");
        }
    }

    @PostMapping("/addtempuser")
    public void addtemp() throws IOException {
        TempUser u = new TempUser();
        u.setUsername("Ramesh");
        FileInputStream fi = new FileInputStream("src/main/java/background.jpg");
        byte im[] = new byte[fi.available()];
        fi.read(im);
        u.setImage(im);
        tur.save(u);
        fi.close();

    }

    @GetMapping("/image/{id}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("id") Long id) {
        TempUser user = tur.findById(id).orElse(null);

        if (user != null && user.getImage() != null) {
            ByteArrayResource resource = new ByteArrayResource(user.getImage());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"image.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/findid")
    public ResponseEntity<Users> findUserById(@RequestParam("id") String id) {
        Optional<Users> user = ur.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
