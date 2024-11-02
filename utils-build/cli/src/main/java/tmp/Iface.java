package tmp;

import java.lang.reflect.Method;

public class Iface {

  static Method INSTANCE;

  static {
    try {
      INSTANCE = Iface.class.getDeclaredMethod("getInstance");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  Iface getInstance(){
    return null;
  }


  public static void main(String[] args) {
    new Iface();
  }
}
