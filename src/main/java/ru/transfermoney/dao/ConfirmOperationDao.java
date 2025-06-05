package ru.transfermoney.dao;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConfirmOperationDao {
    private final Map<String, String> confirmOperations = new ConcurrentHashMap<>();
    private static final String VERIFICATION_CODE = "0000";


    public void addOperation(String operationId) {
        confirmOperations.put(operationId, VERIFICATION_CODE);
    }

    public String getOperationVerificationCode(String operationId) {
        return confirmOperations.get(operationId);
    }

    public boolean isOperationExists(String operationId) {
        return confirmOperations.containsKey(operationId);
    }
}
