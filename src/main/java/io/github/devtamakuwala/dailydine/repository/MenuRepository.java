package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT m FROM Menu m WHERE m.expired = false")
    List<Menu> findAllWhereExpiredIsFalse();

    @Query("SELECT m FROM Menu m WHERE m.expired = TRUE")
    List<Menu> findAllWhereExpiredIsTrue();
}
