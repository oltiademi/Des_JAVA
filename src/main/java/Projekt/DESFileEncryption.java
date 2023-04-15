package Projekt;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class DESFileEncryption {
    private static Cipher encrypt;
    private static Cipher decrypt;
    @FXML
    private Button selectFileButton;
    @FXML
    private TextField saveLocationTextField;
    @FXML
    private TextField saveLocationTextField1;
    @FXML
    private Label labelTxt;
    //generate a key
    public SecretKey secretKey;
    {
        try {
            secretKey = KeyGenerator.getInstance("DES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    //method for selecting the input file
    @FXML
    private void selectFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        Stage stage = (Stage) selectFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            saveLocationTextField.setText(selectedFile.getAbsolutePath());
        }
    }
    //method for selecting the output file
    @FXML
    private void selectFile1(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        Stage stage = (Stage) selectFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            saveLocationTextField1.setText(selectedFile.getAbsolutePath());
        }
    }
    //method for encrypting
    public void encryptFile() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InterruptedException {
        //paths specified
        try {
            //get input file location
            String textFile = saveLocationTextField.getText();
            //get output file location
            String encryptedData = saveLocationTextField1.getText();
            //set the algorithm
            encrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
            encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
            // calling encrypt() method to encrypt the file
            encryption(new FileInputStream(textFile), new FileOutputStream(encryptedData));
            labelTxt.setText("File encrypted successfully...");
        }
        //paths not specified
        catch(FileNotFoundException e){
            labelTxt.setText("There was an ERROR encrypting the file... Please, provide a path file!");
        }
    }
    private static void encryption(InputStream input, OutputStream output) throws IOException {
        output = new CipherOutputStream(output, encrypt);
        // calling the writeBytes() method to write the encrypted bytes to the file
        writeBytes(input, output);
    }
    //method for decrypting
    public void decryptFile() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        //paths specified
        try {
            //get file location for input file
            String textFile = saveLocationTextField.getText();
            //get file location for output file
            String decryptedData = saveLocationTextField1.getText();
            //define the algorithm
            decrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
            decrypt.init(Cipher.DECRYPT_MODE, secretKey);
            // calling decrypt() method to decrypt the file
            decryption(new FileInputStream(textFile), new FileOutputStream(decryptedData));

            labelTxt.setText("File decrypted successfully...");
        }
        //paths not specified
        catch(FileNotFoundException e) {
            labelTxt.setText("There was an ERROR decrypting the file... Please, provide a path file!");
        }
    }
    private static void decryption(InputStream input, OutputStream output) throws IOException {
        input = new CipherInputStream(input, decrypt);
        // calling the writeBytes() method to write the decrypted bytes to the file
        writeBytes(input, output);
    }
    // method for writing input bytes to the files
    private static void writeBytes(InputStream input, OutputStream output) throws IOException {
        byte[] writeBuffer = new byte[512];
        int readBytes;
        while ((readBytes = input.read(writeBuffer)) >= 0) {
            output.write(writeBuffer, 0, readBytes);
        }
        // closing the output stream
        output.close();
        // closing the input stream
        input.close();
    }
}

