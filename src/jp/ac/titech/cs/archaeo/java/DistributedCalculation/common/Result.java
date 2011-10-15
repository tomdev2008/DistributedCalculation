package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common;

import java.io.Serializable;

public final class Result<ResultDataType extends Serializable> implements
    Serializable {

  private static final long serialVersionUID = -3224157350785963594L;

  private final ResultDataType result;

  private final ResultCode resultCode;

  public Result(ResultDataType result, ResultCode resultCode) {
    this.result = result;
    this.resultCode = resultCode;
  }

  public ResultDataType getResult() {
    return result;
  }

  public ResultCode getResultCode() {
    return resultCode;
  }

}
