package com.github.MeFisto94.Bytecoder

import org.gradle.api.Plugin
import org.gradle.api.Project

class BytecoderPlugin implements Plugin<Project> {
    @Override
    void apply(Project target) {
        target.extensions.create(BytecoderExtension.NAME, BytecoderExtension)
        target.tasks.create(BytecoderTask.NAME, BytecoderTask)
    }
}
