package com.esprit.sensordata.services;

public interface SensorDataService {
    void processSensorData();

    void generateStatistics();

    public void saveFilteredSensorDataToCSV();


}
