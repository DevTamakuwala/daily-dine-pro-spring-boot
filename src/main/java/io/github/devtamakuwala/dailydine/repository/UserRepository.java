package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * Finds all unverified mess owners.
     * - @Query: Specifies the JPQL query to execute.
     * - @EntityGraph: Optimizes the query by eagerly fetching the associated `mess` entity, preventing the N+1 problem.
     *
     * @return A list of all unverified mess owners.
     */
    @Query(value = "SELECT DISTINCT u FROM User u " +
            "WHERE u.role = 'MessOwner' AND u.active = false")
    @EntityGraph(attributePaths = {"mess"})
    List<User> findAllUnverifiedMessOwners();
}
