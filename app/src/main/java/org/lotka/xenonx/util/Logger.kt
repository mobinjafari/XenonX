package org.lotka.xenonx.util

import android.util.Log

import org.lotka.xenonx.BuildConfig

var isUnitTest = false

fun printLogD(className: String?, message: String) {

    if (org.lotka.xenonx.BuildConfig.DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    } else if (org.lotka.xenonx.BuildConfig.DEBUG && isUnitTest) {
        println("$className: $message")
    }
}

/*
    Priorities: Log.DEBUG, Log. etc....
 */
fun cLog(msg: String?) {
    msg?.let {
        if (!org.lotka.xenonx.BuildConfig.DEBUG) {
            //FirebaseCrashlytics.getInstance().log(it)
        }
    }

}
