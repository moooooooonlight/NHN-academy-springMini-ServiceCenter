package com.nhnacademy.nhnmartservicecenter.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Inquiry implements Comparable<Inquiry>{
    public enum category {
        Complain,
        Suggestion,
        RefundAndExchange,
        Praise,
        ETC
    }

    private long id;
    private String userId;
    private String userName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private category category;
    private List<String> filePaths; // 변경된 부분
    private Answer answer;

    public Inquiry(String userId, String userName, String title, String content, Inquiry.category category, List<String> filePaths) {
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.category = category;
        this.filePaths = filePaths;
        this.answer = null;
    }

    public Inquiry(long id, String userId, String userName, String title, String content, Inquiry.category category, List<String> filePaths, Answer answer) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.category = category;
        this.filePaths = filePaths;
        this.answer = answer;
    }

    @Override
    public int compareTo(Inquiry o) {
        return o.createdAt.compareTo(this.createdAt);
    }

}
