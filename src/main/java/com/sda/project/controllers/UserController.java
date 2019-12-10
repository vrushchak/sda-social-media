package com.sda.project.controllers;

import com.sda.project.entities.User;
import com.sda.project.entities.UserGender;
import com.sda.project.entities.UserStatus;
import com.sda.project.repositories.UsersRepository;
import com.sda.project.services.PostService;
import com.sda.project.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        //model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("genders", UserGender.values());
        return "user-add";
    }

    @PostMapping("/adduser")
    public String create(@Valid User newUser, Model model, BindingResult result, HttpSession session,
                         @RequestParam ("file") MultipartFile file) throws IOException {
        String resultFileName = "";
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
        }
        User savedUser = userService.createUser(newUser);
        savedUser.setProfilePicture(resultFileName);
        if(result.hasErrors()) {
            return "user-add";
        }
        else {

            session.setAttribute("user", savedUser);
            return "user-profile";
        }

    }

    @GetMapping("/friend/add/{friendId}")
    public String addFriend(@PathVariable("friendId") Integer friendId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.addFriend(user, friendId);
        model.addAttribute("users", userService.getActiveUsers());
        return "user-list";
    }

    @PostMapping("/login")
    public String login(Model model, User user, HttpSession session){
        User valUser = userService.validateUser(user);
        if(valUser == null) {
            return  "index";
        }
        else {
            session.setAttribute("user", valUser);
            model.addAttribute("posts", postService.getPostsByUserId(valUser.getUserId()));
            return "user-profile";
        }

    }

    @GetMapping("/delete")
    public String delete(HttpSession session){
        User user = (User) session.getAttribute("user");
        userService.deleteUser(user);
        return "redirect:index";
    }

    @GetMapping("/feed")
    public String goToFeed(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("posts", postService.getPostsForFeedByUserId(user.getUserId()));
        return "feed";
    }

    @GetMapping("/list-of-friends")
    public String goToFriendList(){
        return "friend-list";
    }

    @GetMapping("/list-of-users")
    public String goToListOfUsers(Model model){
        model.addAttribute("users", userService.getActiveUsers());
        return "user-list";
    }
}
