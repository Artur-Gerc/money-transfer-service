package ru.transfermoney.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.models.ConfirmOperation;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfirmOperationControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private final GenericContainer<?> container = new GenericContainer<>("transfer-money:latest")
            .withExposedPorts(8080);

    @Test
    void validPasswordTest(){
        Integer port = container.getMappedPort(8080);

        ConfirmOperation confirmOperation = new ConfirmOperation();
        confirmOperation.setCode("1234");

        ResponseEntity<OperationResponseDto> response = restTemplate.postForEntity("http://localhost:" + port + "/confirmOperation", confirmOperation, OperationResponseDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
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
    void invalidPasswordTest(ConfirmOperation confirmOperation){
        Integer port = container.getMappedPort(8080);

        ResponseEntity<OperationResponseDto> response = restTemplate.postForEntity("http://localhost:" + port + "/confirmOperation", confirmOperation, OperationResponseDto.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}
