package com.vector.app.simulation.individualParams;

import java.util.HashMap;
import java.util.Map;

import com.vector.app.simulation.individual.Individual;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor(staticName = "of")
public class IndividualParams {
    private Map<String, Double> distances;
    private Map<String, Integer> times;

    public IndividualParams getCopy(){
        return IndividualParams.of(new HashMap<>(distances), new HashMap<>(times));
    }
}
