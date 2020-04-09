package com.github.MeFisto94.Bytecoder

import com.github.MeFisto94.Bytecoder.Enums.Allocator
import com.github.MeFisto94.Bytecoder.Enums.Backend
import de.mirkosertic.bytecoder.backend.CompileOptions
import de.mirkosertic.bytecoder.backend.LLVMOptimizationLevel
import de.mirkosertic.bytecoder.optimizer.KnownOptimizer
import org.gradle.api.logging.Logger

class BytecoderExtension {
    public static final NAME = 'bytecoder'

    // TODO Define options for your plugin. Here are some examples:
    // boolean greatFeatureEnabled = true
    // int threshold = 100
    // File workDir

    Backend backend = Backend.JAVASCRIPT
    String mainClassName = ""
    String buildDirectoryOffset = "."
    boolean debugOutput = false
    KnownOptimizer optimizer = KnownOptimizer.ALL
    boolean enableExceptions = true
    String filenamePrefix = "bytecoder"
    int wasmMinimumPageSize = 512
    int wasmMaximumPageSize = 1204
    boolean minify = true
    boolean preferStackifier = false
    Allocator registerAllocator = Allocator.LINEAR
    private final String[] additionalClassesToLink = new String[0]
    private final String[] additionalResources = new String[0]
    LLVMOptimizationLevel optimizationLevel = LLVMOptimizationLevel.defaultValue()

    CompileOptions toCompileOptions(Logger logger) {
        return new CompileOptions(new LoggingAdapter(logger), debugOutput, optimizer, enableExceptions, filenamePrefix,
                wasmMinimumPageSize, wasmMaximumPageSize, minify, preferStackifier, registerAllocator.toAllocator(),
                additionalClassesToLink, additionalResources, optimizationLevel)
    }
}
