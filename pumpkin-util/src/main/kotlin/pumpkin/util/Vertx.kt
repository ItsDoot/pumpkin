package pumpkin.util

import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

class VertxContinuation<in T>(private val ctx: Context, private val cont: Continuation<T>) : Continuation<T> by cont {
    override fun resumeWith(result: Result<T>) {
        ctx.runOnContext { cont.resumeWith(result) }
    }
}

@UseExperimental(InternalCoroutinesApi::class)
object VertxDispatcher : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor, Delay {
    private val ctx: Context
        get() = checkNotNull(Vertx.currentContext()) { "VertxScope must be used on a vertx thread!" }

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        VertxContinuation(ctx, continuation)

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        ctx.dispatcher()
    }
}