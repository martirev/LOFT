package core;

public class Set {

    private int reps;
    private int weight;

    /**
     * Constructor for a set that lets you specify the number of repetitions and
     * weight
     * 
     * @param reps   number of repetitions
     * @param weight weight of the set
     */
    public Set(int reps, int weight) {
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * A method to get the number of repetitions in the set
     * 
     * @return the number of repetitions in the set
     */
    public int getReps() {
        return reps;
    }

    /**
     * A method to get the weight of the set
     * 
     * @return the weight of the set
     */
    public int getWeight() {
        return weight;
    }

}
