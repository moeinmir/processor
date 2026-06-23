package com.tapsel.processor.repository

import com.tapsel.processor.entity.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client, Long> {
    fun findByClientCode(clientCode: String): Client?
}
