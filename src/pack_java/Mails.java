package pack_java;

import pack_java.configuration.ConfigIni;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Cette classe gere l'envoi du mail recapitulatif des ajouts dans Colibri
 *
 * @author Erwan Le Batard--Polès
 * @version 1.0
 */
public class Mails {

    /**
     * Le corps du mail
     */
    private Map<String, String> contenuMail;


    /**
     * L'objet du mail, pouvant etre modifie
     */
    private String objet;


    /**
     * Booleen permettant de savoir s'il le fichier de configuration a ete modifie
     */
    private boolean modified = false;


    /**
     * Constructeur initialisant les attributs de l'objet
     *
     * @param nbNbvlTaches Nombre de taches, pour remplir l'objet du mail
     */
    public Mails(int nbNbvlTaches) {

        //On charge les donnees sauvegardes si il y en a
        if (Files.exists(Paths.get("data\\mail.obj"))) {
            chargement();
            setObjet("Nouvelles demandes sur " + ConfigIni.getInstance().getAdresseSiteColibri());
        } else {
            this.contenuMail = new HashMap<>();
            setObjet(nbNbvlTaches + " nouvelles demandes sur " + ConfigIni.getInstance().getAdresseSiteColibri());
        }
    }


    /**
     * Sauvegarde le fichier du mail si ce dernier a ete modifie lors de l'execution
     */
    private void sauvegarde() {
        if (modified) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("data\\mail.obj"))))) {
                oos.writeObject(contenuMail);
                Main.ecrire_log("\t\tSauvegarde des donnees du mail reussie");
                modified = false;
            } catch (FileNotFoundException fnfe) {
                Main.ecrire_log("[X]\tLe fichier de sauvegarde du mail n'a pas pu etre ouvert");
            } catch (IOException e) {
                Main.ecrire_log("[X]\tEchec de la sauvegarde du mail");
                e.printStackTrace();
            }
        }
    }


    /**
     * Charge le fichier du mail
     */
    private void chargement() {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File("data\\mail.obj"))))) {
            contenuMail = (Map<String, String>) ois.readObject();
            setObjet("**reprise** Nouvelles demandes sur " + ConfigIni.getInstance().getAdresseSiteColibri());
            Main.ecrire_log("[O]\tChargement des donnees du mail reussi");
        } catch (FileNotFoundException fnfe) {
            Main.ecrire_log("[X]\tLe fichier de sauvegarde du mail n'existe pas ou n'a pas pu etre ouvert");
        } catch (ClassNotFoundException e) {
            Main.ecrire_log("[X]\tEchec du chargement du mail : Objet non trouve");
        } catch (IOException e) {
            Main.ecrire_log("[X]\tEchec du chargement du mail");
            e.printStackTrace();
        }
    }


    /**
     * Supprime le fichier du mail s'il existe
     */
    private static void suppressionFichierMail() {

        try {
            if (Files.deleteIfExists(Paths.get("data\\mail.obj")))
                Main.ecrire_log("[O]\tSuppression du fichier mail reussie");
        } catch (IOException e) {
            Main.ecrire_log("[X]\tErreur lors de la suppression du fichier mail");
        }
    }


    /**
     * Permet d'ajouter une ligne dans le corps du mail pour la creation d'un projet
     *
     * @param numDemande  Numéro du ticketIXXI
     * @param nomProjet   Nom du projet ajoute
     * @param application Nom de l'application ou est cree le projet
     */
    public void ajouter_projet(String numDemande, String nomProjet, String application) {

        contenuMail.put(application, "Nouveau projet : [" + numDemande + "] " + nomProjet + "</br>" + contenuMail.getOrDefault(application, ""));
        modified = true;
        sauvegarde();
    }


    /**
     * Permet d'ajouter une ligne dans le corps du mail pour l'ajout d'une nouvelle tache
     *
     * @param numTache    Le numero de la tache creee
     * @param nomTache    Le nom de la tache creee
     * @param application Le nom de l'application ou la tache est creee
     * @param projet      Le nom du projet ou la tache est creee
     */
    public void ajouter_tache(String numTache, String nomTache, String application, String projet) {

        contenuMail.put(application, contenuMail.getOrDefault(application, "") + "</br>Nouvelle tâche dans le projet <b>" + projet + "</b> : [" + numTache + "] " + nomTache);
        modified = true;
        sauvegarde();
    }


    /**
     * Permet de modifier l'objet du mail
     *
     * @param objet Le nouvel objet du mail
     */
    public void setObjet(String objet) {

        this.objet = objet;
    }


    /**
     * Renvoi le contenu du mail, forme a partir de la map des differents ajouts
     *
     * @return La chaine du corps du mail
     */
    private String former_contenu_mail() {

        String res = "Création de nouvelles demandes sur Colibri à partir des redmine</br>";

        Set<String> applis = this.contenuMail.keySet();
        if (applis.size() == 0)
            res = "Aucun ajout";
        for (String appli : applis) {
            res += "</br></br>---------------------------------------------------------</br>Application <b>" + appli + "</b> : </br></br>" + contenuMail.get(appli) + "</br>";
        }

        return res;
    }


    /**
     * Configure le serveur et les differents parametres et envoi le mail
     */
    public void envoyer_mail() {

        try {
            String smtpHost = "ptx.send.corp.sopra";

            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.host", smtpHost);
            props.setProperty("mail.smtp.port", "587");

            Session session = Session.getInstance(props);

            //Le message
            Message message = new MimeMessage(session);
            message.addRecipients(Message.RecipientType.TO, ConfigIni.getInstance().getDestinataireMail());
            message.setFrom(new InternetAddress(ConfigIni.getInstance().getEnvoyeurMail()));
            message.setSubject(objet);
            message.setContent(former_contenu_mail(), "text/html; charset=UTF-8");

            Transport tr = session.getTransport("smtp");

            tr.connect(smtpHost, ConfigIni.getInstance().getLoginMail(), ConfigIni.getInstance().getPasswordMail());
            message.saveChanges();

            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

            suppressionFichierMail();

        } catch (AuthenticationFailedException afe) {
            Main.ecrire_log("[X]\tEchec de l'authentification sur le serveur mail");
        } catch (NoSuchProviderException e) {
            Main.ecrire_log("[X]\tPas de transport disponible pour ce protocole");
            e.printStackTrace();
        } catch (AddressException e) {
            Main.ecrire_log("[X]\tProbleme lors du parse des adresse de destination du mail");
            e.printStackTrace();
        } catch (SendFailedException sfe) {
            Main.ecrire_log("[X]\tErreur lors de l'envoi du mail : Mauvaise(s) adresse(s)");
        } catch (MessagingException e) {
            Main.ecrire_log("[X]\tErreur lors du traitement du mail (MessagingException)");
            e.printStackTrace();
        }
    }
}
