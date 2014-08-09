package com.my.demonews.domain;

/**
 * Created by Administrator on 2014/8/9.
 */
public class NewInfo {

    private String title;
    private String detail;
    private Integer comment;
    private String img;

    public NewInfo(String title, String detail, Integer comment, String img) {
        this.title = title;
        this.detail = detail;
        this.comment = comment;
        this.img = img;
    }

    public NewInfo() {
    }

    @Override
    public String toString() {
        return "NewInfo{" +
                "title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", comment=" + comment +
                ", img='" + img + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
