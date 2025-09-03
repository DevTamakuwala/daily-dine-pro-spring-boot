package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.DTO.UnverifiedMessOwnerDTO;
import io.github.devtamakuwala.dailydine.model.Mess;
import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.MessRepository;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing mess-related operations.
 * This class encapsulates the business logic for messes and interacts with the
 * MessRepository and UserRepository to perform database operations.
 */
@Service
public class MessService {

    @Autowired
    private MessRepository repository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates or updates a mess in the database.
     * This method uses the JpaRepository's save() method, which handles both creation
     * of new entities and updates to existing ones.
     * @param mess The Mess entity to be saved.
     */
    public void createMess(Mess mess) {
        repository.save(mess);
    }

    /**
     * Retrieves a list of all unverified mess owners.
     * This method delegates the call to the `findAllUnverifiedMessOwners` method in the `UserRepository`,
     * which efficiently fetches the required data using a DTO projection.
     *
     * @return A list of DTOs representing all unverified mess owners.
     */
    public List<UnverifiedMessOwnerDTO> getAllUnverifiedMess() {
        return userRepository.findAllUnverifiedMessOwners();
    }

    /**
     * Retrieves a list of all users who are mess owners.
     * This method delegates the call to the `findAllMess` method in the `UserRepository`,
     * which is optimized to fetch the associated mess details and avoid the N+1 problem.
     *
     * @return A list of all User entities who are mess owners.
     */
    public List<User> getAllMess() {
        return userRepository.findAllMess();
    }
}
