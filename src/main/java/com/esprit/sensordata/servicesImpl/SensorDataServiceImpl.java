package com.esprit.sensordata.servicesImpl;

import com.esprit.sensordata.daos.SensorDataDao;
import com.esprit.sensordata.entities.SensorData;
import com.esprit.sensordata.services.SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorDataServiceImpl implements SensorDataService {
    private static final Logger logger = LoggerFactory.getLogger(SensorDataServiceImpl.class);

    @Autowired
    private SensorDataDao sensorDataDao;

    @Override
    public void processSensorData() {
        System.out.println("Running processSensorData method");
        try {
            // Lire les fichiers CSV du répertoire "new"
            readSensorDataFromCSV();
            System.out.println("saye processSensorData method");

        } catch (IOException e) {
            System.out.println("mch saye processSensorData method");
            logger.error("Error processing sensor data: {}", e.getMessage());

        }
    }

    private void readSensorDataFromCSV() throws IOException {
        logger.info("Running readSensorDataFromCSV method");
        System.out.println("Running readSensorDataFromCSV method");

        String newDir = "sensorData/new";
        String archiveDir = "sensorData/archived";

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(newDir), "*.csv")) {
            for (Path entry : stream) {
                List<SensorData> sensorDataList = new ArrayList<>();
                try (BufferedReader br = Files.newBufferedReader(entry)) {
                    br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {
//                        String[] values = line.split(",");
//                        if (values.length == 3) {
//                            String sensorId = values[0];
//                            try {
//                                Double reading = Double.parseDouble(values[1]);
//                                Double threshold = Double.parseDouble(values[2]);
//                                SensorData sensorData = new SensorData(sensorId, reading, threshold);
//                                sensorDataList.add(sensorData);
//                            } catch (NumberFormatException e) {
//                                logger.error("Error parsing double values in line: {}", line);
//                                System.err.println("Error parsing double values in line: " + line);
//                            }
//                        } else {
//                            logger.error("Invalid line format: {}", line);
//                            System.err.println("Invalid line format: " + line);
//                        }
                        String[] values = line.split(",");
                        SensorData sensorData = new SensorData();
                        sensorData.setSensorId(values[0]);
                        sensorData.setReading(Double.valueOf(values[1]));
                        sensorData.setThreshold(Double.valueOf(values[2]));
                        sensorDataList.add(sensorData);
                    }
                }
                sensorDataDao.saveAll(sensorDataList);
                Files.move(entry, Paths.get(archiveDir, entry.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            logger.error("Error reading and saving data", e);
            System.out.println("mch saye readSensorDataFromCSV");
        }
    }


    @Override
    public void generateStatistics() {
        // Filtrer les lectures "correctes" de la base de données
        List<SensorData> correctReadings = filterCorrectReadings();

        // Calculer le pourcentage de lectures correctes
        double percentageCorrect = calculatePercentageCorrect(correctReadings);

        // Afficher les statistiques dans la console
        System.out.println("Percentage of correct readings: " + percentageCorrect + "%");
    }

    @Override
    public void saveFilteredSensorDataToCSV()  {
        List<SensorData> correctReadings = filterCorrectReadings();
        List<SensorData> filteredReadings = correctReadings.stream()
                .filter(this::isReadingCorrect)
                .collect(Collectors.toList());

        try (FileWriter csvWriter = new FileWriter("sensorData/filtered/filtered_sensor_data.csv")) {
            csvWriter.append("sensorId,reading,threshold\n"); // Header row
            for (SensorData reading : filteredReadings) {
                csvWriter.append(reading.getSensorId())
                        .append(",")
                        .append(String.valueOf(reading.getReading()))
                        .append(",")
                        .append(String.valueOf(reading.getThreshold()))
                        .append("\n");
            }
            csvWriter.flush();
            System.out.println("Filtered sensor data saved to sensorData/filtered/filtered_sensor_data.csv");
        } catch (IOException e) {
            logger.error("Error saving filtered sensor data to CSV", e);
            System.err.println("Error saving filtered sensor data to CSV: " + e.getMessage());
        }
    }

    private double calculatePercentageCorrect(List<SensorData> correctReadings) {
        int totalReadings = sensorDataDao.findAll().size();
        if (totalReadings == 0) {
            return 0.0;
        }
        return (double) correctReadings.size() / totalReadings * 100;
    }

    private List<SensorData> filterCorrectReadings() {
        List<SensorData> correctReadings = new ArrayList<>();
        List<SensorData> allReadings = sensorDataDao.findAll();
        for (SensorData reading : allReadings) {
            if (isReadingCorrect(reading)) {
                correctReadings.add(reading);
            }
        }
        return correctReadings;
    }

    private boolean isReadingCorrect(SensorData reading) {
        double threshold = reading.getThreshold();
        double allowedDeviation = threshold * 0.2; // 20% deviation
        double minAllowedReading = threshold - allowedDeviation;
        double maxAllowedReading = threshold + allowedDeviation;
        double actualReading = reading.getReading();
        return actualReading >= minAllowedReading && actualReading <= maxAllowedReading;
    }
}
