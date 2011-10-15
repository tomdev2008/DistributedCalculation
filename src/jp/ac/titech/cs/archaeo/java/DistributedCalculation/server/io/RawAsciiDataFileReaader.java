package jp.ac.titech.cs.archaeo.java.DistributedCalculation.server.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class RawAsciiDataFileReaader {

  public static Double[][] read(File inputDataFile) throws IOException {
    List<List<Double>> rawData = new ArrayList<List<Double>>();
    BufferedReader br = new BufferedReader(new FileReader(inputDataFile));
    for (String line = br.readLine(); line != null; line = br.readLine()) {
      String[] values = line.split(" ");
      List<Double> rawLine = new ArrayList<Double>();
      for (String value : values) {
        rawLine.add(Double.parseDouble(value));
      }
      rawData.add(rawLine);
    }
    Double[][] retData = new Double[rawData.get(0).size()][rawData.size()];
    for (int y = 0; y < rawData.size(); y++) {
      for (int x = 0; x < rawData.get(y).size(); x++) {
        retData[x][y] = rawData.get(y).get(x);
      }
    }
    return retData;
  }

}
