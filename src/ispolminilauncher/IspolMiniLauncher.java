/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ispolminilauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Fran
 */
public class IspolMiniLauncher {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //files
        File parentFile = new File(".");
        File datesFile = new File(parentFile,"lang\\dlic.txt");
        
        //read the date
        Date dateLicense = readDate(datesFile);
        System.out.println("Dia con licencia activa:");
        System.out.println(dateLicense);
        
        //change the date
        Date today = new Date();
        exec(new String[]{"cmd","/c","date "+dateFormat.format(dateLicense)});
        
        //execute istram
        executeIstram(parentFile);
        
        //change the date
        exec(new String[]{"cmd","/c","date "+dateFormat.format(today)});
    }
    
    public static void executeIstram (File f) {
        try {
            //start process
            String command[] = new String[]{"istram.exe","-tope","-mdep"};
            ProcessBuilder probuilder = new ProcessBuilder( command );
            Process p = probuilder.start();
            
            //Read input stream
            BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = bf.readLine()) != null) {
            System.out.println(line);
            if(line.contains("Inicializando la Configuracion Local..."))
                break;
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Problemas al ejecutar istram.exe",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void exec (String cmd[]) {
        try {
            Runtime.getRuntime().exec(cmd);
        }catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println("Failed");
        }
    }
    
    public static Date readDate (File f) {
        Date date = new Date();
        //1. leemos el archivo util para obtener la fecha de licencia
        try {
            //days with license active
            int numbers;
            
            //read file
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line;
            
            while((line = bf.readLine()) != null) {
                String tokens[] = line.split(" ");
                if(tokens[1].contains("q")) {
                    numbers = Integer.parseInt(tokens[2]);
                    if(numbers > 10) {
                        date = dateFormat.parse(tokens[0]);
                        break;
                    }
                }
            }
            
            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                    "No se encontro la carpeta de instalación de Istram",
                    "Problemas con el directorio de Istram",
                    JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
           JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Problemas con lectura de archivos",
                    JOptionPane.WARNING_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Problemas fechas",
                    JOptionPane.WARNING_MESSAGE);
        }
        
        return date;
    }
    
    
    
}
