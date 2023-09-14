package org.example.demo;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * MyClassFileTransformer
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-13 00:33:19
 */
@Slf4j
public class MyClassFileTransformer implements ClassFileTransformer {

    /**
     * 当某个类将要被加载之前，都会线进入到此方法中
     *
     * @param loader              类加载器
     * @param className           将要加载的类的全限定名，格式：aa/bb/cc/Demo
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return byte[] 需要增强就返回实际的 bytes，否则返回 null 即可
     * @author wangweijun
     * @since 2023/9/13 00:34
     */
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] bytecode = null;
        if ("com/example/demojavaagent/service/HelloService".equals(className)) {
            // asm, javassist, cglib, bytebuddy 来加强
            ClassPool classPool = ClassPool.getDefault();
            try {
                CtClass ctClass = classPool.get("com.example.demojavaagent.service.HelloService");
                CtMethod sayHelloMethod = ctClass.getDeclaredMethod("sayHello", new CtClass[]{classPool.get("java.lang.String")});
                sayHelloMethod.insertBefore("{System.out.println(\"before say\");}");
                bytecode = ctClass.toBytecode();
                log.info("transform class {} completed.", className);
            } catch (Exception e) {
                log.error("transform error", e);
            }
        }
        return bytecode;
    }
}
