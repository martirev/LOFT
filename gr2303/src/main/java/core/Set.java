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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Set other = (Set) obj;
        if (reps != other.reps)
            return false;
        if (weight != other.weight)
            return false;
        return true;
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
