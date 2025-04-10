package com.nhnacademy.nhnmartservicecenter.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Inquiry {
    public enum category{
        Complain,
        Suggestion,
        RefundAndExchange,
        Praise,
        ETC
    }
    private long id;
    private String userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private category category;
    private String filePath;
    private Answer answer;

    public Inquiry(String userId, String title,String content, Inquiry.category category, String filePath) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.category = category;
        this.filePath = filePath;
        this.answer = null;
    }
    public Inquiry(long id,String userId,String title, String content, Inquiry.category category, String filePath) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.category = category;
        this.filePath = filePath;
        this.answer = null;
    }
}
