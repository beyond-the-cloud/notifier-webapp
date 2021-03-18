package edu.neu.csye7125.notifier.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    /**
     * HelloWorld GET Controller
     * Used for verify server is running, can also be used for health check or readiness check
     * @return
     */
    @GetMapping({"/v1/helloworld", "/v1/health"})
    public ResponseEntity healthCheck() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
