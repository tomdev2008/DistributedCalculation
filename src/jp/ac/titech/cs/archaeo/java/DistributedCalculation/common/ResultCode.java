package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.resource.ResourceManager;

public enum ResultCode {

  Success, // it was successful.
  Failed, // it was failed.
  NoError, // there are no problems.
  AlreadyRegistered, // the client already registered to the task daemon.
  NotAssigned, // the client was not assigned working task.
  RMIRemoteException, // remote exception invoked.
  ;

  @Override
  public String toString() {
    ResourceManager resourceManager = ResourceManager.getInstance();
    return resourceManager.getString("ResultCode." + name());
  }

}
