package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.rmi;

public enum RMICommandCode {

  RequestTask, // correspond to requestTask()
  RegisterClient, // correspond to registerClient()
  SendAliveSignal, // correspond to sendAliveSignal()
  Quit, // correspond to quit()
  ;

}
