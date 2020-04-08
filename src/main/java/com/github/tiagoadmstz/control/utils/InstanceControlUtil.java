package com.github.tiagoadmstz.control.utils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public abstract class InstanceControlUtil {

    private static final WindowCloseListener LISTENER = new WindowCloseListener();
    private static final Map<String, Object> INSTANCIAS = new HashMap();

    public static void putInstance(String nome, Object object) {
        INSTANCIAS.put(nome, object);
        if (object instanceof JFrame) {
            JFrame frame = (JFrame) object;
            frame.addWindowListener(LISTENER);
        }
    }

    public static <T extends Object> T getInstance(Class<? extends T> clazz) {
        try {
            if (!INSTANCIAS.containsKey(clazz.getSimpleName())) return createInstance(clazz.getSimpleName());
            return (T) INSTANCIAS.get(clazz.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isInstanced(Class<? extends Object> clazz) {
        return INSTANCIAS.containsKey(clazz.getSimpleName());
    }

    public static void removeInstance(String nome) {
        INSTANCIAS.remove(nome);
    }

    private static <T extends Object> T createInstance(String nome) throws Exception {
        Class<?> clazz = Class.forName(nome);
        for (Constructor construtor : clazz.getConstructors()) {
            if (construtor.getParameterCount() == 0) {
                Object ob = construtor.newInstance();
                putInstance(clazz.getSimpleName(), ob);
                return (T) ob;
            }
        }
        return null;
    }

    private static void clear() {
        INSTANCIAS.values().forEach(ob -> {
            JFrame frame = (JFrame) ob;
            frame.dispose();
        });
        INSTANCIAS.clear();
    }

    private static class WindowCloseListener extends WindowAdapter {

        public WindowCloseListener() {
            super();
        }

        @Override
        public void windowClosed(WindowEvent event) {
            if (event.getID() == WindowEvent.WINDOW_CLOSED) removeInstance(event.getSource().getClass().getSimpleName());
        }
    }

}
