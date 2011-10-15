package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.task;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.Task;

/**
 * @author lycaon
 */
public abstract class TaskDistributor<TaskType extends Task<?, ?>> {

  abstract public void noticeFailure(TaskType task);

  abstract public double getProgressRatio();

  abstract public TaskType requestNextTask(TaskType previousTask,
      long loadCapability);
  
}
