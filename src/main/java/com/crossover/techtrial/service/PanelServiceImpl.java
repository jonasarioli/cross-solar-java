package com.crossover.techtrial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.PanelRepository;


/**
 * PanelServiceImpl for panel related handling.
 * @author Crossover
 *
 */
@Service
public class PanelServiceImpl implements PanelService {

  @Autowired
  PanelRepository panelRepository;
  
  /* (non-Javadoc)
   * @see com.crossover.techtrial.service.PanelService#register(com.crossover.techtrial.model.Panel)
   */
  
  @Override
  public Panel register(Panel panel) { 
    return panelRepository.save(panel);
  }
  
  public Panel findBySerial(String serial) {
    return panelRepository.findBySerial(serial);
  }
  
  @Override
  public List<Panel> getAll() {
    return panelRepository.findAll();
  }
}
