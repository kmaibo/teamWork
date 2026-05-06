package org.ted.teamworkbankappliaction.controller;

import org.springframework.web.bind.annotation.*;
import org.ted.teamworkbankappliaction.model.User;
import org.ted.teamworkbankappliaction.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable long id, @RequestBody User user) {
        User edit = userRepository.findById(id).orElse(null);
        edit.setEmail(user.getEmail());
        edit.setName(user.getName());
        edit.setPhone(user.getPhone());
        return userRepository.save(edit);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
    }
}
