package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.Mess;
import io.github.devtamakuwala.dailydine.repository.MessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessService {

    @Autowired
    private MessRepository repository;

    public void createMess(Mess mess) {
        repository.save(mess);
    }

}
