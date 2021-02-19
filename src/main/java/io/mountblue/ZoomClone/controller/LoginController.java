package io.mountblue.ZoomClone.controller;

import io.mountblue.ZoomClone.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @RequestMapping(value = "/dashboard", method = {RequestMethod.GET, RequestMethod.POST})
    public String showDashBoard(HttpSession httpSession, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        httpSession.setAttribute("loggedUser", (UserPrincipal)authentication.getPrincipal());
        model.addAttribute("username", authentication.getName());
        return "dashboard";
    }
}
/*

    public class MyUser {

        String name;
        String pass;
        OpenViduRole role;

        public MyUser(String name, String pass, OpenViduRole role) {
            this.name = name;
            this.pass = pass;
            this.role = role;
        }
    }

    public static Map<String, MyUser> users = new ConcurrentHashMap<>();

    public LoginController() {
        users.put("publisher1", new MyUser("publisher1", "pass", OpenViduRole.PUBLISHER));
        users.put("publisher2", new MyUser("publisher2", "pass", OpenViduRole.PUBLISHER));
        users.put("subscriber", new MyUser("subscriber", "pass", OpenViduRole.SUBSCRIBER));
    }

    @RequestMapping(value = "/")
    public String logout(HttpSession httpSession) {
        if (checkUserLogged(httpSession)) {
            return "redirect:/dashboard";
        } else {
            httpSession.invalidate();
            return "index";
        }
    }

    @RequestMapping(value = "/dashboard", method = { RequestMethod.GET, RequestMethod.POST })
    public String login(@RequestParam(name = "user", required = false) String user,
                        @RequestParam(name = "pass", required = false) String pass, Model model, HttpSession httpSession) {

        // Check if the user is already logged in
        String userName = (String) httpSession.getAttribute("loggedUser");
        if (userName != null) {
            // User is already logged. Immediately return dashboard
            model.addAttribute("username", userName);
            return "dashboard";
        }

        // User wasn't logged and wants to
        if (login(user, pass)) { // Correct user-pass

            // Validate session and return OK
            // Value stored in HttpSession allows us to identify the user in future requests
            httpSession.setAttribute("loggedUser", user);
            model.addAttribute("username", user);

            // Return dashboard.html template
            return "dashboard";

        } else { // Wrong user-pass
            // Invalidate session and redirect to index.html
            httpSession.invalidate();
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    private boolean login(String user, String pass) {
        return (user != null && pass != null && users.containsKey(user) && users.get(user).pass.equals(pass));
    }

    private boolean checkUserLogged(HttpSession httpSession) {
        return !(httpSession == null || httpSession.getAttribute("loggedUser") == null);
    }*/
