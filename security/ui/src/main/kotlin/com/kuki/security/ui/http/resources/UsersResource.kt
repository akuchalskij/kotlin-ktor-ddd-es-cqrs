package com.kuki.security.ui.http.resources

import io.ktor.resources.*

@Resource("/users")
class UsersResource(val limit: Long = 25, val offset: Long = 0) {

    @Resource("{id}")
    class User(val parent: UsersResource = UsersResource(), val id: String) {

        @Resource("change-email")
        class ChangeEmail(val parent: User)
    }
}
