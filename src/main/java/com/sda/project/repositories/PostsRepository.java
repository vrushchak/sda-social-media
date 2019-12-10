package com.sda.project.repositories;

import com.sda.project.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostsRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllByUserUserId(Integer userId);
    List<Post> findAllByUserUserIdIn(Set<Integer> userIds);
}
