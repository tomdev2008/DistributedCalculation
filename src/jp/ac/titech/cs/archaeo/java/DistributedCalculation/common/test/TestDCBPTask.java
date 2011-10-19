package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.DCBPTask;

import org.apache.log4j.Logger;

public final class TestDCBPTask {

  private DCBPTask task;

  private Logger logger;

  private ResourceManager resourceManager;

  private ExecutorService taskExecutorService;

  public static void main(String[] args) {
    new TestDCBPTask(args);
  }

  public TestDCBPTask(String[] args) {
    logger = Utility.getLogger(TestDCBPTask.class);
    taskExecutorService = Executors.newSingleThreadExecutor();
    String inputPropertiesFileName = args[0];
    Properties inputProperties = new Properties();
    try {
      inputProperties.load(new FileReader(new File(inputPropertiesFileName)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    Double[] inputData = new Double[1000];
    double gradationValueRange = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.gradationValueRange"));
    for (int i = 0; i < inputData.length; i++) {
      inputData[i] = (i % 2 == 0) ? -Math.random() * (gradationValueRange / 2)
          : Math.random() * (gradationValueRange / 2);
      logger.info("inputData[" + i + "] = " + inputData[i]);
    }
    task = new DCBPTask(inputProperties, inputData);
    logger.info("DCBPTask run...");
    Future<Long> future = taskExecutorService.submit(task);
    Double[] resultData = null;
    try {
      logger.info("ProcTime is " + future.get());
      resultData = task.getResultData();
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } catch (ExecutionException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    if (resultData != null) {
      for (int i = 0; i < resultData.length; i++) {
        logger.info("resultData[" + i + "] = " + resultData[i]);
      }
    }
  }
}
