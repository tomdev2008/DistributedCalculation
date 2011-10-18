package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class RawAsciiDataFileWriter {

  private static String DATA_SEPARATOR_STRING = " ";
  private static String EOL = System.getProperty("line.separator");

  /**
   * @param resultDataFile
   * @param resultData
   *          Double[nTraces][nSamples]
   * @throws IOException
   */
  public static void write(File resultDataFile, Double[][] resultData)
      throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(resultDataFile));
    StringBuilder sb = new StringBuilder();
    for (int sampleN = 0; sampleN < resultData[0].length; sampleN++) {
      sb.setLength(0);
      for (int traceN = 0; traceN < resultData.length; traceN++) {
        if (sb.length() > 0)
          sb.append(DATA_SEPARATOR_STRING);
        sb.append(resultData[traceN][sampleN]);
      }
      bw.write(sb.toString() + EOL);
    }
    bw.close();
  }

}
