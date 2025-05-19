package ru.transfermoney.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.transfermoney.exceptions.CardDataException;
import ru.transfermoney.models.Amount;
import ru.transfermoney.models.TransferRequest;
import ru.transfermoney.util.TransferDataValidateUtil;

import java.util.stream.Stream;

public class TransferDataValidateUtilTest {

    @Test
    void validData(){
        TransferRequest transferRequest = new TransferRequest(
                "1111111111111111",
                "12/25",
                "111",
                "2222222222222222",
                new Amount("RUB", 1000));

        Assertions.assertDoesNotThrow(() -> TransferDataValidateUtil.validateTransferData(transferRequest));
    }

    @Test
    void invalidData(){
        TransferRequest transferRequest = new TransferRequest(
                "11111111111111111",
                "12/25",
                "111",
                "2222222222222222",
                new Amount("RUB", 1000));

        Assertions.assertThrows(CardDataException.class, () -> TransferDataValidateUtil.validateTransferData(transferRequest));
    }

    public static Stream<Arguments> testData(){
        return Stream.of(
                Arguments.of(new TransferRequest(
                        "11111111111111111",
                        "12/25",
                        "111",
                        "2222222222222222",
                        new Amount("RUB", 1000))),
                Arguments.of(new TransferRequest(
                        "1111111111111111",
                        "12/2",
                        "111",
                        "2222222222222222",
                        new Amount("RUB", 1000))),
                Arguments.of(new TransferRequest(
                        "1111111111111111",
                        "12/25",
                        "11",
                        "2222222222222222",
                        new Amount("RUB", 1000))),
                Arguments.of(new TransferRequest(
                        "1111111111111111",
                        "12/25",
                        "111",
                        "22222222222222222",
                        new Amount("RUB", 1000)))
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testInvalidData(TransferRequest transferRequest){

        Assertions.assertThrows(CardDataException.class, () -> TransferDataValidateUtil.validateTransferData(transferRequest));
    }
}
