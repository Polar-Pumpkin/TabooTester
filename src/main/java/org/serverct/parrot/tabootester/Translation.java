package org.serverct.parrot.tabootester;

import taboolib.common.io.Project1Kt;
import taboolib.common.platform.command.CommandHeader;

import java.util.HashMap;
import java.util.Map;

public class Translation {

    public void translate() {
        final Map<String, Class<?>> map = new HashMap<>();
        Project1Kt.getRunningClasses().stream()
                .filter(clazz -> clazz.isAnnotationPresent(CommandHeader.class))
                .forEach(clazz -> map.put(clazz.getAnnotation(CommandHeader.class).name(), clazz));
    }

}
