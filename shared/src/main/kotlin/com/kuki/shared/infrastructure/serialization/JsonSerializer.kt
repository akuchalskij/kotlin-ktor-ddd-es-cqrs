package com.kuki.shared.infrastructure.serialization

import com.kuki.framework.domain.Event
import com.kuki.security.domain.event.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val jsonSerializer = Json {
    serializersModule = SerializersModule {
        polymorphic(Event::class) {
            subclass(UserCreated::class)
            subclass(UserActivated::class)
            subclass(UserActivationSent::class)
            subclass(UserActivationResent::class)
            subclass(UserDeleted::class)
            subclass(UserPersonalNameChanged::class)
            subclass(UserResetPasswordRequested::class)
            subclass(UserResetPasswordConfirmed::class)
            subclass(UserSignedIn::class)
        }
    }
}
