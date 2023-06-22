/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DAO.EntregaActividadDAO;
import POJO.Actividad;
import POJO.EntregaActividad;
import Utils.ShowMessage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class ActEstItemController implements Initializable {

    @FXML
    private Label lbNombreAct;
    @FXML
    private Label lbFechaInicio;
    @FXML
    private Label lbFechaCierre;
    @FXML
    private Button idBtnEntregarActividad;
    @FXML
    private Button idBtnModificarEntrega;
    @FXML
    private Button idBtnComprobar;
    
    private Actividad actividad;
    private EntregaActividad entregaActividad;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        idBtnComprobar.setVisible(false);
        idBtnModificarEntrega.setVisible(false);
        // Crear y configurar el servicio
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(10); // Esperar 0.5 segundos
                        return null;
                    }
                };
            }
        };

        // Configurar el evento "succeeded" del servicio
        service.setOnSucceeded(event -> {
            // Mostrar el botón "Consultar"
            //////idBtnConsultar.setVisible(true);
            // Simular el evento del botón "Consultar"
            idBtnComprobar.fire();
            //System.out.print(actividad.getIdActividad());
        });

        // Iniciar el servicio
        service.start();
        
    }    

    @FXML
    private void btnEntregarActividad(ActionEvent event) {
        irEntregarActividad();
    }
    
    public void ponerDatos(Actividad actividad){
        Date fechaInicio = actividad.getFechaInicio();
        String formato  = "dd/MM/yyyy";
        SimpleDateFormat dateFormato = new SimpleDateFormat(formato);
        String dateStringFechaInicio = dateFormato.format(fechaInicio);
        
        Date fechaCierre = actividad.getFechaCierre();
        String dateStringFechaCierre = dateFormato.format(fechaCierre);
        
        lbNombreAct.setText(actividad.getNombre());
        lbFechaInicio.setText("Fecha de inicio: " + dateStringFechaInicio);
        lbFechaCierre.setText("Fecha de cierre: " + dateStringFechaCierre);
        this.actividad = actividad;
    }
    
    private void irEntregarActividad(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/EntregarActividad.fxml"));
        Parent root = loader.load();
        EntregarActividadController controlador = loader.getController();
        controlador.ponerDatos(actividad);
        Scene escena = new Scene(root);
        Stage escenarioBase = (Stage) lbFechaCierre.getScene().getWindow();
        escenarioBase.setScene(escena);
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }
    
    public void comprobarEntrega(){
        try {
            EntregaActividad entrega = EntregaActividadDAO.obtenerEntrega(actividad);
            if (entrega != null) {
                System.out.println("Hay una entrega en el metodo comprobarEntrega");
                this.entregaActividad = entrega;
            } else {
                System.out.println("No hay una entrega en el metodo comprobarEntrega");
            }
            System.out.println(entrega.getIdEntregaActividad());
            System.out.println(entrega.getIdActividad());
            System.out.println(entrega.getDescripcion());
            if (entrega != null && entrega.getIdEntregaActividad() != 0) {
                idBtnEntregarActividad.setVisible(false);
                idBtnModificarEntrega.setVisible(true);
                System.out.println("La actividad tiene una entrega");
                ModificarEntregaController entregaControlador = new ModificarEntregaController();
                entregaControlador.prepaparEntrega(entrega);
            } else {
                idBtnEntregarActividad.setVisible(true);
                idBtnModificarEntrega.setVisible(false);
                System.out.println("La actividad no tiene una entrega");
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnComprobar(ActionEvent event) {
        comprobarEntrega();
    }

    private void ID(ActionEvent event) {
        System.out.println(actividad.getIdActividad());
    }

    @FXML
    private void btnModificarEntrega(ActionEvent event) {
        comprobarEntrega();
        irModificarEntrega();
    }
    
    private void irModificarEntrega(){
       try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/ModificarEntrega.fxml"));
        Parent root = loader.load();
        ModificarEntregaController controlador = loader.getController();
        controlador.prepaparEntrega(this.entregaActividad);
        controlador.ponerDescripcionAct(this.actividad.getDescripcion());
           if (this.entregaActividad != null) {
               System.out.println("Hay una entrega en el item");
           } else {
               System.out.println("No hay una entrega en el item");
           }
        Scene escena = new Scene(root);
        Stage escenarioBase = (Stage) lbFechaCierre.getScene().getWindow();
        escenarioBase.setScene(escena);
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        } 
    }
    
}
