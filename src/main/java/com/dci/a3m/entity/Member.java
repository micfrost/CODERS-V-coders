package com.dci.a3m.entity;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Role = "ROLE_MEMBER";

    // Mapping with User
    @Valid
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private User user;

    // Mapping with Posts
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts;

    // Mapping with Comments
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // Mapping with Likes
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Like> likes;

    // Mapping with FriendshipInvitations
    @OneToMany(mappedBy = "invitingMember", cascade = CascadeType.ALL)
    private List<FriendshipInvitation> createdInvitations;

    @OneToMany(mappedBy = "acceptingMember", cascade = CascadeType.ALL)
    private List<FriendshipInvitation> receivedInvitations;



    // PERSONAL INFO
    private String firstName;
    private String lastName;

    // birthDate not in future
    @PastOrPresent(message = "Birth date can not be in the future")
    private LocalDate birthDate;

    private String profilePicture;


    // ADDRESS
    private String country;
    private String city;
    private String postalCode;
    private String phone;



    // METHODS

    // addPost - content only
    public void addPost(String content) {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(new Post(
                content,
                this));
    }

    // addPost with mediaUrl
    public void addPost(String content, String mediaUrl) {
     if (posts == null) {
         posts = new ArrayList<>();
     }
        posts.add(new Post(
                content,
                mediaUrl,
                this));
    }



    // removePost
    public void removePost(Post post) {
        if (posts != null) {
            posts.removeIf(p -> p.equals(post));
        }
    }

    // addComment
    public void addComment(String content, Post post) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(new Comment(
                content,
                this,
                post));
    }


    // removeComment
    public void removeComment(Comment comment) {
        if (comments != null) {
            comments.removeIf(c -> c.equals(comment));
        }
    }

    // addLike to a post
    public void addLike(Post post) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(new Like(
                this,
                post));
    }

    // addLike to a comment
    public void addLike(Comment comment) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(new Like(
                this,
                comment));
    }

    // removeLike from a post

    public void removeLike(Post post) {
        if (likes != null) {
            likes.removeIf(like -> like.getPost().equals(post));
        }
    }

    // removeLike from a comment
    public void removeLike(Comment comment) {
        if (likes != null) {
            likes.removeIf(like -> like.getComment().equals(comment));
        }
    }


    // CONSTRUCTORS
    public Member() {
    }

    public Member(String firstName, String lastName, LocalDate birthDate, String profilePicture, String city, String country, String postalCode, String phone) {


        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.profilePicture = profilePicture;


        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    // GETTERS AND SETTERS


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    // formatted birthdate as "dd-MM-yyyy"
    public String getFormattedBirthDate() {
        if(this.birthDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return birthDate.format(formatter);
    }

    //set formatted birthdate
    public void setFormattedBirthDate(String formattedBirthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.birthDate = LocalDate.parse(formattedBirthDate, formatter);
    }


    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
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

    public List<FriendshipInvitation> getCreatedInvitations() {
        return createdInvitations;
    }

    public void setCreatedInvitations(List<FriendshipInvitation> createdInvitations) {
        this.createdInvitations = createdInvitations;
    }

    public List<FriendshipInvitation> getReceivedInvitations() {
        return receivedInvitations;
    }

    public void setReceivedInvitations(List<FriendshipInvitation> receivedInvitations) {
        this.receivedInvitations = receivedInvitations;
    }



}
