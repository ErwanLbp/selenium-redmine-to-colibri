package pack_java.colibri;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pack_java.configuration.ConfigIni;

import java.util.List;

/**
 * La classe Colibri_Site specifie ou trouver les blocs dont on a besoin,
 * pour permettre la mise a jour facile si le code html change
 *
 * @author Erwan Le Batard--Poles
 * @version 1.0
 */
class Site_Colibri {

    /**
     * Renvoi l'adresse du site
     *
     * @return L'adresse du site
     */
    static String adresse_site() {
        return ConfigIni.getInstance().getAdresseSiteColibri();
    }


    /**
     * Renvoi l'adresse de la page des projets
     *
     * @return L'adresse de la page projets
     */
    static String adresse_page_projet() {
        return ConfigIni.getInstance().getAdresseProjetsColibri();
    }


    /**
     * La sous classe Login_Page permet de simplifier l'acces dans le code aux donnees
     * dont on a besoin sur la page de connexion
     */
    static class Page_Login {

        /**
         * Renvoi l'element 'identifiant' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_TXT_USERNAME(WebDriver driver) {
            return driver.findElement(By.id("form_auth_name"));
        }

        /**
         * Renvoi l'element 'mot de passe' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_TXT_PASSWORD(WebDriver driver) {
            return driver.findElement(By.id("form_auth_password"));
        }

        /**
         * Renvoi l'element bouton de connexion de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_BTN_CONNECT(WebDriver driver) {
            return driver.findElement(By.cssSelector("button.btn.btn-primary"));
        }
    }


    /**
     * La sous classe Page_Projets permet de simplifier l'acces dans le code aux donnees
     * dont on a besoin sur la page des projets
     */
    static class Page_Projets {

        /**
         * Renvoi l'element 'lien vers le projet' de la page
         *
         * @param driver  Le driver sur lequel on cherche l'element
         * @param appli   Le nom de l'appli a chercher sur la page
         * @param ssProjet Le nom du sous projet a chercher
         * @return Le WebElement trouve
         */
        static WebElement LOC_LNK_NOMPROJET(WebDriver driver, String appli, String ssProjet) {

            List<WebElement> l = driver.findElements(By.xpath("//a[contains(.,'Application "+appli+"')]/ancestor::tr/following::a[contains(.,'"+ssProjet+"')]"));
            return (l.size() > 0) ? l.get(0) : null;
        }


        /**
         * Renvoi l'element 'bouton nouveau projet' de la page
         *
         * @param driver  Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_BTN_AJOUTPROJET(WebDriver driver) {

            return driver.findElement(By.xpath("//button[@type='button' and contains(.,'Ajouter')]"));
        }
    }


    /**
     * La sous classe Page_DetailProjet permet de simplifier l'acces dans le code aux donnees
     * dont on a besoin sur la page des projets
     */
    static class Page_DetailProjet {

        /**
         * Renvoi l'element 'bouton ajouter une tache' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_BTN_AJOUTERTACHE(WebDriver driver) {
            return driver.findElement(By.xpath("(//button[@type='button' and contains(.,'Ajouter une tâche')])[1]/parent::a"));
        }
    }


    /**
     * La sous classe Page_AjoutTache permet de simplifier l'acces dans le code aux donnees
     * dont on a besoin sur la page d'une tache
     */
    static class Page_AjoutTache {

        /**
         * Renvoi l'element 'select type de tache' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_SLCT_TYPETACHE(WebDriver driver) {
            return driver.findElement(By.id("select"));
        }


        /**
         * Renvoi l'element 'numero de la tache' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_TXT_NUMTACHE(WebDriver driver) {
            return driver.findElement(By.id("form_task_name"));
        }


        /**
         * Renvoi l'element 'nom de la tache' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_TXT_NOMTACHE(WebDriver driver) {
            return driver.findElement(By.name("name"));
        }


        /**
         * Renvoi l'element 'budget de la tache' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_TXT_BUDGETTACHE(WebDriver driver) {
            return driver.findElement(By.id("form_task_budget_jh"));
        }


        /**
         * Renvoi l'element 'type de facturation de la tache' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_SLCT_FACTURATIONTACHE(WebDriver driver) {
            return driver.findElement(By.id("type_factu"));
        }


        /**
         * Renvoi l'element 'commentaire' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_TXT_COMMENTAIRE(WebDriver driver) {
            return driver.findElement(By.id("form_task_comment"));
        }


        /**
         * Renvoi l'element 'bouton valider' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_BTN_VALIDER(WebDriver driver) {
            return driver.findElement(By.xpath("//input[@type='submit' and @value='Valider']"));
        }
    }


    /**
     * La sous classe Page_AjoutProjet permet de simplifier l'acces dans le code aux donnees
     * dont on a besoin sur la page d'ajout d'un projet
     */
    static class Page_AjoutProjet {

        /**
         * Renvoi l'element 'select nom de l'application' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_SLCT_NOMAPPLI(WebDriver driver) {
            return driver.findElement(By.id("form_project_app"));
        }


        /**
         * Renvoi l'element 'nom du projet' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_TXT_NOMPROJET(WebDriver driver) {
            return driver.findElement(By.id("form_project_name"));
        }


        /**
         * Renvoi l'element 'budget du projet' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_TXT_BUDGET(WebDriver driver) {
            return driver.findElement(By.id("form_project_budget_jh"));
        }


        /**
         * Renvoi l'element 'Template projet' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_SLCT_TEMPLATE(WebDriver driver) {
            return driver.findElement(By.id("form_task_template"));
        }


        /**
         * Renvoi l'element 'bouton valider' de la page
         *
         * @param driver Le driver sur lequel on cherche l'element
         * @return Le WebElement trouve
         */
        static WebElement LOC_BTN_VALIDER(WebDriver driver) {
            return driver.findElement(By.xpath("//input[@type='submit' and @value='Valider' and @name='action']"));
        }
    }
}
