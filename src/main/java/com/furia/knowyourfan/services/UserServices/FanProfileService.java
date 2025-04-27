package com.furia.knowyourfan.services.UserServices;


import com.furia.knowyourfan.model.Entitys.FanProfile;
import com.furia.knowyourfan.model.Entitys.User;
import com.furia.knowyourfan.repositories.FanProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FanProfileService {

    private final FanProfileRepository repository;

    public FanProfileService(FanProfileRepository repository) {
        this.repository = repository;
    }

    public Optional<FanProfile> findByUser(User user) {
        return repository.findByUser(user);
    }

    public void save(FanProfile profile) {
        repository.save(profile);
    }
}
