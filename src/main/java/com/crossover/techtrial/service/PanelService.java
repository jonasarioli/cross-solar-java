package com.crossover.techtrial.service;

import java.util.List;

import com.crossover.techtrial.model.Panel;

/**
 * PanelService interface for Panels.
 * 
 * @author Crossover
 *
 */
public interface PanelService {

	/**
	 * Register a panel for electricity monitoring.
	 * 
	 * @param panel
	 *            to register with system.
	 */

	Panel register(Panel panel);

	Panel findBySerial(String serial);

	List<Panel> getAll();
}
