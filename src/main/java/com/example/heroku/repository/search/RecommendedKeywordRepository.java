package com.example.heroku.repository.search;

import com.example.heroku.entity.search.RecommendedKeywords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecommendedKeywordRepository extends JpaRepository<RecommendedKeywords, Long> {

    @Query("select r from RecommendedKeywords r where r.id= :id")
    RecommendedKeywords findByKeywordId(Long id);

    @Query("select r from RecommendedKeywords r where r.isSelected= :isSelected")
    List<RecommendedKeywords> findByKeywordStatus(boolean isSelected);

    @Query("select r from RecommendedKeywords r where r.recommendedKeyword= :recommendedKeyword")
    Optional<RecommendedKeywords> findByKeyword(String recommendedKeyword);

}
