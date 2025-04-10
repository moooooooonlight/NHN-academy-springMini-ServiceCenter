package com.nhnacademy.nhnmartservicecenter.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
public class Answer { private String content;
    private LocalDateTime creatAt;
    private User user;

    public Answer(String content, User user) {
        this.content = content;
        this.creatAt = LocalDateTime.now();
        this.user = user;
    }
}
