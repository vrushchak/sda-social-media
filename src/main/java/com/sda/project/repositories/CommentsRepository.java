package com.sda.project.repositories;

import com.sda.project.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, Integer> {
}
