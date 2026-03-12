package com.smarthome.energy.service;

import com.smarthome.energy.dto.StatsResponse;
import com.smarthome.energy.entity.Appliance;
import com.smarthome.energy.entity.User;
import com.smarthome.energy.repository.ApplianceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

    private static final double COST_PER_UNIT = 5.0;
    private static final int DAYS_IN_MONTH = 30;

    private final ApplianceRepository applianceRepository;

    public StatsService(ApplianceRepository applianceRepository) {
        this.applianceRepository = applianceRepository;
    }

    public StatsResponse computeStats(User user) {
        List<Appliance> appliances = applianceRepository.findByUser(user);

        int activeCount = (int) appliances.stream()
                .filter(Appliance::isOn)
                .count();

        double totalKwh = appliances.stream()
                .mapToDouble(a -> a.getPowerKw() * (a.getSeconds() / 3600.0))
                .sum();

        double todayCost = totalKwh * COST_PER_UNIT;
        double monthlyKwh = totalKwh * DAYS_IN_MONTH;
        double monthlyBill = monthlyKwh * COST_PER_UNIT;

        StatsResponse response = new StatsResponse();
        response.setActiveCount(activeCount);
        response.setTotalKwh(totalKwh);
        response.setTodayCost(todayCost);
        response.setMonthlyKwh(monthlyKwh);
        response.setMonthlyBill(monthlyBill);
        return response;
    }
}

