package com.example.sweater.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String text;
    private String tag;
    private String filename;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

    public Message(String text, String tag, User user) {
        this.text = text;
        this.tag = tag;
        this.author = user;
    }
}
