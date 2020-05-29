package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import model.*;

import model.database.*;
import model.kassabon.*;
import model.korting.KortingEnum;
import model.korting.KortingStrategy;
import model.log.Log;
import view.KassaView;
import view.panels.BetaalPane;
import view.panels.KassaPane;
import view.panels.LogPane;
import view.panels.ProductOverviewPane;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Vanhaeren Corentin, Sateur Maxime
 * cleanup necessary for unused classes
 */

public class KassaviewController implements Observer {

    private KassaView kassaView;
    private KassaVerkoop kassaVerkoop;
    private ArtikelWinkelmand artikelWinkelmand;
    private InstellingController instellingController;
    private LoadSaveContext loadSaveContext;
    private ProductController productController;
    private LogController logController;

    //private LoadsaveArtikeltekst loadsaveArtikeltekst;
    private KassaPane pane;
    private BetaalPane betaalPane;
    private boolean isHoldEmpty = true;

    private Properties properties = new Properties();
    private String path = "src" + File.separator + "bestanden" + File.separator + "KassaApp.properties";

    public void setloadsaveStrategy(LoadSaveEnum loadSaveEnum){instellingController.setLoadSaveStrategy(loadSaveEnum);}

    public KassaviewController(KassaVerkoop kassaVerkoop, InstellingController instellingController, ProductController productController, LogController logController ) throws DatabaseException, IOException, BiffException {
        this.kassaVerkoop = kassaVerkoop;
        kassaVerkoop.addObserver(this);
        this.productController = productController;
        this.instellingController = instellingController;
        this.logController = logController;
        kassaVerkoop.setKorting(instellingController.getKortingStrategy());//needs re-writing
    }

    public void setPane(KassaPane pane){this.pane = pane;}

    public ObservableList<Artikel> getArtikels(){
        return FXCollections.observableArrayList(kassaVerkoop.getWinkelmandje());
    }



    public void addProductKassaVerkoop(String code) {
        try{
            Artikel a = productController.getArtikel(code);
            int voorraad = a.getVoorraad();
            int aantalInMand = 0;
            for(Artikel artikel : kassaVerkoop.getWinkelmandje()){
                if(artikel.getCode().equals(code)){
                    aantalInMand++;
                }
            }
            if(voorraad > aantalInMand){
                kassaVerkoop.updateAddArtikel(a);

                System.out.println("Kassaverkoop: " + a.getOmschrijving());
            }else{
                throw new IllegalArgumentException("Niet in voorraad");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }



    public void removeProductKassaVerkoop(int index) {
        kassaVerkoop.updateRemoveArtikel(index);
        if(kassaVerkoop.getWinkelmandje().isEmpty()){
            pane.setOnholdDisabled(true);
            pane.setAFSLUITENDisabled(true);
        }
    }

    public ArrayList<Log> getLogs(){
        return logController.getLogs();
    }

    public void setOnHold() {
        kassaVerkoop.setOnHold();
        isHoldEmpty = false;
    }

    public void setOffHold() {
        kassaVerkoop.setOffHold();
        isHoldEmpty = true;
    }

    public void pasWinkelkarAan(){
        kassaVerkoop.pasWinkelkarAan();
    }

    public void setProperty(String key,String value){
        instellingController.setProperty(key,value);
    }

    public String getProperty(String key){
        return instellingController.getProperty(key);
    }

    public void setProductPane(ProductOverviewPane productOverviewPane){
        productController.setProductPane(productOverviewPane);
    }

    public void setLogPane(LogPane logPane){
        logController.setLogPane(logPane);
    }


    public double totaalPrijs() {
        return kassaVerkoop.getTotalPrijs();
    }

    public void setLoadStrategy(LoadSaveEnum loadSaveEnum){
        instellingController.setLoadSaveStrategy(loadSaveEnum);
    }

    //should not be used, use the one in productcontroller, this one will still works, probably...
    public ArrayList<Artikel> load() throws IOException, DatabaseException, BiffException {
        return productController.loadArtikels();
    }

    public void setKortingStrategy(){
        instellingController.setKortingStrategy();
    }

    public ArrayList<Artikel> getWinkelmandje() {
        return kassaVerkoop.getWinkelmandje();
    }

    public ArrayList<ArtikelWinkelmand> getWinkelmandMetAantal() {
        return kassaVerkoop.getWinkelmandMetAantal();
    }

    public KassaVerkoop getKassaVerkoop() {
        return kassaVerkoop;
    }

    public void setKassaVerkoop(KassaVerkoop kassaVerkoop) {
        this.kassaVerkoop = kassaVerkoop;
    }

    public double Kortingprijs(){
        return kassaVerkoop.berekenKorting();
    }

    public double totalePrijsMetKorting(){
        return kassaVerkoop.berekenPrijsMetKorting();
    }

    public void betaal() throws WriteException, BiffException, IOException, DomainException, DatabaseException {
        double korting = kassaVerkoop.berekenKorting();
        double totalebedragmetkorting = kassaVerkoop.berekenPrijsMetKorting();
        double totaleprijs = kassaVerkoop.getTotalPrijs();
        Log log = new Log(totaleprijs, korting, totalebedragmetkorting);
        logController.addLog(log);
        System.out.println(log.toString());
        pasVoorraadAan(kassaVerkoop.getWinkelmandje());
        productController.updateProducts();
        kassaVerkoop.betaal();
        if(!isHoldEmpty) pane.setRestoreonholdDisabled(false);
        //uitbereiding nodig in labo 10
    }

    public void setTypeKorting(KortingEnum kortingEnum){ instellingController.setTypeKorting(kortingEnum);}

    public void annuleer(){
        kassaVerkoop.annuleer();
    }

    public void cancel(){
        pane.setAFSLUITENDisabled(false);
        if(isHoldEmpty){
            pane.setRestoreonholdDisabled(true);
            pane.setOnholdDisabled(false);
        }else{
            pane.setRestoreonholdDisabled(false);
            pane.setOnholdDisabled(true);
        }
    }

    public ObservableList<Artikel> loadData() throws DatabaseException, IOException, BiffException {return productController.loadData();}

    public ArrayList<Artikel> loadinMemory() throws IOException, DatabaseException, BiffException {
        return productController.loadArtikels();
    }

    public KortingStrategy getKortingStrategy(){
        return kassaVerkoop.getKorting();
    }

    public String generateRekening(Component kassabon){
        if(Boolean.parseBoolean(getProperty("property.headerdatumtijd"))){
            kassabon = new HeaderDatumTijd(kassabon);//werkt
        }

        if(Boolean.parseBoolean(getProperty("property.headerboodschap"))){
            kassabon = new HeaderAlgemeneBoodschap(kassabon, getProperty("property.headerboodschaptext"));//werkt
        }

        if(Boolean.parseBoolean(getProperty("property.footerboodschap"))){
            kassabon = new FooterAlgemeneBoodschap(kassabon, getProperty("property.footerboodschaptext"));//werkt
        }

        if(Boolean.parseBoolean(getProperty("property.footerkorting"))){
            kassabon = new FooterKorting(kassabon);
        }

        if(Boolean.parseBoolean(getProperty("property.footerBTW"))){
            kassabon = new FooterBTW(kassabon);
        }
        return kassabon.genereerKassabon();
    }

    public void setBetaalPane(KassaviewController kassaviewController){
        this.betaalPane = new BetaalPane(kassaviewController);
    }

    @Override
    public void update(KassaVerkoop verkoop) {
        pane.updateTotaalPrijs(verkoop.getTotalPrijs());
        pane.updateTotaalPrijsKorting(verkoop.berekenPrijsMetKorting());
        pane.setWinkelmandje(verkoop.getWinkelmandje());
        pane.updateTotaalKorting(verkoop.berekenKorting());
        if(this.betaalPane != null){
            betaalPane.setEindPrijs(verkoop.berekenPrijsMetKorting());
        }
    }

    private void pasVoorraadAan(ArrayList<Artikel> artikels) throws WriteException, BiffException, DatabaseException, DomainException, IOException {
        for(Artikel artikel : artikels){
            productController.pasVoorraadAan(artikel);
        }
    }
}
