package com.kuki.framework.eventhandling


import com.kuki.framework.domain.DomainMessage
import com.kuki.framework.domain.Event
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Test
import java.util.*
import kotlin.test.BeforeTest

class SimpleEventBusTest {

    private lateinit var eventBus: TestSimpleEventBus

    @BeforeTest
    fun setup() {
        eventBus = TestSimpleEventBus()
    }

    @Test
    fun testSubscribe() {
        val listener = object : EventListener {
            override suspend fun handle(event: Event) {
                println(event)
            }
        }

        val listenerTwo = object : EventListener {
            override suspend fun handle(event: Event) {
                println(event)
            }
        }

        eventBus.subscribe(listener)
        eventBus.subscribe(listenerTwo)

        assert(eventBus.eventListeners.contains(listener))
        assert(eventBus.eventListeners.size == 2)
    }

    @Test
    fun testPublish() = runTest {
        val listener = object : EventListener {
            override suspend fun handle(event: Event) {
                println(event)
            }
        }

        eventBus.subscribe(listener)

        eventBus.publish(
            listOf(
                DomainMessage(
                    id = UUID.randomUUID().toString(),
                    payload = TestEvent("test"),
                    sequenceNumber = 0,
                    occurredOn = Clock.System.now()
                )
            )
        )

        assert(eventBus.eventListeners.size == 1)
        assert(eventBus.eventListeners.contains(listener))
        assert(!eventBus.isPublishing)
        assert(eventBus.eventQueue.isEmpty())
    }

    data class TestEvent(val message: String) : Event
}
