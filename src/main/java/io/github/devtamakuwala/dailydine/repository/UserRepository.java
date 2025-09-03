package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.DTO.UnverifiedMessOwnerDTO;
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
 * Spring Data JPA automatically creates a proxy bean that implements this interface at runtime.
 * The generic types are:
 * - User: The domain type that this repository manages.
 * - Integer: The data type of the User entity's primary key (userId).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their email address.
     * Spring Data JPA automatically implements this method based on its name.
     * @param email The email of the user to find.
     * @return The User object if found, otherwise null.
     */
    User findByEmail(String email);

    /**
     * Finds all unverified mess owners and projects them into a DTO.
     * This is an efficient way to fetch only the data needed by the client.
     *
     * - @Query: Specifies a custom JPQL (Java Persistence Query Language) query to execute.
     *   This query selects specific fields and constructs a new UnverifiedMessOwnerDTO for each result.
     * - @EntityGraph(attributePaths = {"mess"}): Optimizes the query by eagerly fetching the associated `mess` entity.
     *   This avoids the N+1 query problem by fetching the user and their mess in a single query.
     *
     * @return A list of DTOs representing all unverified mess owners.
     */
    @Query("SELECT new io.github.devtamakuwala.dailydine.DTO.UnverifiedMessOwnerDTO(u.userId, u.email, u.firstName, u.lastName, u.mess, u.createdAt) FROM User u WHERE u.role = 'MESS_OWNER' AND u.active = false")
    @EntityGraph(attributePaths = {"mess"})
    List<UnverifiedMessOwnerDTO> findAllUnverifiedMessOwners();


    /**
     * Finds all users who are mess owners.
     *
     * - @Query: Specifies a custom JPQL query to select all users with the role 'MessOwner'.
     * - @EntityGraph(attributePaths = {"mess"}): Optimizes the query by eagerly fetching the associated `mess` entity,
     *   preventing the N+1 query problem.
     *
     * @return A list of all User entities who are mess owners.
     */
    @Query("select u from User u where u.role='MESS_OWNER'")
    @EntityGraph(attributePaths = {"mess"})
    List<User> findAllMess();
}
