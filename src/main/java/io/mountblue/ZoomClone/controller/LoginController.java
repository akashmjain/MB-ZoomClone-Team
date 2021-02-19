package io.mountblue.ZoomClone.controller;

import io.mountblue.ZoomClone.model.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping(value = {"/login", "/"})
    public String login(HttpSession httpSession) {
        if (httpSession == null || httpSession.getAttribute("loggedUser") == null) {
            httpSession.invalidate();
            return "login";
        } else {
            return "redirect:/dashboard";
        }
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String showDashBoard(@RequestParam(name = "sessonName", required = false) String sessonName,
                                HttpSession httpSession, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        httpSession.setAttribute("loggedUser",authentication.getName());
        model.addAttribute("username", authentication.getName());
        model.addAttribute("sessonName", sessonName);
        return "dashboard";
    }
}

