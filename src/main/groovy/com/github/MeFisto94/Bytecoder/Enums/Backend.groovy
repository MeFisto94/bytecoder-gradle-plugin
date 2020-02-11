package com.github.MeFisto94.Bytecoder.Enums

import de.mirkosertic.bytecoder.backend.CompileTarget

enum Backend {
    JAVASCRIPT(CompileTarget.BackendType.js),
    WEB_ASSEMBLY(CompileTarget.BackendType.wasm);

    CompileTarget.BackendType val

    Backend(CompileTarget.BackendType val) {
        this.val = val
    }

    CompileTarget.BackendType toBackendType() {
        return val
    }
}
