package view.panels;

import controller.KassaviewController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import model.log.Log;

import java.time.LocalDate;
import java.time.LocalTime;

public class LogPane extends GridPane {

    private KassaviewController kassaviewController;
    private TableView<Log> table;

    public LogPane(KassaviewController kassaviewController){
        this.kassaviewController = kassaviewController;
        kassaviewController.setLogPane(this);

        this.setPadding(new Insets(5, 5, 5, 5));
        this.setVgap(5);
        this.setHgap(5);

        Label label1 = new Label("Logs");
        label1.setFont(new Font("Arial", 20));

        this.setTable();
        table.setItems(FXCollections.observableArrayList(kassaviewController.getLogs()));

        this.getChildren().addAll(label1,table);
    }

    private void setTable(){
        table = new TableView<>();
        table.setRowFactory( t -> new TableRow<>());
        this.add(new Label("Logs:"), 0, 0, 1, 1);
        TableColumn<Log,LocalDate> columnDatum = new TableColumn<>("Datum");
        columnDatum.setMinWidth(100);
        columnDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
        TableColumn<Log,LocalTime> columnTijdstip = new TableColumn<>("Tijdtip");
        columnTijdstip.setMinWidth(100);
        columnTijdstip.setCellValueFactory(new PropertyValueFactory<>("tijdtip"));
        TableColumn<Log,Double> columnTotaalBedrag = new TableColumn<>("Totaal");
        columnTotaalBedrag.setMinWidth(50);
        columnTotaalBedrag.setCellValueFactory(new PropertyValueFactory<>("totaalbedrag"));
        TableColumn<Log,Double> columnKorting = new TableColumn<>("Korting");
        columnKorting.setMinWidth(50);
        columnKorting.setCellValueFactory(new PropertyValueFactory<>("korting"));
        TableColumn<Log,Double> columnTeBetalenMetKorting = new TableColumn<>("Betaald");
        columnTeBetalenMetKorting.setMinWidth(50);
        columnTeBetalenMetKorting.setCellValueFactory(new PropertyValueFactory<>("tebetalenbedrag"));

        table.getColumns().addAll(columnDatum,columnTijdstip,columnTotaalBedrag,columnKorting,columnTeBetalenMetKorting);
    }

    public void updateLogTable(){
        table.setItems(FXCollections.observableArrayList(kassaviewController.getLogs()));
        table.refresh();
    }

}
