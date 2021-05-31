package Global;

import Structures.Sequence;
import Structures.SequenceListe;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {
    Properties prop;

    public Configuration(String Save) {
        prop = new Properties();
        try {
            InputStream propIn = new FileInputStream(Save);
            prop.load(propIn);
            System.out.println(prop);
            String home = System.getProperty("user.home");
            //FileInputStream f = new FileInputStream(home + File.separator + ".sokoban");
            prop = new Properties(prop);
            //prop.load(f);
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture de la configuration : " + e);
        }
    }

    public String lis(String cle) {
        String resultat = prop.getProperty(cle);
        if (resultat == null)
            throw new NoSuchElementException("Propriété " + cle + " non définie");
        return resultat;
    }

    public void delete(String save) {
        File myFile = new File(save);
        myFile.delete();
    }
}
