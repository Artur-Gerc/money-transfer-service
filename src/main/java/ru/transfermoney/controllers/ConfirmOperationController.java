package ru.transfermoney.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.exceptions.PasswordException;
import ru.transfermoney.models.ConfirmOperation;

@CrossOrigin
@RestController
@RequestMapping("/confirmOperation")
@Slf4j
public class ConfirmOperationController {

    @PostMapping
    public ResponseEntity<OperationResponseDto> confirmOperation(@RequestBody ConfirmOperation confirmOperation) {

        String password = confirmOperation.getCode();
        log.info("Проверка кода подтверждения операции с id: {}", confirmOperation.getOperationId());
        if(password == null || password.isBlank() || password.length() < 4) {
            log.warn("Ошибка валидации кода подтверждения: пароль пуст или меньше 4 символов");
            throw new PasswordException("Пароль пуст или меньше 4 символов");
        }

        OperationResponseDto operationResponseDto = new OperationResponseDto();
        operationResponseDto.setOperationId(confirmOperation.getOperationId());

        log.info("Успешное выполнение операции с ID: {}", operationResponseDto.getOperationId());

        return ResponseEntity.ok(operationResponseDto);
    }
}
