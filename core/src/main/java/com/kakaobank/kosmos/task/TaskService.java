package com.kakaobank.kosmos.task;

import io.netty.util.internal.StringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.management.Attribute;
import java.io.IOException;
import java.lang.annotation.Native;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

@Service
public class TaskService {

    private final ApplicationContext applicationContext;

    public TaskService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Thread thread = new Thread(() -> {
            while (true) {
                getTaskClasses();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    Set<Class<Task>> getTaskClasses() {
        Path path = Paths.get("C:\\develop\\project\\dev\\plugin_test\\tasks\\file-task\\build\\libs");

        Set<URL> urls = new HashSet<>();
        Set<String> classNameSet = new HashSet<>();
        try {
            DirectoryStream<Path> jarStream = Files.newDirectoryStream(path, "*.jar");
            for (Path jar : jarStream) {
                JarFile jarFile = new JarFile(jar.toFile());
                Manifest manifest = jarFile.getManifest();
                Attributes attributes = manifest.getMainAttributes();
                System.out.println(attributes.getValue("class-names"));
                String[] classNames = StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(attributes.getValue("class-names")).replace("[", "").replace("]", ""));
                System.out.println(Arrays.toString(classNames));

                classNameSet.addAll(Arrays.asList(classNames));

                URL url = jar.toRealPath().toUri().toURL();
                urls.add(url);

                System.out.println(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        URLClassLoader loader = URLClassLoader.newInstance(urls.toArray(new URL[0]),
                getClass().getClassLoader());

        for (String className : classNameSet) {
            try {
                Class<? extends Task> clazz = loader.loadClass(className).asSubclass(Task.class);
                System.out.println(clazz);

                try {
                    Constructor[] constructors = clazz.getConstructors();
                    for (Constructor constructor : constructors) {
                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        Object[] parameters = new Object[parameterTypes.length];
                        for (int i = 0; i < parameters.length; i++) {
                            parameters[i] = applicationContext.getBean(parameterTypes[i]);
                        }
                        Task task = (Task) constructor.newInstance(parameters);
                        task.execute("value");
                    }
//                    Task aa = clazz.newInstance();
//                    aa.execute("value");
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            loader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptySet();
    }

    private Class<? extends Task> loadTaskClass(ClassLoader classLoader) {


        return null;
    }
}
