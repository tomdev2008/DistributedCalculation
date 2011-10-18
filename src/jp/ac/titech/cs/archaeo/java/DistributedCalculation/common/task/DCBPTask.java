package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task;

import java.util.Properties;

public final class DCBPTask extends Task<Double[], Double[]> {

  private static final long serialVersionUID = 2620256117378296021L;

  public DCBPTask(Properties inputProperties, Double[] inputData) {
    super(inputProperties, inputData);
  }

  @Override
  protected void process() {

    double initialAlpha = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.initialAlpha"));

    double initialBeta = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.initialBeta"));

    double initialDecayRatio = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.initialDecayRatio"));

    int gradationValueRange = Integer.parseInt(inputProperties
        .getProperty("preprocess.params.gradationValueRange"));

    /**
     * messageUpper[sample number][gradation value]
     */
    double[][] messageUpper = new double[inputData.length][gradationValueRange];
    for (int i = 0; i < messageUpper.length; i++)
      messageUpper[i] = new double[gradationValueRange];

    /**
     * messageLower[sample number][gradation value]
     */
    double[][] messageLower = new double[inputData.length][gradationValueRange];
    for (int i = 0; i < messageLower.length; i++)
      messageLower[i] = new double[gradationValueRange];

  }

}
