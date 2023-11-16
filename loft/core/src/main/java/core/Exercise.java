package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Exercise class represents a single exercise in a workout routine. It
 * contains two constructors; one that only lets you specify the name of the
 * exercise and one that lets you specify the name and add an arbitrary number
 * of sets to the exercise.
 * <p>
 * The class contains two fields, one that holds a name and a list of sets.
 * The class provides methods to add sets to the exercise, get the name of the
 * exercise, formats the name, get all sets in the exercise, calculate the total 
 * weight of all sets in the exercise, get the highest weight in the exercise,
 * check if two exercises are equal and a methods that returns the exercise
 * hashcode witch is used in the equals method. Also there are a toString method
 * that returns the name of the exercise.
 * </p>
 */
public class Exercise {

    private String name;
    private List<Set> sets = new ArrayList<Set>();

    /**
     * Constructor for an exercise that only lets you specify the name.
     *
     * @param name the name of the exercise
     * @throws IllegalArgumentException if the name is null or empty
     */
    public Exercise(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = formatName(name);
    }

    /**
     * Constructor for an exercise that lets you specify the name and lets you add
     * an arbitrary number of sets.
     *
     * @param name the name of the exercise
     * @param sets the sets to add to the exercise
     * @throws IllegalArgumentException if the name is null
     */
    public Exercise(String name, Set... sets) {
        this(name);
        this.sets = new ArrayList<Set>(Arrays.asList(sets));
    }

    /**
     * Converts a string to PascalCase format and removes all unnecessary
     * whitespaces.
     *
     * @param name the string to be converted
     * @return the converted string in PascalCase format
     */
    private static String formatName(String name) {
        String[] words = name.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)));
            sb.append(word.substring(1).toLowerCase());
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Adds a set to the exercise.
     *
     * @param set the set to add to the exercise
     */
    public void addSet(Set set) {
        sets.add(set);
    }

    /**
     * A method to get the name of the exercise.
     *
     * @return the name of the exercise
     */
    public String getName() {
        return name;
    }

    /**
     * A method to get all sets in the exercise.
     *
     * @return A list of sets in the exercise
     */
    public List<Set> getSets() {
        return new ArrayList<Set>(sets);
    }

    /**
     * Calculates the total weight of all sets in the exercise.
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
     * A method that lets you get the highest weight in the exercise. Is useful for
     * calculating the pr of an exercise.
     *
     * @return the highest weight in the exercise
     */
    public int getLocalPr() {
        return sets.stream().mapToInt(s -> s.getWeight()).max().orElse(0);
    }

    /**
     * A method that lets you get the heaviest lifted set in the exercise.
     *
     * @return the heaviest lifted weight in a set in the exercise
     */
    public int getHeaviestLiftedSet() {
        return sets.stream().mapToInt(s -> s.getWeight() * s.getReps()).max().orElse(0);
    }

    /**
     * A toString that returns the name of the exercise.
     *
     * @return the name of the exercise
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * A method to get the hashcode of the exercise.
     *
     * @return the hashcode of the exercise
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + sets.hashCode();
        return result;
    }

    /**
     * A method to check if two exercises are equal.
     *
     * @param obj the object to compare to
     * @return boolean true if the exercises are equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Exercise other = (Exercise) obj;
        if (!name.equals(other.getName())) {
            return false;
        }
        if (!sets.equals(other.getSets())) {
            return false;
        }
        return true;
    }
}
