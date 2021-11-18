package es.antoniomc.uMusic;

import es.antoniomc.uMusic.DAO.DAOException;
import es.antoniomc.uMusic.DAO.MariaDB.MariaDBDiscoDAO;
import es.antoniomc.uMusic.model.Disco;
import es.antoniomc.uMusic.utils.MariaDBConexion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class buscarDiscoModalController {

    @FXML
    private TableColumn<Disco, String> colArtista;

    @FXML
    private TableColumn<Disco, String> colFecha;

    @FXML
    private TableColumn<Disco, String> colFoto;

    @FXML
    private TableColumn<Disco, String> colNombre;

    @FXML
    private TableView<Disco> tabDiscos;
    
protected static ObservableList<Disco> discos = FXCollections.observableArrayList();
    
	@FXML
	public void initialize() {
		setInfo();
	}
	
	private void setInfo() {
		setdiscos();
	}

	private void setdiscos() {
		MariaDBDiscoDAO discoDAO = new MariaDBDiscoDAO(MariaDBConexion.getConexion());
		try {
			discos.setAll(discoDAO.mostrarTodos());

			this.colNombre.setCellValueFactory(discos -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(discos.getValue().getNombre());
				return v;
			});
			;
			this.colFecha.setCellValueFactory(discos -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(discos.getValue().getFecha().toString());
				return v;
			});
			;
			this.colFoto.setCellValueFactory(discos -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(discos.getValue().getFoto());
				return v;
			});
			;
			this.colArtista.setCellValueFactory(discos -> {
				SimpleStringProperty v = new SimpleStringProperty();
				v.setValue(discos.getValue().getArtista().getNombre());
				return v;
			});
			;
			this.tabDiscos.setItems(discos);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
