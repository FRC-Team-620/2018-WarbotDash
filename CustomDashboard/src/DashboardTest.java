import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class DashboardTest extends Application
{
	private static NetworkTableInstance networkTables;
	
	public static void main(String[] args) throws InterruptedException
	{
		networkTables = NetworkTableInstance.getDefault();
		networkTables.startClientTeam(620);
		networkTables.startDSClient();

		launch();
	}

	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("Hello World!");

		BorderPane root = new BorderPane();
		
		Text text = new Text("");
		ObservableList<String> autoOptions = FXCollections.observableArrayList();
		ComboBox<String> dropDown = new ComboBox<>(autoOptions);
		Button upload = new Button("Upload");

		root.setTop(text);
		root.setCenter(dropDown);
		root.setBottom(upload);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		NetworkTableEntry startingPositionOptions = networkTables.getEntry("/SmartDashboard/Starting Position/options");
		NetworkTableEntry startingPositionSelected = networkTables.getEntry("/SmartDashboard/Starting Position/selected");
		
		upload.setOnMousePressed(e -> startingPositionSelected.setString(dropDown.getValue()));
		
		Timeline updater = new Timeline(new KeyFrame(Duration.millis(20), ae ->
		{
			if(networkTables.isConnected())
			{
				text.setText("Connected");
				autoOptions.setAll(startingPositionOptions.getStringArray(new String[] {"Unable to Load Options"}));
			}
			else
				text.setText("Not Connected");
		}));
		updater.setCycleCount(Timeline.INDEFINITE);
		updater.play();
		
		primaryStage.show();
	}
}
