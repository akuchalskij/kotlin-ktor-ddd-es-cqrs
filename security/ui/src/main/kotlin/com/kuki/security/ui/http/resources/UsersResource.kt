package com.kuki.security.ui.http.resources

import io.ktor.resources.*

@Suppress("unused")
@Resource("/users")
class UsersResource(val limit: Long = 25, val offset: Long = 0) {

    @Resource("me")
    class User(val parent: UsersResource = UsersResource()) {
        @Resource("activate")
        class Activate(val parent: User = User())

        @Resource("send-activate")
        class SendActivate(val parent: User = User())

        @Resource("reset-password")
        class ResetPassword(val parent: User = User())

        @Resource("reset-password-confirm")
        class ResetPasswordConfirm(val parent: User = User())

        @Resource("change-email")
        class ChangeEmail(val parent: User = User())

        @Resource("change-password")
        class ChangePassword(val parent: User = User())
    }
}
