package sit707_week7;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class BodyTemperatureMonitorTest {

    
	@Test
	public void testStudentIdentity() {
		String studentId = "220007878";
		Assert.assertNotNull("Student ID is null", studentId);
	}

	@Test
	public void testStudentName() {
		String studentName = "Deepak Kumar Khatri";
		Assert.assertNotNull("Student name is null", studentName);
	}
	
	
	
	@Test
    public void testReadTemperatureNegative() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        CloudService cloudService = mock(CloudService.class);
        NotificationSender notificationSender = mock(NotificationSender.class);

        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);

        when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);

        double temperature = bodyTemperatureMonitor.readTemperature();

        assertEquals(-1.0, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureZero() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        CloudService cloudService = mock(CloudService.class);
        NotificationSender notificationSender = mock(NotificationSender.class);

        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);

        when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);

        double temperature = bodyTemperatureMonitor.readTemperature();

        assertEquals(0.0, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureNormal() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        CloudService cloudService = mock(CloudService.class);
        NotificationSender notificationSender = mock(NotificationSender.class);

        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);

        when(temperatureSensor.readTemperatureValue()).thenReturn(36.0);

        double temperature = bodyTemperatureMonitor.readTemperature();

        assertTrue(temperature >= 35.0 && temperature <= 37.0);
    }

    @Test
    public void testReadTemperatureAbnormallyHigh() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        CloudService cloudService = mock(CloudService.class);
        NotificationSender notificationSender = mock(NotificationSender.class);

        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);

        when(temperatureSensor.readTemperatureValue()).thenReturn(100.0);

        double temperature = bodyTemperatureMonitor.readTemperature();

        assertEquals(100.0, temperature, 0.01);
    }

    @Test
    public void testReportTemperatureReadingToCloud() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        CloudService cloudService = mock(CloudService.class);
        NotificationSender notificationSender = mock(NotificationSender.class);

        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);

        TemperatureReading temperatureReading = new TemperatureReading();
        bodyTemperatureMonitor.reportTemperatureReadingToCloud(temperatureReading);

        verify(cloudService, times(1)).sendTemperatureToCloud(temperatureReading);
    }

    @Test
    public void testInquireBodyStatusNormalNotification() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        CloudService cloudService = mock(CloudService.class);
        NotificationSender notificationSender = mock(NotificationSender.class);

        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);

        when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("NORMAL");

        bodyTemperatureMonitor.inquireBodyStatus();

        verify(notificationSender, times(1)).sendEmailNotification(any(Customer.class), anyString());
    }

    @Test
    public void testInquireBodyStatusAbnormalNotification() {
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        CloudService cloudService = mock(CloudService.class);
        NotificationSender notificationSender = mock(NotificationSender.class);

        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);

        when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("ABNORMAL");

        bodyTemperatureMonitor.inquireBodyStatus();

        verify(notificationSender, times(1)).sendEmailNotification(any(FamilyDoctor.class), anyString());
    }
}




