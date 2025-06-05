package ru.transfermoney.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.transfermoney.dto.OperationResponseDto;
import ru.transfermoney.dto.TransferRequestDto;
import ru.transfermoney.models.TransferRequest;
import ru.transfermoney.service.TransferService;
import ru.transfermoney.util.CommissionUtil;
import ru.transfermoney.util.TransferDataValidateUtil;


@Slf4j
@CrossOrigin("https://serp-ya.github.io")
@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;
    private final ModelMapper modelMapper;

    public TransferController(TransferService transferService, ModelMapper modelMapper) {
        this.transferService = transferService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<OperationResponseDto> transferMoney(@RequestBody TransferRequestDto transferRequestDto) {
        TransferRequest transferRequest = modelMapper.map(transferRequestDto, TransferRequest.class);

        log.info("Запрос на перевод: {}", transferRequest);
        log.info("Комиссия: {}", CommissionUtil.calculateCommission(transferRequest.getAmount().getValue()));

        TransferDataValidateUtil.validateTransferData(transferRequest);

        return ResponseEntity.ok().body(transferService.addOperation(transferRequest));
    }
}
