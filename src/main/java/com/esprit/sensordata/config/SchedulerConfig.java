package com.esprit.sensordata.config;

import com.esprit.sensordata.services.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired
    private SensorDataService sensorDataService;

    @Scheduled(cron = "*/20 * * * * *") //  planification toutes les 20 secondes
    public void processSensorData() {
        sensorDataService.processSensorData();
    }

    @Scheduled(cron = "*/20 * * * * *")
    public void generateStatistics() {
        sensorDataService.generateStatistics();
    }
    @Scheduled(cron = "*/20 * * * * *")
    public void saveFilteredSensorDataToCSV(){
        sensorDataService.saveFilteredSensorDataToCSV();
    }
}
