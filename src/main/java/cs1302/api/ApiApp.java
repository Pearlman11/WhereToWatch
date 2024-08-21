package cs1302.api;
/*testing */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.Optional;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */

public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;


    public static  final String OMDURL = "http://www.omdbapi.com/";
    public static final String OMDAPIKEY = "?&apikey=f758e023&";
    public static final String CLICKED_STATUS = "Searching for: ";

    public static final String WATCHMODEURL = "https://api.watchmode.com/v1/title/";
    public static final String WATCHMODEURL2 = "/details/?apiKey=" +
        "IyBdVVAShlY7inin8hrLnbZou6sv2Kx4CUvH9a5x&append_to_response=sources";




    static final String FILEPATH =  "resources/WatchIDandMovieName.rtf";


    TabPane tabPane;
    Tab mainTab;
    Tab loadedTab;
    VBox mainPage;
    VBox loadingPage;

    HBox services;

    Tab servicesTab;
    VBox servicesPage;
    /*Variables for loadedTab page. */


    /* Initializing variables for WhereToWatch Homepage. */
    Label searchLabel;

    Image logo;
    Button searchButton;
    Text appName;
    TextField searchField;
    HBox searchBar;
    HBox titleBox;
    Text directions;
    Map<String,String> moviesMap;
    TextField yearField;
    Label yearLabel;

    // ComboBox for the dropdown, user can select movie, Series or episode
    Label typeLabel;
    ComboBox<String> searchType;


    /* variables for OMD API */
    String omdTitle;
    String type;
    String omdResponseType = "json";
    String omdPlot;
    String year;
    String omdQuery;

    OmdResponse omdResponse;

    /* Creating instances for after the page has loaded. */
    HBox loadedPage;
    VBox posterScreen;
    VBox movieInfo;
    ImageView posterView;
    Image posterImage;
    Text movieInformation;


    /* Required Paramaters for the WatchMode API. */
    int titleId;

    String watchQuery;
    WatchModeResponse watchResponse;
    /*Table view for the services. */
    TableView<Sources> movieTable;
    TableColumn<Sources,String> nameColumn;
    TableColumn<Sources,Double> priceColumn;
    TableColumn<Sources,String> urlColumn;
    TableColumn<Sources,String> typeColumn;
    Label tableLabel;
    ObservableList<Sources> sourcesList;
    ObservableList<Sources> currentItems;
    Sources[] oldSources;

    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent clipboardContent = new ClipboardContent();
    Sources currentSource = new Sources();
    ObservableList<Sources> sourceData = FXCollections.observableArrayList();

    Text titleText;
    Text actorsText;
    Text awardText;
    Text plotText;
    TextFlow movieTextFlow;
    Text similarTitlesText;
    String[] similarTitlesArr;
    String placeholder;
    int seconds;
    int rateLimitPerMinute;
    Text loadedDirections;
    Text attribution;


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
    } // ApiApp


    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    /** {@inheritDoc} */
    @Override
    public void init() {
        this.setVariables();
        EventHandler<ActionEvent> searchButtonClicked = (event) -> {
            Runnable taskForThread = () -> {
                this.setSearchValues();
                this.loadPage();
                this.tabPane.getSelectionModel().select(loadedTab);

            };
            runInThread(taskForThread);
        };
        searchButton.setOnAction(searchButtonClicked);

    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(this.root);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("GalleryApp!");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        Platform.runLater(() -> this.stage.setResizable(true));
    } // start


    /**
     * Method used to initialize the variables used in the app.
     */
    public void setVariables() {
        this.setMoreVariables();
        /* Labels. */
        searchLabel = new Label("Search:");
        yearLabel = new Label("Year:");
        /*TextFields. */
        searchField = new TextField("Fight Club");
        yearField = new TextField("1999");
        /* Button(s). */
        searchButton = new Button("Find WTW");
        /*Combobox */
        typeLabel = new Label("Type:");
        searchType = new ComboBox<>();
        searchType.getItems().addAll("Movie",  "Series");
        searchType.getSelectionModel().selectFirst();

        attribution = new Text("Movie information provided by WatchModeAPI and OMDbAPI");
        typeLabel.setLabelFor(searchType);
        loadedPage = new HBox();
        posterScreen = new VBox();
        movieInfo = new VBox();
        loadedDirections = new Text("Click on tab 3 to find streaming services!\n");
        loadedDirections.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        this.movieInfo.getChildren().addAll(loadedDirections,movieTextFlow);
        loadedPage.getChildren().addAll(posterScreen,movieInfo);

        /*Setting labels. */
        searchLabel.setLabelFor(searchField);
        this.searchBar.getChildren().addAll(searchLabel,searchField,
            yearLabel, yearField, searchButton, typeLabel,searchType);
        searchBar.setHgrow(searchLabel, Priority.ALWAYS);
        searchBar.setHgrow(searchField, Priority.ALWAYS);
        searchBar.setHgrow(searchButton, Priority.ALWAYS);
        searchBar.setHgrow(yearLabel, Priority.ALWAYS);
        searchBar.setHgrow(yearField, Priority.ALWAYS);
        titleBox.setHgrow(appName, Priority.ALWAYS);
        titleBox.setAlignment(Pos.CENTER);
        directions.setFont(new Font(14));
        this.mainPage.getChildren().addAll(titleBox,searchBar,directions);
        this.loadedPage.setSpacing(50);
        root.setVgrow(loadedPage, Priority.ALWAYS);
        this.root.setSpacing(10);
        servicesPage.setSpacing(5);
        servicesPage.setPadding(new Insets(10, 0, 0, 10));
        servicesPage.getChildren().addAll(tableLabel,movieTable);
        mainTab.setContent(mainPage);
        loadedTab.setContent(loadedPage);
        loadedTab.setDisable(true);
        servicesTab.setDisable(true);
        servicesTab.setContent(servicesPage);
        tabPane.getTabs().addAll(mainTab,loadedTab,servicesTab);
        this.posterScreen.getChildren().addAll(posterView,attribution);
        this.root.getChildren().add(tabPane);
    } // setVariables

    /**
     * Run the task in a new thread.
     * @param r the task to be run.
     */
    public static void runInThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.start();
    } //runInThread

    /** used to retrieve the unique ID from dataset for the WatchMode API.
     * @throw IOException if the title is not found.
     * @param title the title to return the id of.
     * @return the corresponding id from the title.
     */
    public String getWatchID(String title) throws IOException {
        String id = null;

        for (Entry<String,String> entry: moviesMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(title + "\\")) {
                id = entry.getKey();
                break;
            }
        } // for
        if (id == null) {
            throw new IOException("ID NOT FOUND.");
        }
        return id;
    }

    /**
     * Set corresponding values when the search button is clicked.
     */
    public void setSearchValues() {

        omdTitle = searchField.getText();
        year = yearField.getText();
        type = searchType.getValue();
        omdQuery = buildOMDQuery(omdTitle, type, omdResponseType, year);


    }

    /**
     * used to build the OMD query. Credit to Dr.Barnes and Dr.Cotterell.
     * @param title the title to be searched for
     * @param type the type of the search (movie,tv-show)
     * @param year the year of the show or movie.
     * @param responseType the format of the response
     * @return the omdQuery.
     */
    private String buildOMDQuery(String title, String type, String responseType, String year) {
        String query;
        String newTitle = title.substring(0,title.length());
        String encodedTitle = URLEncoder.encode(newTitle, StandardCharsets.UTF_8);
        String encodedType = URLEncoder.encode(type, StandardCharsets.UTF_8);
        String encodedResponseType = URLEncoder.encode(responseType, StandardCharsets.  UTF_8);
        if (!year.equals(null)) {

            String encodedYear = URLEncoder.encode(year, StandardCharsets.UTF_8);
            query = String.format("t=%s&type=%s&r=%s&y=%s",
            encodedTitle,encodedType,encodedResponseType,encodedYear);
        } else {
            query = String.format("t=%s&type=%s&r=%s",encodedTitle,encodedType,encodedResponseType);
        }
        return OMDURL + OMDAPIKEY + query;
    }

    /**
     * Method used to create the Watchmode API query.
     * @return the watch API query.
     * @throws IOException
     */

    private String buildWatchQuery() throws IOException {

        String watchTitle = getWatchID(omdResponse.title);
        return WATCHMODEURL + watchTitle + WATCHMODEURL2;


    }

    /**
     * Throws an {@link java.io.IOException} if the HTTP status code of the
     * {@link java.net.http.HttpResponse} supplied by {@code response} is not
     * {@code 200 OK}. Credit to Dr.Barnes and Dr.Cotterell.
     * @param <T>
     * @param response
     * @throws IOException
     */

    private static <T> void ensureGoodResponse(HttpResponse<T> response) throws IOException {
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        }
    }

    /**
     * Helper method used to load the page when the button is clicked.
     */
    public void loadPage() {
        String responseBody;
        setSearchValues();
        omdQuery = buildOMDQuery(this.omdTitle, this.type,
        this.omdResponseType, this.year);
        URI uri = URI.create(omdQuery);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .build();
        try {
            moviesMap = EncodedMovies.getMap();
            this.directions.setText(CLICKED_STATUS + this.omdTitle);
            this.directions.setStyle("-fx-font: 12 arial;");
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            ApiApp.ensureGoodResponse(response);
            responseBody = response.body();
            omdResponse = GSON.<OmdResponse>fromJson(responseBody, OmdResponse.class);
            this.posterImage = new Image(omdResponse.poster);
            this.posterView.setImage(posterImage);
            this.titleText.setText(omdResponse.title + " (" + omdResponse.year + ")");
            titleText.setStyle("-fx-font: 14 arial; -fx-font-weight: bold;");
            this.actorsText.setText("\n\nActors:\n" + omdResponse.actors);
            this.actorsText.setStyle("-fx-font: 12 arial;");
            this.awardText.setText("\n\nAwards:\n" + omdResponse.awards);
            this.awardText.setStyle("-fx-font: 12 arial;");
            this.plotText.setText("\n\nPlot:\n" + omdResponse.plot);
            this.plotText.setWrappingWidth(250);
            this.plotText.setStyle("-fx-font: 12 arial;");
            loadedTab.setDisable(false);
            servicesTab.setDisable(false);
            if (seconds != 0 && rateLimitPerMinute <= 0) {
                System.out.println(seconds);
                Platform.runLater(() -> {
                    this.getAlert("Rate limit exceeed, try again after: " + seconds + " seconds");
                });
            }
            Platform.runLater( () -> this.watchModeInfo());
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
            this.getAlert(ie.getMessage());
        } catch (IllegalArgumentException iae) {
            this.getAlert(iae.getMessage());
        } catch (IOException ex) {
            Platform.runLater(() -> {
                this.getAlert("Could not initialize movie key-value pairs");
            });
        } catch (NullPointerException npe) {
            Platform.runLater(() -> {
                this.getAlert("Invalid Moive/Series please try again.");
            });
        } catch (NoSuchElementException nfe) {
            Platform.runLater(() -> {
                this.getAlert("Invalid Moive/Series please try again.");
            });
        } // try
        Platform.runLater(() -> this.directions.setText("Results for: "
            + this.omdTitle + " found."));
    } // loadpage

    /**
     * Helper method used to get the content of the cell clicked in sources table.
     */
    private void copySelected () {
        currentSource = movieTable.getSelectionModel().getSelectedItem();
        try {
            clipboardContent.putString(currentSource.weburl);
            clipboard.setContent(clipboardContent);
        } catch (NullPointerException npe) {
            this.getAlert("Only click on the cells please");
        }


    } // copySelected

    /** used to run alerts.
     * @param s  the message to display to the user.
     */
    public void getAlert(String s) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setWidth(500);
        alert.setTitle("Error");
        alert.setContentText(s);
        alert.showAndWait();
    }

    /**
     * Method used to initialize additional variables.
     */
    public void setMoreVariables() {

        titleText = new Text();
        actorsText = new Text();
        awardText = new Text();
        plotText = new Text();
        similarTitlesText = new Text();
        movieTextFlow = new TextFlow(titleText,actorsText,awardText,plotText,similarTitlesText);
        movieTable = new TableView<Sources>();
        nameColumn = new TableColumn<>("Name");
        nameColumn.setSortable(false);
        urlColumn = new TableColumn<>("Link");
        urlColumn.setSortable(false);
        priceColumn = new TableColumn<>("Price");
        priceColumn.setSortable(false);
        typeColumn = new TableColumn<>("Type");
        typeColumn.setSortable(false);
        movieTable.setEditable(false);
        tableLabel = new Label("Where To watch");
        tabPane = new TabPane();
        mainTab = new Tab();
        loadedTab = new Tab();
        mainPage = new VBox();
        loadingPage = new VBox();
        servicesPage = new VBox();
        servicesTab = new Tab();
        mainTab.setText("Search!");
        loadedTab.setText("Movie Information");
        servicesTab.setText("Where to Watch");
        this.posterView = new ImageView();
        this.movieInformation = new Text();
        appName = new Text("Where To Watch");
        appName.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));

        //setting the position of the text
        appName.setX(50);
        appName.setY(130);
        //Setting the Stroke
        appName.setStrokeWidth(2);
        // Setting the stroke color
        appName.setStroke(Color.BLUE);
        // setting the searchBar
        searchBar = new HBox(8);
        titleBox = new HBox();
        titleBox.getChildren().add(appName);
        directions = new Text("Please enter a title to search for and"
        + " we will show you where to watch it!!");
        Platform.runLater (() -> {
            try {
                moviesMap = EncodedMovies.getMap();
            } catch (IOException ie) {
                this.getAlert("COULD NOT INITIALIZE MOIVES KEY-VALUE PAIRS");
            }
        });




    } // setMoreVariables



    /**
     * Create the watchmode request and get the response.
     */
    public void watchModeInfo() {
        String responseBody;
        String tempString;
        try {
            watchQuery = buildWatchQuery();
            URI uri = URI.create(watchQuery);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

            HttpResponse<String> response = HTTP_CLIENT
                .send(request,BodyHandlers.ofString());
            Optional<String> retryAfterMinutes = response.headers()
                .firstValue("X-RateLimit-Limit");
            Optional<String> retryAfterSeconds = response.headers()
                .firstValue("Retry-After");
            if (retryAfterMinutes.isPresent()) {
                String retryAfterMinutesValue = retryAfterMinutes.get();
                String retryAfterSecondsValue = retryAfterSeconds.get();

                rateLimitPerMinute = Integer.parseInt(retryAfterMinutesValue);

                seconds = Integer.parseInt(retryAfterSecondsValue);

            }

            ApiApp.ensureGoodResponse(response);
            responseBody = response.body();
            watchResponse = GSON.<WatchModeResponse>fromJson(responseBody, WatchModeResponse.class);
            this.currentItems = this.movieTable.getItems();
            this.similarTitlesArr = getSimilarTitles();
            this.placeholder = "";
            for (int i = 0; i < similarTitlesArr.length / 2; i++ ) {
                tempString = similarTitlesArr[i].substring(0,
                similarTitlesArr[i].length() - 1) + "\n";
                this.placeholder += (tempString);

            }

            Platform.runLater(() -> this.similarTitlesText.setText("\n\n Similar Titles:\n"
                + placeholder));
            this.similarTitlesText.setWrappingWidth(250);
            this.similarTitlesText.setStyle("-fx-font: 12 arial;");

            if (!this.currentItems.isEmpty()) {
                this.sourceData.clear();
                this.movieTable.getItems().clear();
            }
            initializeTableView(buildData(watchResponse.sources));
        } catch (IOException ex) {
            this.getAlert(ex.getMessage());
        } catch (InterruptedException ie) {
            this.getAlert(ie.getMessage());
        } catch (NullPointerException npe) {
            this.getAlert(npe.getMessage());
        }

    } //watchModeInfo

/**
 * used to initalize the sources table.
 * @param sourceData
 */
    public void initializeTableView(ObservableList<Sources> sourceData) {

        this.movieTable.getColumns().clear();

        movieTable.setItems(sourceData);
        nameColumn.setCellValueFactory(cellData ->
        new ReadOnlyStringWrapper(cellData.getValue().name));
        movieTable.getColumns().add(nameColumn);

        priceColumn.setCellValueFactory(cellData ->
        new ReadOnlyObjectWrapper<>(cellData.getValue().price));
        movieTable.getColumns().add(priceColumn);

        typeColumn.setCellValueFactory(cellData ->
        new ReadOnlyStringWrapper(cellData.getValue().type));
        movieTable.getColumns().add(typeColumn);

        urlColumn.setCellValueFactory(cellData ->
        new ReadOnlyStringWrapper(cellData.getValue().weburl));
        movieTable.getColumns().add(urlColumn);

        movieTable.getColumns().setAll(nameColumn,priceColumn,urlColumn,typeColumn);

        movieTable.getSelectionModel().setCellSelectionEnabled(true);
        movieTable.setOnMouseClicked((MouseEvent event) -> copySelected() );

    }


    /**
     * used to initalize the sources table.
     * @param sourcesArr
     * @return the list the table will mirror.
     */
    public ObservableList<Sources> buildData(Sources[] sourcesArr) {


        for (Sources row: sourcesArr) {
            sourceData.add(row);
        } //for




        return sourceData;
    }

    /**
     * Return the string from the id of the similar titles.
     * @param id
     * @return the string value from the watch api key value.
     */
    public String watchIdToTitle(String id) {

        if (this.moviesMap.get(id) == null) {
            throw new NullPointerException("The id does not have a corresponding title");
        }

        return this.moviesMap.get(id);
    }

    /**
     * Used to get the similar titles for the movie that was searched for.
     * @return an array containing the similar titles.
     */
    public String[] getSimilarTitles() {
        String[] similarMovieTitles = new String[this.watchResponse.similartitles.length];

        for (int i = 0; i < this.watchResponse.similartitles.length; i++) {
            similarMovieTitles[i] = this.watchIdToTitle(this.watchResponse
                .similartitles[i].substring(0,this.watchResponse.similartitles[i].length()));
        }

        return similarMovieTitles;

    }


} // ApiApp
