package com.github.MeFisto94.Bytecoder

import org.gradle.api.logging.Logger

class LoggingAdapter implements de.mirkosertic.bytecoder.api.Logger {
    Logger internalLogger

    LoggingAdapter(Logger internalLogger) {
        this.internalLogger = internalLogger
    }

    @Override
    void info(String aMessage, Object... aArguments) {
        internalLogger.info(aMessage, aArguments)
    }

    @Override
    void warn(String aMessage, Object... aArguments) {
        internalLogger.warn(aMessage, aArguments)
    }

    @Override
    void debug(String aMessage, Object... aArguments) {
        internalLogger.debug(aMessage, aArguments)
    }
}
