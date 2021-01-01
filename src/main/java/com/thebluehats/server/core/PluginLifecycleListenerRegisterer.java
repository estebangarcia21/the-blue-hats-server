package com.thebluehats.server.core;

import com.thebluehats.server.game.utils.PluginLifecycleListener;
import com.thebluehats.server.game.utils.Registerer;

import java.util.ArrayList;
import java.util.Arrays;

public class PluginLifecycleListenerRegisterer implements Registerer<PluginLifecycleListener> {
    private final ArrayList<PluginLifecycleListener> pluginLifecycleListeners = new ArrayList<>();

    @Override
    public void register(PluginLifecycleListener[] objects) {
        pluginLifecycleListeners.addAll(Arrays.asList(objects));
    }

    public ArrayList<PluginLifecycleListener> getListeners() {
        return pluginLifecycleListeners;
    }
}
