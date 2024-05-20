package com.esprit.sensordata.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@Entity
public class SensorData implements Serializable {
    @Id
    private String sensorId;
    private Double reading;
    private Double threshold;



}

