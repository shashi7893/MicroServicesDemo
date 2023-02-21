package com.example.service3.services;

import org.springframework.stereotype.Service;

@Service
public class Service3Service{

    public String getConcatenatedName(String name, String surname){
        return name + " " + surname;
    }
}
