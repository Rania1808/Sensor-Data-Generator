package com.esprit.sensordata;

import com.esprit.sensordata.util.SensorDataGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@EnableScheduling
@SpringBootApplication
public class SensorDataApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SensorDataApplication.class, args);
        SensorDataGenerator.generateSensorDataCSV();
    }

}
