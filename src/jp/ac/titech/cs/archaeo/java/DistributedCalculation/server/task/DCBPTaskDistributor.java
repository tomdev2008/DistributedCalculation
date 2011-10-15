package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.task;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.DCBPTask;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.io.RawAsciiDataFileReaader;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.support.ServerApp;

import org.apache.log4j.Logger;

public final class DCBPTaskDistributor extends TaskDistributor<DCBPTask> {

  private final Logger logger;

  private final ResourceManager resourceManager;

  private int nTraces;

  private Double[][] inputData;

  private Double[][] resultData;

  private TaskExecState[] taskExecStates;

  private ConcurrentHashMap<Integer, DCBPTask> tasksMap;

  public DCBPTaskDistributor() {
    logger = Utility.getLogger(DCBPTaskDistributor.class);
    resourceManager = ResourceManager.getInstance();

    File inputDataFile = new File(ServerApp
        .getInputProperties("preporcess.inputfile.name"));
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
    int nextTaskN = -1;
    for (int traceN = 0; traceN < nTraces; traceN++) {
      if (!taskExecStates[traceN].isExcuted()) {
        nextTaskN = traceN;
        break;
      }
    }
    newTask = tasksMap.get(nextTaskN);
    return newTask;
  }

  @Override
  public void noticeFailure(DCBPTask task) {
  }

}
