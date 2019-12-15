package pumpkin.util

import mu.KLogger
import mu.KotlinLogging

inline fun <reified T> KotlinLogging.logger(): KLogger =
    this.logger(T::class.java.name)