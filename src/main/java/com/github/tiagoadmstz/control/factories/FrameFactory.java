package com.github.tiagoadmstz.control.factories;

import com.github.tiagoadmstz.control.utils.InstanceControlUtil;
import lombok.SneakyThrows;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class FrameFactory {

    public static <T extends JFrame> T getInstance(Class<T> frameClass, boolean visible, boolean singleton, Object... args) {
        T instance = singleton && InstanceControlUtil.isInstanced(frameClass) ? InstanceControlUtil.getInstance(frameClass) : createInstance(frameClass, args);
        if (visible) instance.setVisible(true);
        if (singleton) InstanceControlUtil.putInstance(frameClass.getSimpleName(), instance);
        return instance;
    }

    public static <T extends JFrame> T getInstanceNewThread(Class<T> frameClass, boolean visible, Object... args) {
        new Thread(() -> {
            T instance = InstanceControlUtil.isInstanced(frameClass) ? InstanceControlUtil.getInstance(frameClass) : createInstance(frameClass, args);
            InstanceControlUtil.putInstance(frameClass.getSimpleName(), frameClass);
            if (visible) instance.setVisible(true);
        }).start();
        return InstanceControlUtil.getInstance(frameClass);
    }

    public static void verifyInstanceAndDispose(Class<? extends JFrame> frameClass) {
        if (InstanceControlUtil.isInstanced(frameClass)) {
            InstanceControlUtil.getInstance(frameClass).dispose();
            InstanceControlUtil.removeInstance(frameClass.getSimpleName());
        }
    }

    @SneakyThrows
    private static <T extends JFrame> T createInstance(Class<T> jFrame, Object... args) {
        FrameFactoryMethods frameFactoryMethods = new FrameFactoryMethods();
        Method declaredMethod = getMethod(jFrame, frameFactoryMethods);
        if (declaredMethod == null) return (T) jFrame.getConstructor().newInstance();
        return (T) declaredMethod.invoke(frameFactoryMethods, args);
    }

    private static <T extends JFrame> Method getMethod(Class<T> jFrame, FrameFactoryMethods frameFactoryMethods) {
        Optional<Method> optionalMethod = Arrays.stream(frameFactoryMethods.getClass().getDeclaredMethods()).filter(method -> "create".concat(jFrame.getSimpleName()).equals(method.getName())).findFirst();
        if (optionalMethod.isPresent()) return optionalMethod.get();
        return null;
    }

}