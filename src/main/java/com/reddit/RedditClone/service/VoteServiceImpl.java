package com.reddit.RedditClone.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.User;
import com.reddit.RedditClone.model.Vote;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.UserRepository;
import com.reddit.RedditClone.repository.VoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public void saveVote(Vote vote) {
        User user = null;
        Post post = postRepository.getById(vote.getPostId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        user = userService.findUserByEmail(email);
        Optional<User> optionalUser = userRepository.findById(vote.getUserId());

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        if (user == null) {
            System.out.println("User not found!!");
            return;
        }

        vote.setUserId(user.getId());
        Vote voteByPostIdAndUserId = voteRepository.findByPostIdAndUserId(post.getId(), user.getId());

        if (voteByPostIdAndUserId != null) {
            if (vote.isUpVoted()) {
                if (voteByPostIdAndUserId.isDownVoted()) {
                    post.setDownVoteCount(post.getDownVoteCount() - 1);
                    post.setUpVoteCount(post.getUpVoteCount() + 1);
                    post.setVoteCount(post.getVoteCount() + 1);
                    voteByPostIdAndUserId.setUpVoted(false);
                    voteByPostIdAndUserId.setDownVoted(false);
                } else if (!voteByPostIdAndUserId.isUpVoted()) {
                    post.setUpVoteCount(post.getUpVoteCount() + 1);
                    post.setVoteCount(post.getVoteCount() + 1);
                    voteByPostIdAndUserId.setUpVoted(true);
                }

                voteRepository.save(voteByPostIdAndUserId);
            } else if (vote.isDownVoted()) {
                if (voteByPostIdAndUserId.isUpVoted()) {
                    post.setUpVoteCount(post.getUpVoteCount() - 1);
                    post.setDownVoteCount(post.getDownVoteCount() + 1);
                    post.setVoteCount(post.getVoteCount() - 1);
                    voteByPostIdAndUserId.setDownVoted(false);
                    voteByPostIdAndUserId.setUpVoted(false);
                } else if (!voteByPostIdAndUserId.isDownVoted()) {
                    post.setDownVoteCount(post.getDownVoteCount() + 1);
                    post.setVoteCount(post.getVoteCount() - 1);
                    voteByPostIdAndUserId.setDownVoted(true);
                }
                voteRepository.save(voteByPostIdAndUserId);
            }
        } else {
            vote.setContributed(true);
            if (vote.isUpVoted()) {
                post.setUpVoteCount(post.getUpVoteCount() + 1);
                post.setVoteCount(post.getVoteCount() + 1);
                vote.setUpVoted(true);
                vote.setDownVoted(false);
            } else if (vote.isDownVoted()) {
                post.setVoteCount(post.getVoteCount() - 1);
                post.setDownVoteCount(post.getDownVoteCount() + 1);
                vote.setDownVoted(true);
                vote.setUpVoted(false);
            }
            voteRepository.save(vote);
        }
        postRepository.save(post);
    }

    @Override
    public Map<Long, Map<Long, Vote>> getVotesByPosts(List<Post> posts) {
        List<Long> postIds = new ArrayList<>();
        for (Post post : posts) {
            postIds.add(post.getId());
        }
        List<Vote> votes = voteRepository.findVotesByPostId(postIds);
        Map<Long, Map<Long, Vote>> votesPostMap = new HashMap<>();

        for (Vote vote : votes) {
            Map<Long, Vote> votesUserMap = new HashMap<>();

            votesUserMap.put(vote.getUserId(), vote);
            votesPostMap.put(vote.getPostId(), votesUserMap);
        }
        return votesPostMap;
    }
}