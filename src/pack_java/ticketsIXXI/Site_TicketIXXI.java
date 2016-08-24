package pack_java.ticketsIXXI;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pack_java.configuration.ConfigIni;

/**
 * La classe TicketIXXI_Site spécifie où trouver les blocs dont on a besoin,
 * pour permettre la mise à jour facile si le code html change
 *
 * @author Erwan Le Batard--Polès
 * @version 1.0
 */
class Site_TicketIXXI {

    /**
     * Renvoi l'adresse du site
     *
     * @return L'adresse du site
     */
    static String adresse() {
        return ConfigIni.getInstance().getAdresseSiteTicketIXXI();
    }


    /**
     * Renvoi l'adresse de la page des projets
     *
     * @return L'adresse de la page projets
     */
    static String adresse_page_projet() {
        return ConfigIni.getInstance().getAdresseProjetsTicketIXXI();
    }


    /*
    * La sous classe Login_Page permet de simplifier l'accès dans le code aux données
    * dont on a besoin sur la page de connexion
    *
    */
    static class Page_Login {
        /**
         * Renvoi l'élément 'identifiant' de la page
         *
         * @param driver Le driver sur lequel on cherche l'élément
         * @return Le WebElement trouvé
         */
        static WebElement LOC_TXT_USERNAME(WebDriver driver) {
            return driver.findElement(By.id("username"));
        }

        /**
         * Renvoi l'élément 'mot de passe' de la page
         *
         * @param driver Le driver sur lequel on cherche l'élément
         * @return Le WebElement trouvé
         */
        static WebElement LOC_TXT_PASSWORD(WebDriver driver) {
            return driver.findElement(By.id("password"));
        }

        /**
         * Renvoi l'élément bouton de connexion de la page
         *
         * @param driver Le driver sur lequel on cherche l'élément
         * @return Le WebElement trouvé
         */
        static WebElement LOC_BTN_CONNECT(WebDriver driver) {
            return driver.findElement(By.name("login"));
        }
    }

    /*
    * La sous classe Projects_Page permet de simplifier l'accès dans le code aux données
    * dont on a besoin sur la page des projets
    *
    */
    static class Page_Projects {
        /**
         * Renvoi l'élément 'lien vers toutes les demandes' de la page
         *
         * @param driver Le driver sur lequel on cherche l'élément
         * @return Le WebElement trouvé
         */
        static WebElement LOC_LNK_TTESDEMANDES(WebDriver driver) {
            return driver.findElement(By.linkText("Voir toutes les demandes"));
        }
    }

    /*
   * La sous classe Issues_Page permet de simplifier l'accès dans le code aux données
   * dont on a besoin sur la page des demandes
   *
   */
    static class Page_Issues {
        /**
         * Renvoi l'élément 'lien vers CSV' de la page
         *
         * @param driver Le driver sur lequel on cherche l'élément
         * @return Le WebElement trouvé
         */
        static WebElement LOC_LNK_CSV(WebDriver driver) {
            return driver.findElement(By.linkText("CSV"));
        }

        /**
         * Renvoi l'élément 'bouton radio toutes les colonnes' de la page
         *
         * @param driver Le driver sur lequel on cherche l'élément
         * @return Le WebElement trouvé
         */
        static WebElement LOC_RBTN_TTESCOLONNES(WebDriver driver) {
            return driver.findElement(By.id("columns_all"));
        }

        /**
         * Renvoi l'élément 'bouton submit' de la page
         *
         * @param driver Le driver sur lequel on cherche l'élément
         * @return Le WebElement trouvé
         */
        static WebElement LOC_BTN_SUBMIT(WebDriver driver) {
            return driver.findElement(By.cssSelector("input[type=\"submit\"]"));
        }
    }
}
