package com.kakaobank.kosmos.task;

import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.management.Attribute;
import java.io.IOException;
import java.lang.annotation.Native;
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

    @PostConstruct
    public void init() {
        getTaskClasses();
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
                String[] classNames = StringUtils.commaDelimitedListToStringArray(attributes.getValue("class-names").replace("[", "").replace("]", ""));
                System.out.println(Arrays.toString(classNames));

                classNameSet.addAll(Arrays.asList(classNames));

                URL url = jar.toRealPath().toUri().toURL();
                urls.add(url);

                System.out.println(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClassLoader loader = URLClassLoader.newInstance(urls.toArray(new URL[0]),
                getClass().getClassLoader());

        for (String className : classNameSet) {
            try {
                Class<? extends Task> clazz = loader.loadClass(className).asSubclass(Task.class);
                System.out.println(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return Collections.emptySet();
    }

    private Class<? extends Task> loadTaskClass(ClassLoader classLoader) {


        return null;
    }
}
