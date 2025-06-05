package ru.transfermoney.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.transfermoney.controllers.TransferController;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.dto.TransferRequestDto;
import ru.transfermoney.exceptions.CardDataException;
import ru.transfermoney.models.Amount;
import ru.transfermoney.models.TransferRequest;
import ru.transfermoney.service.TransferService;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @InjectMocks
    private TransferController transferController;

    @Mock
    private TransferService transferService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void testValidData(){
        TransferRequestDto transferRequestDto = new TransferRequestDto(
                "1111222233334444",
                "12/25",
                "123",
                "5555666677778888",
                new Amount("RUB", 900));

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAmount(transferRequestDto.getAmount());
        transferRequest.setCardFromCVV(transferRequestDto.getCardFromCVV());
        transferRequest.setCardFromNumber(transferRequestDto.getCardFromNumber());
        transferRequest.setCardToNumber(transferRequestDto.getCardToNumber());
        transferRequest.setCardFromValidTill(transferRequestDto.getCardFromValidTill());

        OperationResponseDto operationResponseDto = new OperationResponseDto();

        Mockito.when(modelMapper.map(any(), eq(TransferRequest.class))).thenReturn(transferRequest);
        Mockito.when(transferService.addOperation(any())).thenReturn(operationResponseDto);

        ResponseEntity<OperationResponseDto> responseEntity = transferController.transferMoney(transferRequestDto);

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
    void testInvalidData(TransferRequestDto transferRequestDto){

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAmount(transferRequestDto.getAmount());
        transferRequest.setCardFromCVV(transferRequestDto.getCardFromCVV());
        transferRequest.setCardFromNumber(transferRequestDto.getCardFromNumber());
        transferRequest.setCardToNumber(transferRequestDto.getCardToNumber());
        transferRequest.setCardFromValidTill(transferRequestDto.getCardFromValidTill());

        Mockito.when(modelMapper.map(any(), eq(TransferRequest.class))).thenReturn(transferRequest);

        Assertions.assertThrows(CardDataException.class, () -> transferController.transferMoney(transferRequestDto));
    }
}
