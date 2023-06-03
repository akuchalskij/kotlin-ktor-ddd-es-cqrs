package com.kuki.security.application.query.api

import com.kuki.framework.queryhandling.Query


data class FindAllUsersQuery(val limit: Long, val offset: Long) : Query
