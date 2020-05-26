package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jxl.read.biff.BiffException;
import model.Artikel;
import model.database.DatabaseException;
import model.database.LoadSaveContext;
import model.database.LoadSaveEnum;
import model.database.StrategyLoadSave;
import view.panels.ProductOverviewPane;

import java.io.IOException;
import java.util.ArrayList;

public class ProductController {

    private LoadSaveContext loadSaveContext;
    private ProductOverviewPane pane;

    public ProductController(InstellingController instellingController) throws DatabaseException, IOException, BiffException {
        StrategyLoadSave loadSaveStrategy = instellingController.getLoadSaveStrategy();
        loadSaveContext = new LoadSaveContext(loadSaveStrategy);
    }

    public ObservableList<Artikel> loadData() throws DatabaseException, IOException, BiffException {
        return FXCollections.observableArrayList(this.loadArtikels());
    }

    public ArrayList<Artikel> loadArtikels() throws DatabaseException, IOException, BiffException {
    return loadSaveContext.load();
    }

    public Artikel getArtikel(String code){
        return loadSaveContext.getArtikel(code);
    }

    public void setProductPane(ProductOverviewPane pane){
        this.pane = pane;
    }

    //unnecesary
    /*
    public void updateProductsInTabel(){
        this.pane.updateProducts();
    }*/
}
