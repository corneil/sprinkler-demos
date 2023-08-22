package io.spring.sprinkler.app;

import java.time.LocalDate;
import java.util.List;

import io.spring.sprinkler.common.SimulationService;
import io.spring.sprinkler.common.SprinklerHistory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    private final SimulationService simulationService;

    public WebController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("")
    public String root(Model model) {
        return index(model);
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<SprinklerHistory> history = simulationService.listHistory();
        model.addAttribute("dates", history.stream().map(SprinklerHistory::date).map(LocalDate::toString).toList());
        model.addAttribute("rainFall", history.stream().map(SprinklerHistory::rain).map(r -> r == null ? 0.0 : r).toList());
        model.addAttribute("predictions", history.stream().map(SprinklerHistory::weatherPrediction).map(p -> p == null ? 0.0 : p).toList());
        model.addAttribute("runTime", history.stream().map(SprinklerHistory::runTime).map(r -> r == null ? 0.0 : r).toList());
        model.addAttribute("history", history);
        model.addAttribute("weatherData", simulationService.listAllWeather());
        model.addAttribute("status", simulationService.listAllStatus());
        return "index";
    }
}
