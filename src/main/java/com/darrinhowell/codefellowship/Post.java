package com.darrinhowell.codefellowship;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Post {
    String body;
    Date recordedAt;
    ApplicationUser author;

    public Post() {}

    public Post(String body, Date recordedAt) {
        this.body = body;
        this.recordedAt = recordedAt;
    }
}
