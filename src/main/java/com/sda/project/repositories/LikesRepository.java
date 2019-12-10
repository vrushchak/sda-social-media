package com.sda.project.repositories;

import com.sda.project.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Like, Integer> {
}
