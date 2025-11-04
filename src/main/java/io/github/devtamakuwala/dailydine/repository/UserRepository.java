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
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their email address.
     */
    User findByEmail(String email);

    /**
     * Finds all unverified mess owners and projects them into a DTO.
     */
    @Query("SELECT new io.github.devtamakuwala.dailydine.DTO.UnverifiedMessOwnerDTO(u.userId, u.email, u.firstName, u.lastName, u.mess, u.createdAt) FROM User u WHERE u.role = 'MessOwner' AND u.active = false")
    @EntityGraph(attributePaths = {"mess"})
    List<UnverifiedMessOwnerDTO> findAllUnverifiedMessOwners();

    /**
     * Finds all verified mess owners and projects them into a DTO.
     */
    @Query("SELECT new io.github.devtamakuwala.dailydine.DTO.UnverifiedMessOwnerDTO(u.userId, u.email, u.firstName, u.lastName, u.mess, u.createdAt) FROM User u WHERE u.role = 'MessOwner' AND u.active = true")
    @EntityGraph(attributePaths = {"mess"})
    List<UnverifiedMessOwnerDTO> findAllVerifiedMessOwners();


    /**
     * Finds all users who are mess owners
     */
    @Query("select u from User u where u.role='MessOwner'")
    @EntityGraph(attributePaths = {"mess"})
    List<User> findAllMess();


    /**
     * Counts the total number of mess owners who are inactive (pending verification).
     * This is efficient as it performs a direct count query on the database.
     */
    @Query("select count(u) from User u where u.role='MessOwner' and u.active=false")
    Integer countPendingMessVerification();

    @Query("select count(u) from User u where u.role='MessOwner'")
    Integer countMess();

    @Query("select count(u) from User u where u.role='Customer'")
    Integer countAllCustomers();
}
