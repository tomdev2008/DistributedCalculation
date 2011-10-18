package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.task;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.DCBPTask;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.io.RawAsciiDataFileReaader;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.io.RawAsciiDataFileWriter;

import org.apache.log4j.Logger;

public final class DCBPTaskDistributor extends TaskDistributor<DCBPTask> {

  private final Logger logger;

  private final ResourceManager resourceManager;

  private int nTraces;

  private Double[][] inputData;

  private Double[][] resultData;

  private TaskExecState[] taskExecStates;

  private ConcurrentHashMap<Integer, DCBPTask> tasksMap;

  public DCBPTaskDistributor(Properties inputProperties) {
    super(inputProperties);
    logger = Utility.getLogger(DCBPTaskDistributor.class);
    resourceManager = ResourceManager.getInstance();

    String inputFileName = this.inputProperties
        .getProperty("preporcess.inputfile.name");
    logger.info("inputFileName = " + inputFileName);
    File inputDataFile = new File(inputFileName);
    try {
      inputData = RawAsciiDataFileReaader.read(inputDataFile);
      nTraces = inputData.length;
      resultData = new Double[nTraces][];
    } catch (IOException e) {
      e.printStackTrace();
    }
    taskExecStates = new TaskExecState[nTraces];
    tasksMap = new ConcurrentHashMap<Integer, DCBPTask>();
    for (int traceN = 0; traceN < nTraces; traceN++) {
      taskExecStates[traceN] = TaskExecState.NotExecuted;
      DCBPTask task = new DCBPTask(inputData[traceN]);
      tasksMap.put(traceN, task);
    }
  }

  @Override
  public double getProgressRatio() {
    return (double) tasksMap.size() / (double) inputData.length;
  }

  @Override
  public DCBPTask requestNextTask(DCBPTask previousTask, long loadCapability) {
    DCBPTask newTask = null;
    if (previousTask != null) {
      Set<Map.Entry<Integer, DCBPTask>> entrySet = tasksMap.entrySet();
      for (Map.Entry<Integer, DCBPTask> entry : entrySet) {
        if (entry.getValue().equals(previousTask)) {
          int traceN = entry.getKey();
          resultData[traceN] = previousTask.getResultData();
          tasksMap.remove(traceN);
          taskExecStates[traceN] = TaskExecState.Finished;
        }
      }
    }
    for (int traceN = 0; traceN < nTraces; traceN++) {
      if (!taskExecStates[traceN].isExcuted()) {
        newTask = tasksMap.get(traceN);
        break;
      }
      // if there were no tasks, all processes are finished.
      String outputFileName = inputProperties
          .getProperty("postprocess.resultfile.name");
      logger.info("write result file.");
      try {
        RawAsciiDataFileWriter.write(new File(outputFileName), resultData);
      } catch (IOException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
    }
    return newTask;
  }

  @Override
  public void noticeFailure(DCBPTask task) {
    if (task != null) {
      Set<Map.Entry<Integer, DCBPTask>> entrySet = tasksMap.entrySet();
      for (Map.Entry<Integer, DCBPTask> entry : entrySet) {
        if (entry.getValue().equals(task)) {
          int traceN = entry.getKey();
          taskExecStates[traceN] = TaskExecState.NotExecuted;
        }
      }
    }
  }

}
