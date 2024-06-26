package com.dci.a3m.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String background;
    private String mediaUrl; // only photos because of a cloud storage

    // formatted Pattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime createdAt;


    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes;

    // Constructors
    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    public Post(String content, Member member) {
        this.content = content;
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }

    public Post(String content, String mediaUrl, Member member) {
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }

    public Post(String content, String mediaUrl, String background, Member member) {
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.background = background;
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }


    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtFormatted() {
        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Format the LocalDateTime object using the defined formatter
        return createdAt.format(formatter);
    }

    public String getCreatedAtFormattedOnlyDate() {
        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Format the LocalDateTime object using the defined formatter
        return createdAt.format(formatter);
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }


}
