package sample;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    private String url;
    URL goToUrl;
    StringBuilder str = new StringBuilder();
    List<Country> countries = new ArrayList<>();
    List<Country> allcountries = new ArrayList<>();
    String dateRap;
    int cases;
    int deaths;
    String countriesAndTerritories;
    String geoId;
    String countryterritoryCode;
    int popData2018;
    String continentExp;
    int toplamOlum;
    int UlkeBasinaToplamOlen;
    int toplamVaka;
    int UlkeBasinaToplamVaka;
    double olumOrani;
    double atakHizi;
    String[] ulkeAdi = new String[208];
    ListView<String> ulkeAdiListViewTotalDeath;
    ListView<String> ulkeAdiListViewTotalCases;
    List<String> pandemiAllDate = new ArrayList<>();

    Date now = new Date();
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    TableView<Country> tableView;


    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();


        //Country
        TableColumn<Country, String> countryName = new TableColumn<>("Country");
        countryName.setMinWidth(200);
        countryName.setCellValueFactory(new PropertyValueFactory<>("countriesAndTerritories"));

        //newCases
        TableColumn<Country, String> newCases = new TableColumn<>("New Cases");
        newCases.setMinWidth(130);
        newCases.setCellValueFactory(new PropertyValueFactory<>("cases"));

        //newDeaths
        TableColumn<Country, String> newDeaths = new TableColumn<>("New Deaths");
        newDeaths.setMinWidth(130);
        newDeaths.setCellValueFactory(new PropertyValueFactory<>("deaths"));

        //Population
        TableColumn<Country, String> population = new TableColumn<>("Population");
        population.setMinWidth(130);
        population.setCellValueFactory(new PropertyValueFactory<>("popData2018"));

        //ToplamÖlüm
        TableColumn<Country, String> toplamOlenSayisi = new TableColumn<>("Total Deaths");
        toplamOlenSayisi.setMinWidth(130);
        toplamOlenSayisi.setCellValueFactory(new PropertyValueFactory<>("totalDeath"));

        //ToplamVaka
        TableColumn<Country, String> toplamVakaSayisi = new TableColumn<>("Total Cases");
        toplamVakaSayisi.setMinWidth(130);
        toplamVakaSayisi.setCellValueFactory(new PropertyValueFactory<>("totalCases"));

        //Mortality
        TableColumn<Country, String> ulkeOlumOrani = new TableColumn<>("Mortality");
        ulkeOlumOrani.setMinWidth(160);
        ulkeOlumOrani.setMaxWidth(500);
        ulkeOlumOrani.setCellValueFactory(new PropertyValueFactory<>("mortality"));

        //AtakHızı
        TableColumn<Country, String> ulkeAtakHizi = new TableColumn<>("Atack Rate");
        ulkeAtakHizi.setMinWidth(190);
        ulkeAtakHizi.setMaxWidth(500);
        ulkeAtakHizi.setCellValueFactory(new PropertyValueFactory<>("atackRate"));


        tableView = new TableView<>();
        tableView.setMaxHeight(200);
        tableView.setMinWidth(200);

        VBox vBoxTotalDeath = new VBox();//linechart , listview ve button koyulacak içice
        VBox vBoxTotalCases = new VBox();//linechart , listview ve button koyulacak içice

        Button showLineChartTotalDeath = new Button();
        showLineChartTotalDeath.setText("Show Chart");

        Button showLineChartTotalCases = new Button();
        showLineChartTotalCases.setText("Show Chart");


        Label label = new Label();
        label.setText("Dataset URL : ");
        label.setFont(Font.font(25));

        TextField textField = new TextField();
        textField.setFont(Font.font(15));
        textField.setMinWidth(500);
        textField.setText("https://opendata.ecdc.europa.eu/covid19/casedistribution/xml/");

        Button button = new Button("Get Data");
        button.setFont(Font.font(15));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(label, textField, button);


        //Total Death line chart
        final CategoryAxis xAxisTD = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxisTD.setLabel("Date");
        final LineChart<String, Number> lineChartTotalDeath =
                new LineChart<String, Number>(xAxisTD, yAxis);

        lineChartTotalDeath.setTitle("Total Death per Countries");

        lineChartTotalDeath.setMaxHeight(300);
        lineChartTotalDeath.setMinWidth(900);
        lineChartTotalDeath.setMaxWidth(1000);

        //Total Cases line chart
        final CategoryAxis xAxisTC = new CategoryAxis();
        final NumberAxis yAxisTC = new NumberAxis();
        xAxisTC.setLabel("Date");
        final LineChart<String, Number> lineChartTotalCases =
                new LineChart<String, Number>(xAxisTC, yAxisTC);

        lineChartTotalCases.setTitle("Total Cases per Countries");

        lineChartTotalCases.setMaxHeight(300);
        lineChartTotalCases.setMinWidth(900);
        lineChartTotalCases.setMaxWidth(1000);



        //Linke gidip veriyi çekip işleme
        //
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                url = textField.getText();
                countries.clear();
                allcountries.clear();
                if (!url.equals("")) {

                    InputStream in = null;
                    try {
                        goToUrl = new URL(url);
                        in = goToUrl.openStream();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scanner scan = new Scanner(in);


                    while (scan.hasNext()) {
                        str.append(scan.nextLine());

                    }
                    String recordGroupPatternString = "(<record>)(.*?)(</record>)";
                    Pattern recordGroupPattern = Pattern.compile(recordGroupPatternString);
                    Matcher recordGroupMatcher = recordGroupPattern.matcher(str);
                    int i = 1;
                    while (recordGroupMatcher.find()) {
                        //System.out.println(i + " " + recordGroupMatcher.group(2));
                        //i++;

                        String dateRepGroupPatternString = "(<dateRep>)(.*?)(</dateRep>)";
                        Pattern dateRepGroupPattern = Pattern.compile(dateRepGroupPatternString);
                        Matcher dateRepGroupMatcher = dateRepGroupPattern.matcher(recordGroupMatcher.group(2));

                        while (dateRepGroupMatcher.find()) {
                            dateRap = dateRepGroupMatcher.group(2);
                        }

                        String casesGroupPatternString = "(<cases>)(.*?)(</cases>)";
                        Pattern casesGroupPattern = Pattern.compile(casesGroupPatternString);
                        Matcher casesGroupMatcher = casesGroupPattern.matcher(recordGroupMatcher.group(2));

                        while (casesGroupMatcher.find()) {
                            cases = Integer.parseInt(casesGroupMatcher.group(2));
                        }

                        String deathsGroupPatternString = "(<deaths>)(.*?)(</deaths>)";
                        Pattern deathsGroupPattern = Pattern.compile(deathsGroupPatternString);
                        Matcher deathsGroupMatcher = deathsGroupPattern.matcher(recordGroupMatcher.group(2));

                        while (deathsGroupMatcher.find()) {
                            deaths = Integer.parseInt(deathsGroupMatcher.group(2));
                        }

                        String countriesAndTerritoriesGroupPatternString = "(<countriesAndTerritories>)(.*?)(</countriesAndTerritories>)";
                        Pattern countriesAndTerritoriesGroupPattern = Pattern.compile(countriesAndTerritoriesGroupPatternString);
                        Matcher countriesAndTerritoriesGroupMatcher = countriesAndTerritoriesGroupPattern.matcher(recordGroupMatcher.group(2));

                        while (countriesAndTerritoriesGroupMatcher.find()) {
                            countriesAndTerritories = countriesAndTerritoriesGroupMatcher.group(2);
                        }

                        String geoIdGroupPatternString = "(<geoId>)(.*?)(</geoId>)";
                        Pattern geoIdGroupPattern = Pattern.compile(geoIdGroupPatternString);
                        Matcher geoIdGroupMatcher = geoIdGroupPattern.matcher(recordGroupMatcher.group(2));

                        while (geoIdGroupMatcher.find()) {
                            geoId = geoIdGroupMatcher.group(2);
                        }

                        String countryterritoryCodeGroupPatternString = "(<countryterritoryCode>)(.*?)(</countryterritoryCode>)";
                        Pattern countryterritoryCodeGroupPattern = Pattern.compile(countryterritoryCodeGroupPatternString);
                        Matcher countryterritoryCodeGroupMatcher = countryterritoryCodeGroupPattern.matcher(recordGroupMatcher.group(2));

                        while (countryterritoryCodeGroupMatcher.find()) {
                            countryterritoryCode = countryterritoryCodeGroupMatcher.group(2);
                        }

                        String popData2018GroupPatternString = "(<popData2018>)(.*?)(</popData2018>)";
                        Pattern popData2018GroupPattern = Pattern.compile(popData2018GroupPatternString);
                        Matcher popData2018GroupMatcher = popData2018GroupPattern.matcher(recordGroupMatcher.group(2));

                        while (popData2018GroupMatcher.find()) {
                            if (!popData2018GroupMatcher.group(2).equals(""))
                                popData2018 = Integer.parseInt(popData2018GroupMatcher.group(2));
                            else
                                popData2018 = 0;
                        }

                        String continentExpGroupPatternString = "(<continentExp>)(.*?)(</continentExp>)";
                        Pattern continentExpGroupPattern = Pattern.compile(continentExpGroupPatternString);
                        Matcher continentExpGroupMatcher = continentExpGroupPattern.matcher(recordGroupMatcher.group(2));

                        while (continentExpGroupMatcher.find()) {
                            continentExp = continentExpGroupMatcher.group(2);
                        }


                        countries.add(new Country(dateRap, cases, deaths, countriesAndTerritories, geoId, countryterritoryCode, popData2018, continentExp));

                    }

                    //System.out.println(countries);
                    //Vaka günlerini sırasıyla alma
                    for (int y = 0; y < countries.size(); y++) {
                        if (countries.get(y).getCountriesAndTerritories().equals("China")) {
                            pandemiAllDate.add(countries.get(y).getDateRap());
                        }
                    }

                    //Gün gün toplam ölümleri hesaplayıp list'e ekleme
                    for (int k = 0; k < countries.size(); k++) {


                        UlkeBasinaToplamOlen = 0;
                        UlkeBasinaToplamOlen = totalDeath(countries.get(k).getCountriesAndTerritories(), countries.get(k).getDateRap());
                        UlkeBasinaToplamVaka = 0;
                        UlkeBasinaToplamVaka = totalCases(countries.get(k).getCountriesAndTerritories(), countries.get(k).getDateRap());
                        olumOrani = 0;
                        olumOrani = (double) UlkeBasinaToplamOlen / (double) UlkeBasinaToplamVaka;
                        atakHizi = 0;
                        if (countries.get(k).getPopData2018() == 0) {
                            atakHizi = 0;
                        } else
                            atakHizi = (double) UlkeBasinaToplamVaka / (double) countries.get(k).getPopData2018();
                        allcountries.add(new Country(countries.get(k).getDateRap(), countries.get(k).getCases(), countries.get(k).getDeaths()
                                , countries.get(k).getCountriesAndTerritories(), countries.get(k).getGeoId()
                                , countries.get(k).getCountryterritoryCode()
                                , countries.get(k).getPopData2018(), countries.get(k).getContinentExp(), UlkeBasinaToplamOlen, UlkeBasinaToplamVaka, olumOrani, atakHizi));


                    }


                    //tableview'e veriyi çekme
                    tableView.setItems(getCountry());

                    //listview'e ulke adlarını çekme
                    ulkeAdi = getUlkeAdi();
                    ulkeAdiListViewTotalDeath = new ListView<>(FXCollections.observableArrayList(ulkeAdi));
                    ulkeAdiListViewTotalDeath.setPrefSize(250, 250);
                    ulkeAdiListViewTotalDeath.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                    //listview'e ulke adlarını çekme
                    ulkeAdi = getUlkeAdi();
                    ulkeAdiListViewTotalCases = new ListView<>(FXCollections.observableArrayList(ulkeAdi));
                    ulkeAdiListViewTotalCases.setPrefSize(250, 250);
                    ulkeAdiListViewTotalCases.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


                    vBoxTotalDeath.getChildren().addAll(ulkeAdiListViewTotalDeath, showLineChartTotalDeath);
                    vBoxTotalCases.getChildren().addAll(ulkeAdiListViewTotalCases, showLineChartTotalCases);
                    System.out.println("bitti");

                }

            }
        });

        final int[] isNull = {0};
        tableView.getColumns().addAll(countryName, toplamVakaSayisi, newCases, toplamOlenSayisi, newDeaths, population, ulkeOlumOrani, ulkeAtakHizi);


        final XYChart.Series[] series = new XYChart.Series[209];

        //Butona tıklayınca listviewden ulke adını çekip linechart çizdirme
        //Total Death line chart verilerini çizdirme
        //Toplam ölüm
        showLineChartTotalDeath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                lineChartTotalDeath.getData().clear();
                for (int z = 0; z < series.length; z++) {
                    series[z] = new XYChart.Series();
                }
                for (Integer i : ulkeAdiListViewTotalDeath.getSelectionModel().getSelectedIndices()) {
                    isNull[0] =0;
                    series[i].getData().clear();
                    String ulke = ulkeAdi[i];
                    series[i].setName(ulke);

                    for (int x = pandemiAllDate.size() - 1; x >= 0; x--) {
                        for (int k = allcountries.size() - 1; k >= 0; k--) {
                            if (ulke.equals(allcountries.get(k).getCountriesAndTerritories()) && pandemiAllDate.get(x).equals(allcountries.get(k).getDateRap())) {
                                series[i].getData().add(new XYChart.Data(pandemiAllDate.get(x), allcountries.get(k).getTotalDeath()));
                                isNull[0] =1;
                            }
                        }
                        if(isNull[0]==0){
                            series[i].getData().add(new XYChart.Data(pandemiAllDate.get(x),0));
                        }

                    }
                    lineChartTotalDeath.getData().add(series[i]);
                }


            }
        });

        //Butona tıklayınca listviewden ulke adını çekip linechart çizdirme
        //Total Death line chart verilerini çizdirme
        //Toplam Vaka
        showLineChartTotalCases.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lineChartTotalCases.getData().clear();
                for (int z = 0; z < series.length; z++) {
                    series[z] = new XYChart.Series();
                }
                for (Integer i : ulkeAdiListViewTotalCases.getSelectionModel().getSelectedIndices()) {
                    isNull[0] =0;
                    series[i].getData().clear();
                    String ulke = ulkeAdi[i];
                    series[i].setName(ulke);

                    for (int x = pandemiAllDate.size() - 1; x >= 0; x--) {
                        for (int k = allcountries.size() - 1; k >= 0; k--) {
                            if (ulke.equals(allcountries.get(k).getCountriesAndTerritories()) && pandemiAllDate.get(x).equals(allcountries.get(k).getDateRap())) {
                                series[i].getData().add(new XYChart.Data(pandemiAllDate.get(x), allcountries.get(k).getTotalCases()));
                                isNull[0] =1;
                            }
                        }
                        if(isNull[0]==0){
                            series[i].getData().add(new XYChart.Data(pandemiAllDate.get(x),0));
                        }

                    }
                    lineChartTotalCases.getData().add(series[i]);
                }
            }
        });

        HBox lineChartHboxTotalDeath = new HBox();
        HBox lineChartHboxTotalCases = new HBox();

        //linechart ve yanına listview,button ekleme
        lineChartHboxTotalDeath.getChildren().addAll(lineChartTotalDeath, vBoxTotalDeath);
        lineChartHboxTotalCases.getChildren().addAll(lineChartTotalCases,vBoxTotalCases);

        //tableview ile linechart totaldeath birleştirme
        BorderPane borderPaneCenter = new BorderPane();
        borderPaneCenter.setCenter(tableView);
        borderPaneCenter.setBottom(lineChartHboxTotalDeath);


        root.setTop(hBox);
        root.setCenter(borderPaneCenter);
        root.setBottom(lineChartHboxTotalCases);


        primaryStage.setTitle("Covid19");
        primaryStage.setScene(new Scene(root, 1500, 800));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

    //ulke adlarını çekiyor
    public String[] getUlkeAdi() {
        String[] ulkeAdlari = new String[208];
        List<Country> list = new ArrayList();
        list = getCountry();
        for (int i = 0; i < list.size(); i++) {
            ulkeAdlari[i] = list.get(i).getCountriesAndTerritories();
        }
        return ulkeAdlari;
    }

    //tableview için
    public ObservableList<Country> getCountry() {
        ObservableList<Country> country = FXCollections.observableArrayList();
        country.clear();
        boolean b = false;
        for (int j = 0; j < allcountries.size(); j++) {
            //gece 12 den sonra veriler hemen güncellenmiyor
            //güncellenene kadar dünün tarihini güncellediği an bugünün tarihine göre işlem yapıyor
            if (allcountries.get(j).getDateRap().equals(df.format(now))) {
                country.addAll(allcountries.get(j));
                b = true;
            } else if (allcountries.get(j).getDateRap().equals(getYesterdayDateString()) && b == false) {
                country.addAll(allcountries.get(j));
            }
        }


        return country;
    }


    //gece 12den sonra hemen güncelleme olmadığı için bugünün tarihini aldığımızda veriler gelmiyor
    //bu yüzden gece 12 den sonrasında veriler yenilenene kadar if/else kullandım
    //verileri güncellenene kadar düne göre çekiyor
    public Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    //dünün tarihini string hale getirme
    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(yesterday());
    }

    //gün gün toplam ölüm sayısını hesaplatma
    public int totalDeath(String a, String date) {
        String[] dateSplit = date.split("/");
        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);

        toplamOlum = 0;
        for (int k = 0; k < countries.size(); k++) {
            if (a.equals(countries.get(k).getCountriesAndTerritories())) {
                String[] countriesDateSplit = countries.get(k).getDateRap().split("/");
                int countriesDay = Integer.parseInt(countriesDateSplit[0]);
                int countriesMonth = Integer.parseInt(countriesDateSplit[1]);
                int countriesYear = Integer.parseInt(countriesDateSplit[2]);
                if ((countriesDay <= day && countriesMonth <= month && countriesYear <= year)
                        || (countriesDay >= day && countriesMonth < month && countriesYear == year)
                        || (countriesDay >= day && countriesMonth >= month && countriesYear < year))
                    toplamOlum += countries.get(k).getDeaths();
            }
        }
        return toplamOlum;
    }

    //gün gün toplam vaka sayısını hesaplatma
    public int totalCases(String a, String date) {
        String[] dateSplit = date.split("/");
        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);
        toplamVaka = 0;
        for (int k = 0; k < countries.size(); k++) {
            if (a.equals(countries.get(k).getCountriesAndTerritories())) {
                String[] countriesDateSplit = countries.get(k).getDateRap().split("/");
                int countriesDay = Integer.parseInt(countriesDateSplit[0]);
                int countriesMonth = Integer.parseInt(countriesDateSplit[1]);
                int countriesYear = Integer.parseInt(countriesDateSplit[2]);
                if ((countriesDay <= day && countriesMonth <= month && countriesYear <= year)
                        || (countriesDay >= day && countriesMonth < month && countriesYear == year)
                        || (countriesDay >= day && countriesMonth >= month && countriesYear < year))
                    toplamVaka += countries.get(k).getCases();
            }
        }
        return toplamVaka;
    }


}
