package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface defines the repository for the User entity.
 * By extending JpaRepository, it inherits a wealth of CRUD (Create, Read, Update, Delete)
 * and pagination/sorting methods for the User entity without requiring any implementation.
 *
 * The generic types are:
 * - User: The domain type that this repository manages.
 * - Integer: The data type of the User entity's primary key (userId).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
