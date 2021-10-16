package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Post;

import java.util.List;

public interface PostService {

    Post savePost(Post post);

    List<Post> getAllPosts();

    List<Post> getBySubRedditId(Long id);

    Post getPostById(Long postId);

    void updatePostById(Post post);

    void deleteById(Long postId);
}
