package ua.kpi.dzidzoiev.is.controller;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ua.kpi.dzidzoiev.is.service.FileEncryptService;
import ua.kpi.dzidzoiev.is.service.FileHelper;
import ua.kpi.dzidzoiev.is.service.SymmetricService;
import ua.kpi.dzidzoiev.is.service.symmetric.CipherDescription;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ua.kpi.dzidzoiev.is.service.Holder.Log;

/**
 * Created by dzidzoiev on 11/30/15.
 */
public class SymmetricTabController implements Initializable {

    public TextField sym_edit_key;
//    public ToggleButton sym_tog_file_auto;
    public TextField sym_edit_source_file;
    public TextField sym_edit_dest_file;
    //    public ChoiceBox<CipherDescription> sym_drop_enc_mode;
    public ChoiceBox<CipherDescription> sym_drop_alg;
    public Button sym_btn_encrypt;
    public Button sym_btn_select_source;
    public Button sym_btn_select_dest;
    public Button sym_btn_decrypt;
    public GridPane sym_root;
    public Button sym_btn_refresh_key;
    public Stage stage;

    private SymmetricService symmetricService;
    private FileEncryptService fileEncryptService;
    private List<CipherDescription> algs;

    public SymmetricTabController() {
        symmetricService = new SymmetricService();
        fileEncryptService = new FileEncryptService();
        algs = getAlgorithms();
    }

    public Stage getStage() {
        return stage;
    }

    public SymmetricTabController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public void initialize(URL location, ResourceBundle resources) {
//        sym_tog_key_auto.setSelected(true);
//
//        sym_tog_key_auto.setOnAction(e -> {
//            boolean selected = sym_tog_key_auto.isSelected();
//            if (selected) {
//                sym_edit_key.setEditable(false);
//                setKey(getNewRandomKey());
//            } else {
//                sym_edit_key.setEditable(true);
//                setKey("");
//            }
//            sym_edit_key.setText(getKey());
//        });

//        sym_edit_key.setEditable(false);
//        sym_edit_key.setOnKeyReleased(e -> {
//                    setKey(sym_edit_key.getText());
//                    System.out.println(getKey());
//                }
//        );

//        final Scene scene = sym_root.getScene();
//
//        scene.setOnDragOver(event -> {
//            Dragboard db = event.getDragboard();
//            if (db.hasFiles()) {
//                event.acceptTransferModes(TransferMode.COPY);
//            } else {
//                event.consume();
//            }
//        });
//
//        // Dropping over surface
//        scene.setOnDragDropped(event -> {
//            Dragboard db = event.getDragboard();
//            boolean success = false;
//            if (db.hasFiles()) {
//                success = true;
//                String filePath = null;
//                for (File file:db.getFiles()) {
//                    selectFile(file);
//                }
//            }
//            event.setDropCompleted(success);
//            event.consume();
//        });

//        sym_tog_file_auto.setSelected(true);
//        sym_tog_file_auto.setOnAction(e -> {
//            if (sym_tog_file_auto.isSelected()) {
//                sym_edit_source_file.setDisable(true);
//                sym_edit_source_file.setPromptText("Файл генерується автоматично");
//                sym_btn_select_source.setDisable(true);
//            } else {
//                sym_edit_source_file.setDisable(false);
//                sym_edit_source_file.setPromptText("Вкажіть вихідний файл");
//                sym_btn_select_source.setDisable(false);
//            }
//            e.consume();
//        });

//        sym_edit_source_file.setDisable(true);
//        sym_edit_source_file.setEditable(false);
        sym_edit_source_file.setText(FileHelper.getTempFile());
//        sym_edit_source_file.setPromptText("Файл генерується автоматично");

//        sym_btn_select_source.setDisable(true);
        sym_btn_select_source.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Вихідний файл");
            File chosenFile = fileChooser.showOpenDialog(sym_root.getScene().getWindow());
            if (chosenFile != null) {
                selectFile(chosenFile);
            }
            e.consume();
        });

//        sym_edit_dest_file.setEditable(false);
        sym_edit_dest_file.setText(FileHelper.getTempFolder());
        sym_btn_select_dest.setOnAction(e -> {
            DirectoryChooser fileChooser = new DirectoryChooser();
            fileChooser.setTitle("Папка для збереження");
            File chosenFile = fileChooser.showDialog(sym_root.getScene().getWindow());
            if (chosenFile != null) {
                String absolutePath = chosenFile.getAbsolutePath();
                Log.info("Dest file chosen " + absolutePath);
                sym_edit_dest_file.setText(absolutePath);
            }
            e.consume();
        });

        sym_drop_alg.setItems(FXCollections.observableArrayList(algs));
        sym_drop_alg.getSelectionModel().selectFirst();

        sym_btn_encrypt.setOnAction(e -> {
            try {
                String newFile = fileEncryptService.encrypt(
                        sym_edit_source_file.getText(),
                        sym_edit_dest_file.getText(),
                        sym_drop_alg.getValue(),
                        sym_edit_key.getText());
                sym_edit_source_file.setText(newFile);
                createDialog("Шифрування завершено.");
            } catch (Exception ex) {
                createExceptionDialog(ex);
            }
            e.consume();
        });

        sym_btn_decrypt.setOnAction(e -> {
            try {
                String newFile = fileEncryptService.decrypt(
                        sym_edit_source_file.getText(),
                        sym_edit_dest_file.getText(),
                        sym_drop_alg.getValue(),
                        sym_edit_key.getText());
                createDialog("Розшифрування завершено.");
            } catch (Exception ex) {
                createExceptionDialog(ex);
            }
            e.consume();
        });

        sym_btn_refresh_key.setOnAction(e -> {
            regenerateKey();
            e.consume();
        });
        regenerateKey();
    }

    private void selectFile(File chosenFile) {
        String absolutePath = chosenFile.getAbsolutePath();
        Log.info("Source file chosen " + absolutePath);
        sym_edit_source_file.setText(absolutePath);
        sym_edit_dest_file.setText(FileHelper.getDir(absolutePath));
    }

    private void createDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    private void createExceptionDialog(Throwable ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText("Could not find file blabla.txt!");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    private void regenerateKey() {
        sym_edit_key.setText(getNewRandomKey());
    }

    private String getNewRandomKey() {
        return symmetricService.generateKey(getSelectedCipherDescription());
    }

    private CipherDescription getSelectedCipherDescription() {
        return sym_drop_alg.getSelectionModel().selectedItemProperty().get();
    }

    public List<CipherDescription> getAlgorithms() {
        return symmetricService.getAvailableCiphers().stream()
                .sorted((c1, c2) -> c1.toString().compareTo(c2.toString()))
                .collect(Collectors.toList());
    }
}
