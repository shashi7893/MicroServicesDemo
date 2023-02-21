package com.example.service2.controllers;

import com.example.service2.services.Service2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service2")
public class Service2Controller {
    @Autowired
    Service2Service service2Service;

    @GetMapping("/fetchstring")
    public ResponseEntity<String> fetchString(){
        return service2Service.fetchString();
    }
}
