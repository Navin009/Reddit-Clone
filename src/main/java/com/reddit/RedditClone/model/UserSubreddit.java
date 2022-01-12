package com.reddit.RedditClone.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_subreddit")
@Getter
@Setter
public class UserSubreddit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", unique = false)
    private Long userId;
    @Column(name = "subreddit_id", unique = false)
    private Long subredditId;
}
