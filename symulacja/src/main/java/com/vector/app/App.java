package com.vector.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import com.vector.app.chart.Chart;
import com.vector.app.memento.Caretaker;
import com.vector.app.memento.Originator;
import com.vector.app.simulation.controller.Controller;
import com.vector.app.simulation.generator.GeneratePopulation;
import com.vector.app.simulation.individual.Individual;
import com.vector.app.simulation.population.Population;
import com.vector.app.simulation.room.Room;

public class App extends JFrame {
    public JButton saveBut, undoBut, redoBut;
    Caretaker caretaker = new Caretaker();
    Originator originator = new Originator();
    int savedPopulations = 0, counterPopulation = 0;

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    private Population population;

    public static void main(String[] args) {
        new App();
    }

    public App(){
        Random random = new Random();
        Room room = new Room(40, 30);
        setPopulation(GeneratePopulation.generateNotResistPopulation(500, room));
        //Mementos mementos = new Mementos(new HashMap<>());
        Controller controller = new Controller();


//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setSize(1000, 700);
//        f.setLocation(800, 50);
//        f.setVisible(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocation(50, 50);
        this.setTitle("Symulacja choroby");
        //this.setVisible(true);

        Chart chart = new Chart(getPopulation().getPopulation().toString());

        ButtonListener saveListener = new ButtonListener();
        ButtonListener redoListener = new ButtonListener();
        ButtonListener undoListener = new ButtonListener();


        saveBut = new JButton("Save");
        undoBut = new JButton("Undo");
        redoBut = new JButton("Redo");

        saveBut.addActionListener(saveListener);
        redoBut.addActionListener(redoListener);
        undoBut.addActionListener(undoListener);

//        saveBut.addActionListener(e -> {
//            if(e.getSource() == saveBut) {
//                Population currentPopulation = getPopulation();
//
//                originator.set(currentPopulation);
//                caretaker.addMemento(originator.storeInMemento());
//
//                savedPopulations++;
//                counterPopulation++;
//
//                undoBut.setEnabled(true);
//            }
//        });
//
//        undoBut.addActionListener(e -> {
//            if(e.getSource() == undoBut) {
//                if(counterPopulation>=1) {
//                    counterPopulation--;
//                    setPopulation(originator.restoreFromMemento(caretaker.getMemento(counterPopulation)));
//                    redoBut.setEnabled(true);
//                }
//                else {
//                    undoBut.setEnabled(false);
//                }
//            }
//        });
//
//        redoBut.addActionListener(e -> {
//                if(e.getSource()==redoBut) {
//                    if((savedPopulations -1) > counterPopulation) {
//                        counterPopulation++;
//
//                        setPopulation(originator.restoreFromMemento(caretaker.getMemento(counterPopulation)));
//
//                        undoBut.setEnabled(true);
//                    }
//                    else {
//                        redoBut.setEnabled(false);
//                    }
//                }
//        });

        chart.add(saveBut);
        chart.add(undoBut);
        chart.add(redoBut);

        this.add(chart);
        this.setVisible(true);

        controller.prepareSimulation(population);
        int counter = 0;
        for (; ; ) {
//            if (counter == 100) {
//                population.setPopulation(mementos.getMemento(1));
//            }
            //mementos.addMemento(counter, new ArrayList<>(population.getPopulation()));

//            if(counter == 3){
//                originator.set(population.getCopy());
//                caretaker.addMemento(originator.storeInMemento());
//            }
//
//            if(counter == 20){
//                population = originator.restoreFromMemento(caretaker.getMemento(0));
//            }

            for (int i = 0; i < 25; i++) {

                population.getInfected().forEach(Individual::handle);
                chart.updateList(population.getPopulation().toString());
//                f.add(chart);
//                f.revalidate();
//                f.repaint();
                this.add(chart);
                this.revalidate();
                this.repaint();

                for (int j = 0; j < population.getInfected().size(); j++) {
                    Individual individual = population.getInfected().get(j);
                    individual.clearParams(population);
                    individual.getDistances(population.getNotInfected());
                    individual.getTimes(population.getNotInfected());

                    Map<String, Integer> times = individual.getIndividualParams().getTimes();

                    for (String key : times.keySet()) {
                        if (times.get(key) >= 75) {
                            population.getIndividual(key).handle(individual);
                        }
                    }
                }
                population.getPopulation().forEach(individual1 -> individual1.generatePosition(room, random));
                population.deleteIfExited();
                if (counter % 7 == 0) {
                    population.addIndividual(GeneratePopulation.getNotResistIndividual(room));
                }
            }

            System.out.println(counter);
            System.out.println("Infected: " + population.getInfected().size() + " " + "Not infected: " + population.getNotInfected().size());
            if (counter == 10000) {
                break;
            }
            if (population.getInfected().size() == 0) {
                break;
            }
            counter++;
        }
        population.getPopulation().forEach(System.out::println);
        System.out.println("Infected: " + population.getInfected().size() + " " + "Not infected: " + population.getNotInfected().size());
    }
    
    class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == saveBut) {
                Population currentPopulation = getPopulation().getCopy();

                originator.set(currentPopulation);
                caretaker.addMemento(originator.storeInMemento());

                savedPopulations++;
                counterPopulation++;

                undoBut.setEnabled(true);
            }
            else{
                if(e.getSource() == undoBut) {
                    if(counterPopulation>=1) {
                        counterPopulation--;
                        setPopulation(originator.restoreFromMemento(caretaker.getMemento(counterPopulation)));
                        redoBut.setEnabled(true);
                    }
                    else {
                    undoBut.setEnabled(false);
                    }
                }
                else{
                    if(e.getSource()==redoBut) {
                        if((savedPopulations -1) > counterPopulation) {
                            counterPopulation++;

                            setPopulation(originator.restoreFromMemento(caretaker.getMemento(counterPopulation)));

                            undoBut.setEnabled(true);
                        }
                        else {
                            redoBut.setEnabled(false);
                        }
                    }
                }
            }
        }
    }
}
