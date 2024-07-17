package com.essaid.whistle.plugin.seutil;

import com.google.cloud.verticals.foundations.dataharmonization.function.CallableFunction;
import com.google.cloud.verticals.foundations.dataharmonization.function.context.MetaData;
import com.google.cloud.verticals.foundations.dataharmonization.function.context.Registries;
import com.google.cloud.verticals.foundations.dataharmonization.plugin.FunctionCollectionBuilder;
import com.google.cloud.verticals.foundations.dataharmonization.plugin.Plugin;
import java.util.List;

public class SeUtilPlugin implements Plugin {

  public static final String PACKAGE_NAME = "seutil";
  private Registries registries;

  @Override
  public String getPackageName() {
    return PACKAGE_NAME;
  }

  @Override
  public List<CallableFunction> getFunctions() {
    FunctionCollectionBuilder functions = new FunctionCollectionBuilder(PACKAGE_NAME)
        .addAllJavaPluginFunctionsInClass(SeUtilFunctions.class);
    List<CallableFunction> build = functions.build();
    return build;
  }

  @Override
  public void onLoaded(Registries registries, MetaData metaData) {
    this.registries = registries;
  }
}
