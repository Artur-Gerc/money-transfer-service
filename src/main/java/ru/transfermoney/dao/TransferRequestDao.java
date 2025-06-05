package ru.transfermoney.dao;

import org.springframework.stereotype.Component;
import ru.transfermoney.models.TransferRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransferRequestDao {
    private final Map<String, TransferRequest> requests = new ConcurrentHashMap<>();

    public void addTransferRequest(TransferRequest request) {
        requests.put(request.getOperationId(), request);
    }

    public TransferRequest getTransferRequest(String operationId) {
        return requests.get(operationId);
    }
}
