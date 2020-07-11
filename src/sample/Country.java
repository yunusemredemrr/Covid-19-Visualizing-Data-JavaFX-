package sample;

public class Country implements Comparable<Country>{
    String dateRap;
    int cases;
    int deaths;
    String countriesAndTerritories;
    String geoId;
    String countryterritoryCode;
    int popData2018;
    String continentExp;
    int totalDeath;
    int totalCases;
    double mortality;
    double atackRate;

    public double getAtackRate() {
        return atackRate;
    }

    public void setAtackRate(double atackRate) {
        this.atackRate = atackRate;
    }

    public Country() {
    }

    public Country (String dateRap, int cases, int deaths, String countriesAndTerritories, String geoId
            , String countryterritoryCode, int popData2018, String continentExp) {
        this.dateRap = dateRap;
        this.cases = cases;
        this.deaths = deaths;
        this.countriesAndTerritories = countriesAndTerritories;
        this.geoId = geoId;
        this.countryterritoryCode = countryterritoryCode;
        this.popData2018 = popData2018;
        this.continentExp = continentExp;

    }

    public double getMortality() {
        return mortality;
    }

    public void setMortality(double mortality) {
        this.mortality = mortality;
    }

    public Country (String dateRap, int cases, int deaths, String countriesAndTerritories, String geoId
            , String countryterritoryCode, int popData2018, String continentExp
            , int totalDeath, int totalCases, double mortality,double atackRate) {
        this.dateRap = dateRap;
        this.cases = cases;
        this.deaths = deaths;
        this.countriesAndTerritories = countriesAndTerritories;
        this.geoId = geoId;
        this.countryterritoryCode = countryterritoryCode;
        this.popData2018 = popData2018;
        this.continentExp = continentExp;
        this.totalDeath = totalDeath;
        this.totalCases = totalCases;
        this.mortality = mortality;
        this.atackRate = atackRate;

    }

    public int getTotalDeath() {
        return totalDeath;
    }

    public void setTotalDeath(int totalDeath) {
        this.totalDeath = totalDeath;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public String getDateRap() {
        return dateRap;
    }

    public void setDateRap(String dateRap) {
        this.dateRap = dateRap;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public String getCountriesAndTerritories() {
        return countriesAndTerritories;
    }

    public void setCountriesAndTerritories(String countriesAndTerritories) {
        this.countriesAndTerritories = countriesAndTerritories;
    }

    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    public String getCountryterritoryCode() {
        return countryterritoryCode;
    }

    public void setCountryterritoryCode(String countryterritoryCode) {
        this.countryterritoryCode = countryterritoryCode;
    }



    public int getPopData2018() {
        return popData2018;
    }

    public void setPopData2018(int popData2018) {
        this.popData2018 = popData2018;
    }

    public String getContinentExp() {
        return continentExp;
    }

    public void setContinentExp(String continentExp) {
        this.continentExp = continentExp;
    }

    @Override
    public int compareTo(Country other) {
        return this.geoId.compareTo(other.geoId);
    }

    @Override
    public String toString(){
        return "Tarih-> "+dateRap +" |Vaka-> "+cases+" |Olum-> "+deaths+" |Ulke-> "+countriesAndTerritories
                +" Population-> "+popData2018+" |UlkeId-> "+geoId+" |UlkeKodu-> "+countryterritoryCode+" |Kıta-> "+continentExp
                +" toplam olen" + totalDeath +"\n";
    }

}
