package com.kuki.security.ui.http.routes

import com.kuki.security.ui.http.module
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class JwtRoutesTest {

    @Test
    fun testPostJwtCreate() = testApplication {
        application {
            module()
        }
        client.post("/jwt/create").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostJwtRefresh() = testApplication {
        application {
            module()
        }
        client.post("/jwt/refresh").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostJwtVerify() = testApplication {
        application {
            module()
        }
        client.post("/jwt/verify").apply {
            TODO("Please write your test here")
        }
    }
}
