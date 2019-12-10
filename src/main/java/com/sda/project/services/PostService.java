package com.sda.project.services;

import com.sda.project.entities.*;
import com.sda.project.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserService userService;


    public List<Post> getPostsByUserId(Integer userId) {
        return postsRepository.findAllByUserUserId(userId);
    }

    public List<Post> getPostsForFeedByUserId(Integer userId) {
        User user = userService.findUserById(userId);
        Set<Integer> userIds = new HashSet<>();
        userIds.add(userId);
        for (Friend friend : user.getFriends()) {
            userIds.add(friend.getSecondUser().getUserId());
        }
        return postsRepository.findAllByUserUserIdIn(userIds);
    }

    /*public LocalDateTime formatDate(LocalDateTime givenDate){
    }*/

    public void savePost(Post post, User user) {
        post.setDate(LocalDateTime.now());
        post.setUser(user);
        postsRepository.save(post);
    }

    public Post getPostById(Integer postId) {
        return  postsRepository.findById(postId).get();
    }

    public void saveComment(Integer postId, Comment comment, User user) {
        Post post = getPostById(postId);
        comment.setDate(LocalDateTime.now());
        comment.setPost(post);
        comment.setUser(user);
        post.getComments().add(comment);
        postsRepository.save(post);
    }

    public void saveLike(Integer postId, User user){
        Post post = getPostById(postId);
        boolean alreadyLiked = false;
        for (Like l : post.getLikes()) {
            if (l.getUser().getUserId() == user.getUserId()) {
                post.getLikes().remove(l);
                alreadyLiked = true;
                break;
            }
        }

        if(!alreadyLiked) {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            post.getLikes().add(like);
        }

        postsRepository.save(post);
    }
}
