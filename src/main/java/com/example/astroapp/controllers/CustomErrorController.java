package com.example.astroapp.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

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
