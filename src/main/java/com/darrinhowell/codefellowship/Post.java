package com.darrinhowell.codefellowship;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String body;
    public Date recordedAt;
    public ApplicationUser author;

    public Post() {}

    public Post(String body, Date recordedAt) {
        this.body = body;
        this.recordedAt = recordedAt;
    }
}
