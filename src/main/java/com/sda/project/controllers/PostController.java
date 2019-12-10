package com.sda.project.controllers;

import com.sda.project.entities.Comment;
import com.sda.project.entities.Post;
import com.sda.project.entities.User;
import com.sda.project.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public String savePost(Post post, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        postService.savePost(post, user);
        model.addAttribute("posts", postService.getPostsByUserId(user.getUserId()));
        return  "user-profile";
    }

    @PostMapping("/{postId}/comment")
    public String saveComment(@PathVariable("postId") Integer postId, Comment comment, HttpSession session, Model model) {
        User loggedinUser = (User)session.getAttribute("user");
        postService.saveComment(postId, comment, loggedinUser);
        model.addAttribute("posts", postService.getPostsByUserId(loggedinUser.getUserId()));
        return "user-profile";
    }

    @GetMapping("/{postId}/like")
    public String saveLike(@PathVariable("postId") Integer postId, HttpSession session, Model model) {
        User loggedinUser = (User)session.getAttribute("user");
        postService.saveLike(postId, loggedinUser);
        model.addAttribute("posts", postService.getPostsByUserId(loggedinUser.getUserId()));
        return "user-profile";
    }
}
