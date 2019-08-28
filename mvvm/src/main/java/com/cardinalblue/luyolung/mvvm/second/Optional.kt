package com.cardinalblue.luyolung.mvvm.second

// TODO: Move to util package.
data class Optional<T>(val value: T? = null) {

    val isEmpty get() = value == null
}