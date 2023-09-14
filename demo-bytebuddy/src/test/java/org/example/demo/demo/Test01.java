package org.example.demo.demo;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.*;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Test01
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-08-29 00:12:10
 */
public class Test01 {

    private String path;
    private String jarPath;

    @BeforeEach
    public void init() {
        path = Test01.class.getClassLoader().getResource("").getPath();
        jarPath = "/Users/wangweijun/IdeaProjects/java-all-blue/demo-bytebuddy/target/demo-bytebuddy-1.0-SNAPSHOT.jar";
        // path = /Users/wangweijun/IdeaProjects/java-all-blue/demo-bytebuddy/target/test-classes/
        System.out.println("path = " + path);

    }


    /**
     * 生成一个类
     */
    @Test
    public void test1() throws IOException {

        // 如不指定，默认命名策略是：new NamingStrategy.SuffixingRandom("ByteBuddy");
        NamingStrategy.SuffixingRandom suffixingRandom = new NamingStrategy.SuffixingRandom("test");

        // Unloaded 表示生成的字节码还没有加载到 JVM
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                // 指定命名策略，如果指定 .name 则命名策略失效
                // .with(suffixingRandom)

                // 是否校验类的生成规则，默认：TypeValidation.ENABLED，略有性能消耗
                // .with(TypeValidation.DISABLED)

                // 表示是 Object 的子类
                // .subclass(Object.class)
                .subclass(UserManager.class)

                // 指定具体的类名（全限定名）
                // 如果命名不合法，则会报错：a.b.1SubObject，如果设置 .with(TypeValidation.DISABLED) 则会不进行校验
                // .name("a.b.1SubObject")
                .name("a.b.SubObject")

                // 生成的类的命名规则
                // 在不指定命名策略的前提下：
                //      1. 对于父类是 JDK 自带的类的情况下：net.bytebuddy.renamed.java.lang.Object$ByteBuddy$Qjk6YzVV
                //      2. 对于父类非 JDK 自带的类的情况下：org.example.demo.demo.UserManager$ByteBuddy$iBPGIuT7
                // 在指定命名策略的前提下：
                //      1. 对于父类是 JDK 自带的类的情况下：net.bytebuddy.renamed.java.lang.Object$test$SfejLFaH
                //      2. 对于父类非 JDK 自带的类的情况下：org.example.demo.demo.UserManager$test$avjbpuep
                .make();

        // 获取生成的类的字节码，可以字节写入文件
        // byte[] bytes = unloaded.getBytes();
        // FileUtils.writeByteArrayToFile(new File(path + "a/b/SubObject.class"), bytes);

        // 表示把生成的类的字节码，保存到某个目录
        unloaded.saveIn(new File(path));

        // 把生成的字节码文件注入到某个 Jar 包中
        // unloaded.inject(new File(jarPath));
    }

    /**
     * 对实例方法进行插桩
     * 插桩（stub、instrumentation）：一般指的是字节码插桩，就是对字节码进行修改或增强
     * 埋点：插桩是实现埋点的一种方式
     */
    @Test
    public void test2() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubObject")
                // named：通过名字指定要拦截的方法
                .method(ElementMatchers.named("toString"))
                // 指定拦截到方法后，应该如何处理
                .intercept(FixedValue.value("hello bytebuddy"))
                .make();

        // Loaded 表示字节码已经背加载到 JVM 中
        DynamicType.Loaded<UserManager> loaded = unloaded.load(this.getClass().getClassLoader());
        // 获取到 Class 对象
        Class<? extends UserManager> loadedClass = loaded.getLoaded();

        UserManager userManager = loadedClass.getDeclaredConstructor().newInstance();
        // net.bytebuddy.dynamic.loading.ByteArrayClassLoader@7fb95505
        System.out.println("userManager.getClass().getClassLoader() = " + userManager.getClass().getClassLoader());
        // hello bytebuddy
        System.out.println("userManager.toString() = " + userManager.toString());

        // loaded 和 unloaded 一样，也是继承 DynamicType 类，同样有 saveIn，inject，getBytes 等方法
        loaded.saveIn(new File(path));
    }


    /**
     * 动态增强的三种方式
     * 1. subclass：继承，重写原方法，静态方法不能使用，因为静态方式属于类，而不是对象，不能被继承
     * 命名策略：1. 对于父类是 JDK 自带的类的情况下：net.bytebuddy.renamed.java.lang.Object$ByteBuddy$Qjk6YzVV
     * 2. 对于父类非 JDK 自带的类的情况下：org.example.demo.demo.UserManager$ByteBuddy$iBPGIuT7
     * 2. rebase：变基，保留原方法，并重命名为：xxx$original$Fv2LRx7Q 格式，xxx 为拦截后的逻辑，静态方法也可以使用
     * 命名策略：不指定的话，和目标类的全类名保持一致
     * 3. redefine: 重定义，原方法不再保留，静态方法不能使用，因为不会保留原方法，所以拦截器中不能通过 @SuperCall 调用
     * 命名策略：不指定的话，和目标类的全类名保持一致
     */
    @Test
    public void test3() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                // .rebase(UserManager.class)
                // .redefine(UserManager.class)
                .name("a.b.SubObject")

                // named：通过名字指定要拦截的方法
                .method(ElementMatchers.named("getUsername")
                        .and(
                                ElementMatchers.returns(TypeDescription.ForLoadedType.of(String.class))
                                // .or(ElementMatchers.returns(TypeDescription.ForLoadedType.of(Class.class)))
                                // .or(ElementMatchers.returns(TypeDescription.ForLoadedType.of(Object.class)))
                        )
                )
                // 指定拦截到方法后，应该如何处理
                .intercept(FixedValue.value("hello bytebuddy"))

                .method(ElementMatchers.named("print")
                        .and(ElementMatchers.returns(TypeDescription.ForLoadedType.of(void.class))))
                .intercept(FixedValue.value(TypeDescription.ForLoadedType.of(void.class)))

                .method(ElementMatchers.named("getAge")
                        .and(ElementMatchers.returns(TypeDescription.ForLoadedType.of(int.class))))
                .intercept(FixedValue.value(18))

                .make();


        unloaded.saveIn(new File(path));
    }

    /**
     * 插入新方法
     */
    @Test
    public void test4() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubObject")
                // defineMethod：定义方法的名字、返回值、修饰符
                .defineMethod("getAddress", String.class, Modifier.PUBLIC + Modifier.STATIC)
                // withParameter: 指定方法的参数
                .withParameter(Integer.class, "id")
                .withParameter(String.class, "name")
                // 指定拦截到方法后，应该如何处理
                .intercept(FixedValue.value("Anhui Hefei"))
                .make();

        unloaded.saveIn(new File(path));
    }

    /**
     * 插入新属性
     */
    @Test
    public void test5() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubObject")
                // defineField：定义属性的名字、返回值、修饰符
                .defineField("name", String.class, Modifier.PRIVATE)
                // 指定 name 的 getter 和 setter 所在的接口
                .implement(UserNameInterface.class)
                // 指定拦截到方法后，应该如何处理
                .intercept(FieldAccessor.ofField("name"))
                .make();

        unloaded.saveIn(new File(path));
    }

    /**
     * 方法委托：自定义拦截逻辑时使用
     */
    @Test
    public void test6() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubObject")
                .method(ElementMatchers.named("getUsername"))
                // 默认委托到 UserManageInterceptor1 类中与拦截的方法签名一致的静态方法
                // .intercept(MethodDelegation.to(UserManageInterceptor1.class))
                // 默认委托到 UserManageInterceptor2 类中与拦截的方法签名一致的方法
                // .intercept(MethodDelegation.to(new UserManageInterceptor2()))
                // 通过 bytebuddy 的注解来指定增强的方法
                .intercept(MethodDelegation.to(new UserManageInterceptor3()))
                .make();

        DynamicType.Loaded<UserManager> loaded = unloaded.load(this.getClass().getClassLoader());
        Class<? extends UserManager> loadedClass = loaded.getLoaded();

        UserManager userManager = loadedClass.getDeclaredConstructor().newInstance();
        System.out.println("userManager.getUsername() = " + userManager.getUsername(1L));

        unloaded.saveIn(new File(path));
    }

    /**
     * 动态修改方法入参
     * 使用 @Morph
     * 1. 自定义一个接口：MorphCallable
     * 2. 在 UserManageInterceptor4 拦截器处使用 @Morph 替代 @SuperCall
     * 3. 指定拦截器前，需要先调用 withBinders 指定 @Morph 指定的参数类型
     */
    @Test
    public void test7() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubObject")
                .method(ElementMatchers.named("getUsername"))
                // 通过 bytebuddy 的注解来指定增强的方法
                .intercept(MethodDelegation
                        .withDefaultConfiguration()
                        // 在 UserManageInterceptor4 中使用 MorphCallable 之前需要告诉 @Morph 的类型是 MorphCallable 这个类
                        .withBinders(Morph.Binder.install(MorphCallable.class))
                        .to(new UserManageInterceptor4()))
                .make();

        DynamicType.Loaded<UserManager> loaded = unloaded.load(this.getClass().getClassLoader());
        Class<? extends UserManager> loadedClass = loaded.getLoaded();

        UserManager userManager = loadedClass.getDeclaredConstructor().newInstance();

        // userManager.getUsername() = 用户 ID：101 的名字是：e8af7790-cb3b-4ce7-95d5-d588a2826562
        System.out.println("userManager.getUsername() = " + userManager.getUsername(1L));

        unloaded.saveIn(new File(path));
    }


    /**
     * 对构造方法进行插桩
     */
    @Test
    public void test8() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubObject")
                // 拦截构造方法
                // .constructor(ElementMatchers.isDeclaredBy(UserManager.class).and(ElementMatchers.takesArguments(String.class)))
                .constructor(ElementMatchers.any())
                // 执行在构造方法执行完成之后，再委托给拦截器
                .intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(new UserManageInterceptor5())))
                .make();

        DynamicType.Loaded<UserManager> loaded = unloaded.load(this.getClass().getClassLoader());
        Class<? extends UserManager> loadedClass = loaded.getLoaded();

        UserManager userManager = loadedClass.getDeclaredConstructor(String.class).newInstance("hello");

        unloaded.saveIn(new File(path));

    }

    /**
     * 对静态方法进行插桩
     */
    @Test
    public void test9() throws Exception {
        File file = new File("/Users/wangweijun/IdeaProjects/java-all-blue/demo-bytebuddy/src/test/java/org/example/demo/Test01.java");
        System.out.println("FileUtils.sizeOf(file) = " + FileUtils.sizeOf(file));

        DynamicType.Unloaded<FileUtils> unloaded = new ByteBuddy()
                // 静态方法不能使用 subclass，因为静态方式属于类，而不是对象，不能被继承
                // .subclass(FileUtils.class)
                // 静态方法不能使用 redefine，因为不会保留原方法，所以拦截器中不能通过 @SuperCall 调用
                // .redefine(FileUtils.class)
                .rebase(FileUtils.class)
                .name("a.b.SubObject")
                // 拦截构造方法
                .method(ElementMatchers.named("sizeOf"))
                // 执行在构造方法执行完成之后，再委托给拦截器
                .intercept(MethodDelegation.to(new UserManageInterceptor6()))
                .make();

        DynamicType.Loaded<FileUtils> loaded = unloaded.load(this.getClass().getClassLoader());
        Class<? extends FileUtils> loadedClass = loaded.getLoaded();

        Method sizeOfMethod = loadedClass.getMethod("sizeOf", File.class);
        Object fileSize = sizeOfMethod.invoke(null, file);
        System.out.println("fileSize = " + fileSize);

        unloaded.saveIn(new File(path));
    }


    /**
     * 使用子类加载器：打破双亲委派机制
     */
    @Test
    public void test10() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubObject")
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("hello bytebuddy"))
                .make();

        // 默认类加载器
        // DynamicType.Loaded<UserManager> loaded = unloaded.load(this.getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);

        // 使用 ClassLoadingStrategy.Default.CHILD_FIRST 子类加载器，打破类双亲委派机制
        DynamicType.Loaded<UserManager> loaded = unloaded.load(this.getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST);


        Class<? extends UserManager> loadedClass = loaded.getLoaded();

        UserManager userManager = loadedClass.getDeclaredConstructor().newInstance();
        System.out.println("userManager.getClass().getClassLoader() = " + userManager.getClass().getClassLoader());
        System.out.println("userManager.toString() = " + userManager.toString());

        loaded.saveIn(new File(path));
    }

    /**
     * 自定义类加载路径：加载应用意外的类
     * 1. 可以从 Jar 包加载：ClassFileLocator.ForJarFile.of("xxx")
     * 2. 可以从 文件夹 加载：new ClassFileLocator.ForFolder("xxx")，只能加载 class 文件
     * 如果有相关以来，都需要加载进来
     */
    @Test
    public void test11() throws Exception {
        // 从指定的 Jar 包加载
        String beanPath = "/Users/wangweijun/.m2/repository/org/springframework/spring-beans/6.0.11/spring-beans-6.0.11.jar";
        // spring-beans 依赖了 spring-core，所以都需要加载进来
        String corePath = "/Users/wangweijun/.m2/repository/org/springframework/spring-core/6.0.11/spring-core-6.0.11.jar";
        ClassFileLocator beanJarFileLocator = ClassFileLocator.ForJarFile.of(new File(beanPath));
        ClassFileLocator coreJarFileLocator = ClassFileLocator.ForJarFile.of(new File(corePath));

        // 从指定目录加载 class 文件
        String folder = "/Users/wangweijun/IdeaProjects/demo/target/classes";
        ClassFileLocator.ForFolder folderLocator = new ClassFileLocator.ForFolder(new File(folder));

        // 系统类加载器，必须要有，否则找不到 JDK
        ClassFileLocator systemLocator = ClassFileLocator.ForClassLoader.ofSystemLoader();

        // 组合绑定
        ClassFileLocator.Compound compound = new ClassFileLocator.Compound(beanJarFileLocator, coreJarFileLocator, folderLocator, systemLocator);

        TypePool typePool = TypePool.Default.of(compound);

        // 写入全类名，获取 typeDefinition
        TypeDescription typeDefinitionForJar = typePool.describe("org.springframework.beans.factory.support.RootBeanDefinition").resolve();

        DynamicType.Unloaded<Object> unloadedForJar = new ByteBuddy()
                .redefine(typeDefinitionForJar, compound)
                // .name("a.b.RootBeanDefinition")
                .method(ElementMatchers.named("getTargetType"))
                .intercept(FixedValue.nullValue())
                .make();

        unloadedForJar.saveIn(new File(path));


        TypeDescription typeDefinitionForFolder = typePool.describe("com.example.demo.domain.Test").resolve();

        DynamicType.Unloaded<Object> unloadedForFolder = new ByteBuddy()
                .redefine(typeDefinitionForFolder, compound)
                // .name("a.b.RootBeanDefinition")
                .method(ElementMatchers.named("hello"))
                .intercept(FixedValue.nullValue())
                .make();

        unloadedForFolder.saveIn(new File(path));
    }

    /**
     * 清空方法体
     * 比如：保护源码，防止反编译获取源码
     */
    @Test
    public void test12() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .redefine(UserManager.class)
                // 只清空 UserManager.class 和父类的中的方法
                // .method(ElementMatchers.any())
                // 只清空 UserManager.class 中的方法
                .method(ElementMatchers.any().and(ElementMatchers.isDeclaredBy(UserManager.class)))
                // 清空方法体
                .intercept(StubMethod.INSTANCE)
                .make();


        unloaded.saveIn(new File(path));
    }

}
