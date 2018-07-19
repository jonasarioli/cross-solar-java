package com.crossover.techtrial.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;

/**
 * HourlyElectricityService interface for all services realted to
 * HourlyElectricity.
 * 
 * @author Crossover
 *
 */
public interface HourlyElectricityService {
	HourlyElectricity save(HourlyElectricity hourlyElectricity, String panelSerial);

	Page<HourlyElectricity> getAllHourlyElectricityByPanelId(String panelSerial, Pageable pageable);

	List<HourlyElectricity> getAllHourlyElectricityByReadingAtBetween(LocalDate date, Panel panel);
}
