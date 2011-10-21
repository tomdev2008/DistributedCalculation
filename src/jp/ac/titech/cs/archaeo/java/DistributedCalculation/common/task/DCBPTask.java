package jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.task;

import java.util.Arrays;
import java.util.Properties;

import jp.ac.titech.cs.archaeo.java.DistributedCalculation.common.Utility;

import org.apache.log4j.Logger;

public final class DCBPTask extends Task<Double[], Double[]> {

  private static final long serialVersionUID = 2620256117378296021L;

  private Logger logger;

  public DCBPTask(Properties inputProperties, Double[] inputData) {
    super(inputProperties, inputData);
    logger = Utility.getLogger(DCBPTask.class);
  }

  @Override
  protected void process() {

    int nSamplingTime = inputData.length;

    double initialAlpha = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.initialAlpha"));

    double initialBeta = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.initialBeta"));

    double initialDecayRatio = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.initialDecayRatio"));

    int gradationValueRange = Integer.parseInt(inputProperties
        .getProperty("preprocess.params.gradationValueRange"));

    double initialPriorProbability = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.initialPriorProbability"));

    double initialPosteriorProbability = Double.parseDouble(inputProperties
        .getProperty("preprocess.params.initialPosteriorProbability"));

    /**
     * messageUpper[sampling time][gradation value]
     */
    double[][] messageUpper = new double[nSamplingTime][gradationValueRange];

    double[][] messageUppserStar;

    /**
     * messageLower[sampling time][gradation value]
     */
    double[][] messageLower = new double[nSamplingTime][gradationValueRange];

    double[][] messageLowerStar;

    /**
     * messagePriorProbability[gradation value]
     */
    double[] messagePriorProbability = new double[gradationValueRange];
    Arrays.fill(messagePriorProbability, initialPriorProbability);

    /**
     * messagePosteriorProbability[sampling time][gradation value]
     */
    double[][] messagePosteriorProbability = new double[nSamplingTime][gradationValueRange];

    double epsilon = 0.0000010;
    double dse = 0.0;

    MESSAGE_CHECK_LOOP: do {

      messageUppserStar = Arrays.copyOf(messageUpper, messageUpper.length);
      messageLowerStar = Arrays.copyOf(messageLower, messageLower.length);

      MESSAGE_UPDATE_LOOP: for (int samplingTimeN = 0; samplingTimeN < nSamplingTime; samplingTimeN++) {

        double[][] messageUpperNumerator = new double[nSamplingTime][gradationValueRange];
        double[][] messageLowerNumerator = new double[nSamplingTime][gradationValueRange];

        double[] messageUpperDenominator = new double[nSamplingTime];
        double[] messageLowerDenominator = new double[nSamplingTime];

        /**
         * p <- fy<br>
         * q <- fy-dy
         */
        double upperDenominator = 0.1;
        double lowerDenominator = 0.1;
        for (int p = 0; p < gradationValueRange; p++) {
          double upperNumerator = 0.1;
          double lowerNumerator = 0.1;
          for (int q = 0; q < gradationValueRange; q++) {

          }
          messageUpperNumerator[samplingTimeN][p] = upperNumerator;
          messageLowerNumerator[samplingTimeN][p] = lowerNumerator;
          upperDenominator += upperNumerator;
          lowerDenominator += lowerNumerator;

        }
        messageUpperDenominator[samplingTimeN] = upperDenominator;
        messageLowerDenominator[samplingTimeN] = lowerDenominator;

        for (int p = 0; p < gradationValueRange; p++) {
          messageUpper[samplingTimeN][p] = messageUpperNumerator[samplingTimeN][p]
              / messageUpperDenominator[samplingTimeN];
          messageLower[samplingTimeN][p] = messageLowerNumerator[samplingTimeN][p]
              / messageLowerDenominator[samplingTimeN];
          dse += Math.abs(messageUppserStar[samplingTimeN][p]
              - messageUpper[samplingTimeN][p])
              + Math.abs(messageLowerStar[samplingTimeN][p]
                  - messageLower[samplingTimeN][p]);
        }
        dse /= (samplingTimeN + 1);
        logger.info("dse = " + dse);
      } // End of MESSAGE_UPDATE_LOOP
    } while (dse > epsilon); // End of MESSAGE_CHECK_LOOP
  }
}
