package com.example.enes_karabulut.library;

import java.util.HashMap;
import java.util.Map;


public class Book {

    public String id;
    public String title;
    public String author;
    public String year;
    public String details;
    public String book_image;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}