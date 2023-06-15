package pl.raportsa.timesheet.controller.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final static String PING_URL="/v1/api/ping";

    @GetMapping(PING_URL)
    public String ping (Authentication authentication){
       return  "Logged as " + authentication.getName();
    }
}
