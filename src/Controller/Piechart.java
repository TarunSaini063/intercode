/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

/**
 * FXML Controller class
 *
 * @author tarun
 */
public class Piechart implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private PieChart piechart;
    ObservableList<PieChart.Data> pieChartData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        piechart.setData(pieChartData);
    }

    void setIndex(ObservableList<PieChart.Data> ChartData) {
        this.pieChartData=ChartData;
        piechart.setData(pieChartData);
    }

}
