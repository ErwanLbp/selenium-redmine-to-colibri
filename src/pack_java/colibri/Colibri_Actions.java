package pack_java.colibri;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import pack_java.Mails;
import pack_java.Main;
import pack_java.configuration.ConfigIni;

import java.util.ArrayList;


/**
 * Cette classe va creer une nouvelle tache dans Colibri par ligne du fichier CSV recupere
 *
 * @author Erwan Le Batard--Poles
 * @version 1.0
 */
public class Colibri_Actions {

    /**
     * Met en place les preference et le profil necessaire au lancement de la fenêtre
     * puis ouvre la fenêtre Firefox
     * et appelle les fonctions d'actions
     *
     * @param driver        La fenêtre firefox
     * @param mail          L'objet Mail pour permettre l'envoi du récapitulatif
     * @param listeDemandes Liste des demandes
     */
    public static void run(WebDriver driver, Mails mail, ArrayList<String[]> listeDemandes) {

        //Execution des actions pour acceder a la page des projets
        accesURLpageConnexion(driver);
        remplissageChampsConnexion(driver);

        //Creations de nouvelles taches sur colibri
        creerNouvellesTaches(driver, mail, listeDemandes);
    }


    /**
     * Acces a la page http://172.17.177.175/colibri/
     *
     * @param driver La fenêtre firefox
     */
    private static void accesURLpageConnexion(WebDriver driver) {

         Main.ecrire_log("[O]\tAcces au site " + Site_Colibri.adresse_site());
        driver.get(Site_Colibri.adresse_site());
    }


    /**
     * Remplissage des champs de connexion et clic sur le bouton login
     *
     * @param driver La fenêtre firefox
     */
    private static void remplissageChampsConnexion(WebDriver driver) {

         Main.ecrire_log("[O]\tRemplissage des champs de connexion");
        new Select(Site_Colibri.Page_Login.LOC_TXT_USERNAME(driver)).selectByVisibleText(ConfigIni.getInstance().getLoginColibri());
        Site_Colibri.Page_Login.LOC_TXT_PASSWORD(driver).clear();
        Site_Colibri.Page_Login.LOC_TXT_PASSWORD(driver).sendKeys(ConfigIni.getInstance().getPasswordColibri());
        Site_Colibri.Page_Login.LOC_BTN_CONNECT(driver).click();
    }


    /**
     * Boucle iterant sur la liste de demandes, lançant pour chaque demande 'ajout d'une tâche
     *
     * @param driver        La fenêtre Firefox
     * @param mail          L'objet Mail pour permettre l'envoi du récapitulatif
     * @param listeDemandes La liste des demandes IXXI, sous forme de tableau
     */
    private static void creerNouvellesTaches(WebDriver driver, Mails mail, ArrayList<String[]> listeDemandes) {

         Main.ecrire_log("[O]\tCreation des taches");

        final int size = listeDemandes.size();
        //Pour chaque demande
        for (int i = 1; i <= size; i++) {
            String[] demande = listeDemandes.get(size - i);

            if (Integer.parseInt(demande[0]) > ConfigIni.getInstance().getLastAddedTask()) {
                String projet = ConfigIni.getInstance().getProjetColibri(demande[2]);
                String application = ConfigIni.getInstance().getApplicationColibri(demande[1]);

                 Main.ecrire_log("\t[" + i + "/" + size + "] Demande " + demande[0] + " : Application " + application + " : " + projet);

                //Retourner a la page de projet
                driver.get(Site_Colibri.adresse_site() + Site_Colibri.adresse_page_projet());

                if (!demande[2].equals("Evolution")) {

                    //Trouver le projet associe s'il existe et cliquer dessus
                    WebElement we = Site_Colibri.Page_Projets.LOC_LNK_NOMPROJET(driver, application, projet);
                    if (we != null) {

                        we.sendKeys(Keys.RETURN);

                        //TODO Regarder si cette tache est pas deja ajoutee

                        //Arrive sur la page du projet,
                        //Cliquer sur le bouton <Ajouter une tâche>
                        Site_Colibri.Page_DetailProjet.LOC_BTN_AJOUTERTACHE(driver).sendKeys(Keys.RETURN);

                         Main.ecrire_log("\t\tLancement ajout tache"); //TODO ou dire "tache deja creee"

                        //Arrive sur la page d'ajout d'une tâche,
                        //Remplir les champs de la nouvelle tâche
                        page_AjoutTache(driver, demande);

                        //Ajout de la tâche au mail
                        mail.ajouter_tache(demande[0], demande[6], application, projet);

                    } else {
                         Main.ecrire_log("[X]\tProjet non trouvé sur : " + driver.getCurrentUrl());
                        //TODO Creer le projet s'il existe pas ?
                    }

                } else {

                    //TODO Regarder si ce projet est pas deja cree

                    Site_Colibri.Page_Projets.LOC_BTN_AJOUTPROJET(driver).click();

                     Main.ecrire_log("\t\tLancement creation projet"); //TODO ou dire "projet deja creee"

                    //Arrive sur la page de creation d'un projet
                    //Remplir les champs du nouveau projet
                    page_AjoutProjet(driver, demande, projet);

                    //Ajout du projet au mail
                    mail.ajouter_projet(demande[0], demande[6], application);
                }
                //On met à jour le fichier de configuration
                ConfigIni.getInstance().setLastAddedTask(demande[0]);
            }
        }
    }


    /**
     * Remplis les champs de la tâche a partir de la demande envoyee en argument
     * Puis valide l'ajout de la tâche
     *
     * @param driver  La fenêtre Firefox
     * @param demande Une demande IXXI, sous forme de tableau de String
     */
    private static void page_AjoutTache(WebDriver driver, String[] demande) {

        //Remplir le type de tâche a partir de demande[2] (tracker)
        new Select(Site_Colibri.Page_AjoutTache.LOC_SLCT_TYPETACHE(driver)).selectByVisibleText(ConfigIni.getInstance().getTypeTacheColibri(demande[2]));

        //Remplir le Numero de la tâche a partir de demande[0] (#)
        Site_Colibri.Page_AjoutTache.LOC_TXT_NUMTACHE(driver).sendKeys(demande[0]);

        //Remplir le Nom de la tâche a partir de demande[6) (Sujet)
        Site_Colibri.Page_AjoutTache.LOC_TXT_NOMTACHE(driver).sendKeys(demande[6]);

        //Remplir le budjet a 0
        Site_Colibri.Page_AjoutTache.LOC_TXT_BUDGETTACHE(driver).sendKeys("0");

        //Remplir le type de facturation de la tâche
        new Select(Site_Colibri.Page_AjoutTache.LOC_SLCT_FACTURATIONTACHE(driver)).selectByVisibleText(ConfigIni.getInstance().getTypeForfaitColibri(demande[2]));

        //Remplir le champ de commentaire avec le nom de la demande au cas ou il y ai eu troncature
        Site_Colibri.Page_AjoutTache.LOC_TXT_COMMENTAIRE(driver).sendKeys(demande[6]);

        //Cliquer sur le bouton valider
        Site_Colibri.Page_AjoutTache.LOC_BTN_VALIDER(driver).click();
    }


    /**
     * Remplis les champs du projet a partir de la demande envoyee en argument
     * Puis valide la creation du projet
     *
     * @param driver  La fenêtre Firefox
     * @param demande Une demande IXXI, sous forme de tableau de String
     * @param projet  Nom du template du projet a creer
     */
    private static void page_AjoutProjet(WebDriver driver, String[] demande, String projet) {

        //Remplir le nom de l'application a partir de demande[1] (Projet)
        new Select(Site_Colibri.Page_AjoutProjet.LOC_SLCT_NOMAPPLI(driver)).selectByVisibleText(ConfigIni.getInstance().getApplicationColibri(demande[1]));

        //Remplir le nom du projet à partir de demande[6] (Sujet)
        Site_Colibri.Page_AjoutProjet.LOC_TXT_NOMPROJET(driver).sendKeys(demande[6]);

        //Remplir le budjet du projet à 0
        Site_Colibri.Page_AjoutProjet.LOC_TXT_BUDGET(driver).sendKeys("0");

        new Select(Site_Colibri.Page_AjoutProjet.LOC_SLCT_TEMPLATE(driver)).selectByVisibleText(projet);

        //Cliquer sur le bouton valider
        Site_Colibri.Page_AjoutProjet.LOC_BTN_VALIDER(driver).click();
    }
}