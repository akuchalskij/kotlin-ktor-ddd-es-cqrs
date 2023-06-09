package com.kuki.security.ui.http.resources

import io.ktor.resources.*

@Resource("/users")
class UsersResource(val limit: Long = 25, val offset: Long = 0) {

    @Resource("me")
    class User(val parent: UsersResource = UsersResource()) {

        @Resource("change-email")
        class ChangeEmail(val parent: User = User())

        @Resource("change-password")
        class ChangePassword(val parent: User = User())
    }
}
