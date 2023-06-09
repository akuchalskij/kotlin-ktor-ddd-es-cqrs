package com.kuki.shared.infrastructure.serialization

import com.kuki.framework.domain.Event
import com.kuki.security.domain.event.UserEmailChanged
import com.kuki.security.domain.event.UserPasswordChanged
import com.kuki.security.domain.event.UserSignedIn
import com.kuki.security.domain.event.UserWasCreated
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val jsonSerializer = Json {
    serializersModule = SerializersModule {
        polymorphic(Event::class) {
            subclass(UserWasCreated::class)
            subclass(UserEmailChanged::class)
            subclass(UserPasswordChanged::class)
            subclass(UserSignedIn::class)
        }
    }
}
