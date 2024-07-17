package com.essaid.whistle.common;

import com.google.cloud.verticals.foundations.dataharmonization.data.Array;
import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.exceptions.NoMatchingOverloadsException;
import com.google.cloud.verticals.foundations.dataharmonization.function.CallableFunction;
import com.google.cloud.verticals.foundations.dataharmonization.function.context.RuntimeContext;
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

}
