package com.essaid.whistle.cli;

import org.junit.jupiter.api.Test;

public class MainTests {

  @Test
  void help() {
    Main.main(new String[]{"simple", "--help"});
//    Main.main(new String[]{"--help"});
  }


  @Test
  void test() {
    Main.main(new String[]{"simple", "--input", "/one", "-i", "/two"});
  }


  @Test
  void argGroup() {
    Main.main(new String[]{
            "simple",
            "-m", "/main/file",
            "--input", "/one",
            "-i", "/two"
//        ,
//            "-id", "/input-dir-1",  "-idp", "pattern-1",
//            "-id", "/input-dir-2"
        }
    );

  }


  @Test
  void cores(){
    System.out.println("============= Cores: "+Runtime.getRuntime().availableProcessors());
  }

}
