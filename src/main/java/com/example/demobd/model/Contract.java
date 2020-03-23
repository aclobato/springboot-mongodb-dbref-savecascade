package com.example.demobd.model;

import com.example.demobd.mongodb.CascadeSave;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Contract {
    @Id
    private String id;

    private String name;

    @DBRef(lazy = true)
    @CascadeSave
    private List<User> users;

    @DBRef
    @CascadeSave
    private User admin;
}
