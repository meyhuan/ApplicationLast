package com.meyhuan.javassist

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**
 * User  : guanhuan
 * Date  : 2017/5/11
 */

public class PreDexTransform extends Transform {
    Project mProject

    public PreDexTransform(Project project) {
        mProject = project
    }

    // Transfrom在Task列表中的名字
    // TransfromClassesWithPreDexForXXXX
    @Override
    String getName() {
        return "PreDex"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    //指定 Transform 的作用范围
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        log("transform >>>>>")
        //Transform 的 input 有两种类型，目录 和 jar，分开遍历
        inputs.each { TransformInput input->
            input.directoryInputs.each { DirectoryInput directoryInput->
                log("directoryInput name = " + directoryInput.name +", path = " + directoryInput.file.absolutePath)

                JavassistInject.injectDir(directoryInput.file.getAbsolutePath(), "com", mProject)

                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

                //将 input 的目录复制到 output 指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            input.jarInputs.each { JarInput jarInput ->

                log("jarInput name = " + jarInput.name +", path = " + jarInput.file.absolutePath)

                JavassistInject.injectDir(jarInput.file.getAbsolutePath(), "com", mProject)

                //重命名输出文件（同目录 copyFile 会冲突）
                def jarName = jarInput.name
                def md5Name = jarInput.file.hashCode()
                if(jarName.endsWith(".jar")){
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }

    void log(String log){
        mProject.logger.error(log)
    }

}
