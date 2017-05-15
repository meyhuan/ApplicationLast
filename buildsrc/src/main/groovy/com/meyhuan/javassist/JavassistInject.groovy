package com.meyhuan.javassist

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

import java.lang.annotation.Annotation

public class JavassistInject {

    public static final String JAVA_ASSIST_APP = "com.meyhuan.applicationlast.MyApp"
    public static final String JAVA_ASSIST_MOBCLICK = "com.umeng.analytics.MobclickAgent"

    private final static ClassPool pool = ClassPool.getDefault()

    public static void injectDir(String path, String packageName, Project project) {
        pool.appendClassPath(path)
        String androidJarPath = project.android.bootClasspath[0].toString()
        log("androidJarPath: " + androidJarPath, project)
        pool.appendClassPath(androidJarPath)
        importClass(pool)
        File dir = new File(path)
        if(!dir.isDirectory()){
            return
        }
        dir.eachFileRecurse { File file->
            String filePath = file.absolutePath
            log("filePath : " + filePath, project)
            if(filePath.endsWith(".class") && !filePath.contains('R$')
                    && !filePath.contains('R.class') && !filePath.contains("BuildConfig.class")){
                log("filePath my : " + filePath, project)
                int index = filePath.indexOf(packageName);
                boolean isMyPackage = index != -1;
                if(!isMyPackage){
                    return
                }
                String className = JavassistUtils.getClassName(index, filePath)
                log("className my : " + className, project)
                CtClass c = pool.getCtClass(className)
                log("CtClass my : " + c.getSimpleName() , project)
                for(CtMethod method : c.getDeclaredMethods()){
                    log("CtMethod my : " + method.getName() , project)
                    if(checkOnClickMethod(method)){
                        log("checkOnClickMethod my : " + method.getName() , project)
                        injectMethod(method)
                        c.writeFile(path)
                    }
                }
            }
        }

    }

    private static boolean checkOnClickMethod(CtMethod method ){
        return method.getName().endsWith("onClick")  && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].getName().equals("android.view.View") ;
    }

    private static void injectMethod(CtMethod method){
        method.insertAfter("System.out.println((\$1).getTag());")
        method.insertAfter("MobclickAgent.onEvent(MyApp.getInstance(), (\$1).getTag().toString());")
    }

    private static void log(String msg, Project project){
        project.logger.log(LogLevel.ERROR, msg)
    }

    private static void importClass(ClassPool pool){
        pool.importPackage(JAVA_ASSIST_APP)
        pool.importPackage(JAVA_ASSIST_MOBCLICK)
    }

}