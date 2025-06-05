package ru.transfermoney.service;

import org.springframework.stereotype.Service;
import ru.transfermoney.dao.CardDao;
import ru.transfermoney.dao.ConfirmOperationDao;
import ru.transfermoney.dao.TransferRequestDao;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.models.Card;
import ru.transfermoney.models.TransferRequest;

@Service
public class TransferService {
    private final CardDao cardDao;
    private final TransferRequestDao transferRequestDao;
    private final ConfirmOperationDao confirmOperationDao;


    public TransferService(CardDao cardDao, TransferRequestDao transferRequestDao, ConfirmOperationDao confirmOperationDao) {
        this.cardDao = cardDao;
        this.transferRequestDao = transferRequestDao;
        this.confirmOperationDao = confirmOperationDao;


        // Добавляем тестовые карты
        Card card1 = new Card();
        card1.setNumber("1111222233334444");
        card1.setValidTill("12/25");
        card1.setCvv("123");
        card1.setBalance(100000);
        cardDao.addCard(card1);

        Card card2 = new Card();
        card2.setNumber("5555666677778888");
        card2.setValidTill("06/24");
        card2.setCvv("456");
        card2.setBalance(50000);
        cardDao.addCard(card2);
    }

    public OperationResponseDto addOperation(TransferRequest request) {

        OperationResponseDto responseDto = new OperationResponseDto();
        String operationId = responseDto.getOperationId();

        request.setOperationId(operationId);

        transferRequestDao.addTransferRequest(request);
        confirmOperationDao.addOperation(operationId);
        return responseDto;
    }
}
