package com.example.userservice.cotroller;


import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.handler.AuthenticateHandlerRequest;
import com.example.userservice.model.News;
import com.example.userservice.security.JwtTokenProvider;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    protected PasswordEncoder passwordEncoder;



    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findDTOById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserDTO user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<UserDTO> authenticate(
            @RequestBody AuthenticateHandlerRequest user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.authenticate(user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }


    @GetMapping(value = "/getAuthenticatedUser")
    public String getAuthenticatedUser() {
        return userService.getUsername();
    }

    @PutMapping(value="/update_role")
    public ResponseEntity<String> editUserRole(@RequestParam Long id , @RequestParam String role){
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserRole(id,role));
    }

}
