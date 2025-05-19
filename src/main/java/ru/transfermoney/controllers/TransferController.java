package ru.transfermoney.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.exceptions.CardDataException;
import ru.transfermoney.models.TransferRequest;
import ru.transfermoney.util.CommissionUtil;
import ru.transfermoney.util.TransferDataValidateUtil;


@Slf4j
@CrossOrigin("https://serp-ya.github.io")
@RestController
@RequestMapping("/transfer")
public class TransferController {

    @PostMapping
    public ResponseEntity<OperationResponseDto> transferMoney(@RequestBody TransferRequest transferRequest) {
        log.info("Запрос на перевод: {}", transferRequest);
        log.info("Комиссия: {}", CommissionUtil.calculateCommission(transferRequest.getAmount().getValue()));
        TransferDataValidateUtil.validateTransferData(transferRequest);

        return ResponseEntity.ok().body(new OperationResponseDto());
    }
}
