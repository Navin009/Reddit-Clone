package com.reddit.RedditClone.repository;

import com.reddit.RedditClone.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubredditId(Long id);

    @Query("SELECT p FROM Post p WHERE lower(p.title) LIKE %?1% OR " +
            "lower(p.author) LIKE %?1% OR lower(p.content) LIKE %?1%")
    List<Post> searchByString(String searchString);


    @Query("SELECT pp FROM Post pp WHERE pp.subredditId= ?1 order by pp.upVoteCount desc")
    List<Post> findAllPopularPostsBySubredditId(Long subredditId);

    List<Post> findBySubredditIdOrderByCreatedAtDesc(Long subredditId);

    List<Post> findBySubredditIdOrderByVoteCount(Long subredditId);

    @Query("SELECT p FROM Post p WHERE p.subredditId = ?1 AND p.upVoteCount > 10 AND p.downVoteCount > 10 ORDER BY p.voteCount ASC, p.upVoteCount DESC, p.downVoteCount DESC")
    List<Post> findControversialPosts(Long subredditId);

}
