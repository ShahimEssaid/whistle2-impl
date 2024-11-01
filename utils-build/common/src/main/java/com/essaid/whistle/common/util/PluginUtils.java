package com.essaid.whistle.common.util;

import com.google.cloud.verticals.foundations.dataharmonization.function.context.MetaData;
import com.google.cloud.verticals.foundations.dataharmonization.function.context.Registries;
import com.google.cloud.verticals.foundations.dataharmonization.plugin.Plugin;
import java.util.HashSet;
import java.util.Set;

public class PluginUtils {

  public static final String LOADED_PLUGIN_TYPES = PluginUtils.class.getName()+".LOADED_PLUGIN_TYPES";
  public static boolean loadPlugin(Plugin plugin, Registries registries, MetaData metaData){
    Set<Class<? extends Plugin>> loadedPluginTypes =  metaData.getMeta(LOADED_PLUGIN_TYPES);
    if(loadedPluginTypes == null){
      loadedPluginTypes = new HashSet<>();
      metaData.setMeta(LOADED_PLUGIN_TYPES, loadedPluginTypes);
    }

    if(loadedPluginTypes.add(plugin.getClass())){

      return true;
    }

    return false;
  }

}
