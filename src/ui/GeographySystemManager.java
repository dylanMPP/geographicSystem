package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import exception.CountryNotFoundException;
import exception.WrittenFormatException;
import model.GeographySystemController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class GeographySystemManager {

    public static Scanner reader;
    public static GeographySystemController controller;

    public static void main(String[] args) throws IOException, WrittenFormatException, CountryNotFoundException {
        init();
        showMainMenu();
    }

    public static void init(){
        reader = new Scanner(System.in);
        controller = new GeographySystemController();
    } // init method

    public static void showMainMenu() throws IOException, WrittenFormatException, CountryNotFoundException {
        try {
            controller.load();
        } catch (FileNotFoundException fileNotFoundException){
            JOptionPane.showMessageDialog(null, fileNotFoundException.getMessage());
        } catch (IOException ioException){
            JOptionPane.showMessageDialog(null, ioException.getMessage());
        }

        JOptionPane.showMessageDialog(null, "- - - - - WELCOME TO THE GEOGRAPHY SOFTWARE - - - - -");

        boolean runFlag = true;

        while (runFlag) {
            String menu = "";

            menu+=("\n\n      WHAT DO YOU WANT TO DO?");
            menu+=("\n|----------------------------------------------------");
            menu+=("|\n|| 1. Generate UUID for 'INSERT INTO' command.\n|" +
                    "| 2. Insert command.\n|" +
                    "| 3. Import data from a file .SQL\n|" +
                    "| 0. Exit.\n|"+
                    "|--------------------------------------"
            );
            int decision = Integer.parseInt(JOptionPane.showInputDialog(menu));

            switch (decision) {
                case 1:
                    String uuid = controller.generateUUID();
                    System.out.println(uuid);
                    JOptionPane.showMessageDialog (null, "This is the ID for the new element: "+uuid+"\n\n" +
                            "COPY IT FROM THE CONSOLE AND PASTE IT IN ITS RESPECTIVE PLACE IN THE COMMAND. \n" +
                            "Anyway, you can choose and type the ID you want. PRESS OK TO CONTINUE");
                    break;

                case 2:
                    String command = JOptionPane.showInputDialog("Type the command below.");
                    try {
                        String msg = controller.callCommandMethod(command);

                        if(!msg.equalsIgnoreCase("")){
                            JOptionPane.showMessageDialog(null, msg);
                        } else {
                            JOptionPane.showMessageDialog(null, "The command couldn't be executed.");
                        }
                    } catch (WrittenFormatException writtenFormatException){
                        JOptionPane.showMessageDialog(null, writtenFormatException);
                    } catch (CountryNotFoundException countryNotFoundException){
                        JOptionPane.showMessageDialog(null, countryNotFoundException);
                    }
                    break;

                case 3:
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "SQL files", "sql", "SQL");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            if(!controller.importFromSQL(chooser.getSelectedFile().getName()).equalsIgnoreCase("")){
                                JOptionPane.showMessageDialog(null, "The file has been imported");
                            } else {
                                JOptionPane.showMessageDialog(null, "The file couldn't be imported.");
                            }
                        } catch (WrittenFormatException writtenFormatException) {
                            JOptionPane.showMessageDialog(null, writtenFormatException.getMessage());
                        } catch (CountryNotFoundException countryNotFoundException){
                            JOptionPane.showMessageDialog(null, countryNotFoundException.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a valid file");
                    }
                    break;

                case 0:
                    try{
                        controller.save();
                    } catch (FileNotFoundException fileNotFoundException){
                        JOptionPane.showMessageDialog(null, fileNotFoundException.getMessage());
                    } catch (IOException ioException){
                        JOptionPane.showMessageDialog(null, ioException.getMessage());
                    }

                    JOptionPane.showMessageDialog(null, "Thank you for using our software. Come back soon!");
                    runFlag = false;
                    break;
            } // switch
        } // while


    } // show main menu method
}
