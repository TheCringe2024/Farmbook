package com.example.farmbook;

import com.example.farmbook.dao.LivestockDAO;
import com.example.farmbook.model.Livestock;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LivestockListController {

    @FXML
    private ListView<String> livestockListView;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    private final LivestockDAO livestockDAO = new LivestockDAO();

    @FXML
    public void initialize() {
        loadLivestock();
    }

    private void loadLivestock() {
        List<Livestock> animals = livestockDAO.findAll();
        livestockListView.setItems(FXCollections.observableArrayList());
        for (Livestock animal : animals) {
            livestockListView.getItems().add(animal.toString());
        }
        if (animals.isEmpty()) {
            livestockListView.getItems().add("No animals recorded yet.");
        }
    }

    @FXML
    private void handleAddNew() {
        try {
            Stage stage = (Stage) addButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("add-livestock-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            stage.setScene(scene);
            stage.setTitle("Farmbook - Add Livestock");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LivestockListController.class.getResource("homepage-view.fxml")
        );

        Stage stage = (Stage) livestockListView.getScene().getWindow();

        stage.setScene(new Scene(loader.load(), 1000, 800));
        stage.setTitle("FarmBook");
        stage.sizeToScene();
        stage.centerOnScreen();
    }
}
