package ru.transfermoney.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.models.ConfirmOperation;
import ru.transfermoney.service.ConfirmOperationService;
import ru.transfermoney.service.TransferService;

@CrossOrigin
@RestController
@RequestMapping("/confirmOperation")
@Slf4j
public class ConfirmOperationController {

    private final ConfirmOperationService confirmOperationService;

    public ConfirmOperationController(ConfirmOperationService confirmOperationService) {
        this.confirmOperationService = confirmOperationService;
    }

    @PostMapping
    public ResponseEntity<OperationResponseDto> confirmOperation(@RequestBody ConfirmOperation confirmOperation) {

        confirmOperationService.confirmOperation(confirmOperation);

        OperationResponseDto operationResponseDto = new OperationResponseDto();
        operationResponseDto.setOperationId(confirmOperation.getOperationId());

        log.info("Успешное выполнение операции с ID: {}", operationResponseDto.getOperationId());

        return ResponseEntity.ok(operationResponseDto);
    }
}
