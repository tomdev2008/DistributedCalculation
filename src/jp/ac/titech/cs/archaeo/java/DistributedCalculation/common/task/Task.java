package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task;

import java.io.Serializable;
import java.rmi.server.UID;
import java.util.concurrent.Callable;

public abstract class Task<InputDataType, ResultDataType> implements
    Callable<Long>, Serializable {

  private static final long serialVersionUID = -9023511989137561830L;
  
  protected InputDataType inputData;
  
  protected ResultDataType resultData;

  protected final UID taskUID;

  abstract protected void process();

  public Task(InputDataType inputData) {
    taskUID = new UID();
    this.inputData = inputData;
    this.resultData = null;
  }

  @Override
  public final Long call() {
    long startTime = System.currentTimeMillis();
    process();
    return System.currentTimeMillis() - startTime;
  }

  public final ResultDataType getResultData() {
    return resultData;
  }

  public final UID getUID() {
    return taskUID;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final boolean equals(Object obj) {
    if (obj instanceof Task) {
      Task<?, ?> task = (Task<?, ?>) obj;
      return task.getUID().equals(this.getUID());
    }
    return false;
  }

}
