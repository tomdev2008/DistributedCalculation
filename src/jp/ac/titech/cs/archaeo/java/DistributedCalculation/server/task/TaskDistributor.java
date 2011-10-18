package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.task;

import java.util.Properties;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.Task;

/**
 * @author lycaon
 */
public abstract class TaskDistributor<TaskType extends Task<?, ?>> {

  protected Properties inputProperties;
  
  public TaskDistributor(Properties inputProperties) {
    this.inputProperties = inputProperties;
  }

  abstract public void noticeFailure(TaskType task);

  abstract public double getProgressRatio();

  abstract public TaskType requestNextTask(TaskType previousTask,
      long loadCapability);
  
}
