package com.reddit.RedditClone.repository;

import java.util.List;

import com.reddit.RedditClone.model.Subreddit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

    @Query("SELECT s FROM Subreddit s WHERE lower(s.name) LIKE %?1% OR " +
            "lower(s.description) LIKE %?1%")
    List<Subreddit> searchByString(String searchString);

    @Query("SELECT s FROM Subreddit s join UserSubreddit us " +
            "on s.id = us.subredditId WHERE us.userId = ?1")
    List<Subreddit> searchByUser(Long id);

    List<Subreddit> findAllByCommunityTypeName(String communityType);

    Subreddit findByName(String subredditName);

    @Query("select s from Subreddit s, Subscription us where s.id = us.subredditId and us.userId = :userId")
    List<Subreddit> findUserSubreddits(Long userId);
}
