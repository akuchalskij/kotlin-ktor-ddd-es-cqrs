package com.kuki.security.application.query.api

import com.kuki.framework.queryhandling.Query

data class GetVerifiedTokenQuery(val token: String) : Query
