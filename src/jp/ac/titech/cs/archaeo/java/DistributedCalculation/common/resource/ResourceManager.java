package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource;

import java.util.ResourceBundle;

public final class ResourceManager {

  private ResourceBundle resourceBundle;
  private ResourceBundle buildNoResourceBundle;

  private static ResourceManager resourceManager;

  private ResourceManager() {
    resourceBundle = ResourceBundle
        .getBundle("jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.DistributedCalculation");
    buildNoResourceBundle = ResourceBundle
        .getBundle("jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.buildNo");
  }

  public static synchronized ResourceManager getInstance() {
    if (resourceManager == null) {
      resourceManager = new ResourceManager();
    }
    return resourceManager;
  }

  public String getString(String key) {
    String value = null;
    if (key.equals("buildNo")) {
      value = buildNoResourceBundle.getString(key);
    } else {
      value = resourceBundle.getString(key);
    }
    return value;
  }
}
