package com.example.heroku.repository.boards;

import com.example.heroku.entity.boards.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
}
