package com.darrinhowell.codefellowship;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String body;
    public Date recordedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public ApplicationUser user;

    public Post() {}

    public Post(String body, Date recordedAt) {
        this.body = body;
        this.recordedAt = recordedAt;
    }

    public String toString() {
        return this.body;
    }
}
