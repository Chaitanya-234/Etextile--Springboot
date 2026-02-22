package com.etextile.TextileECommerce.Exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle 404 - Resource Not Found
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundError(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "Page not found.");
        return "error-404"; // Returns a 404 error view
    }

    // Handle 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public String handleServerError(Exception ex, Model model) {
        model.addAttribute("message", "An unexpected error occurred.");
        return "error-500"; // Returns a 500 error view
    }

}