package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Exercise {

    private String name;
    private List<Set> sets = new ArrayList<Set>();

    public Exercise(String name) {
        this.name = name;
    }

    public Exercise(String name, Set... sets) {
        this.name = name;
        this.sets = new ArrayList<Set>(Arrays.asList(sets));
    }

    public void addSet(Set set) {
        sets.add(set);
    }

    public String getName() {
        return name;
    }

    public List<Set> getSets() {
        return new ArrayList<Set>(sets);
    }

    public int getTotalWeight() {
        int totalWeight = 0;
        for (Set set : sets) {
            totalWeight += set.getWeight() * set.getReps();
        }
        return totalWeight;
    }

    public int getLocalPr() {
        return sets.stream().mapToInt(s -> s.getWeight()).max().getAsInt();
    }

    @Override
    public String toString() {
        return getName();
    }

}
