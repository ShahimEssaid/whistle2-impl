package com.essaid.whistle.common.util;

import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.data.NullData;
import com.google.cloud.verticals.foundations.dataharmonization.debug.DebugInfo;
import com.google.cloud.verticals.foundations.dataharmonization.exceptions.NoMatchingOverloadsException;
import com.google.cloud.verticals.foundations.dataharmonization.function.CallableFunction;
import com.google.cloud.verticals.foundations.dataharmonization.function.context.RuntimeContext;
import com.google.cloud.verticals.foundations.dataharmonization.proto.Pipeline.FunctionCall;
import com.google.cloud.verticals.foundations.dataharmonization.registry.PackageRegistry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;

public class FunctionUtils {

  public static Optional<CallableFunction> getFunction(RuntimeContext context, String packageName
      , String functionName
      , Data... args) {

    PackageRegistry<CallableFunction> packageRegistry = context.getRegistries()
        .getFunctionRegistry(packageName);
    Set<CallableFunction> overloads = packageRegistry.getOverloads(
        ImmutableSet.of(packageName.trim()), functionName);

    if (overloads.isEmpty()) {
      return Optional.empty();
    }

    try {
      CallableFunction select = context.getOverloadSelector()
          .select(ImmutableList.copyOf(overloads), args);
      return Optional.of(select);
    } catch (NoMatchingOverloadsException e) {
      return Optional.empty();
    }
  }

  public static Data optionallyCallFunction(RuntimeContext context, String packageName,
      String functionName, Data... args){
    Optional<CallableFunction> function = getFunction(context, packageName, functionName, args);

    if(function.isEmpty()){
      return NullData.instance;
    }

    DebugInfo debugInfo = context.top().getDebugInfo();
    if(debugInfo != null){
      debugInfo.setCallsiteToNextStackFrame(FunctionCall.getDefaultInstance());
    }

    CallableFunction callableFunction = function.get();

    return callableFunction.call(context, args);

  }
}
