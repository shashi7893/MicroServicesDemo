package com.example.service1.model;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class SubclassResponse {
    private String name;
    private List<SubclassResponse> subclasses;

    public SubclassResponse(String name) {
        this.name = name;
        this.subclasses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<SubclassResponse> getSubclasses() {
        return subclasses;
    }
}
