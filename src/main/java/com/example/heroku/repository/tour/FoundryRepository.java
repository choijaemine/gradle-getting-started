package com.example.heroku.repository.tour;


import com.example.heroku.entity.tour.Foundry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface FoundryRepository extends JpaRepository<Foundry, Long> {
    @Query("select f from Foundry f where f.name = :name")
    Foundry findByFoundryName(String name);
}
