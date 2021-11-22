package com.vector.app.simulation.individualParams;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor(staticName = "of")
public class IndividualParams {
    private Map<String, Double> distances;
    private Map<String, Integer> times;
}
