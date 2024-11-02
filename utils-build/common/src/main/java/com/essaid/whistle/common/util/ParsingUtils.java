package com.essaid.whistle.common.util;

import com.google.cloud.verticals.foundations.dataharmonization.Environment;
import com.google.cloud.verticals.foundations.dataharmonization.Transpiler;
import com.google.cloud.verticals.foundations.dataharmonization.debug.proto.Debug.FileInfo;
import com.google.cloud.verticals.foundations.dataharmonization.imports.ImportPath;
import com.google.cloud.verticals.foundations.dataharmonization.proto.Pipeline.PipelineConfig;

public class ParsingUtils {

  public static Environment createDefaultEnvironment() {
    // see com.google.cloud.verticals.foundations.dataharmonization.TranspilerData.INIT_ENV_NAME
    return new Environment("<init>");
  }

  public static PipelineConfig parseWhistle(String whistleString, ImportPath importPath,
      Environment environment, boolean throwException) {
    Transpiler transpiler = new Transpiler(environment, throwException);
    FileInfo fileInfo = importPath.toFileInfo();
    PipelineConfig config = transpiler.transpile(whistleString, fileInfo);

    config.getImportsList();
    return config;
  }

}
