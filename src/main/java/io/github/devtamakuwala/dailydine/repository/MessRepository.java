package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.Mess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for Mess
 * */
@Repository
public interface MessRepository extends JpaRepository<Mess, Integer> {
    // Other mess-specific queries can go here
}
