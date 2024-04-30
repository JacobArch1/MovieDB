import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainScene extends Login{
    String mySQLPassword = "pass";

    @FXML
    private Label DetailedMediaTitle, MediaOverview, MediaInfo, lblMenuTitle, lblLibraryTitle, DetailedPersonLabel, 
    PersonOverview, lblKnownFor, lblBdayDday;

    @FXML
    private ImageView DetailedMovieImage, ImageBackdrop, DetailedPersonImage;

    @FXML
    public AnchorPane apLibrary, apSearch, apMovieDetails, apMenuResults, apPersonDetails;

    @FXML
    private Button btnSearch, btnAddToLibrary, btnRemoveFromLibrary, btnBack, btnBack2;

    @FXML
    private TextField tfSearch;

    @FXML
    private ProgressBar pbUserScore;

    @FXML
    private HBox hbCastList, hbKnownRoles;

    @FXML
    private VBox vbLibrary, vbSearchResults, vbMenuResults;

    @FXML 
    private ScrollPane spSearch, spLibrary, spCast, spMenu, spRoles; 

    @FXML
    private MenuItem miMoviesNowPlaying, miMoviesPopular, miMoviesTopRated, miMoviesUpcoming, 
    miTVAiringToday, miTVOnTv, miTVPopular, miTVTopRated, miLibTVShows, miLibMovies, miLogout, miSettings;

    Connection con;
    private Stack<AnchorPane> paneStack = new Stack<>();
    static boolean menuisMovie, tvLibrary = false;
    static List<Media> movieSearchResultsList = new ArrayList<>();
    static List<Media> tvSearchResultsList = new ArrayList<>();
    static List<Media> menuResultsList = new ArrayList<>();
    private Media selectedMedia;
    private Person selectedPerson;
    static String menuShowing = "";

    public void initialize() throws SQLException, IOException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb", "root", mySQLPassword);

        lblMenuTitle.setText("Movies Playing Now");
        paneStack.push(apMenuResults);
        menuShowing = "MovPN";
        menuisMovie = true;
        try {
            renderMenuResults( "https://api.themoviedb.org/3/movie/now_playing?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void addMovies(VBox vBox, List<Media> list) throws IOException {
        final int moviesPerRow = 6;
        
        HBox currentHBox = new HBox();
        vBox.getChildren().add(currentHBox);
    
        for (int i = 0; i < list.size(); i++) {
            Media media = list.get(i);
    
            ImageView imageView = new ImageView(media.getImage());
            imageView.setFitWidth(175);
            imageView.setFitHeight(269);
            imageView.setCursor(Cursor.HAND);
    
            Label label = new Label(media.getTitle());
            label.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
            label.setStyle("-fx-text-fill: #01b4e4;");
            label.setMaxWidth(150);
            label.setWrapText(true);
            
            VBox vbox = new VBox();
            vbox.getChildren().addAll(imageView, label);
            vbox.setPadding(new Insets(5));
    
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        selectedMedia = media;
                        mediaClicked(event, media);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
    
            currentHBox.getChildren().add(vbox);

            if ((i + 1) % moviesPerRow == 0 && i + 1 < list.size()) {
                currentHBox = new HBox();
                vBox.getChildren().add(currentHBox);
            }
        }
    }

    @FXML
    void personClicked(MouseEvent event, Person person) throws Exception{
        getPersonInfo(person);
        showPane(apPersonDetails);
        resetScroll();
        menuShowing = "";

        DetailedPersonLabel.setText(person.getName());
        PersonOverview.setText(person.getBiography());
        lblBdayDday.setText(person.getBirthday() + " - " + person.getDeathday());
        lblKnownFor.setText("Known For: " + person.getCareer());
        Image image = new Image(person.getImage());
        DetailedPersonImage.setImage(image);

        hbKnownRoles.getChildren().clear();
        addKnownRoles(hbKnownRoles, person.getKnownMedia());
    }

    public void addKnownRoles(HBox hBox, List<Media> knownRoles) throws IOException{
        for (int i = 0; i < knownRoles.size(); i++){
            Media media = knownRoles.get(i);

            ImageView imageView = new ImageView(media.getImage());
            imageView.setFitWidth(130);
            imageView.setFitHeight(189);

            Label label1 = new Label(media.getTitle());
            label1.setFont(Font.font("Calibri", 16));
            label1.setStyle("-fx-text-fill: #01b4e4;");
            label1.setWrapText(true);

            VBox vbox = new VBox();
            vbox.getChildren().addAll(imageView, label1);
            vbox.setPadding(new Insets(5));

            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        selectedMedia = media;
                        mediaClicked(event, media);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            hBox.getChildren().add(vbox);
        }
    }

    @FXML
    void mediaClicked(MouseEvent event, Media media) throws SQLException, IOException {
        showPane(apMovieDetails);
        resetScroll();
        menuShowing = "";

        DetailedMediaTitle.setText(media.getTitle());
        MediaOverview.setText(media.getDescription());
        MediaInfo.setText(media.getReleaseDate());
        Image image = new Image(media.getImage());
        DetailedMovieImage.setImage(image);
        Image backImage = new Image(media.getBackImage());
        ImageBackdrop.setImage(backImage);
        pbUserScore.setProgress(media.getScore()/10);

        hbCastList.getChildren().clear();
        addCastList(hbCastList, media.getCast());
        ShowButton();
    }

    public void addCastList(HBox hBox, List<Person> castList) throws IOException{
        for (int i = 0; i < castList.size(); i++){
            Person person = castList.get(i);

            ImageView imageView = new ImageView(person.getImage());
            imageView.setCursor(Cursor.HAND);
            imageView.setFitWidth(130);
            imageView.setFitHeight(189);

            Label label1 = new Label(person.getName());
            Label label2 = new Label(person.getCharacter());
            label1.setFont(Font.font("Calibri", 16));
            label2.setFont(Font.font("Calibri", 14));
            label1.setStyle("-fx-text-fill: #01b4e4;");
            label2.setTextFill(Color.WHITE);
            label1.setWrapText(true);
            label2.setWrapText(true);

            VBox vbox = new VBox();
            vbox.getChildren().addAll(imageView, label1, label2);
            vbox.setPadding(new Insets(5));

            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        selectedPerson = person;
                        personClicked(event, selectedPerson);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            hBox.getChildren().add(vbox);
        }
    }
    
    @FXML
    void miLibMoviesClicked(ActionEvent event) throws SQLException, IOException {
        resetScroll();
        showPane(apLibrary);
        Login.user.myMovieLibrary.clear();
        Login.user.idList.clear();
        lblLibraryTitle.setText(user.getUsername() + "'s Movies");
        menuShowing = "";
        tvLibrary = false;

        movieSearchResultsList.clear();
        tvSearchResultsList.clear();

        renderLibrary(Login.user.myMovieLibrary, true);
    }

    @FXML
    void miLibTVShowsClicked(ActionEvent event) throws SQLException, IOException {
        resetScroll();
        showPane(apLibrary);
        Login.user.myTVLibrary.clear();
        Login.user.idList.clear();
        lblLibraryTitle.setText(user.getUsername() + "'s Shows");
        menuShowing = "";
        tvLibrary = true;

        movieSearchResultsList.clear();
        tvSearchResultsList.clear();

        renderLibrary(Login.user.myTVLibrary, false);
    }

    public void renderLibrary(List<Media> library, boolean isMovie) throws SQLException, IOException{
        library.clear();
        Login.user.idList.clear();

        String query = "movie";
        String table = "user_movie_list";
        String dataID = "movie_id";
        if(isMovie == false){
            query = "tv";
            table = "user_tv_list";
            dataID = "tv_id";
        }

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT " + dataID + " FROM " + table + " WHERE user_id = " + Login.user.getId());
        while(rs.next()){
            Login.user.idList.add(rs.getLong(1));
        }
        
        try {
            for(int i = 0; i < Login.user.idList.size(); i++){
                long id = Login.user.idList.get(i);
                String url = "https://api.themoviedb.org/3/" + query + "/"+ id +"?api_key=489bc0e902b5137de4ef51427448ad16&language=en";
                String searchResults = connectEndpoint(url);

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(searchResults);

                String title = (String) jsonObject.get("title");
                if (title == null){ title = (String) jsonObject.get("original_name"); }
                String releaseDate = (String) jsonObject.get("release_date");
                if (releaseDate == null){ releaseDate = (String) jsonObject.get("first_air_date"); }
                String description = (String) jsonObject.get("overview");
                String image = (String) jsonObject.get("poster_path");
                String BackdropImage = (String) jsonObject.get("backdrop_path");
                double popularity = (double) jsonObject.get("popularity");
                Object voteAverage = jsonObject.get("vote_average");
                double score = 0.0;
                if(voteAverage instanceof Number){ score = ((Number) voteAverage).doubleValue(); }
                List <Person> castList = getCredits(id, isMovie);

                Media media = new Media(id, title, releaseDate, description, image, castList, isMovie, BackdropImage, score);
                media.setPopularity((int)popularity);
                library.add(media);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        vbLibrary.getChildren().clear();
        addMovies(vbLibrary, library);
    }

    void search() throws IOException, SQLException{
        resetScroll();
        showPane(apSearch);
        btnAddToLibrary.setVisible(false);
        btnRemoveFromLibrary.setVisible(false);
        menuShowing = "";
        
        movieSearchResultsList.clear();
        tvSearchResultsList.clear();

        String query = tfSearch.getText();
        if (!query.equals("")){
            try {
                query = query.replace(" ","%20");

                String url = "https://api.themoviedb.org/3/search/tv?api_key=489bc0e902b5137de4ef51427448ad16&language=en&query=" + query;
                String searchResults = connectEndpoint(url);
                getData(searchResults, tvSearchResultsList, false);

                url = "https://api.themoviedb.org/3/search/movie?api_key=489bc0e902b5137de4ef51427448ad16&language=en-US&query=" + query;
                searchResults = connectEndpoint(url);
                getData(searchResults, movieSearchResultsList, true);
                
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        vbSearchResults.getChildren().clear();
        List<Media> SearchList = Stream.concat(movieSearchResultsList.stream(), tvSearchResultsList.stream()).collect(Collectors.toList());
        Collections.sort(SearchList,Comparator.comparingInt(Media::getPopularity).reversed());
        addMovies(vbSearchResults, SearchList);
    }
    
    public static String connectEndpoint(String urlString) throws Exception{
        if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
            urlString = "https://" + urlString;
        }
    
        @SuppressWarnings("deprecation")
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            String response = "";

            while ((inputLine = in.readLine()) != null) {
                response += (inputLine);
            }
            in.close();

            return response;

        } else {
            return "Error" + responseCode;
        }
    }

    public static void getData(String jsonString, List<Media> list, boolean isMovie) throws Exception{
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        JSONArray resultsArray = (JSONArray) jsonObject.get("results");
        
        for (int i = 0; i < resultsArray.size(); i++) {
            JSONObject movieObject = (JSONObject) resultsArray.get(i);

            long id = (long) movieObject.get("id");
            String title = (String) movieObject.get("title");
            if (title == null){ title = (String) movieObject.get("original_name");}
            String releaseDate = (String) movieObject.get("release_date");
            if (releaseDate == null){ releaseDate = (String) movieObject.get("first_air_date");}
            String description = (String) movieObject.get("overview");
            if(description == null){description = "No Description Available.";}
            String image = (String) movieObject.get("poster_path");
            String BackdropImage = (String) movieObject.get("backdrop_path");
            double popularity = (double) movieObject.get("popularity");
            Object voteAverage = movieObject.get("vote_average");
            double score = 0.0;
            if(voteAverage instanceof Number){ score = ((Number) voteAverage).doubleValue(); }
            List<Person> castList = getCredits(id, isMovie);

            Media media = new Media(id, title, releaseDate, description, image, castList, isMovie, BackdropImage, score);
            media.setPopularity((int)popularity);
            list.add(media);
        }
    }

    public static void getExtraData(Media media)throws Exception{
        
    }

    public Person getPersonInfo(Person person) throws Exception{
        String url = "https://api.themoviedb.org/3/person/" + person.getID() + "?api_key=489bc0e902b5137de4ef51427448ad16&language=en";
        String personResults = connectEndpoint(url);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(personResults);

        String biography = (String) jsonObject.get("biography");
        String career = (String) jsonObject.get("known_for_department");
        String birthday = (String) jsonObject.get("birthday");
        if(birthday == null){ birthday = "Null"; }
        String deathday = (String) jsonObject.get("deathday");
        if(deathday == null){ deathday = "Null"; }

        person.setBiography(biography);
        person.setCareer(career);
        person.setBirthday(birthday);
        person.setDeathday(deathday);

        /*url = "api.themoviedb.org/3/person/" + person.getID() + "/tv_credits?api_key=489bc0e902b5137de4ef51427448ad16";
        String creditsResults = connectEndpoint(url);
        jsonObject = (JSONObject) parser.parse(creditsResults);*/

        return person;
    }

    public static List<Person> getCredits(long id, boolean isMovie) throws Exception{
        String querySTR = "movie";
        if(isMovie == false){
            querySTR = "tv";
        }
        String url = "https://api.themoviedb.org/3/" + querySTR + "/" + id + "/credits?api_key=489bc0e902b5137de4ef51427448ad16&language=en";
        String credits = connectEndpoint(url);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(credits);
        JSONArray resultsArray = (JSONArray) jsonObject.get("cast");

        List<Person> castList = new ArrayList<>();

        for (int i = 0; i < resultsArray.size(); i++) {
            JSONObject movieObject = (JSONObject) resultsArray.get(i);
            long pid = (long) movieObject.get("id");
            String name = (String) movieObject.get("name");
            String character = (String) movieObject.get("character");
            String image = (String) movieObject.get("profile_path");
            Person person = new Person(pid, name, character, image);
            castList.add(person);
        }

        return castList;
    }

    @FXML
    void btnAddClicked() throws SQLException {
        String table = "user_tv_list";
        String data = "tv_id";
        if (selectedMedia.getisMovie() == true){
            table = "user_movie_list";
            data = "movie_id";
        }

        Statement stmt = con.createStatement();
        stmt.execute("INSERT INTO " + table + " (user_id, " + data + ") VALUES (" + Login.user.getId() + "," + selectedMedia.getId() + ")");
        btnAddToLibrary.setVisible(false);
        btnRemoveFromLibrary.setVisible(true);
    }

    @FXML
    void btnRemoveClicked() throws SQLException{
        String table = "user_tv_list";
        String data = "tv_id";
        if (selectedMedia.getisMovie() == true){
            table = "user_movie_list";
            data = "movie_id";
        }

        Statement stmt = con.createStatement();
        stmt.execute("DELETE FROM " + table + " WHERE user_id = (" + Login.user.getId() + ") AND " + data + " = " + selectedMedia.getId());
        btnRemoveFromLibrary.setVisible(false);
        btnAddToLibrary.setVisible(true);
        Login.user.myMovieLibrary.clear();
        Login.user.myTVLibrary.clear();
        Login.user.idList.clear();
    }

    void renderMenuResults(String url) throws Exception{
        showPane(apMenuResults);
        resetScroll();
        btnRemoveFromLibrary.setVisible(false);
        menuResultsList.clear();

        String searchResults = connectEndpoint(url);
        getData(searchResults, menuResultsList, menuisMovie);
        vbMenuResults.getChildren().clear();
        Collections.sort(menuResultsList,Comparator.comparingInt(Media::getPopularity).reversed());
        addMovies(vbMenuResults, menuResultsList);
    }

    @FXML
    void miMoviesNowPlayingClicked(ActionEvent event) throws Exception {
        if (menuShowing != "MovPN"){
            setMenu("Movies Playing Now", "MovPN", true, 
            "https://api.themoviedb.org/3/movie/now_playing?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        }
    }

    @FXML
    void miMoviesPopularClicked(ActionEvent event) throws Exception {
        if (menuShowing != "MovPop"){
            setMenu("Popular Movies", "MovPop", true, 
            "https://api.themoviedb.org/3/movie/popular?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        }
    }

    @FXML
    void miMoviesTopRatedClicked(ActionEvent event) throws Exception {
        if (menuShowing != "MovTR"){
            setMenu("Top Rated Movies", "MovTR", true, 
            "https://api.themoviedb.org/3/movie/top_rated?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        }
    }

    @FXML
    void miMoviesUpcomingClicked(ActionEvent event) throws Exception {
        if (menuShowing != "MovUPC"){
            setMenu("Upcoming Movies", "MovUPC", true, 
            "https://api.themoviedb.org/3/movie/upcoming?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        }
    }

    @FXML
    void miTVAiringTodayClicked(ActionEvent event) throws Exception {
        if (menuShowing != "TvAirTod"){
            setMenu("TV Shows Airing Today", "TvAirTod", false, 
            "https://api.themoviedb.org/3/tv/airing_today?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        }
    }

    @FXML
    void miTVOnTvClicked(ActionEvent event) throws Exception {
        if (menuShowing != "TvOnTod"){
            setMenu("TV Shows Airing This Week", "TvOnTod", false, 
            "https://api.themoviedb.org/3/tv/on_the_air?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        }
    }

    @FXML
    void miTVPopularClicked(ActionEvent event) throws Exception {
        if (menuShowing != "TvPop"){
            setMenu("Popular TV Shows", "TvPop", false, 
            "https://api.themoviedb.org/3/tv/popular?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        }
    }

    @FXML
    void miTVTopRatedClicked(ActionEvent event) throws Exception {
        if (menuShowing != "TvTopRated"){
            setMenu("Top Rated TV Shows", "TvTopRated", false, 
            "https://api.themoviedb.org/3/tv/top_rated?api_key=489bc0e902b5137de4ef51427448ad16&language=en");
        }
    }

    @FXML
    void tfSearchEnterPressed(ActionEvent event) throws IOException, SQLException {
        search();
    }

    @FXML
    void btnSearchClicked(MouseEvent  event) throws IOException, SQLException {
        search();
    }

    @FXML
    void btnBackClicked(ActionEvent event) throws SQLException, IOException {
        vbLibrary.getChildren().clear();
        resetScroll();
        if (!paneStack.isEmpty()) {
            paneStack.pop();
        }
        if (!paneStack.isEmpty()) {
            AnchorPane previousPane = paneStack.peek();
            showPane(previousPane);
            
            if (previousPane == apLibrary) {
                if (!tvLibrary) {
                    renderLibrary(Login.user.myMovieLibrary, true);
                } else {
                    renderLibrary(Login.user.myTVLibrary, false);
                }
            }
        }
    }

    @FXML
    void miLogoutClicked(ActionEvent event) throws SQLException, IOException {
        Login.user.myMovieLibrary.clear();
        Login.user.myTVLibrary.clear();
        Login.user.idList.clear();
        movieSearchResultsList.clear();
        tvSearchResultsList.clear();
        tfSearch.setText("");
        SceneController scene = new SceneController();
        try {
            scene.SwitchScenes(event, "Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void ShowButton() throws SQLException {
        String table = "user_movie_list";
        String dataID = "movie_id";
        if(selectedMedia.getisMovie() == false){
            table = "user_tv_list";
            dataID = "tv_id";
        }

        btnRemoveFromLibrary.setVisible(false);
        btnAddToLibrary.setVisible(false);

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("Select * From " + table + " Where user_id = " + Login.user.getId()
            + " AND " + dataID + " = " + selectedMedia.getId());
        if(rs.next()){
            btnRemoveFromLibrary.setVisible(true);
        }
        else{
            btnAddToLibrary.setVisible(true);
        }
    }

    void showPane(AnchorPane pane) throws SQLException{
        if (!paneStack.isEmpty() && paneStack.peek() != pane) {
            paneStack.push(pane);
        }
        apLibrary.setVisible(false); 
        apSearch.setVisible(false); 
        apMovieDetails.setVisible(false);
        apMenuResults.setVisible(false); 
        if(pane == apMovieDetails){ShowButton();}
        apPersonDetails.setVisible(false);
        pane.setVisible(true);
    }

    void setMenu(String title, String MenuShowing, boolean isMovie, String link) throws Exception{
        lblMenuTitle.setText(title);
        menuShowing = MenuShowing;
        menuisMovie = isMovie;
        renderMenuResults(link);
    }

    void resetScroll(){
        spSearch.setVvalue(0);
        spLibrary.setVvalue(0);
        if(paneStack.lastElement() != apPersonDetails){
            spCast.setHvalue(0);
        }
        spMenu.setVvalue(0);
    }
}