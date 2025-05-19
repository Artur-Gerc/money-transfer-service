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
import ru.transfermoney.controllers.ConfirmOperationController;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.exceptions.PasswordException;
import ru.transfermoney.models.ConfirmOperation;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class ConfirmOperationControllerTest {

    @InjectMocks
    private ConfirmOperationController controller;

    @Test
    void validPasswordTest() {
        ConfirmOperation confirmOperation = new ConfirmOperation();
        confirmOperation.setOperationId("1");
        confirmOperation.setCode("1234");

        ResponseEntity<OperationResponseDto> response = controller.confirmOperation(confirmOperation);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(confirmOperation.getOperationId(), response.getBody().getOperationId());
    }

    static Stream<Arguments> testData(){
        return Stream.of(
                Arguments.of(new ConfirmOperation("1", null)),
                Arguments.of(new ConfirmOperation("1", "11")),
                Arguments.of(new ConfirmOperation("1", " "))
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void invalidPasswordTest(ConfirmOperation confirmOperation) {
        Assertions.assertThrows(PasswordException.class, () -> controller.confirmOperation(confirmOperation));
    }
}
