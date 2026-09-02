package com.example.farmbook;

import com.example.farmbook.dao.CropDAO;
import com.example.farmbook.dao.DatabaseConnection;
import com.example.farmbook.model.Crop;

import java.util.List;

public class TestCropSave {
    public static void main(String[] args) {
        DatabaseConnection.initialiseCropTable();

        CropDAO cropDAO = new CropDAO();
        Crop tomatoes = new Crop("Tomatoes", "Field A", 50, "2025-08-12");
        cropDAO.save(tomatoes);

        System.out.println("Saved crop. Here's everything in the database now:");
        List<Crop> allCrops = cropDAO.findAll();
        for (Crop c : allCrops) {
            System.out.println(c);
        }
    }
}
