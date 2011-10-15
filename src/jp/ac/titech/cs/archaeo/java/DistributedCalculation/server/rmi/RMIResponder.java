package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.rmi.server.UnicastRemoteObject;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ClientProfile;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Result;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ServerInterface;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.rmi.RMICommandCode;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.rmi.RMIResponderInterface;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.Task;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.support.ServerApp;

import org.apache.log4j.Logger;

public final class RMIResponder extends UnicastRemoteObject implements
    RMIResponderInterface {

  private static final long serialVersionUID = -4958417596870302924L;

  public static final String SERVICE_NAME = "DistributedCalculation";

  private static RMIResponder instance;

  private final Logger logger;

  private ServerInterface server;

  protected RMIResponder(int port) throws RemoteException {
    super(port);
    logger = Utility.getLogger(RMIResponder.class);
    server = new ServerApp();
  }

  public synchronized static RMIResponder getInstance(int port)
      throws RemoteException {
    if (instance == null)
      instance = new RMIResponder(port);
    return instance;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <ResultDataType extends Serializable> Result<ResultDataType> request(
      RMICommandCode commandCode, Object... args) throws RemoteException {
    Result<ResultDataType> result = null;
    switch (commandCode) {
    case RequestTask:
      result = (Result<ResultDataType>) server.requestTask(
          (Task<?, ?>) args[0], (UID) args[1]);
      break;
    case RegisterClient:
      result = (Result<ResultDataType>) server
          .registerClient((ClientProfile) args[0]);
      break;
    case SendAliveSignal:
      result = (Result<ResultDataType>) server.sendAliveSignal((UID) args[0]);
      break;
    case Quit:
      server.quit((UID) args[0]);
      break;
    default:
      logger.info("unknown rmi commandCode: " + commandCode);
      break;
    }
    return result;
  }

}
