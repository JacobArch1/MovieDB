import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Media {
    private long id;
    private String title;
    private String releaseDate;
    private String description;
    private String Image;
    private String BackdropImage;
    private double score;
    private List<Person> cast = new ArrayList<>();
    private boolean isMovie;

    int popularity;

    public Media(long id, String title, String releaseDate, String description, String image, List<Person> cast, boolean isMovie, String BackdropImage, double score) {
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

    public Media(long id, String title, String image, boolean isMovie){
        this.id = id;
        this.title = title;
        this.Image = image;
        this.isMovie = isMovie;
    }

    public Media() {
        //default
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        LocalDate inputDate = LocalDate.parse(releaseDate, inputFormatter);

        String outputDateString = inputDate.format(outputFormatter);
        return outputDateString;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage()  { 
        if(this.Image == null || this.Image.trim().isEmpty()){
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

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean getisMovie(){
        return isMovie;
    }
    
    public void setisMovie(boolean isMovie){
        this.isMovie = isMovie;
    }

    public String getBackImage(){
        return "https://media.themoviedb.org/t/p/w1920_and_h800_multi_faces/" + BackdropImage;
    }

    public void setBackImage(String BackdropImage){
        this.BackdropImage = BackdropImage;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}