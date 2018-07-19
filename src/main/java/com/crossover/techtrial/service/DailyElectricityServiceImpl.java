package com.crossover.techtrial.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.dto.DailyElectricityDTO;
import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.DailyElectricityRepository;

/**
 * DailyElectricityServiceImpl will handle daily summary electricity generated by a Panel.
 *
 * @author Jonas Arioli
 *
 */

@Service
public class DailyElectricityServiceImpl implements DailyElectricityService {
	
	@Autowired
	DailyElectricityRepository dailyElectricityRepository;
    
	@Autowired
	PanelService panelService;


	@Override
	public DailyElectricity save(DailyElectricity dailyElectricity) {
		return dailyElectricityRepository.save(dailyElectricity);
	}

	@Override
	public List<DailyElectricityDTO> getAllDailyElectricityByPanelSerial(String panelSerial) {
		Panel panel = panelService.findBySerial(panelSerial);
		if(panel == null)
			throw new ResourceNotFoundException(String.format("The panel '%s' was not found", panelSerial));
		
		List<DailyElectricity> entities = dailyElectricityRepository.findAllByPanelId(panel.getId());

        if (entities.isEmpty()) {
            throw new ResourceNotFoundException("there is no daily electricity for panel serial: " + panelSerial);
        }

        return entities.stream()
                .map(entity -> new DailyElectricityDTO(
                                entity.getDate(),
                                entity.getDailySum(),
                                entity.getDailyAverage(),
                                entity.getDailyMin(),
                                entity.getDailyMax()))
                .collect(Collectors.toList());
	}

}
