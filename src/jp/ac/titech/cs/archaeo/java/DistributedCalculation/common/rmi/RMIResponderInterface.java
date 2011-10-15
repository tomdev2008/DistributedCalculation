package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Result;

public interface RMIResponderInterface extends Remote, Serializable {

  public <ResultDataType extends Serializable> Result<ResultDataType> request(
      RMICommandCode commandCode, Object... args) throws RemoteException;

}
