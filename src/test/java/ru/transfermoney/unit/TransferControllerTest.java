package ru.transfermoney.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.transfermoney.controllers.TransferController;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.dto.TransferRequestDto;
import ru.transfermoney.exceptions.CardDataException;
import ru.transfermoney.models.Amount;
import ru.transfermoney.models.TransferRequest;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @InjectMocks
    private TransferController transferController;

    @Test
    void testValidData(){
        TransferRequestDto transferRequest = new TransferRequestDto(
                "1111111111111111",
                "12/25",
                "111",
                "2222222222222222",
                new Amount("RUB", 1000));

        ResponseEntity<OperationResponseDto> responseEntity = transferController.transferMoney(transferRequest);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
    }


    public static Stream<Arguments> testData(){
        return Stream.of(
                Arguments.of(new TransferRequestDto(
                        "11111111111111111",
                        "12/25",
                        "111",
                        "2222222222222222",
                        new Amount("RUB", 1000))),
                Arguments.of(new TransferRequestDto(
                        "1111111111111111",
                        "12/2",
                        "111",
                        "2222222222222222",
                        new Amount("RUB", 1000))),
                Arguments.of(new TransferRequestDto(
                        "1111111111111111",
                        "12/25",
                        "11",
                        "2222222222222222",
                        new Amount("RUB", 1000))),
                Arguments.of(new TransferRequestDto(
                        "1111111111111111",
                        "12/25",
                        "111",
                        "22222222222222222",
                        new Amount("RUB", 1000)))
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void testInvalidData(TransferRequestDto transferRequest){

        Assertions.assertThrows(CardDataException.class, () -> transferController.transferMoney(transferRequest));
    }
}
