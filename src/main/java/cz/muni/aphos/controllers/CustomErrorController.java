package cz.muni.aphos.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * The Custom error controller displays a customized error page.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Display an error page on the /error endpoint.
     *
     * @param request the request that ended with error
     * @param model   the model
     * @return the error.html template
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString();
        String message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
        model.addAttribute("status", status);
        if (status.equals("404")) {
            message = "Page not found";
        }
        model.addAttribute("message", message);
        return "error";
    }
}
