package ru.transfermoney.util;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.transfermoney.exceptions.CardDataException;
import ru.transfermoney.models.TransferRequest;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@UtilityClass
@Slf4j
public class TransferDataValidateUtil {

    public void validateTransferData(TransferRequest transferRequest) {
//        log.info("Начало валидации данных для следующей операции: {}", transferRequest);

        if (!checkCardNumberLength(transferRequest.getCardFromNumber())) {
//            log.warn("Номер карты отправителя должен быть 16 символов");
            throw new CardDataException("Номер карты отправителя должен быть 16 символов");
        }

        if (!checkCvcNumber(transferRequest.getCardFromCVV())) {
//            log.warn("CVC / CVC2 код отправителя должен быть равен 3 символам");
            throw new CardDataException("CVC / CVC2 код отправителя должен быть равен 3 символам");
        }

        if (!checkValidTill(transferRequest.getCardFromValidTill())) {
//            log.warn("Истекла дата действия карты отправителя");
            throw new CardDataException("Истекла дата действия карты отправителя");
        }

        if (!checkCardNumberLength(transferRequest.getCardToNumber())){
//            log.warn("Номер карты получателя должен быть 16 символов");
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
//            log.warn("Дата действия карты должна состоять из 4 символов в формате: мм/гг");
            throw new CardDataException("Дата действия карты должна состоять из 4 символов в формате: мм/гг");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth cardExpiry;

        try {
            cardExpiry = YearMonth.parse(expirationDate, formatter);
        } catch (DateTimeParseException ex) {
//            log.warn("Неверный формат даты действия карты");
            throw new CardDataException("Неверный формат даты действия карты");
        }

        YearMonth now = YearMonth.now();

        return !cardExpiry.isBefore(now);
    }
}
