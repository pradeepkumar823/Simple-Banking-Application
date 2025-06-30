package com.bankapp.controller;

import com.bankapp.model.User;
import com.bankapp.model.BankAccount;
import com.bankapp.repository.UserRepository;
import com.bankapp.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // refers to register.html
    }

    // Handle form submission
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "redirect:/register?error"; // username taken
        }

        // Encode password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // ✅ Create a bank account with 0 balance for the user
        BankAccount account = new BankAccount(user.getUsername(), 0.0);
        bankAccountRepository.save(account);

        return "redirect:/login?registered";
    }
}
