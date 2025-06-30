package com.bankapp.controller;

import com.bankapp.model.BankAccount;
import com.bankapp.repository.BankAccountRepository;
import com.bankapp.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;

@Controller
public class BankController {

    @Autowired
    private BankService service;

    @Autowired
    private BankAccountRepository bankAccountRepository; // ✅ FIXED

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        String username = principal.getName(); // fetch logged-in user's name
        BankAccount account = bankAccountRepository.findByUsername(username);

        if (account == null) {

            account = new BankAccount(username, 0.0);
            bankAccountRepository.save(account);
        }

        model.addAttribute("balance", account.getBalance());
        model.addAttribute("username", username);
        return "index";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam double amount, Principal principal) {
        service.deposit(principal.getName(), amount);
        return "redirect:/";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam double amount,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        boolean success = service.withdraw(principal.getName(), amount);

        if (!success) {
            redirectAttributes.addFlashAttribute("error", "❌ Insufficient balance!");
        }

        return "redirect:/";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
