package com.smarthome.energy.dto;

public class StatsResponse {

    private int activeCount;
    private double totalKwh;
    private double todayCost;
    private double monthlyKwh;
    private double monthlyBill;

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public double getTotalKwh() {
        return totalKwh;
    }

    public void setTotalKwh(double totalKwh) {
        this.totalKwh = totalKwh;
    }

    public double getTodayCost() {
        return todayCost;
    }

    public void setTodayCost(double todayCost) {
        this.todayCost = todayCost;
    }

    public double getMonthlyKwh() {
        return monthlyKwh;
    }

    public void setMonthlyKwh(double monthlyKwh) {
        this.monthlyKwh = monthlyKwh;
    }

    public double getMonthlyBill() {
        return monthlyBill;
    }

    public void setMonthlyBill(double monthlyBill) {
        this.monthlyBill = monthlyBill;
    }
}

