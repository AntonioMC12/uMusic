package es.antoniomc.uMusic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class borrarModalController {


    @FXML
    private Button btnCancion;

    @FXML
    private Button btnPlaylist;
    
    @FXML
    public void borrarrCancion(ActionEvent event) {
    	App.GoTo(event, "borrarCancionModal");
    }
    
    @FXML
    public void borrarLista(ActionEvent event) {
    	App.GoTo(event, "borrarPlaylistModal");
    }

}
