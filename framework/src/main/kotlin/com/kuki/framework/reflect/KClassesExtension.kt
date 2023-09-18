package com.kuki.framework.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Provides a way to get the generic type of a class.
 */
fun <T : Any> KClass<T>.genericType(): KType =
    this.supertypes.firstOrNull()?.arguments?.first()?.type ?: error("Not found type for ${this.simpleName}")
