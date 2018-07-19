package com.crossover.techtrial.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.LongSummaryStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.service.DailyElectricityService;
import com.crossover.techtrial.service.HourlyElectricityService;
import com.crossover.techtrial.service.PanelService;

/**
 * Scheduler to consolidate on daily bases all the electricity generated.s
 *
 * @author Jonas Arioli
 */
@Component
@EnableScheduling
@ConditionalOnProperty(value = "crosssolar.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class DailyElectricityAggregator {

    private static final String CRON = "0 0 0 * * *";
    private static final String TIME_ZONE = "America/Sao_Paulo";
    
    @Autowired
    HourlyElectricityService hourlyElectricityService;
    
    @Autowired
    DailyElectricityService dailyElectricityService;
    
    @Autowired
    PanelService panelService;

    @Scheduled(cron = CRON, zone = TIME_ZONE)
    public void consolidateDailyElectricityGeneration() {
    	List<Panel> panels = this.panelService.getAll();
    	
    	LocalDate yesterday = LocalDate.now().minusDays(1);

        for (Panel panel : panels) {
            List<HourlyElectricity> hourlyElectricities = hourlyElectricityService
                    .getAllHourlyElectricityByReadingAtBetween(yesterday, panel);

            if (hourlyElectricities.isEmpty()) {
                continue;
            }

            LongSummaryStatistics summaryStatistics = hourlyElectricities.stream()
                    .mapToLong(HourlyElectricity::getGeneratedElectricity)
                    .summaryStatistics();

            DailyElectricity dailyElectricity = new DailyElectricity();
            dailyElectricity.setPanel(panel);
            dailyElectricity.setDailyAverage(summaryStatistics.getAverage());
            dailyElectricity.setDailyMax(summaryStatistics.getMax());
            dailyElectricity.setDailyMin(summaryStatistics.getMin());
            dailyElectricity.setDailySum(summaryStatistics.getSum());
            dailyElectricity.setDate(yesterday);
            
            
            this.dailyElectricityService.save(dailyElectricity);
        }
    }
}
