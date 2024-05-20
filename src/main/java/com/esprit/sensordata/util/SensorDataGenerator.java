package com.esprit.sensordata.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SensorDataGenerator {
    public static void generateSensorDataCSV() throws IOException {
        final FileWriter csvWriter = new FileWriter("sensorData/new/sensor_data.csv");
        csvWriter.append("sensorId,reading,threshold\n"); // Header row
        final Random random = new Random();

        String sensorId;
        Double reading;
        Double threshold;
        for (int i = 1; i <= 100; i++) {
            sensorId = "hash_" + i;
            reading = random.nextDouble() * 100; // 0.0 to 99.9
            threshold = reading + random.nextDouble() * 10; // Threshold slightly higher than reading

            csvWriter.append(sensorId).append(",").append(reading.toString()).append(",").append(threshold.toString()).append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Sensor data generated and saved to sensorData/new/sensor_data.csv");
    }
}
