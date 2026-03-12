package com.smarthome.energy.repository;

import com.smarthome.energy.entity.Appliance;
import com.smarthome.energy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplianceRepository extends JpaRepository<Appliance, Long> {

    List<Appliance> findByUser(User user);
}

