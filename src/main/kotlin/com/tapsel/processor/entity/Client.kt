package com.tapsel.processor.entity

import com.tapsel.common.constant.ClientType
import com.tapsel.common.dto.EventDto
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
class Client(val name: String?,
             val clientType: ClientType,
             val description: String?,
             val clientCode: String,
             val balance: BigDecimal,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null;

    companion object{
        fun createPublisher(eventDto: EventDto): Client {
            return Client(null, clientType = ClientType.PUBLISHER, null, clientCode = eventDto.publisherId!!,
                BigDecimal.ZERO)
        }
        fun createAdvertiser(eventDto: EventDto): Client {
            return Client(null,clientType = ClientType.ADVERTISER,null,clientCode =eventDto.advertiserId!!, BigDecimal.ZERO)
        }

    }

}
