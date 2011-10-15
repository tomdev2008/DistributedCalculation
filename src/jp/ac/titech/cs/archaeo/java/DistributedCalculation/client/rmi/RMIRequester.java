package jp.ac.titech.cs.archaeo.java.DistributedCalculation.client.rmi;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UID;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ClientProfile;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Result;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ResultCode;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.ServerInterface;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.rmi.RMICommandCode;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.rmi.RMIResponderInterface;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task.Task;
import jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.rmi.RMIResponder;

import org.apache.log4j.Logger;

public class RMIRequester implements ServerInterface {

  private RMIResponderInterface responder;

  private Logger logger;

  public RMIRequester(String serverInetAddr) {
    logger = Utility.getLogger(RMIRequester.class);
    try {
      String responderLocation = "//" + serverInetAddr + "/"
          + RMIResponder.SERVICE_NAME;
      logger.info("Task Daemon Location = " + responderLocation);
      responder = (RMIResponderInterface) Naming.lookup(responderLocation);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (NotBoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Result<Task<?, ?>> requestTask(Task<?, ?> previousTask,
      UID clientProfileUID) {
    synchronized (responder) {
      Result<Task<?, ?>> result = new Result<Task<?, ?>>(null,
          ResultCode.Failed);
      try {
        result = responder.<Task<?, ?>> request(RMICommandCode.RequestTask,
            previousTask, clientProfileUID);
      } catch (RemoteException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
      return result;
    }
  }

  @Override
  public Result<UID> registerClient(ClientProfile clientProfile) {
    synchronized (responder) {
      Result<UID> result = new Result<UID>(null, ResultCode.Failed);
      try {
        result = responder.<UID> request(RMICommandCode.RegisterClient,
            clientProfile);
      } catch (RemoteException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
      return result;
    }
  }

  @Override
  public Result<Boolean> sendAliveSignal(UID clientProfileUID) {
    synchronized (responder) {
      Result<Boolean> result = new Result<Boolean>(false, ResultCode.Failed);
      try {
        result = responder.<Boolean> request(RMICommandCode.SendAliveSignal,
            clientProfileUID);
      } catch (RemoteException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
      return result;
    }
  }

  @Override
  public void quit(UID clientProfileUID) {
    synchronized (responder) {
      try {
        responder.<Serializable> request(RMICommandCode.Quit, clientProfileUID);
      } catch (RemoteException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
    }
  }

}
