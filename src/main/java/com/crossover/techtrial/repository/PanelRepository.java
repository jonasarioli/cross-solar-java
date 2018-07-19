package com.crossover.techtrial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.Panel;
/**
 * PanelRepository allows all operations to Panel Entity.
 * @author Crossover
 *
 */

@RestResource(exported = false)
public interface PanelRepository extends JpaRepository<Panel, Long> {
	
  Panel findBySerial(String serial);
}
