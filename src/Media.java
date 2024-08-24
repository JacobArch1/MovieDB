import java.util.ArrayList;
import java.util.List;

public class Media {
    private long id;
    private long budget;
    private long runtime;
    private String title;
    private String releaseDate;
    private String description;
    private String Image;
    private String BackdropImage;
    private String tagline;
    private String director;
    private String genres;
    private double score;
    private List<Person> cast = new ArrayList<>();
    private boolean isMovie;

    int popularity;

    public Media(long id, String title, String releaseDate, String description, String image, List<Person> cast,
            boolean isMovie, String BackdropImage, double score) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.Image = image;
        this.cast = cast;
        this.isMovie = isMovie;
        this.BackdropImage = BackdropImage;
        this.score = score;
    }

    public Media(String releaseDate, String description, List<Person> cast, String BackdropImage) {
        this.description = description;
        this.releaseDate = releaseDate;
        this.cast = cast;
        this.BackdropImage = BackdropImage;
    }

    public Media(long id, String title, String image, boolean isMovie) {
        this.id = id;
        this.title = title;
        this.Image = image;
        this.isMovie = isMovie;
    }

    public Media() {
        // default
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        // grab the year from the release date by splitting the string
        String[] date = releaseDate.split("-");
        this.releaseDate = date[0];
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        if (this.Image == null || this.Image.trim().isEmpty()) {
            return "NoImageMedia.png";
        }
        return "https://image.tmdb.org/t/p/w600_and_h900_bestv2" + Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public List<Person> getCast() {
        return cast;
    }

    public void setCast(List<Person> cast) {
        this.cast = cast;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean getisMovie() {
        return isMovie;
    }

    public void setisMovie(boolean isMovie) {
        this.isMovie = isMovie;
    }

    public String getBackImage() {
        return "https://media.themoviedb.org/t/p/w1920_and_h800_multi_faces/" + BackdropImage;
    }

    public void setBackImage(String BackdropImage) {
        this.BackdropImage = BackdropImage;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        if (director == null) {
            this.director = "Unknown";
        }
        this.director = director;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}