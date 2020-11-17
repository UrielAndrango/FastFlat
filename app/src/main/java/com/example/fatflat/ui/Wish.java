package com.example.fatflat.ui;
//A
public class Wish {
    private String id;
    private String name;
    private Integer category;
    private String type;
    private String keywords;
    private Integer value;

    public Wish(){

    }

    public Wish(String id, String name, Integer category, String type, String keywords, Integer value) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.type = type;
        this.keywords = keywords;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
