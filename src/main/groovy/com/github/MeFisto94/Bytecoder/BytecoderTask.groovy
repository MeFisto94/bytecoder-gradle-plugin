package com.github.MeFisto94.Bytecoder

import de.mirkosertic.bytecoder.backend.CompileTarget
import de.mirkosertic.bytecoder.core.BytecodeArrayTypeRef
import de.mirkosertic.bytecoder.core.BytecodeMethodSignature
import de.mirkosertic.bytecoder.core.BytecodeObjectTypeRef
import de.mirkosertic.bytecoder.core.BytecodePrimitiveTypeRef
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ApplicationPluginConvention
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

class BytecoderTask extends DefaultTask {
    public static String NAME = 'bytecoder'
    BytecoderExtension extension

    // The Signature of main(String... args), currently it's hardcoded.
    private static BytecodeMethodSignature mainSignature = new BytecodeMethodSignature(BytecodePrimitiveTypeRef.VOID,
            new BytecodeArrayTypeRef(BytecodeObjectTypeRef.fromRuntimeClass(String.class), 1)
    )

    private static String mainMethodName = "main"

    BytecoderTask() {
        project.afterEvaluate {
            extension = project.extensions."${BytecoderExtension.NAME}"
        }
        dependsOn("classes")
        description = "Bytecoder Transpiler"
        group = "build"
    }

    @TaskAction
    void exec() {
        if (extension.mainClassName.isEmpty()) {
            try {
                extension.mainClassName = project.convention.getPlugin(ApplicationPluginConvention.class).mainClassName
            } catch (IllegalStateException ignored) { // Called when no ApplicationPlugin was found
                throw new TaskExecutionException(this, new IllegalArgumentException("mainClassName must be set or the application plugin used!"));
            }
        }

        ClassLoader loader = createClassLoader()
        CompileTarget target = new CompileTarget(loader, extension.backend.toBackendType())
        Class<?> mainClass

        try {
            mainClass = loader.loadClass(extension.mainClassName)
        } catch (ClassNotFoundException cnf) {
            throw new TaskExecutionException(this, new IllegalArgumentException("Could not find the desired MainClass!", cnf))
        }

        def result = target.compile(extension.toCompileOptions(logger),
                mainClass,
                mainMethodName, mainSignature)

        for (content in result.content) {
            File f = project.buildDir.toPath().resolve(extension.buildDirectoryOffset).resolve(content.fileName).toFile()
            FileOutputStream fos
            try {
                fos = new FileOutputStream(f)
                content.writeTo(fos)
            } catch (Exception ex) {
                throw new TaskExecutionException(this, new IOException("Failed while trying to write the output file", ex))
            } finally {
                // Groovy didn't like try with resources, as they usually have their own API, but that didn't allow us to rethrow the exception
                fos.close()
            }
        }

        println "Compilation succeeded!"
    }

    protected final ClassLoader createClassLoader() throws TaskExecutionException {
        try {
            List<URL> list = new ArrayList<>()
            getProject().getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().each {
                it.compileClasspath.each {
                    list.add(it.toURI().toURL())
                }
                it.output.classesDirs.each {
                    list.add(it.toURI().toURL())
                }
            }

            return new URLClassLoader(list.toArray(new URL[0]) as URL[], getClass().getClassLoader())
        } catch (IllegalStateException ignored) {
            throw new TaskExecutionException(this, new IllegalStateException("The Java Plugin is required by this Task"))
        } catch (MalformedURLException e) {
            throw new TaskExecutionException(this, e)
        }
    }
}
