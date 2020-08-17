package org.nal.fastabreaker.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.nal.fastabreaker.business.service.FastaStripper;
import org.nal.fastabreaker.exception.ProcessException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class BreakerController implements Initializable {

	@FXML
	private TextField selectedFilePath;

	@FXML
	private TextField selectedOutputPath;

	@FXML
	private Button btnSelectFile;

	@FXML
	private Button btnSelectOutputPath;

	@FXML
	private Button btnConvert;

	private File file;

	private File outputFolder;

	private FileChooser fileChooser;

	private DirectoryChooser directoryChooser;

	public void convertFile() {

		try {
			this.btnConvert.setDisable(true);
			this.btnConvert.setText("Converting ...");
			this.validadeBeforeConvert();
			FastaStripper.strip(this.file, this.outputFolder);
			this.showInfoMessage("Finished");
		} catch (final Exception e) {
			this.throwErroMessage(e);
		} finally {
			this.btnConvert.setText("Convert");
			this.btnConvert.setDisable(false);
		}
	}

	private void validadeBeforeConvert() {
		if (this.file == null) {
			throw new ProcessException("Fasta file is undefined");
		}
		if (this.outputFolder == null) {
			throw new ProcessException("output folder is undefined");
		}
		if (this.outputFolder.listFiles().length != 0) {
			throw new ProcessException("the output folder must be empty. Please create a new output folder");
		}

	}

	public void selectFile() {
		try {
			this.btnSelectFile.setDisable(true);
			this.file = this.getFile();
			if (this.file != null) {
				this.selectedFilePath.setText(this.file.getAbsolutePath());
				this.validateFile(this.file);
			}
		} catch (final Exception e) {
			this.throwErroMessage(e);
			this.file = null;
			this.selectedFilePath.setText("");
		} finally {
			this.btnSelectFile.setDisable(false);
		}
	}

	private void showInfoMessage(final String text) {
		final Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("INFO");
		alert.setTitle("Processing Information");
		alert.setContentText(text);
		alert.showAndWait();
	}

	private void throwErroMessage(final Exception e) {
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Invalid file");
		alert.setTitle("Processing Error");
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}

	private void validateFile(final File file) {
		if (!file.getAbsoluteFile().toString().contains(".fasta")) {
			throw new ProcessException("The file isn't a fasta file ");
		}

	}

	public void selectOutputFolder() {
		try {
			this.btnSelectOutputPath.setDisable(true);
			this.outputFolder = this.getFolder();
			if (this.outputFolder != null) {
				this.selectedOutputPath.setText(this.outputFolder.getAbsolutePath());
			}
		} catch (final Exception e) {
			this.throwErroMessage(e);
		} finally {
			this.btnSelectOutputPath.setDisable(false);
		}

	}

	private File getFolder() {
		if (this.directoryChooser == null) {
			this.directoryChooser = new DirectoryChooser();
		}
		return this.directoryChooser.showDialog(null);
	}

	private File getFile() {
		if (this.fileChooser == null) {
			this.fileChooser = new FileChooser();
		}
		return this.fileChooser.showOpenDialog(null);
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		this.selectedFilePath.setDisable(true);
		this.selectedOutputPath.setDisable(true);
	}
}
