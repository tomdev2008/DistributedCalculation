package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common;

import java.rmi.server.UID;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.Task;

public interface ServerInterface {

  public Result<Task<?, ?>> requestTask(Task<?, ?> previousTask,
      UID clientProfileUID);

  public Result<UID> registerClient(ClientProfile clientProfile);

  public Result<Boolean> sendAliveSignal(UID clientProfileUID);

  public void quit(UID clientProfileUID);

}
