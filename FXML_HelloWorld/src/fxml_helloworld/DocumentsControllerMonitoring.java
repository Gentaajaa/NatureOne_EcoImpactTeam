package fxml_helloworld;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class DocumentsControllerMonitoring implements Initializable {

    @FXML
    private VBox monitoringProject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateProjectList();

        // Menambahkan stylesheet
        String css = this.getClass().getResource("Resource/Monitoring.css").toExternalForm();
        monitoringProject.getStylesheets().add(css);
    }

    private void updateProjectList() {
        monitoringProject.getChildren().clear();
        for (Project project : ProjectDataManager.getInstance().getSupportedProjects()) {
            monitoringProject.getChildren().add(createProjectBox(project));
        }
    }

    public void addSupportedProject(Project project) {
        monitoringProject.getChildren().add(createProjectBox(project));
    }

    private VBox createProjectBox(Project project) {
        VBox box = new VBox(10);
        box.getStyleClass().add("project-box");

        Label nameLabel = new Label("Project: " + project.getNameProject());
        nameLabel.getStyleClass().add("project-name");

        Label descriptionLabel = new Label("Description: " + project.getDescriptionProject());
        descriptionLabel.getStyleClass().add("project-description");

        Label targetLabel = new Label("Target: " + formatToRupiah(project.getDonationTarget()));
        targetLabel.getStyleClass().add("project-target");

        Label completionLabel = new Label("Completion: " + project.getCompletionPercentage() + "%");
        completionLabel.getStyleClass().add("project-completion");

        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> deleteProject(project));

        VBox labelsBox = new VBox(10, nameLabel, descriptionLabel, targetLabel, completionLabel);
        labelsBox.getChildren().add(deleteButton); // Add delete button at the end of the VBox

        // Create and configure PieChart
        PieChart pieChart = createPieChart(project);

        HBox hBox = new HBox(20, labelsBox, pieChart);
        hBox.setStyle("-fx-alignment: center-left;");
        HBox.setHgrow(pieChart, Priority.ALWAYS); // Ensure PieChart is pushed to the right

        box.getChildren().add(hBox);

        return box;
    }

    private PieChart createPieChart(Project project) {
        PieChart pieChart = new PieChart();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Completion", project.getCompletionPercentage()),
                new PieChart.Data("Remaining", 100 - project.getCompletionPercentage()));
        pieChart.setData(pieChartData);
        pieChart.setPrefSize(200, 200); // Sesuaikan ukuran sesuai kebutuhan
        return pieChart;
    }

    private void deleteProject(Project project) {
        ProjectDataManager.getInstance().removeSupportedProject(project);
        updateProjectList();
    }

    private String formatToRupiah(double amount) {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return rupiahFormat.format(amount);
    }
}