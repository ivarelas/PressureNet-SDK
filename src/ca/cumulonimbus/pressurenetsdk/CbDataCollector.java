package ca.cumulonimbus.pressurenetsdk;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Collect data from onboard sensors and store locally
 * 
 * @author jacob
 *
 */
public class CbDataCollector implements SensorEventListener{

	private String userID = "";
	private SensorManager sm;
	private Context context;
	
	// TODO: Keep a list of recent readings rather than single values
	private double recentPressureReading = 0.0;
	private double recentHumidityReading = 0.0;
	private double recentTemperatureReading = 0.0;
	
	private boolean pressureReadingsActive = false;
	private boolean humidityReadingsActive = false;
	private boolean temperatureReadingsActive = false;
	
    // Get a set of measurements
    public void getSomeMeasurements() {
    	try {
	    	sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    	Sensor pressureSensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
	    	Sensor temperatureSensor = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
	    	Sensor humiditySensor = sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
	    	
	    	if(pressureSensor!=null) {
	    		pressureReadingsActive = sm.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    	}
	    	if(temperatureSensor!=null) {
	    		temperatureReadingsActive = sm.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    	}
	    	if(humiditySensor!=null) {
	    		humidityReadingsActive = sm.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
	    	}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void stopCollectingPressure() {
    	sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sm.unregisterListener(this);
    }
    
    
	/**
	 * Collect a full group of observations. The principle way data
	 * should be gathered.
	 * @return
	 */
    public CbObservationGroup getObservationGroup() {
        // TODO: Implement
    	return null;
    }
    
	public CbObservation getPressureObservation() {
		getSomeMeasurements();
		
		CbObservation pressureObservation = new CbObservation();
		pressureObservation.setTime(System.currentTimeMillis());
		pressureObservation.setUser_id(userID);
		pressureObservation.setObservationType(Sensor.TYPE_PRESSURE + ""); // TODO: Fix hack
		pressureObservation.setObservationValue(recentPressureReading);
		pressureObservation.setObservationUnit("mbar");
		return pressureObservation;
	}
	
	public CbDataCollector(String userID, Context ctx) {
		this.userID = userID;
		this.context = ctx;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		System.out.println("sensor changed: " + event.sensor.getName());
		if(event.sensor.getType() == Sensor.TYPE_PRESSURE) {
			recentPressureReading = event.values[0];
			stopCollectingPressure();
		}
		
	}
}
