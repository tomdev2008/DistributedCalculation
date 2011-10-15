package jp.ac.titech.cs.archaeo.java.DistributedCalculation.client.support;

import java.rmi.server.UID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Result;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ServerInterface;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.Task;

import org.apache.log4j.Logger;

public final class AutoTaskExecutor implements Runnable {

  private Logger logger;

  private ResourceManager resourceManager;

  private ExecutorService taskExecutor;

  private Task<?, ?> task;

  private Future<Long> taskHandle;

  private ServerInterface server;

  private UID clientProfileUID;
  
  public AutoTaskExecutor(ServerInterface server, UID clientProfileUID) {
    logger = Utility.getLogger(AutoTaskExecutor.class);
    resourceManager = ResourceManager.getInstance();
    this.server = server;
    this.clientProfileUID = clientProfileUID;
    taskExecutor = Executors.newFixedThreadPool(1);
    task = null;
  }
  
  @Override
  public void run() {
    while (true) {
      Result<?> result = server.requestTask(task, clientProfileUID);
      task = (Task<?, ?>) result.getResult();
      taskHandle = taskExecutor.submit(task);
      try {
        long procTime = taskHandle.get();
        logger.info("ProcTime is " + procTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      } catch (ExecutionException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
    }
  }

}
