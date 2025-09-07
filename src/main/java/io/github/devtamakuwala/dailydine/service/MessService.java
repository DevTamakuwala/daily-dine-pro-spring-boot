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
 */
@Service
public class MessService {

    @Autowired
    private MessRepository repository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates or updates a mess in the database.
     */
    public void createMess(Mess mess) {
        repository.save(mess);
    }

    /**
     * Retrieves a list of all unverified mess owners.
     */
    public List<UnverifiedMessOwnerDTO> getAllUnverifiedMess() {
        return userRepository.findAllUnverifiedMessOwners();
    }

    /**
     * Retrieves a list of all users who are mess owners.
     */
    public List<User> getAllMess() {
        return userRepository.findAllMess();
    }
}
