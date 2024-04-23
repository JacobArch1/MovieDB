import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private long id;
    private String name;
    private String character;
    private String image;
    private String biography;
    private String career;
    private String birthday;
    private String deathday;

    List<Media> knownMedia = new ArrayList<>();

    public Person(long id, String name, String character, String image, String biography, String career) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.image = image;
        this.biography = biography;
        this.career = career;
    }

    public Person(long id, String name, String character, String image){
        this.id = id;
        this.name = name;
        this.character = character;
        this.image = image;
    }

    public Person(String biography, String career, List<Media> knownMedia){
        this.biography = biography;
        this.career = career;
        this.knownMedia = knownMedia;
    }

    public Person(){
        //default
    }

    public long getID() {
        return id;
    }

    public void setID(long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getImage() {
        if(this.image == null || this.image.trim().isEmpty()){
            return "NoImagePerson.png";
        }
        return "https://image.tmdb.org/t/p/w500/" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public List<Media> getKnownMedia() {
        return knownMedia;
    }

    public void setKnownMedia(List<Media> knownMedia) {
        this.knownMedia = knownMedia;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) throws ParseException {
        if(!birthday.equals("Null")){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            date = dateFormat.parse(birthday);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            this.birthday = yearFormat.format(date);
        }
        else{ this.birthday = "Unknown"; }
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) throws ParseException {
        if(!deathday.equals("Null")){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            date = dateFormat.parse(deathday);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            this.deathday = yearFormat.format(date);
        }
        else if(this.birthday.equals("Unknown")){ this.deathday = "Unknown"; }
        else{ this.deathday = "Today"; }
    }
}