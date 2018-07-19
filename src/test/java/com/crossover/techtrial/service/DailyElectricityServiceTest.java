package com.crossover.techtrial.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.techtrial.dto.DailyElectricityDTO;
import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.DailyElectricityRepository;

@RunWith(SpringRunner.class)
public class DailyElectricityServiceTest {

	@TestConfiguration
    static class DailyElectricityServiceImplTestContextConfiguration {
  
        @Bean
        public DailyElectricityService dailyElectricityService() {
            return new DailyElectricityServiceImpl();
        }        
    }
	
	
	@Autowired
	DailyElectricityService dailyElectricityService;
    
	@MockBean
	DailyElectricityRepository dailyElectricityRepository;
    
	@MockBean
	PanelService panelService;
	
    private static Random random = new Random();

    @Before
    public void setUp() {
        

		BDDMockito.given(panelService.findBySerial(any())).willReturn(panel());
    }
    
    private static Panel panel() {
    	Panel panel = new Panel();
		panel.setBrand("Tesla");
		panel.setLatitude(54.123232);
		panel.setLongitude(54.123232);
		panel.setSerial("232323");
		
		return panel;
    }
    
    private static DailyElectricity dailyElectricity() {
    	
    	DailyElectricity daily = new DailyElectricity();
        daily.setPanel(panel());
        daily.setDailyAverage(random.nextDouble());
        daily.setDailyMax(random.nextLong());
        daily.setDailyMin(random.nextLong());
        daily.setDailySum(random.nextLong());
        daily.setDate(LocalDate.now());
        
        return daily;
        
    }

    @Test
    public void save() {
        DailyElectricity expected = dailyElectricity();
        when(dailyElectricityRepository.save(any())).thenReturn(expected);

        DailyElectricity result = dailyElectricityService.save(expected);

        assertEquals(expected, result);
    }

    @Test
    public void getAllDailyElectricityByPanelId() {
        DailyElectricity dailyElectricity = dailyElectricity();
        List<DailyElectricity> expected = Collections.singletonList(dailyElectricity);
        when(dailyElectricityRepository.findAllByPanelId(any())).thenReturn(expected);

        List<DailyElectricityDTO> result = dailyElectricityService.getAllDailyElectricityByPanelSerial("232323");

        for (DailyElectricityDTO dto : result) {
            assertEquals(dailyElectricity.getDailyAverage(), dto.getAverage());
            assertEquals(dailyElectricity.getDailyMax(), dto.getMax());
            assertEquals(dailyElectricity.getDailyMin(), dto.getMin());
            assertEquals(dailyElectricity.getDailySum(), dto.getSum());
            assertEquals(dailyElectricity.getDate(), dto.getDate());
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getAllDailyElectricityByPanelId_notFound() {
        when(dailyElectricityRepository.findAllByPanelId(any())).thenReturn(Collections.emptyList());
        dailyElectricityService.getAllDailyElectricityByPanelSerial("232323");
    }
}
