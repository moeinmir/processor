package com.tapsel.processor.controller

import com.tapsel.common.dto.EventDto
import com.tapsel.processor.dto.BaseResponse
import com.tapsel.processor.entity.Contract
import com.tapsel.processor.service.AdCrudOperationService
import com.tapsel.processor.service.MessageService
import com.tapsel.processor.util.success
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/contract")
class ContractController(
    private val contractService: AdCrudOperationService? = null,
    private val messageService: MessageService? = null
){
    @GetMapping("")
    fun getContractById(@RequestBody request: EventDto) : ResponseEntity<BaseResponse<Contract.Companion.ContractDto?>> {
        val response = contractService!!.getContractsByEventDto(request)?.getContractDto()
        return ResponseEntity.ok(success(response, messageService!!.get("contract.fetch.success")))
    }
}

