package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task;

public final class DCBPTask extends Task<Double[], Double[]> {

  private static final long serialVersionUID = 2620256117378296021L;

  public DCBPTask(Double[] inputData) {
    super(inputData);
  }

  @Override
  protected void process() {
    for (Double data : inputData)
      System.out.println(data);
  }

}
