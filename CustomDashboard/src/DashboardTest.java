import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
		primaryStage.setTitle("Warbots Dashboard");

		BorderPane root = new BorderPane();
		
		Text connectionStatus = new Text("");
		root.setTop(connectionStatus);
		
		Text dropDownLabel = new Text("Enter Starting Position:");
		
		ObservableList<String> autoOptions = FXCollections.observableArrayList();
		ComboBox<String> dropDown = new ComboBox<>(autoOptions);

		HBox selectionArea = new HBox(dropDownLabel, dropDown);
		
		Text currentSelected = new Text("Not Connected");
		
		VBox info = new VBox(selectionArea, currentSelected);
		
		root.setCenter(info);
		
		Button upload = new Button("Upload");
		
		root.setBottom(upload);
		
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		
		NetworkTableEntry startingPositionOptions = networkTables.getEntry("/SmartDashboard/Starting Position/options");
		NetworkTableEntry startingPositionSelected = networkTables.getEntry("/SmartDashboard/Starting Position/selected");
		
		upload.setOnMousePressed(e -> startingPositionSelected.setString(dropDown.getValue()));
		
		Timeline updater = new Timeline(new KeyFrame(Duration.millis(20), ae ->
		{
			if(networkTables.isConnected())
			{
				connectionStatus.setText("Connected");
				autoOptions.setAll(startingPositionOptions.getStringArray(new String[] {"Unable to Load Options"}));
				currentSelected.setText("Selected: " + startingPositionSelected.getString("Unable to Load"));
			}
			else
			{
				currentSelected.setText("Not Connected");
				connectionStatus.setText("Not Connected");
			}
		}));
		updater.setCycleCount(Timeline.INDEFINITE);
		updater.play();
		
		primaryStage.show();
	}
}
