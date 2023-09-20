package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Exercise {

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Exercise other = (Exercise) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sets == null) {
            if (other.sets != null)
                return false;
        } else if (!sets.equals(other.sets))
            return false;
        return true;
    }

    private String name;
    private List<Set> sets = new ArrayList<Set>();

    /**
     * Constructor for an exercise that only lets you specify the name
     * 
     * @param name the name of the exercise
     */
    public Exercise(String name) {
        this.name = name;
    }

    /**
     * Constructor for an exercise that lets you specify the name and lets you add
     * an arbitrary number of sets
     * 
     * @param name the name of the exercise
     * @param sets the sets to add to the exercise
     */
    public Exercise(String name, Set... sets) {
        this.name = name;
        this.sets = new ArrayList<Set>(Arrays.asList(sets));
    }

    /**
     * Adds a set to the exercise
     * 
     * @param set the set to add to the exercise
     */
    public void addSet(Set set) {
        sets.add(set);
    }

    /**
     * A method to get the name of the exercise
     * 
     * @return the name of the exercise
     */
    public String getName() {
        return name;
    }

    /**
     * A method to get all sets in the exercise
     * 
     * @return A list of sets in the exercise
     */
    public List<Set> getSets() {
        return new ArrayList<Set>(sets);
    }

    /**
     * Calculates the total weight of all sets in the exercise
     * 
     * @return the total weight of all sets in the exercise
     */
    public int getTotalWeight() {
        int totalWeight = 0;
        for (Set set : sets) {
            totalWeight += set.getWeight() * set.getReps();
        }
        return totalWeight;
    }

    /**
     * A method that lets you get the highest wieght in the exercise. Is useful for
     * calculating the pr of an exercise
     * 
     * @return the highest weight in the exercise
     */
    public int getLocalPr() {
        return sets.stream().mapToInt(s -> s.getWeight()).max().getAsInt();
    }

    /**
     * A toString that returns the name of the exercise
     * 
     * @return the name of the exercise
     */
    @Override
    public String toString() {
        return getName();
    }

}
