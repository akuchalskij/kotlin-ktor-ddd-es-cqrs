package com.kuki.security.domain.specification

interface UniqueEmailSpecificationInterface {
    suspend fun isUnique(email: String): Boolean
}
