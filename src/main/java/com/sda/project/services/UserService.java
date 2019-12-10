package com.sda.project.services;

import com.sda.project.entities.Friend;
import com.sda.project.entities.FriendStatus;
import com.sda.project.entities.User;
import com.sda.project.entities.UserStatus;
import com.sda.project.repositories.UsersRepository;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository userRepository;

    public User findUserById(Integer userId) {
        return  userRepository.findById(userId).get();
    }

    public User createUser(User user) {
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    public User validateUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if (!optionalUser.isPresent()
                || !optionalUser.get().getPassword().equals(user.getPassword())
                || optionalUser.get().getStatus() != UserStatus.ACTIVE
        ) {
            return null;
        }

        return optionalUser.get();
    }

    public List<User> getActiveUsers() {
        return userRepository.findAllByStatus(UserStatus.ACTIVE);
    }

    public User deleteUser(User user) {
        user.setStatus(UserStatus.INACTIVE);
        return userRepository.save(user);
    }

    public void addFriend(User firstUser, Integer secondUserId){
        User secondUser = userRepository.findById(secondUserId).get();
        Friend friend = new Friend();
        friend.setFirstUser(firstUser);
        friend.setSecondUser(secondUser);
        friend.setStatus(FriendStatus.APPROVED);
        firstUser.getFriends().add(friend);
        userRepository.save(firstUser);

        Friend friend2 = new Friend();
        friend2.setFirstUser(secondUser);
        friend2.setSecondUser(firstUser);
        friend2.setStatus(FriendStatus.APPROVED);
        secondUser.getFriends().add(friend2);
        userRepository.save(secondUser);
    }
}
