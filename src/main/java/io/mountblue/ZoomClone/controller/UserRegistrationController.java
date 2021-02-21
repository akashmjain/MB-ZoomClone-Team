package io.mountblue.ZoomClone.controller;

import io.mountblue.ZoomClone.model.Users;
import io.mountblue.ZoomClone.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserRegistrationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @RequestMapping(value = "/showRegistrationForm", method = RequestMethod.GET)
    public String showRegistrationForm(Model theModel) {
        theModel.addAttribute("user", new Users());
        return "registration";
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public String registerUserAccount(@ModelAttribute("user") Users theUser) {
        userDetailsService.save(theUser);
        return "redirect:/login?success";
    }

}
