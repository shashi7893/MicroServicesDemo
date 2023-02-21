package com.example.service3.controllers;

import com.example.service3.models.Person;
import com.example.service3.services.Service3Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/service3")
public class Service3Controller {
    @Autowired
    Service3Service service3Service;
    @Autowired
    ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(Service3Service.class);

    @PostMapping("/postbody")
    public String getConcatenatedName(@Valid @RequestBody Person person){
        // convert the object to a JSON string
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(person);
        } catch (JsonProcessingException e) {
            logger.error("Error converting object to JSON", e);
        }

        // log the JSON string
        logger.info("Received json payload: {}", jsonString);
        return service3Service.getConcatenatedName(person.getName(), person.getSurname());
    }
}
