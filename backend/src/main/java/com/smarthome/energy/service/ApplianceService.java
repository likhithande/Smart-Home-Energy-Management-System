package com.smarthome.energy.service;

import com.smarthome.energy.dto.ApplianceDto;
import com.smarthome.energy.entity.Appliance;
import com.smarthome.energy.entity.User;
import com.smarthome.energy.repository.ApplianceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplianceService {

    private final ApplianceRepository applianceRepository;

    public ApplianceService(ApplianceRepository applianceRepository) {
        this.applianceRepository = applianceRepository;
    }

    public List<ApplianceDto> getAppliancesForUser(User user) {
        return applianceRepository.findByUser(user)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ApplianceDto> saveAppliancesForUser(User user, List<ApplianceDto> dtos) {
        List<Appliance> entities = dtos.stream()
                .map(dto -> fromDto(dto, user))
                .collect(Collectors.toList());
        List<Appliance> saved = applianceRepository.saveAll(entities);
        return saved.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ApplianceDto toDto(Appliance appliance) {
        ApplianceDto dto = new ApplianceDto();
        dto.setId(appliance.getId());
        dto.setName(appliance.getName());
        dto.setPowerKw(appliance.getPowerKw());
        dto.setOn(appliance.isOn());
        dto.setSeconds(appliance.getSeconds());
        return dto;
    }

    private Appliance fromDto(ApplianceDto dto, User user) {
        Appliance appliance = new Appliance();
        appliance.setId(dto.getId());
        appliance.setUser(user);
        appliance.setName(dto.getName());
        appliance.setPowerKw(dto.getPowerKw());
        appliance.setOn(Boolean.TRUE.equals(dto.getOn()));
        appliance.setSeconds(dto.getSeconds() == null ? 0L : dto.getSeconds());
        return appliance;
    }
}

