package com.essaid.whistle.common.util;

import com.google.cloud.verticals.foundations.dataharmonization.function.CallableFunction;
import com.google.cloud.verticals.foundations.dataharmonization.function.context.impl.DefaultRegistries;
import com.google.cloud.verticals.foundations.dataharmonization.imports.Loader;
import com.google.cloud.verticals.foundations.dataharmonization.imports.Parser;
import com.google.cloud.verticals.foundations.dataharmonization.modifier.arg.ArgModifier;
import com.google.cloud.verticals.foundations.dataharmonization.plugin.Option;
import com.google.cloud.verticals.foundations.dataharmonization.registry.PackageRegistry;
import com.google.cloud.verticals.foundations.dataharmonization.registry.Registry;
import com.google.cloud.verticals.foundations.dataharmonization.registry.impl.DefaultPackageRegistry;
import com.google.cloud.verticals.foundations.dataharmonization.target.Target;
import com.google.cloud.verticals.foundations.dataharmonization.target.Target.Constructor;
import java.util.Map;

public class RegistryUtils {


  public static DefaultRegistries createDefaultRegistries() {
    return null;
  }

  public static DefaultRegistries createDefaultRegistries(
      PackageRegistry<CallableFunction> functionPackageRegistry,
      Map<String, PackageRegistry<CallableFunction>> pluginFunctionRegistries,
      PackageRegistry<Constructor> targetPackageRegistry,
      Registry<ArgModifier> argModifierRegistry,
      Registry<Loader> loaderRegistry,
      Registry<Parser> parserRegistry,
      Registry<Option> optionRegistry
  ) {
    return new DefaultRegistries(functionPackageRegistry, pluginFunctionRegistries,
        targetPackageRegistry, argModifierRegistry, loaderRegistry, parserRegistry, optionRegistry);
  }

  public  static DefaultPackageRegistry<CallableFunction> createDefaultEmptyFunctionRegistry(){
    return new DefaultPackageRegistry<>();
  }

  public  static DefaultPackageRegistry<Target.Constructor> createDefaultEmptyTargetRegistry(){
    return new DefaultPackageRegistry<>();
  }

  public  static DefaultPackageRegistry<ArgModifier> createDefaultEmptyArgModifierRegistry(){
    return new DefaultPackageRegistry<>();
  }


}
