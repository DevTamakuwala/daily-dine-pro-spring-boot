package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.Mess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessRepository extends JpaRepository<Mess, Integer> {
}
