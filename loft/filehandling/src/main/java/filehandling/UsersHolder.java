package filehandling;

import core.User;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for holding the users in a list. It is used for parsing the
 * json file. It is package-private so as few classes possible can use it.
 */
class UsersHolder {
    private List<User> users;

    /**
     * Constructor for the UsersHolder class.
     *
     * @param users List of users
     */
    public UsersHolder(List<User> users) {
        this.users = new ArrayList<User>(users);
    }

    /**
     * Returns the users in the UsersHolder class.
     *
     * @return List of users
     */
    public List<User> getUsers() {
        return new ArrayList<User>(users);
    }
}
