package com.nhnacademy.nhnmartservicecenter.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
public class Answer {
    private String title;
    private String content;
    private LocalDateTime localDateTime;
    private User user;

    public Answer(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.localDateTime = LocalDateTime.now();
        this.user = user;
    }
}
