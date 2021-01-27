package com.example.daggertest.ui.main

import android.util.Log
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Single<T>.toSuspend():T = suspendCancellableCoroutine { cont ->
    this.subscribe(object : SingleObserver<T> {
        override fun onSubscribe(d: Disposable) {
            Log.e("CoroutineClass", "Single is subscribe")
            cont.invokeOnCancellation {
                Log.e("CoroutineClass", "Single is disposed")
                d.dispose()
            }
        }
        override fun onSuccess(t: T) {
            Log.e("CoroutineClass", "Single is success")
            cont.resume(t)
        }
        override fun onError(e: Throwable) {
            Log.e("CoroutineClass", "Single is error")
            cont.resumeWithException(e)
        }
    } )

   /* this
        .doOnDispose { Log.e("CoroutineClass", "<== dispose ==>") }
        .doFinally { Log.e("CoroutineClass", "<== finally ==>") }
        .subscribe(
            {
                //if (cont.context.isActive) cont.resume("$it => Success")
                if (disposable.isDisposed.not()) cont.resume("$it => Success")
                else Log.e("CoroutineClass", "Single is disposed")
            },
            { cont.resumeWithException(Exception("")) })
    .apply { disposable.add(this) }*/
}