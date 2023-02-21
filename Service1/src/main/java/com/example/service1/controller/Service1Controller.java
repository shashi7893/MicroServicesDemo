package com.example.service1.controller;

import com.example.service1.model.Person;
import com.example.service1.model.SubclassResponse;
import com.example.service1.model.User;
import com.example.service1.service.Service1Service;
import com.example.service1.util.LogMethodParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/service1")
public class Service1Controller {

    @Autowired
    Service1Service service1Service;

    @GetMapping("/getserverstatus")
    @ApiOperation(value = "gets the service status",
                    notes = "When this is invoked, it gives the service up status",
                    response = String.class
    )
    public String getServerStatus(){
        return service1Service.getServerStatus();
    }

    @PostMapping("/addressperson")
    public ResponseEntity<String> addressperson(@Valid @RequestBody Person person){
        return service1Service.addressPerson(person);
    }

    @PostMapping("/adduser")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user){
        return service1Service.addUser(user);
    }

    @LogMethodParam
    @GetMapping("/getuser/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable Long id){
        return service1Service.getUser(id);
    }

    @LogMethodParam
    @GetMapping("/getusers")
    public List<SubclassResponse> getAllUsers(){
        return service1Service.getAllUsers();
    }
}
