package com.reddit.RedditClone.controller;

import java.util.List;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.repository.CommentRepository;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.service.CommentService;
import com.reddit.RedditClone.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController {

    @Autowired
    public CommentService commentService;

    @Autowired
    public PostRepository postRepo;

    @Autowired
    public UserService userService;

    @Autowired
    public CommentRepository commentRepository;


    @ResponseBody
    public List<Comment> getComments (@PathVariable Long postId) {
        List<Comment> findByPostId = commentService.findByPostId(postId);
        return findByPostId;
    }

    @PostMapping("/saveComment/{postId}/comments")
    public String postComment(@PathVariable Long postId, Comment rootComment,
                              @RequestParam(required=false) Long parentId,
                              @RequestParam(required=false) String childCommentText) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }


        commentService.saveComment(postId, rootComment, parentId, childCommentText);
        return "redirect:/viewPost/" + postId;
    }

    @RequestMapping("/deleteComment/{id}")
    private String deleteComment(@PathVariable Long id){
        Comment comment = commentService.getById(id);
        comment.setComment(null);
        commentRepository.save(comment);
        commentService.deleteById(id);
        return "redirect:/viewPost/" + comment.getPost().getId();
    }


}
