package com.essaid.whistle.cli.command;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.ScopeType;

@Command(
    mixinStandardHelpOptions = true,
    subcommands = {
        SimpleCommand.class
    }
)
public class MainCommand implements Callable<Void> {

  @Override
  public Void call() throws Exception {
    return null;
  }
}
