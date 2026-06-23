package com.tapsel.processor.repository

import com.tapsel.common.constant.ExpenseType
import com.tapsel.processor.entity.Contract
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContractRepository : JpaRepository<Contract, Long> {
    fun findByPublisher_ClientCodeAndAdvertizer_ClientCodeAndAdvertiserExpenseTypeAndPublisherIncomeTypeAndAdvertiserExpenseUnitAndPublisherIncomeUnit(
        publisherCode: String,
        advertizerCode: String,
        advertiserExpenseType: ExpenseType,
        publisherIncomeType: ExpenseType,
        advertiserExpenseUnit: Long,
        publisherIncomeUnit: Long
    ): Contract?

    fun findByPublisher_ClientCodeAndPublisherIncomeTypeAndPublisherIncomeUnit(
        publisherCode: String,
        advertizerCode: String,
        advertiserExpenseType: ExpenseType,
        publisherIncomeType: ExpenseType,
        advertiserExpenseUnit: Long,
        publisherIncomeUnit: Long
    ): Contract?

    fun findByAdvertizer_ClientCodeAndAdvertiserExpenseTypeAndAdvertiserExpenseUnit(
        publisherCode: String,
        advertizerCode: String,
        advertiserExpenseType: ExpenseType,
        publisherIncomeType: ExpenseType,
        advertiserExpenseUnit: Long,
        publisherIncomeUnit: Long
    ): List<Contract>

}
