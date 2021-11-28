package net.vespotok.fujin_domains.login_window;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("name")
public class LoginWindowController {

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLoginPage(ModelMap model){
        return "login";
    }

}
