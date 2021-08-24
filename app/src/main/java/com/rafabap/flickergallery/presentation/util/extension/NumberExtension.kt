package com.rafabap.flickergallery.presentation.util.extension

import android.content.res.Resources

fun Int.convertDpToInt(): Int =
    (this * Resources.getSystem().displayMetrics.density + DP_CONVERT_VALUE).toInt()

fun Float.convertDpToInt(): Int =
    (this * Resources.getSystem().displayMetrics.density + DP_CONVERT_VALUE).toInt()

fun Int.convertDpToFloat(): Float =
    (this * Resources.getSystem().displayMetrics.density + DP_CONVERT_VALUE)

fun Int.convertPixelToDp(): Int =
    (this / Resources.getSystem().displayMetrics.density + DP_CONVERT_VALUE).toInt()

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.dpFloat: Float
    get() = (this / Resources.getSystem().displayMetrics.density)

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

private const val DP_CONVERT_VALUE = 0.5f

fun Int.toBoolean() = this != 0