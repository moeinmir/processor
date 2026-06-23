package com.tapsel.processor.service

import com.tapsel.common.dto.EventDto
import com.tapsel.processor.entity.Client
import com.tapsel.processor.entity.Contract
import com.tapsel.processor.exception.BaseException
import com.tapsel.processor.repository.ClientRepository
import com.tapsel.processor.repository.ContractRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

interface AdCrudOperationService{
    fun getContractsByEventDto(eventDto: EventDto): Contract?;
    fun saveContract(contract: Contract): Contract.Companion.ContractDto
    fun getContractById(id: Long): Contract.Companion.ContractDto;
    fun createMissingEntitiesOfEvents(eventDto: EventDto): Contract.Companion.ContractDto;
}

@Service
class AdCrudOperationServiceImpl(
    private val clientRepository: ClientRepository,
    private val contractRepository: ContractRepository,
    private val messageService: MessageService
) : AdCrudOperationService {

    override fun getContractsByEventDto(eventDto: EventDto): Contract? {
        return contractRepository
            .findByPublisher_ClientCodeAndAdvertizer_ClientCodeAndAdvertiserExpenseTypeAndPublisherIncomeTypeAndAdvertiserExpenseUnitAndPublisherIncomeUnit(
                eventDto.publisherId!!,
                eventDto.advertiserId!!,
                eventDto.advertiserExpenseType!!,
                eventDto.publisherIncomeType!!,
                eventDto.advertiserExpense!!,
                eventDto.publisherIncome!!
            );
    }

    override fun saveContract(contract: Contract): Contract.Companion.ContractDto {
        val saved = contractRepository.save(contract)
        return saved.getContractDto()
    }

    override fun getContractById(id: Long): Contract.Companion.ContractDto {
        val contract = contractRepository.findById(id)
            .orElseThrow { BaseException(404,messageService.get("contract.fetch.failed",id))}
        return contract.getContractDto()
    }

    override fun createMissingEntitiesOfEvents(eventDto: EventDto): Contract.Companion.ContractDto {

        val publisher = clientRepository.findByClientCode(eventDto.publisherId!!)
            ?: clientRepository.save(
                Client.createPublisher(eventDto)
            )

        val advertiser = clientRepository.findByClientCode(eventDto.advertiserId!!)
            ?: clientRepository.save(
                Client.createAdvertiser(eventDto)
            )

        val existingContract =
            contractRepository.findByPublisher_ClientCodeAndAdvertizer_ClientCodeAndAdvertiserExpenseTypeAndPublisherIncomeTypeAndAdvertiserExpenseUnitAndPublisherIncomeUnit(
                publisher.clientCode,
                advertiser.clientCode,
                eventDto.advertiserExpenseType!!,
                eventDto.publisherIncomeType!!,
                eventDto.advertiserExpense!!,
                eventDto.publisherIncome!!
            )

        val contract = Contract(
            publisher = publisher,
            advertizer = advertiser,
            advertiserExpenseType = eventDto.advertiserExpenseType!!,
            publisherIncomeType = eventDto.publisherIncomeType!!,
            advertiserExpenseUnit = eventDto.advertiserExpense!!,
            publisherIncomeUnit = eventDto.publisherIncome!!,
            description = null,
            publisherBalanceOnThisContract = BigDecimal.ZERO,
            advertiserBalanceOnThisContract = BigDecimal.ZERO,
        )
        val savedContract = contractRepository.save(contract)
        return savedContract.getContractDto()
    }
}
