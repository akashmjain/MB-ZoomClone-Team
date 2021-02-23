package io.mountblue.ZoomClone.controller;

import io.mountblue.ZoomClone.model.Users;
import io.mountblue.ZoomClone.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Base64;

@Controller
public class LoginController {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @RequestMapping(value = {"/login", "/"})
    public String login(HttpSession httpSession) {
            return "login";
    }

    @RequestMapping(value = "/guest")
    public String guestJoining(@RequestParam(name = "path") String encryptedPath){
        Base64.Decoder decoder = Base64.getDecoder();
        String decryptedPath = new String(decoder.decode(encryptedPath));
        return "redirect:"+decryptedPath;
    }

    @RequestMapping(value = "/main")
    public String showDashBoard(@RequestParam(name = "sessionName", required = false) String sessionName,
                                HttpSession httpSession, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users theUser = userDetailsServiceImpl.findByEmail(authentication.getName());
        String fullName = theUser.getFirstName()+" "+theUser.getLastName();
        if ("anonymousUser".equals(fullName)) fullName = "guest";
        if ("guest".equals(fullName) && sessionName == null) return "redirect:/login";
        httpSession.setAttribute("loggedUser",authentication.getName());
        model.addAttribute("userName",authentication.getName());
        model.addAttribute("data", fullName);
        model.addAttribute("sessionName", sessionName);

        return "main";
    }
}

