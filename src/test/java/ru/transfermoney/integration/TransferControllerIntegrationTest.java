package ru.transfermoney.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.exceptions.ErrorResponse;
import ru.transfermoney.models.Amount;
import ru.transfermoney.models.TransferRequest;

import java.util.stream.Stream;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferControllerIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private final GenericContainer<?> container = new GenericContainer<>("transfer-money:latest")
            .withExposedPorts(8080);

    @Test
    void validDataTest(){
        Integer port = container.getMappedPort(8080);

        String request = """
                {
                    "cardFromNumber": "4111111111111111",
                    "cardFromValidTill": "12/25",
                    "cardFromCVV": "123",
                    "cardToNumber": "5555555555554444",
                    "amount": {
                    "value": 1000,
                    "currency": "RUB"
                  }
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request, headers);

        ResponseEntity<OperationResponseDto> response = restTemplate.postForEntity("http://localhost:" + port + "/transfer", entity, OperationResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
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
                        "1111",
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
                        "2222",
                        new Amount("RUB", 1000)))
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void invalidDataTest(TransferRequest transferRequest){
        Integer port = container.getMappedPort(8080);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/transfer", transferRequest, ErrorResponse.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}
