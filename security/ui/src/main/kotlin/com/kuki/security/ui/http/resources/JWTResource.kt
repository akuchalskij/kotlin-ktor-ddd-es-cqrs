package com.kuki.security.ui.http.resources

import io.ktor.resources.*

@Resource("/jwt")
class JWTResource {
    @Resource("create")
    class Create(val parent: JWTResource = JWTResource())

    @Resource("refresh")
    class Refresh(val parent: JWTResource = JWTResource())

    @Resource("verify")
    class Verify(val parent: JWTResource = JWTResource())
}
