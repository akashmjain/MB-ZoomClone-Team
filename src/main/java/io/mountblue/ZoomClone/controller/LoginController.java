package io.mountblue.ZoomClone.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping(value = {"/login", "/"})
    public String login(HttpSession httpSession) {
            return "login";
    }

    @RequestMapping(value = "/main")
    public String showDashBoard(@RequestParam(name = "sessionName", required = false) String sessionName,
                                HttpSession httpSession, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if ("anonymousUser".equals(userName)) userName = "guest";
        if ("guest".equals(userName) && sessionName == null) return "redirect:/login";
        httpSession.setAttribute("loggedUser",userName);
        model.addAttribute("data", userName);
        model.addAttribute("sessionName", sessionName);
        return "main";
    }
}

