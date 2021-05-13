package io.javalin

import io.javalin.testing.TestUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TestAsyncTimeoutErrorHandling {

    @Test
    fun `that issue on github`() = TestUtil.test(Javalin.create { config ->
        config.asyncRequestTimeout = 100
    }) { app, http ->
        app.get("/") { it.result(getFuture()) }
        app.error(500) { ctx -> ctx.result("My own simple error message") }
        assertThat(http.get("/").body).isEqualTo("My own simple error message")
    }


    private fun getFuture(): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        Executors.newSingleThreadScheduledExecutor().schedule({
            future.complete("Test")
        }, 200, TimeUnit.MILLISECONDS)
        return future
    }
}
