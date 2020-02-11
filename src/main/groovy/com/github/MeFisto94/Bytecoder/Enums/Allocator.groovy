package com.github.MeFisto94.Bytecoder.Enums

/**
 * @see de.mirkosertic.bytecoder.allocator.Allocator
 */
enum Allocator {
    LINEAR(de.mirkosertic.bytecoder.allocator.Allocator.linear),
    PASS_THROUGH(de.mirkosertic.bytecoder.allocator.Allocator.passthru)

    de.mirkosertic.bytecoder.allocator.Allocator val

    Allocator(de.mirkosertic.bytecoder.allocator.Allocator val) {
        this.val = val
    }

    de.mirkosertic.bytecoder.allocator.Allocator toAllocator() {
        return val
    }
}
