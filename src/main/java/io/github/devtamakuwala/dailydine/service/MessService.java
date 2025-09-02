package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.Mess;
import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.MessRepository;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessService {

    @Autowired
    private MessRepository repository;

    @Autowired
    private UserRepository userRepository;

    public void createMess(Mess mess) {
        repository.save(mess);
    }

    /**
     * Retrieves a list of all unverified mess owners.
     * This method calls the `findAllUnverifiedMessOwners` method in the `UserRepository` to retrieve the data.
     *
     * @return A list of all unverified mess owners.
     */
    public List<User> getAllUnverifiedMess() {
        return userRepository.findAllUnverifiedMessOwners();
    }
}
