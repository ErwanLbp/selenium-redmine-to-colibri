package pack_java;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import pack_java.colibri.Colibri_Actions;
import pack_java.configuration.ConfigIni;
import pack_java.parseFichierCSV.ParseCSV;
import pack_java.ticketsIXXI.TicketIXXI_Actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Fonction principale du programme, pour appeler toutes les autres
 *
 * @author Erwan Le Batard--Polès
 * @version 1.0
 */
public class Main {

    /**
     * Appelle les fonctions nécessaires à l'exécution du programme
     * - Télécharge le fichier
     * - Parse le fichier dans la mémoire d'exécution
     * - Crée une tache pour chaque ligne de demande
     *
     * @param args Arguments d'entrée du programme
     */
    public static void main(String[] args) {

        //Récupération du temps au début du programme pour calculer le temps d'exécution
        long timeDeb = System.currentTimeMillis();

        //Formation d'une chaine contenant la date d'aujourd'hui pour le log
        String today_date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

        ecrire_log("******************************* DEBUT " + today_date + " *******************************");

        System.setProperty("file.encoding", "UTF-8");

            /*----------------*
             * Initialisation *
             *----------------*/

        //Formation de la chaine contenant le chemin et le nom du fichier CSV
        String cheminFichierCSV = ConfigIni.getInstance().getPathDossierCSV() + "\\" + ConfigIni.getInstance().getNomFichierCSV();

        //Initialisation du driver|
        WebDriver driver = configurationDriver(ConfigIni.getInstance().getPathDossierCSV());


            /*-------------------------------*
             * Téléchargement du fichier CSV *
             *-------------------------------*/

        //Va récupérer le fichier CSV sur https://tickets.ixxi.net/projects
        TicketIXXI_Actions.run(driver, cheminFichierCSV);


            /*-------------------------------------*
             * Parse du fichier CSV dans une liste *
             *-------------------------------------*/

        //Lis le fichier et rempli une collection d'objet <ArrayList> à partir des lignes
        ArrayList<String[]> listDemandes = ParseCSV.parseCsvToList(cheminFichierCSV);


            /*-------------------------------------------*
             * Ajout des lignes de la liste dans Colibri *
             *-------------------------------------------*/

        //Création la classe Mails pour envoyer le compte rendu de la création des tâches
        Mails mail = new Mails(listDemandes.size());

        //Rempli le site Colibri des demandes du fichier CSV si il y a des nouvelles demandes
        if (listDemandes.size() > 0)
            Colibri_Actions.run(driver, mail, listDemandes);
        else
            ecrire_log("[X]\tAucune tache a ajouter");

        //Envoi du mail récapitulatif si il y a des demandes
        if(listDemandes.size() > 0 || Files.exists(Paths.get("data\\mail.obj")))
            mail.envoyer_mail();


             /*----------*
             * Fermeture *
             *-----------*/

        quitter(driver, cheminFichierCSV);


        //Affichage du temps d'exécution
        ecrire_log("Temps d'execution total : " + ((System.currentTimeMillis() - timeDeb) / 1000) + "s");

        ecrire_log("******************************** FIN " + today_date + " ********************************\n\n");
    }


    /**
     * @param path Le chemin où enregistrer le fichier CSV
     * @return Le driver contenant la fenêtre Firefox
     */
    private static WebDriver configurationDriver(String path) {

        WebDriver driver;

        //Lancement d'un firefox portable avec version downgrade pour eviter les problèmes de compatibilife entre selenium et firefox
        System.setProperty("webdriver.firefox.bin", ".\\Firefox\\firefox.exe");

        //Creation d'un profil pour accepter le telechargement CSV sans confirmation
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
        path = new File("").getAbsolutePath() + "\\" + path;
        profile.setPreference("browser.download.dir", path);
        profile.setPreference("browser.download.folderList", 2);

        //Mise en place du proxy
        profile.setPreference("network.proxy.type", 2);
        profile.setPreference("network.proxy.autoconfig_url", "http://wpad.manh.fr.sopra/wpad.dat");

        //Creation de la fenetre Firefox
        driver = new FirefoxDriver(profile);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        return driver;
    }


    /**
     * Ferme tous les fluxs ouvert pour quitter proprement et supprime le fichier CSV temporaire
     *
     * @param driver    Contient la fenêtre firefox à fermer
     * @param cheminCSV Chaine contenant le chemin vers fichier CSV
     */
    private static void quitter(WebDriver driver, String cheminCSV) {

        //Sauvegarde du fichier de configuration
        ConfigIni.getInstance().sauvegarde();

        //Fermeture de la fenetre Firefox
        driver.quit();

        //Supprime le fichier CSV si il existe
        suppressionFichierCSV(cheminCSV);
    }


    /**
     * Supprime le fichier CSV telecharge si il existe
     *
     * @param cheminCSV Chaine contenant le chemin vers fichier CSV
     */
    private static void suppressionFichierCSV(String cheminCSV) {

        try {
            if (Files.deleteIfExists(Paths.get(cheminCSV)))
                ecrire_log("[O]\tSuppression du fichier CSV reussie");
        } catch (IOException e) {
            ecrire_log("[X]\tErreur lors de la suppression du fichier CSV");
        }
    }


    /**
     * Ecris le message en argument dans la sortie de log
     *
     * @param log Log a ecrire dans le fichier de log
     */
    public static void ecrire_log(String log) {
        System.out.println(log);
        try (BufferedWriter bos = Files.newBufferedWriter(Paths.get("data\\log.txt"), Charset.forName("ISO-8859-1"), StandardOpenOption.APPEND)) {
            bos.write(log + "\n");
        } catch (IOException e) {
            System.out.println("[X]\tEchec de l'ecriture du log");
            e.printStackTrace();
        }
    }
}
