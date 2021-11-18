package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBCancionDAO;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBDiscoDAO;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBGeneroDAO;
import es.antoniomc.uMusic.model.Cancion;
import es.antoniomc.uMusic.model.Disco;
import es.antoniomc.uMusic.model.Genero;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class editarCancionModalController {

	@FXML
	private Button btnGuardar;

	@FXML
	private ComboBox<Cancion> cbCancion;

	@FXML
	private ComboBox<Disco> cbDisco;

	@FXML
	private ComboBox<Genero> cbGenero;

	@FXML
	private Label label;

	@FXML
	private Slider slDuracion;

	@FXML
	private TextField txtNombre;

	protected static ObservableList<Disco> discos = FXCollections.observableArrayList();
	protected static ObservableList<Genero> generos = FXCollections.observableArrayList();
	protected static ObservableList<Cancion> canciones = FXCollections.observableArrayList();

	public void initialize() {
		MariaDBGeneroDAO generoDAO = new MariaDBGeneroDAO(MariaDBConexion.getConexion());
		MariaDBDiscoDAO discoDAO = new MariaDBDiscoDAO(MariaDBConexion.getConexion());
		MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());

		try {
			discos.setAll(discoDAO.mostrarTodos());
			generos.setAll(generoDAO.mostrarTodos());
			canciones.setAll(cancionDAO.mostrarTodos());
			this.cbCancion.setItems(canciones);
			this.cbDisco.setItems(discos);
			this.cbGenero.setItems(generos);
		} catch (DAOException e) {
			showError("Error al obtener los datos");
			e.printStackTrace();
		}

		slDuracion.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				label.setText(Integer.toString(newValue.intValue()));
			}
		});
	}

	@FXML
	void editarCancion(ActionEvent event) {
		if (checkFields()) {
			MariaDBCancionDAO cancionDAO = new MariaDBCancionDAO(MariaDBConexion.getConexion());
			try {
				cancionDAO.guardar(new Cancion(this.cbCancion.getValue().getId(), this.txtNombre.getText(), (float) this.slDuracion.getValue() - 1,
						this.cbDisco.getValue(), this.cbGenero.getValue(), 0));
				showValid("Creado con éxito");

			} catch (DAOException e) {
				showError("Error al crear");
				e.printStackTrace();
			}
		} else {
			showError("Rellene todos los campos");
		}
	}

	private boolean checkFields() {
		return (!this.txtNombre.getText().trim().isEmpty() && !this.cbDisco.getSelectionModel().isEmpty()
				&& !this.cbGenero.getSelectionModel().isEmpty());
	}

	@FXML
	void setCampos(ActionEvent event) {
		this.txtNombre.setText(this.cbCancion.getValue().getNombre());
		this.slDuracion.setValue(Double.valueOf(Float.toString(this.cbCancion.getValue().getDuracion())));
		this.cbDisco.setValue(this.cbCancion.getValue().getDisco());
		this.cbGenero.setValue(this.cbCancion.getValue().getGenero());
	}

	@FXML
	public void showValid(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setHeaderText(null);
		alert.setTitle("Éxito");
		alert.setContentText(text);
		alert.showAndWait();
	}

	@FXML
	public void showError(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setTitle("Error");
		alert.setContentText(text);
		alert.showAndWait();
	}

}
