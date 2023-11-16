package core;

/**
 * The Set class represents one set inside an exercise. It contains a constructor
 * that allows you to specify the number of repetitions and weight.
 * <p>
 * The class contains two fields: the number of repetitions and the weight during
 * the repetition. The class provides getters for the number of repetitions
 * and the weight of the set. Additionally, there is an equals method to check if two
 * sets are equal, and a method that returns the set's hashcode, which is used
 * in the equals method.
 * </p>
 */
public class Set {

    private int reps;
    private int weight;

    /**
     * Constructor for a set that lets you specify the number of repetitions and
     * weight.
     *
     * @param reps   number of repetitions
     * @param weight weight of the set
     */
    public Set(int reps, int weight) {
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * A method to get the number of repetitions in the set.
     *
     * @return the number of repetitions in the set
     */
    public int getReps() {
        return reps;
    }

    /**
     * A method to get the weight of the set.
     *
     * @return the weight of the set
     */
    public int getWeight() {
        return weight;
    }

    /**
     * A method to get the hashcode of the set.
     *
     * @return the hashcode of the set
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + reps;
        result = prime * result + weight;
        return result;
    }

    /**
     * A method to check if two sets are equal.
     *
     * @param obj the object to compare to
     * @return boolean true if the sets are equal, false if not
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
        Set other = (Set) obj;
        if (reps != other.getReps()) {
            return false;
        }
        if (weight != other.getWeight()) {
            return false;
        }
        return true;
    }
}
