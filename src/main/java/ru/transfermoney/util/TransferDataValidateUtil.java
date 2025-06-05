package ru.transfermoney.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.transfermoney.exceptions.CardDataException;
import ru.transfermoney.models.TransferRequest;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
@Slf4j
public class TransferDataValidateUtil {

    public void validateTransferData(TransferRequest transferRequest) {

        if (!checkCardNumberLength(transferRequest.getCardFromNumber())) {
            throw new CardDataException("Номер карты отправителя должен быть 16 символов");
        }

        if (!checkCvcNumber(transferRequest.getCardFromCVV())) {
            throw new CardDataException("CVC / CVC2 код отправителя должен быть равен 3 символам");
        }

        if (!checkValidTill(transferRequest.getCardFromValidTill())) {
            throw new CardDataException("Истекла дата действия карты отправителя");
        }

        if (!checkCardNumberLength(transferRequest.getCardToNumber())){
            throw new CardDataException("Номер карты получателя должен быть 16 символов");
        }

        log.info("Валидация платежных данных успешна: {}", transferRequest);
    }

    public boolean checkCardNumberLength(String cardNumber) {
        return cardNumber.length() == 16;
    }

    public boolean checkCvcNumber(String cvcNumber) {
        return cvcNumber.length() == 3;
    }

    public boolean checkValidTill(String expirationDate) {

        if (expirationDate.length() < 5) {
            throw new CardDataException("Дата действия карты должна состоять из 4 символов в формате: мм/гг");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth cardExpiry;

        try {
            cardExpiry = YearMonth.parse(expirationDate, formatter);
        } catch (DateTimeParseException ex) {
            throw new CardDataException("Неверный формат даты действия карты");
        }

        YearMonth now = YearMonth.now();

        return !cardExpiry.isBefore(now);
    }
}
