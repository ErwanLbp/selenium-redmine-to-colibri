package pack_java.configuration;

import org.ini4j.Ini;
import pack_java.Main;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Cette classe permet le chargement ou la creation d'un fichier de configuration data/config.ini
 * Puis elle contient tous les accesseurs pour recuperer les donnees contenues dans le fichier
 *
 * @author Erwan Le Batard--Poles
 * @version 1.0
 */
public class ConfigIni {

    /**
     * L'objet permettant de traiter le fichier .ini
     */
    private final Ini ini;


    /**
     * Instance du singleton
     */
    private static ConfigIni INSTANCE = new ConfigIni();


    /**
     * Booleen permettant de savoir s'il le fichier de configuration a ete modifie
     */
    private boolean modified = false;


    /**
     * Renvoi l'instance du singleton, la cree si elle n'a jamais ete appele
     *
     * @return L'instance du singleton
     */
    public static ConfigIni getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigIni();
        }
        return INSTANCE;
    }


    /**
     * Charge le fichier .ini et le cree si il n'existe pas
     */
    private ConfigIni() {

        ini = new Ini();

        try {
            ini.load(new FileReader("data\\configRedmineColibri.ini"));
        } catch (IOException e) {
            Main.ecrire_log("[X]\tLecture fichier .ini impossible");
            e.printStackTrace();
            System.exit(1); //FIXME Peut etre pas la meilleur idée
        }
        Main.ecrire_log("[O]\tLecture du fichier .ini reussie");
    }


    /**
     * Sauvegarde le fichier ini si ce dernier a ete modifie lors de l'execution
     */
    public void sauvegarde() {
        if (modified) {
            try (FileWriter fw_config = new FileWriter("data\\configRedmineColibri.ini", false)) {
                ini.store(fw_config);
                Main.ecrire_log("\t\tSauvegarde des nouveaux parametres de configuration");
                modified = false;
            } catch (IOException e) {
                Main.ecrire_log("[X]\tEchec de la sauvegarde des nouveaux parametres de configuration, probleme avec le fichier de config");
            }
        }
    }


    /**
     * Cree un fichier data/config.ini et le remplis des donnees necessaire au fonctionnement de l'application
     */
    private void creation_fichier_config() { //a completer si ajouts de donnees

        Main.ecrire_log("\tCreation du fichier : data/configRedmineColibri.ini");

        /*
         * Ajouts du chemin et nom de fichier pour le CSV
         */
        ini.add("CSV", "pathDossier", "data");
        ini.add("CSV", "nomFichier", "issues.csv");

        /*
         * Ajouts des donnees pour Colibri
         */
        ini.add("Colibri", "adresseSite", "http://172.17.54.110/colibri/");
        ini.add("Colibri", "adressePageProjets", "?page=projects");
        ini.add("Colibri", "login", "admin");
        ini.add("Colibri", "password", "admin");
        ini.add("Colibri", "dernierNumTache", "2365");

        /*
         * Ajouts des donnees ticketIXXI
         */
        ini.add("TicketIXXI", "adresseSite", "https://tickets.ixxi.net/");
        ini.add("TicketIXXI", "adressePageProjets", "login?back_url=https%3A%2F%2Ftickets.ixxi.net%2Fprojects");
        ini.add("TicketIXXI", "login", "cyrille.lahaille@soprasteria.com");
        ini.add("TicketIXXI", "password", "Sopra59!1");

        /*
         * Ajouts donnees pour trouver l'application dans Colibri
         */
        ini.add("ColibriApplication", "SEVo", "SEVo");
        ini.add("ColibriApplication", "SEVt", "SEVt");
        ini.add("ColibriApplication", "OGPC", "OGPC");
        ini.add("ColibriApplication", "default", "SAV");

        /*
         * Ajouts donnees pour trouver le projet dans Colibri
         */
        ini.add("ColibriProjet", "Incident de production", "MCO");
        ini.add("ColibriProjet", "Assistance", "MCO");
        ini.add("ColibriProjet", "Evolution", "EVOL");
        ini.add("ColibriProjet", "default", "MCO");

        /*
         * Ajouts donnees pour trouver le type de forfait dans Colibri
         */
        ini.add("ColibriTypeForfait", "Anomalie", "MCO");
        ini.add("ColibriTypeForfait", "Evolution", "EVO-FORFAI");
        ini.add("ColibriTypeForfait", "default", "MCO");

        /*
         * Ajouts donnees pour trouver le typde demande dans Colibri
         */
        ini.add("ColibriTypeTache", "Anomalie", "Anomalie");
        ini.add("ColibriTypeTache", "Incident de production", "Anomalie");
        ini.add("ColibriTypeTache", "Assistance", "Support");
        ini.add("ColibriTypeTache", "Evolution", "Evolution");
        ini.add("ColibriTypeTache", "default", "Divers");

        /*
         * Ajouts des donnees pour le mail
         */
        ini.add("Mail", "login", "tma.CDS_IXXI");
        ini.add("Mail", "password", "/fOnM+UUMS");
        ini.add("Mail", "envoyeur", "tma.CDS_IXXI@soprasteria.com");
        ini.add("Mail", "destinataire", "erwan.lebatard@soprasteria.com");


        try {
            Files.createFile(Paths.get("data\\config.ini"));
        }catch (FileAlreadyExistsException faee){
            Main.ecrire_log("[X]\tEchec creation du fichier data/config.ini : un fichier de ce nom existe deja");
        } catch (IOException e) {
            Main.ecrire_log("[X]\tEchec creation du fichier data/config.ini");
        }

        try {
            ini.store(new File("data\\config.ini"));
        } catch (IOException e) {
            Main.ecrire_log("[X]\tEchec ecriture du fichier data/config.ini");
        }
    }


    /**
     * Renvoi la valeur configuree pour le chemin du dossier CSV
     *
     * @return Le chemin du dossier CSV
     */
    public String getPathDossierCSV() {

        Ini.Section sct = (Ini.Section) ini.get("CSV");
        return (String) sct.get("pathDossier");
    }


    /**
     * Renvoi la valeur configuree pour le nom du fichier CSV
     *
     * @return Le nom du fichier CSV
     */
    public String getNomFichierCSV() {

        Ini.Section sct = (Ini.Section) ini.get("CSV");
        return (String) sct.get("nomFichier");
    }


    /**
     * Renvoi la valeur configuree pour l'adresse du site TicketIXXI
     *
     * @return L'adresse du site
     */
    public String getAdresseSiteTicketIXXI() {

        Ini.Section sct = (Ini.Section) ini.get("TicketIXXI");
        return (String) sct.get("adresseSite");
    }


    /**
     * Renvoi la valeur configuree pour l'adresse de la page projets du site TicketIXXI
     *
     * @return L'adresse de la page projets
     */
    public String getAdresseProjetsTicketIXXI() {

        Ini.Section sct = (Ini.Section) ini.get("TicketIXXI");
        return (String) sct.get("adressePageProjets");
    }


    /**
     * Renvoi la valeur configuree pour le login du site TicketIXXI
     *
     * @return Le login du site
     */
    public String getLoginTicketIXXI() {

        Ini.Section sct = (Ini.Section) ini.get("TicketIXXI");
        return (String) sct.get("login");
    }


    /**
     * Renvoi la valeur configuree pour le password du site TicketIXXI
     *
     * @return Le password du site
     */
    public String getPasswordTicketIXXI() {

        Ini.Section sct = (Ini.Section) ini.get("TicketIXXI");
        return (String) sct.get("password");
    }


    /**
     * Renvoi la valeur configuree pour l'adresse du site Colibri
     *
     * @return L'adresse du site
     */
    public String getAdresseSiteColibri() {

        Ini.Section sct = (Ini.Section) ini.get("Colibri");
        return (String) sct.get("adresseSite");
    }


    /**
     * Renvoi la valeur configuree pour l'adresse de la page projets du site Colibri
     *
     * @return L'adresse de la page projets
     */
    public String getAdresseProjetsColibri() {

        Ini.Section sct = (Ini.Section) ini.get("Colibri");
        return (String) sct.get("adressePageProjets");
    }


    /**
     * Renvoi la valeur configuree pour le login du site Colibri
     *
     * @return Le login du site
     */
    public String getLoginColibri() {

        Ini.Section sct = (Ini.Section) ini.get("Colibri");
        return (String) sct.get("login");
    }


    /**
     * Renvoi la valeur configuree pour le password du site Colibri
     *
     * @return Le password du site
     */
    public String getPasswordColibri() {

        Ini.Section sct = (Ini.Section) ini.get("Colibri");
        return (String) sct.get("password");
    }


    /**
     * Renvoi la valeur configuree pour la derniere demande IXXI ajoutee
     *
     * @return La derniere valeur ajoutee
     */
    public int getLastAddedTask() {

        Ini.Section sct = (Ini.Section) ini.get("Colibri");
        return Integer.parseInt((String) sct.get("dernierNumTache") != null ? (String) sct.get("dernierNumTache") : "0000");
    }


    /**
     * Change la valeur de la dernière demande IXX traitee
     *
     * @param lastAdded La derniere valeur ajoutee a Colibri
     */
    public void setLastAddedTask(String lastAdded) {

        Ini.Section sct = (Ini.Section) ini.get("Colibri");
        if (Integer.parseInt((String) sct.get("dernierNumTache")) < Integer.parseInt(lastAdded)) {
            sct.replace("dernierNumTache", lastAdded);
            modified = true;
            sauvegarde();
        } else
            Main.ecrire_log("[X]\tImpossible de mettre à jour le dernier numéro de tâche à un numéro inférieur");
    }


    /**
     * Renvoi la valeur configuree pour la transcription projet-application sur le site Colibri
     *
     * @param projetCSV Le projet à transcrire
     * @return L'application à chercher
     */
    public String getApplicationColibri(String projetCSV) {

        Ini.Section sct = (Ini.Section) ini.get("ColibriApplication");
        return ((String) sct.get(projetCSV) != null) ? (String) sct.get(projetCSV) : (String) sct.get("default");
    }


    /**
     * Renvoi la valeur configuree pour la transcription tracker-sous-projet sur le site Colibri
     *
     * @param trackerCSV Le tracker à transcrire
     * @return Le sous projet à chercher
     */
    public String getProjetColibri(String trackerCSV) {

        Ini.Section sct = (Ini.Section) ini.get("ColibriProjet");
        return ((String) sct.get(trackerCSV) != null) ? (String) sct.get(trackerCSV) : (String) sct.get("default");
    }


    /**
     * Renvoi la valeur configuree pour la transcription tracker-type de forfait sur le site Colibri
     *
     * @param trackerCSV Le tracker à transcrire
     * @return Le type de forfait à chercher
     */
    public String getTypeForfaitColibri(String trackerCSV) {

        Ini.Section sct = (Ini.Section) ini.get("ColibriTypeForfait");
        return ((String) sct.get(trackerCSV) != null) ? (String) sct.get(trackerCSV) : (String) sct.get("default");
    }


    /**
     * Renvoi la valeur configuree pour la transcription tracker-type de demande sur le site Colibri
     *
     * @param trackerCSV Le tracker à transcrire
     * @return Le type de demande à chercher
     */
    public String getTypeTacheColibri(String trackerCSV) {

        Ini.Section sct = (Ini.Section) ini.get("ColibriTypeTache");
        return ((String) sct.get(trackerCSV) != null) ? (String) sct.get(trackerCSV) : (String) sct.get("default");
    }


    /**
     * Renvoi la valeur configuree pour l'envoyeur du mail de recapitulatif
     *
     * @return Le mail de l'envoyeur
     */
    public String getEnvoyeurMail() {

        Ini.Section sct = (Ini.Section) ini.get("Mail");
        return (String) sct.get("envoyeur");
    }


    /**
     * Renvoi la valeur configuree pour le login de connexion au serveur mail
     *
     * @return Le login du serveur mail
     */
    public String getLoginMail() {

        Ini.Section sct = (Ini.Section) ini.get("Mail");
        return (String) sct.get("login");
    }


    /**
     * Renvoi la valeur configuree pour le password de connexion au serveur mail
     *
     * @return Le login du serveur mail
     */
    public String getPasswordMail() {

        Ini.Section sct = (Ini.Section) ini.get("Mail");
        return (String) sct.get("password");
    }


    /**
     * Renvoi la valeur configuree pour le destinataire du mail recapitulatif
     *
     * @return Le(s) destinataire(s) du mail
     */
    public InternetAddress[] getDestinataireMail() {

        Ini.Section sct = (Ini.Section) ini.get("Mail");
        InternetAddress[] ia = null;
        try {
            ia = InternetAddress.parse(String.join(",", Arrays.asList((String[]) sct.getAll("destinataire", String[].class))));
        } catch (NullPointerException npe) {
            Main.ecrire_log("[X]\tErreur lors du parse des adresses mail : les adresses sont a null");
        } catch (AddressException e) {
            Main.ecrire_log("[X]\tEchec du parse des adresse mail");
        }
        return ia;
    }
}
