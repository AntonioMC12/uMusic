package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBArtistaDAO;
import es.antoniomc.uMusic.model.Artista;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class buscarArtistaModalController {

    @FXML
    private TableColumn<Artista, String> colFoto;

    @FXML
    private TableColumn<Artista, String> colNacionalidad;

    @FXML
    private TableColumn<Artista, String> colNombre;

    @FXML
    private TableView<Artista> tabartistas;
    
    protected static ObservableList<Artista> artistas = FXCollections.observableArrayList();
    
	@FXML
	public void initialize() {
		setInfo();
	}
	
	private void setInfo() {
		setArtistas();
	}

	private void setArtistas() {
		MariaDBArtistaDAO artistaDAO = new MariaDBArtistaDAO(MariaDBConexion.getConexion());
		try {
			artistas.setAll(artistaDAO.mostrarTodos());

			this.colNombre.setCellValueFactory(artistas -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(artistas.getValue().getNombre());
				return v;
			});
			;
			this.colNombre.setCellValueFactory(artistas -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(artistas.getValue().getNombre());
				return v;
			});
			;
			this.colFoto.setCellValueFactory(artistas -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(artistas.getValue().getFoto());
				return v;
			});
			;
			this.colNacionalidad.setCellValueFactory(artistas -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(artistas.getValue().getNacionalidad());
				return v;
			});
			;
			this.tabartistas.setItems(artistas);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
