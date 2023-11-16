package core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * The User class represents a user in the application. It contains one constructor
 * that lets you specify the name, username, password and email.
 * <p>
 * The class contains the fields; name, username, password, email and a
 * list of workouts. The class provides getters and setters for name, username,
 * password and email. Other methods gives the possibilities to add workouts, get
 * workouts, get the workouts list size, an equals method to check if two users are
 * equal and a methods that returns the users hashcode which is used in the equals method.
 * </p>
 */
public class User {
    private String name;
    private String username;

    // This is a transient field. It will not be serialized.
    private transient String password;
    private String passwordHash;
    private String email;
    private ArrayList<Workout> workouts;

    /**
     * Constructs a new User object with the given name, username, password and
     * email.
     *
     * @param name     the name of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param email    the email of the user
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public User(String name, String username, String password, String email) {
        if (name == null || username == null || password == null || email == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }

        this.name = name;
        this.username = username;
        this.password = password;
        this.passwordHash = hash(password);
        this.email = email;
        workouts = new ArrayList<Workout>();
    }

    /**
     * Returns the name of the user.
     *
     * @return the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the username of the user.
     *
     * @return the user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the user.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the name of the user.
     *
     * @param name the new name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the username of the user.
     *
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Updates the email connected to the user.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the password for the user.
     *
     * @param password the password to set for the user.
     */
    public void setPassword(String password) {
        this.password = password;
        this.passwordHash = hash(password);
    }

    /**
     * Returns the password hash of the user.
     *
     * @return the user's password hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Returns the email of the user.
     *
     * @return the user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the number of workouts the user has.
     *
     * @return number of workouts as int
     */
    public int getNumberOfWorkouts() {
        return workouts.size();
    }

    /**
     * Returns the workouts of the user.
     *
     * @return workouts in an ArrayList
     */
    public ArrayList<Workout> getWorkouts() {
        return new ArrayList<>(workouts);
    }

    /**
     * Adds a workout to the user.
     *
     * @param workout Workout class
     */
    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

    /**
     * Hashes a string using the SHA-256 algorithm. Used for hashing password.
     *
     * @param word the word to hash
     * @return the hashed word as a string
     */
    public static String hash(String word) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // THIS WILL NEVER HAPPEN
            throw new IllegalStateException("SHA-256 is not supported");
        }
        byte[] encodedhash = digest.digest(word.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes the byte array to convert
     * @return the hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hex = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int lastBits = bytes[j] & 0xFF;
            hex[j * 2] = hexArray[lastBits / 16];
            hex[j * 2 + 1] = hexArray[lastBits % 16];
        }
        return new String(hex);
    }

    /**
     * A method to get the hashcode of the user.
     *
     * @return the hashcode of the user
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + username.hashCode();
        result = prime * result + passwordHash.hashCode();
        return result;
    }

    /**
     * A method to check if two users are equal.
     *
     * @param obj the object to compare to
     * @return boolean true if the users are equal, false if not
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
        User other = (User) obj;
        if (!username.equals(other.username)) {
            return false;
        }
        if (!passwordHash.equals(other.passwordHash)) {
            return false;
        }
        return true;
    }
}
