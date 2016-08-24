package pack_java.parseFichierCSV;

import pack_java.Main;
import pack_java.configuration.ConfigIni;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Cette classe va gérer le parsing du fichier téléchargé et supprimer le fichier
 *
 * @author Erwan Le Batard--Polès
 * @version 1.0
 */
public class ParseCSV {

    /**
     * Lis le fichier et stocke chaque ligne dans la mémoire d'exécution
     *
     * @param cheminCSV Chaine contenant le chemin vers fichier CSV
     * @return La liste des demandes, stockée de manière ordonnée
     */
    public static ArrayList<String[]> parseCsvToList(String cheminCSV) {

        ArrayList<String[]> listDemandes = new ArrayList<>();
        int lastAddedTask = ConfigIni.getInstance().getLastAddedTask();
        Main.ecrire_log("\tDerniere tache ajoutee : " + lastAddedTask);
        boolean continuer = true;
        String line = "";
        String cvsSplitBy = ";";
        int cpt = 0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(cheminCSV), Charset.forName("ISO-8859-1"))) {
            br.readLine();
            while (continuer && (line = br.readLine()) != null) {
                String[] demande = line.split(cvsSplitBy);
                if (Integer.parseInt(demande[0]) > lastAddedTask) {
                    listDemandes.add(demande);
                    cpt++;
                } else
                    continuer = false;

            }
            if (!continuer)
                Main.ecrire_log("[O]\tLecture du fichier CSV reussie, " + cpt + " lignes lues (numero de derniere tache atteint)");
            else
                Main.ecrire_log("[O]\tLecture du fichier CSV reussie, " + cpt + " lignes lues (fin de fichier atteint)");

        } catch (FileNotFoundException e) {
            Main.ecrire_log("[X]\tLe fichier CSV a lire est introuvable");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            Main.ecrire_log("[X]\tErreur lors de la lecture du fichier CSV");
            System.exit(1);
        }

        return listDemandes;
    }
}