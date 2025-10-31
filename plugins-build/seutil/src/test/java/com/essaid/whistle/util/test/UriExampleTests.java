package com.essaid.whistle.util.test;


import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UriExampleTests {

  @Test
  @Disabled
  void jar() throws URISyntaxException, IOException {
    URL url = UriExampleTests.class.getResource("/base64decode_1");
    // jar:file:/home/essaids/.gradle/caches/modules-2/files-2.1/com.essaid.groupId.com.google.cloud.verticals.foundations.dataharmonization/runtime/dev-SNAPSHOT/903d75244e63f22a9d476ed6bfdd558ca8f60712/runtime-dev-SNAPSHOT.jar!/base64decode_1

    JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
    URL jarFileURL = urlConnection.getJarFileURL();

    URI uri = url.toURI();
    // jar:file:/home/essaids/.gradle/caches/modules-2/files-2.1/com.essaid.groupId.com.google.cloud.verticals.foundations.dataharmonization/runtime/dev-SNAPSHOT/903d75244e63f22a9d476ed6bfdd558ca8f60712/runtime-dev-SNAPSHOT.jar!/base64decode_1
    Class<? extends URI> uriClass = uri.getClass();

    String file = url.getFile();
    // file:/home/essaids/.gradle/caches/modules-2/files-2.1/com.essaid.groupId.com.google.cloud.verticals.foundations.dataharmonization/runtime/dev-SNAPSHOT/903d75244e63f22a9d476ed6bfdd558ca8f60712/runtime-dev-SNAPSHOT.jar!/base64decode_1

    File fileObject = new File(uri);

    List<String> lines = Files.readAllLines(fileObject.toPath());

    System.out.println(url);
  }

}
