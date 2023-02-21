package com.example.service1.service;

import com.example.service1.model.Person;
import com.example.service1.model.SubclassResponse;
import com.example.service1.model.User;
import com.example.service1.repositories.UserRepository;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Service1Service {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(Service1Service.class);
    public String getServerStatus(){
        return "Up";
    }

    public ResponseEntity<String> addressPerson(Person person){
        String str = "";
        try {
            if(person.getName() == null || person.getSurname() == null || person.getName() == "" || person.getSurname() == ""){
                throw new InvalidFormatException("Input is not valid", person,Person.class);
            }

            JSONObject request = new JSONObject();
            request.put("name", person.getName());
            request.put("surname", person.getSurname());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

            String x = restTemplate.getForObject("http://second-service/service2/fetchstring", String.class);
            String y = restTemplate.postForObject("http://third-service/service3/postbody", entity, String.class);
            if(x == null || y == null){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Resource not Found");
            }
            str = x + " " + y;

            return new ResponseEntity<>(str, HttpStatus.OK);

        }catch(IllegalStateException ex){
            logger.info("Exception in addressPerson : " + ex);
            return new ResponseEntity<>(str, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(ResponseStatusException ex){
            logger.info("Exception in addressPerson : " + ex);
            return new ResponseEntity<>(str, HttpStatus.NOT_FOUND);
        } catch (InvalidFormatException ex) {
            logger.info("Exception in addressPerson : " + ex);
            return new ResponseEntity<>(str, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> addUser(User user){
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    public ResponseEntity<Optional<User>> getUser(Long id){
        Optional<User> user = userRepository.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    public static Map<Long, Map<String, Object>> buildNestedObject(List<Map<String, Object>> list, Long parentId) {
        Map<Long, Map<String, Object>> nestedObj = new HashMap<>();

        // Find all objects that have the specified parent ID
        List<Map<String, Object>> children = list.stream()
                .filter(obj -> obj.get("parentId").equals(parentId))
                .collect(Collectors.toList());

        // Recursively build nested object for each child
        children.forEach(child -> {
            Map<String, Object> childObj = new HashMap<>(child);
            nestedObj.put((Long) childObj.get("id"), childObj);
                                                        //.entrySet()
                                                        //.stream()
                                                        //.filter(x -> x.getKey() == "name" && x.getKey() == "subclasses")
                                                        //.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            childObj.put("subclasses", buildNestedObject(list, (Long) childObj.get("id")));
        });

        return nestedObj;
    }

    public List<SubclassResponse> formatTheResponse(List<Map<String, Object>> list) {
        List<SubclassResponse> responseList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            SubclassResponse response = formatTheResponseHelper(map);
            responseList.add(response);
        }
        return responseList;
    }

    public SubclassResponse formatTheResponseHelper(Map<String, Object> map) {
        SubclassResponse response = new SubclassResponse((String) map.get("name"));
        Map<String, Object> subclassesMap = (Map<String, Object>) map.get("subclasses");
        for (Map.Entry<String, Object> entry : subclassesMap.entrySet()) {
            Map<String, Object> subclassMap = (Map<String, Object>) entry.getValue();
            SubclassResponse subclass = formatTheResponseHelper(subclassMap);
            response.getSubclasses().add(subclass);
        }
        return response;
    }


    public List<SubclassResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        Integer x = 0;
        Long parentId = x.longValue();
        List<Map<String, Object>> list = new ArrayList<>();
        for(User user:users) {
            list.add(Map.of("id", user.getId(), "parentId", user.getParentId(), "name", user.getName()));
        }

        Map<Long, Map<String, Object>> nestedObj = buildNestedObject(list, parentId);

        List<Map<String, Object>> result = new ArrayList<>();
        for(Map.Entry<Long, Map<String, Object>> entry : nestedObj.entrySet()){
            result.add(entry.getValue());
        }

        //Process the response to be as per the required format
        List<SubclassResponse> responseList = formatTheResponse(result);

        return responseList;
    }
}
