package com.github.s8u.annocommand;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@UtilityClass
public class ReflectionUtil {

    @SneakyThrows(Exception.class)
    public static Collection<Method> getMethodsInOrder(Class targetClass) {
        String className = targetClass.getName().replace(".", "/") + ".class";
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(targetClass.getClassLoader().getResourceAsStream(className)));

        String classData = "";
        String line;
        while ((line = reader.readLine()) != null) {
            classData += line;
        }
        classData = classData.substring(classData.indexOf("LineNumberTable"), classData.lastIndexOf("SourceFile"));

        Map<Integer, Method> methods = new TreeMap<>();
        for (Method method : targetClass.getDeclaredMethods()) {
            methods.put(classData.indexOf(method.getName()), method);
        }

        return methods.values();
    }

}
