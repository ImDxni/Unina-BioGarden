package com.unina.biogarden.controller.side;

import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;

public class CropsController {
    @FXML
    private JFXTreeTableView cropsTable;


    @FXML
    public void initialize() {
        cropsTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
    }


}
