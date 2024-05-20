package com.esprit.sensordata.daos;

import com.esprit.sensordata.entities.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataDao  extends JpaRepository<SensorData, Long> {
}
