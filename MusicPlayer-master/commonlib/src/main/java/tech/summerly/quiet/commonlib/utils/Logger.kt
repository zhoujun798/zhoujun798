package tech.summerly.quiet.commonlib.utils

import android.util.Log

/**
 * Created by summer on 17-12-17
 */


private const val DEBUG = true

private const val TAG = "QUIET"

fun log(level: LoggerLevel = LoggerLevel.INFO, lazyMessage: () -> Any?) {
    if (DEBUG) {
        val traceElement = Exception().stackTrace[2]
        val traceInfo = with(traceElement) {
            val source = if (isNativeMethod) "(Native Method)"
            else if (fileName != null && lineNumber >= 0)
                "($fileName:$lineNumber)"
            else
                if (fileName != null) "($fileName)" else "(Unknown Source)"
            source + className.substringAfterLast('.') + "." + methodName
        }
        val message = "$traceInfo: ${lazyMessage().toString()}"
        logByAndroid(message, level)
    }
}

private fun logByAndroid(message: String, level: LoggerLevel, tag: String = TAG) = when (level) {
    LoggerLevel.DEBUG -> Log.d(tag, message)
    LoggerLevel.INFO -> Log.i(tag, message)
    LoggerLevel.WARN -> Log.w(tag, message)
    LoggerLevel.ERROR -> Log.e(tag, message)
}

enum class LoggerLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR
}