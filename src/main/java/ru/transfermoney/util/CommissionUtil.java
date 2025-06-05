package ru.transfermoney.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommissionUtil {
    private static final double PERCENTAGE = 0.0001;

    public double calculateCommission(Integer income) {
        return income * PERCENTAGE;
    }

    public static int calculateCommissionInKopecks(int amountInKopecks) {
        return (int) Math.round(amountInKopecks * 0.0001);
    }
}
