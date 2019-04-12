package com.example.android.campusconnect;

public class Notice {
    String title;
    String body;
    String noticeTime;

    public Notice(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Notice(String title, String body, String noticeTime) {
        this.title = title;
        this.body = body;
        this.noticeTime = noticeTime;
    }

    public Notice() {
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
