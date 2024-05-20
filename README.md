The system simulates receiving new sensor data. In fact, the data received is stored in a csv file having
 three columns sensorId, reading (data read by the sensor) and the threshold. In the context of the sensor
 data, the threshold represents a reference value used for comparison with the sensor’s actual reading. It
 helps determine whether the sensor reading is considered significant or within normal operating range. In
 this use case if the reading is 20 percent higher or lower than the threshold value, the reading should not be
 considered as a correct value.
