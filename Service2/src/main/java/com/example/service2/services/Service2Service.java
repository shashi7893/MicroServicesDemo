package com.example.service2.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Service2Service {

    public ResponseEntity<String> fetchString(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
}
