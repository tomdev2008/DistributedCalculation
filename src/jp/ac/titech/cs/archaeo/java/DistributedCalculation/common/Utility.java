package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common;

import java.rmi.server.UID;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class Utility {

  public static final String HARDWARE_ADDRESS_SEPARATOR = ":";

  static {
    Logger logger = getLogger(Utility.class);

    // Log4j configure
    PropertyConfigurator
        .configure(Utility.class
            .getResource("/jp/ac/titech/cs/archaeo/java/DistributedCalculation/common/resource/log4j.properties"));
    logger.info("Log4j configured.");
  }

  public static Logger getLogger(Class<?> cls) {
    return Logger.getLogger(cls);
  }

  public static UID getUID() {
    return new UID();
  }

  public static String rawHardwareAddrToString(byte[] rawHardwareAddr) {
    StringBuilder sb = new StringBuilder();
    for (byte digit : rawHardwareAddr) {
      if (sb.length() > 0)
        sb.append(HARDWARE_ADDRESS_SEPARATOR);
      if ((digit & 0xFF) < 0x10)
        sb.append(0);
      sb.append(digit & 0xFF);
    }
    return sb.toString();
  }

}
