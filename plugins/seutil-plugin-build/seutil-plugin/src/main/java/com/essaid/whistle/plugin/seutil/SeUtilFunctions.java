package com.essaid.whistle.plugin.seutil;

import com.essaid.whistle.common.FunctionUtils;
import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.function.CallableFunction;
import com.google.cloud.verticals.foundations.dataharmonization.function.context.RuntimeContext;
import com.google.cloud.verticals.foundations.dataharmonization.function.java.PluginFunction;
import java.util.Optional;

public class SeUtilFunctions {

  @PluginFunction
  public static Data fnExists(RuntimeContext context, String packageName,
      String functionName, Data... args) {
    Optional<CallableFunction> function = FunctionUtils.getFunction(context, packageName,
        functionName, args);
    return context.getDataTypeImplementation().primitiveOf(function.isPresent());
  }

}
