package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.support;

import java.io.File;

import javax.swing.JFileChooser;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;

import org.apache.log4j.Logger;

public final class ViewSupport {

  private final Logger logger;

  private final ResourceManager resourceManager;

  public ViewSupport() {
    logger = Utility.getLogger(ViewSupport.class);
    resourceManager = ResourceManager.getInstance();
  }

  public File getNewInputPropertiesFile() {
    logger.info("select new inputproperties file.");
    File newInputPropertiesFile = null;

    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
    int intent = fileChooser.showOpenDialog(null);
    if (intent == JFileChooser.APPROVE_OPTION) {
      newInputPropertiesFile = fileChooser.getSelectedFile();
      logger.info("selectedFileName = " + newInputPropertiesFile.getName());
    }
    return newInputPropertiesFile;
  }

}
