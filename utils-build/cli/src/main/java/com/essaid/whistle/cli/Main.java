package com.essaid.whistle.cli;

import com.essaid.whistle.cli.command.MainCommand;
import picocli.CommandLine;

public class Main {

  public static void main(String[] args) {
    CommandLine  cl = new CommandLine(new MainCommand());
    cl.execute(args);
  }

}
