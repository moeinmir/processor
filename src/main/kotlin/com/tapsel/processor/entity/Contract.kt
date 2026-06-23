package com.tapsel.processor.entity

import com.tapsel.common.constant.EventType
import com.tapsel.common.constant.ExpenseType
import com.tapsel.common.dto.EventDto
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.math.BigDecimal

@Entity
class Contract(
    @ManyToOne(optional = false)
    val publisher: Client,
    @ManyToOne(optional = false)
    val advertizer: Client,
    val advertiserExpenseType: ExpenseType,
    val publisherIncomeType: ExpenseType,
    val advertiserExpenseUnit: Long,
    val publisherIncomeUnit: Long,
    val description: String?,
    var publisherBalanceOnThisContract: BigDecimal,
    var advertiserBalanceOnThisContract: BigDecimal,
) {

    companion object {
        data class ContractDto(
            val publisherCode: String,
            val advertizerName: String,
            val advertiserExpenseType: ExpenseType,
            val publisherIncomeType: ExpenseType,
            val advertiserExpenseUnit: Long,
            val publisherIncomeUnit: Long,
            val description: String?,
            var publisherBalanceOnThisContract: BigDecimal,
            var advertiserBalanceOnThisContract: BigDecimal,
        )
    }

    fun getContractDto(): ContractDto {
        return ContractDto(
            this.publisher.name!!,
            this.advertizer.name!!,
            this.advertiserExpenseType,
            this.publisherIncomeType,
            this.publisherIncomeUnit,
            this.publisherIncomeUnit,
            this.description,
            this.publisherBalanceOnThisContract,
            this.advertiserBalanceOnThisContract
        )
    }



    fun payOrGetPayed(eventType: EventType): Contract {
        if (eventType == EventType.Impression) {
            if (this.publisherIncomeType == ExpenseType.Per1KImpression) {
                this.publisherBalanceOnThisContract =
                    this.publisherBalanceOnThisContract + BigDecimal.valueOf(this.publisherIncomeUnit / 1000)
                    this.publisher.balance + BigDecimal.valueOf(this.publisherIncomeUnit / 1000)
            } else if (this.publisherIncomeType == ExpenseType.PerClick) {
                this.publisherBalanceOnThisContract + BigDecimal.valueOf(this.publisherIncomeUnit)
                this.publisher.balance + BigDecimal.valueOf(this.publisherIncomeUnit)
            }
        } else if (eventType == EventType.Click) {
            if (this.advertiserExpenseType == ExpenseType.Per1KImpression) {
                this.advertiserBalanceOnThisContract =
                    this.publisherBalanceOnThisContract - BigDecimal.valueOf(this.advertiserExpenseUnit / 1000)
                    this.advertizer.balance + BigDecimal.valueOf(this.advertiserExpenseUnit / 1000)
            } else if (this.advertiserExpenseType == ExpenseType.PerClick) {
                this.advertiserBalanceOnThisContract - BigDecimal.valueOf(this.advertiserExpenseUnit)
                this.advertizer.balance + BigDecimal.valueOf(this.advertiserExpenseUnit)
            }
        }
        return this;
    }

    fun createContractForEvent(eventDto: EventDto, publisher: Client, advertiser: Client): Contract {
        return Contract(
            publisher = publisher,
            advertizer = advertiser,
            advertiserExpenseType = eventDto.advertiserExpenseType!!,
            publisherIncomeType = eventDto.publisherIncomeType!!,
            advertiserExpenseUnit = eventDto.advertiserExpense!!,
            publisherIncomeUnit = eventDto.publisherIncome!!,
            description = "Auto contract",
            publisherBalanceOnThisContract = BigDecimal.ZERO,
            advertiserBalanceOnThisContract = BigDecimal.ZERO
        )
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null;
}
