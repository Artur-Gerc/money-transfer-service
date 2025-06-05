package ru.transfermoney.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.transfermoney.dao.CardDao;
import ru.transfermoney.dao.ConfirmOperationDao;
import ru.transfermoney.dao.TransferRequestDao;
import ru.transfermoney.exceptions.CardBalanceException;
import ru.transfermoney.exceptions.NotFoundException;
import ru.transfermoney.exceptions.OperationNotFoundException;
import ru.transfermoney.exceptions.PasswordException;
import ru.transfermoney.models.Card;
import ru.transfermoney.models.ConfirmOperation;
import ru.transfermoney.models.TransferRequest;
import ru.transfermoney.util.CommissionUtil;

import java.security.interfaces.DSAPublicKey;

@Service
@Slf4j
public class ConfirmOperationService {
    private final ConfirmOperationDao confirmOperationDao;
    private final TransferRequestDao transferRequestDao;
    private final CardDao cardDao;

    public ConfirmOperationService(ConfirmOperationDao confirmOperationDao, TransferRequestDao transferRequestDao, CardDao cardDao) {
        this.confirmOperationDao = confirmOperationDao;
        this.transferRequestDao = transferRequestDao;
        this.cardDao = cardDao;
    }

    public String getOperationVerificationCode(String operationId) {
        return confirmOperationDao.getOperationVerificationCode(operationId);
    }

    public boolean isOperationExists(String operationId) {
        return confirmOperationDao.isOperationExists(operationId);
    }

    public void validateOperation(ConfirmOperation confirmOperation) {
        String operationId = confirmOperation.getOperationId();
        log.info("Поиск операции с id: {}", operationId);
        if(!isOperationExists(operationId)) {
            throw new NotFoundException("Операция с id: " + operationId + " не найдена");
        }

        TransferRequest transferRequest = transferRequestDao.getTransferRequest(operationId);
        if(!cardDao.cardExists(transferRequest.getCardToNumber()) || !cardDao.cardExists(transferRequest.getCardFromNumber())) {
            throw new NotFoundException("Карта отправителя или получателя не найдена");
        }

        log.info("Проверка достаточности средств на балансе карты: {}", transferRequest.getCardFromNumber());
        checkBalance(transferRequest);

        log.info("Проверка кода подтверждения операции с id: {}", confirmOperation.getOperationId());
        String password = confirmOperation.getCode();
        if(password == null || password.isBlank() || password.length() < 4) {
            log.warn("Ошибка валидации кода подтверждения: пароль пуст или меньше 4 символов");
            throw new PasswordException("Пароль пуст или меньше 4 символов");
        }

        if(!getOperationVerificationCode(operationId).equals(password)) {
            throw new PasswordException("Введен не верный пароль");
        }
    }

    public void confirmOperation(ConfirmOperation confirmOperation) {
        validateOperation(confirmOperation);
        sendMoney(confirmOperation.getOperationId());
    }

    public void checkBalance(TransferRequest transferRequest) {
        Card cardFrom = cardDao.getCard(transferRequest.getCardFromNumber());
        Integer balance = cardFrom.getBalance();

        int amountWithCommission = transferRequest.getAmount().getValue() + CommissionUtil.calculateCommissionInKopecks(transferRequest.getAmount().getValue());

        if(amountWithCommission > balance) {
            throw new CardBalanceException("На карте отправителя недостаточно средств");
        }
    }

    public void sendMoney(String operationId) {
        TransferRequest transferRequest = transferRequestDao.getTransferRequest(operationId);
        Card cardFrom = cardDao.getCard(transferRequest.getCardFromNumber());
        Card cardTo = cardDao.getCard(transferRequest.getCardToNumber());
        Integer amount = transferRequest.getAmount().getValue();

        cardFrom.setBalance(cardFrom.getBalance() - amount);
        System.out.println(cardFrom.getBalance());

        cardTo.setBalance(cardTo.getBalance() + amount);
        System.out.println(cardTo.getBalance());
    }
}
