package com.crossover.techtrial.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.DailyElectricity;

/**
 * DailyElectricity Repository is for all operations for DailyElectricity.
 * @author Jonas Arioli
 */

@RestResource(exported = false)
public interface DailyElectricityRepository extends CrudRepository<DailyElectricity, Long> {
	List<DailyElectricity> findAllByPanelId(Long panelId);
}
