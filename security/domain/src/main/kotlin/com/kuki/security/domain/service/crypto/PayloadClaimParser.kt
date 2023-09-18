package com.kuki.security.domain.service.crypto


interface PayloadClaimParser {

    fun parse(data: Any?): Any?
}

