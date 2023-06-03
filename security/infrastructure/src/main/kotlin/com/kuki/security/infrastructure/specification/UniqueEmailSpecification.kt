package com.kuki.security.infrastructure.specification

import com.kuki.security.domain.repository.CheckUserByEmailInterface
import com.kuki.security.domain.specification.UniqueEmailSpecificationInterface
import com.kuki.security.domain.valueobject.Email

class UniqueEmailSpecification(
    private val checkUserByEmailInterface: CheckUserByEmailInterface,
) : UniqueEmailSpecificationInterface {
    override suspend fun isUnique(email: String): Boolean =
        checkUserByEmailInterface.existsByEmail(Email.fromString(email)) != null
}
