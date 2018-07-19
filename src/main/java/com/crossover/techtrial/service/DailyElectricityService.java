package com.crossover.techtrial.service;

import java.util.List;

import com.crossover.techtrial.dto.DailyElectricityDTO;
import com.crossover.techtrial.model.DailyElectricity;

/**
 * DailyElectricityService interface for all services realted to DailyElectricity.
 * @author Jonas Arioli
 *
 */
public interface DailyElectricityService {

	DailyElectricity save(DailyElectricity dailyElectricity);

    List<DailyElectricityDTO> getAllDailyElectricityByPanelSerial(String panelSerial);
}
