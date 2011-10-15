package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.task;

public enum TaskExecState {
  
  NotExecuted, // it has not been ever executed.
  NowExecuting, // it is executing now.
  Finished, // it was finished.
  ;
  
  public boolean isExcuted() {
    String name = name();
    if (name.equals("Finished") || name.equals("NowExecuting"))
      return true;
    else
      return false;
  }

}
