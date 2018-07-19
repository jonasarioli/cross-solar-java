package com.crossover.techtrial.controller;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.crossover.techtrial.dto.DailyElectricityDTO;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.service.DailyElectricityService;
import com.crossover.techtrial.service.HourlyElectricityService;
import com.crossover.techtrial.service.PanelService;
import com.fasterxml.jackson.databind.ObjectMapper;;

/**
 * PanelControllerTest class will test all APIs in PanelController.java.
 * 
 * @author Crossover
 *
 */

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PanelControllerTest {

	private static final String SERIAL_ID = "232323";

	private static Random random = new Random();

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private DailyElectricityService dailyService;
	
	@MockBean
	private HourlyElectricityService hourlyElectricityService;

	@MockBean
	private PanelService panelService;

	private static Panel panel() {
		Panel panel = new Panel();
		panel.setBrand("Tesla");
		panel.setLatitude(54.123232);
		panel.setLongitude(54.123232);
		panel.setSerial(SERIAL_ID);

		return panel;
	}

	@Test
	public void testPanelShouldBeRegistered() throws Exception {

		Panel panel = panel();
		BDDMockito.given(panelService.register(Mockito.any())).willReturn(panel);

		this.mockMvc
				.perform(post("/api/register").contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(panel)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.brand", is(panel.getBrand())))
				.andExpect(jsonPath("$.latitude", notNullValue())).andExpect(jsonPath("$.longitude", notNullValue()))
				.andExpect(jsonPath("$.serial", is(panel.getSerial()))).andExpect(jsonPath("$.id").doesNotExist());
	}
	
	private HourlyElectricity hourlyElectricity() {
		HourlyElectricity hourlyElectricity = new HourlyElectricity();
		hourlyElectricity.setId(random.nextLong());
		hourlyElectricity.setGeneratedElectricity(random.nextLong());
		hourlyElectricity.setPanel(panel());
		hourlyElectricity.setReadingAt(LocalDateTime.now());
		
		return hourlyElectricity;
	}

	@Test
	public void testSaveHourlyElectricity() throws Exception {
		
		BDDMockito.given(hourlyElectricityService.save(Mockito.any(), Mockito.any())).willReturn(hourlyElectricity());
		

		this.mockMvc
				.perform(post("/api/panels/{panel-serial}/hourly", SERIAL_ID).contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(hourlyElectricity())))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.generatedElectricity", notNullValue()))
				.andExpect(jsonPath("$.readingAt", notNullValue()));
	}

	@Test
	public void testGetHourlyElectricity() throws Exception {
		
		List<HourlyElectricity> hourlyElectricityList = singletonList(hourlyElectricity());
        Page<HourlyElectricity> page = new PageImpl<>(hourlyElectricityList);
		
		BDDMockito.given(hourlyElectricityService.getAllHourlyElectricityByPanelId(Mockito.any(), Mockito.any())).willReturn(page);
		
		this.mockMvc.perform(get("/api/panels/{panel-serial}/hourly", SERIAL_ID).contentType(APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].generatedElectricity", notNullValue()))
				.andExpect(jsonPath("$.content[0].readingAt", notNullValue()));
	}

	@Test
	public void testGetAllDailyElectricityFromYesterday() throws Exception {
		
		DailyElectricityDTO dailyDTO = new DailyElectricityDTO(LocalDate.now(), random.nextLong(), random.nextDouble(), random.nextLong(), random.nextLong());
		
		List<DailyElectricityDTO> list = new ArrayList<DailyElectricityDTO>(Collections.singletonList(dailyDTO));
		
		BDDMockito.given(dailyService.getAllDailyElectricityByPanelSerial(SERIAL_ID)).willReturn(list);
		
		this.mockMvc.perform(get("/api/panels/{panel-serial}/daily", SERIAL_ID).contentType(APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].date", notNullValue()))
				.andExpect(jsonPath("$[0].average", notNullValue())).andExpect(jsonPath("$[0].min", notNullValue()))
				.andExpect(jsonPath("$[0].max", notNullValue())).andExpect(jsonPath("$[0].sum", notNullValue()));
	}


}
