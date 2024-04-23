import java.util.ArrayList;
import java.util.List;

public class User {
    private String username = "";
    private long id = 0;

    List<Long> idList = new ArrayList<>();
    List<Media> myMovieLibrary = new ArrayList<>();
    List<Media> myTVLibrary = new ArrayList<>();

    public User(String username, long id) {
        this.username = username;
        this.id = id;
    }

    public User() {
        //default
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}