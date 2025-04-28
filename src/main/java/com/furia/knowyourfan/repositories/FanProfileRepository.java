package com.furia.knowyourfan.repositories;


import com.furia.knowyourfan.model.Entitys.FanProfile;
import com.furia.knowyourfan.model.Entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FanProfileRepository extends JpaRepository<FanProfile, Long> {
    Optional<FanProfile> findByUser(User user);


}
