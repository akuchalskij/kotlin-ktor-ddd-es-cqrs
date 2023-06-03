package com.kuki.security.ui.http.routes

import com.kuki.security.ui.http.module
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class UserRoutesTest {

    @Test
    fun testGetUsers() = testApplication {
        application {
            module()
        }

        client.get("/users").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostUsers() = testApplication {
        application {
            module()
        }

        client.post("/users").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetUsersId() = testApplication {
        application {
            module()
        }

        client.get("/users/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutUsersIdChangeEmail() = testApplication {
        application {
            module()
        }

        client.put("/users/{id}/change-email").apply {
            TODO("Please write your test here")
        }
    }
}
