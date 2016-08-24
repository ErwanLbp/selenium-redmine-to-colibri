package pack_java.ticketsIXXI;

import org.openqa.selenium.WebDriver;
import pack_java.Main;
import pack_java.configuration.ConfigIni;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Cette classe va récupérer le fichier comprenant toutes les demandes redmine
 *
 * @author Erwan Le Batard--Polès
 * @version 1.0
 */
public class TicketIXXI_Actions {

    /**
     * Met en place les préférence et le profil nécessaire au lancement de la fenêtre
     * puis ouvre la fenêtre Firefox
     * et appelle les fonctions d'actions
     *
     * @param driver    Contient la fenêtre firefox
     * @param cheminCSV Chaine contenant le chemin vers fichier CSV
     */
    public static void run(WebDriver driver, String cheminCSV) {

        //Contient l'adresse du site auquel on veut accéder
        String baseUrl = Site_TicketIXXI.adresse();

        //Execution des actions pour télécharger le fichier
        accesURLpage(driver, baseUrl);
        remplissageChampsConnexion(driver);
        accesCSV(driver,cheminCSV);
    }


    /**
     * Accès à la page https://tickets.ixxi.net/projects
     *
     * @param driver  Contient la fenêtre firefox
     * @param baseUrl Contient l'adresse du site auquel on veut accéder
     */
    private static void accesURLpage(WebDriver driver, String baseUrl) {

         Main.ecrire_log("[O]\tAcces au site " + baseUrl);
        driver.get(baseUrl + Site_TicketIXXI.adresse_page_projet());
    }


    /**
     * Remplissage des champs de connexion et clic sur le bouton login
     *
     * @param driver Contient la fenêtre firefox
     */
    private static void remplissageChampsConnexion(WebDriver driver) {

         Main.ecrire_log("[O]\tRemplissage des champs de connexion");
        Site_TicketIXXI.Page_Login.LOC_TXT_USERNAME(driver).clear();
        Site_TicketIXXI.Page_Login.LOC_TXT_USERNAME(driver).sendKeys(ConfigIni.getInstance().getLoginTicketIXXI());
        Site_TicketIXXI.Page_Login.LOC_TXT_PASSWORD(driver).clear();
        Site_TicketIXXI.Page_Login.LOC_TXT_PASSWORD(driver).sendKeys(ConfigIni.getInstance().getPasswordTicketIXXI());
        Site_TicketIXXI.Page_Login.LOC_BTN_CONNECT(driver).click();
    }


    /**
     * Accès à la page "Voir toutes les demandes" puis clic sur le bouton CSV
     * puis sélectionne "toutes les colonnes" et telecharge le fichier
     *
     * @param driver La fenêtre firefox
     * @param cheminCSV Chemin vers le fichier CSV
     */
    private static void accesCSV(WebDriver driver, String cheminCSV) {

         Main.ecrire_log("[O]\tAcces à la page toutes les demandes");
        Site_TicketIXXI.Page_Projects.LOC_LNK_TTESDEMANDES(driver).click();
         Main.ecrire_log("[O]\tClick sur CSV");
        Site_TicketIXXI.Page_Issues.LOC_LNK_CSV(driver).click();

        try { //Si il y a deja un fichier CSV on le supprime
            Files.deleteIfExists(Paths.get(cheminCSV));
        } catch (IOException e) {
             Main.ecrire_log("[X]\tErreur lors de la suppression du precedent fichier CSV");
            System.exit(1); //FIXME peut etre pas la meilleur idée
        }

        //Telechargement du fichier
        Site_TicketIXXI.Page_Issues.LOC_RBTN_TTESCOLONNES(driver).click();
        Site_TicketIXXI.Page_Issues.LOC_BTN_SUBMIT(driver).click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Main.ecrire_log("[X]\tProbleme mise en pause du programme (ne devrait pas arriver)");
        }

        //Test si telechragement reussi
        File f = new File(cheminCSV);
        if (f.exists() && !f.isDirectory())
             Main.ecrire_log("[O]\tTelechargement reussi, chemin : " + cheminCSV);
        else
             Main.ecrire_log("[X]\tEchec du telechargement, chemin : " + cheminCSV); //FIXME Faire quelque chose en cas d'erreur
    }
}
