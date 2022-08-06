package com.thinkup.mvi.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

/**
 * ViewModels have many responsibilities.
 * To get more separated features and responsibilities, use an UseCase for each action provided by the ViewModel
 */
abstract class UseCase<R : UseCase.Request, T> {
    /**
     * Use to trigger the UseCase responsibility
     */
    abstract suspend fun execute(request: R? = null): T

    /**
     * If you want execute parallel actions but dependent, use backgroundAsync(block).await()
     * block could be a call to database or service, for example
     */
    protected suspend fun <X> backgroundAsync(context: CoroutineContext = GlobalScope.coroutineContext, block: suspend () -> X): Deferred<X> {
        return CoroutineScope(context).async(context) {
            block.invoke()
        }
    }

    open class Request
}