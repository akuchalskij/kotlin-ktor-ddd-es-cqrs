package com.kuki.security.application.query.api

import com.kuki.framework.queryhandling.Query

data class FindUserByIdQuery(val userId: String) : Query
