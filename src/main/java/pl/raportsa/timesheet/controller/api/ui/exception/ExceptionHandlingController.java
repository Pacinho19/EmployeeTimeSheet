package pl.raportsa.timesheet.controller.api.ui.exception;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import pl.raportsa.timesheet.model.exceptions.SignatureNotFoundException;
import pl.raportsa.timesheet.service.MonthlyReportGenerator;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(SecurityException.class)
    public ModelAndView securityException(SecurityException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("gif", "https://thumbs.gfycat.com/ClearTallKookaburra-size_restricted.gif");
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(SignatureNotFoundException.class)
    public ModelAndView signatureNotFoundException(SignatureNotFoundException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

}
