package fr.insalyon.waso.sma;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.insalyon.waso.util.JsonHttpClient;
import fr.insalyon.waso.util.exception.ServiceException;
import java.io.IOException;
import java.util.HashMap;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author WASO Team
 */
public class ServiceMetier {

    protected String somClientUrl;
    protected String somPersonneUrl;
    protected JsonObject container;
    
    protected JsonHttpClient jsonHttpClient;

    public ServiceMetier(String somClientUrl, String somPersonneUrl, JsonObject container) {
        this.somClientUrl = somClientUrl;
        this.somPersonneUrl = somPersonneUrl;
        this.container = container;
        
        this.jsonHttpClient = new JsonHttpClient();
    }
    
    public void release() {
        try {
            this.jsonHttpClient.close();
        } catch (IOException ex) {
            // Ignorer
        }
    }
    
    /*public void rechercherClientParDenomination(String denomination, String ville) throws ServiceException {
        
        try {
            System.out.println("enter rechercherClientParDenomination");
            // 1. Obtenir la liste des Clients
            
            JsonObject clientContainer = null;
            
            
            clientContainer = this.jsonHttpClient.post(this.somClientUrl, new BasicNameValuePair("SOM", "rechercherClientParDenomination"), new BasicNameValuePair("denomination", denomination),new BasicNameValuePair("ville", ville));
            if (clientContainer == null) {
                throw new ServiceException("Appel impossible au Service Client::rechercherClientParDenomination [" + this.somClientUrl + "]");
            }
            
            //JsonObject clientContainer = clientContainerElement.getAsJsonObject();
            JsonArray clientListe = clientContainer.getAsJsonArray("clients");
            System.out.println(clientListe);
            // 2. Obtenir la liste des Personnes
            
            //JsonArray clients = new JsonArray();
            JsonArray personnesList = new JsonArray();
            for (JsonElement clientElement:clientListe.getAsJsonArray()){
                System.out.println("enter boucle");
                JsonObject c = clientElement.getAsJsonObject();
                JsonArray personneIDList = c.get("personne-ID").getAsJsonArray();
                //System.out.println("list: "+personneIDList);
                for (JsonElement personneID: personneIDList){
                    //System.out.println(personneID.getAsInt());
                    JsonElement personneContainerElement = this.jsonHttpClient.post(this.somPersonneUrl, new BasicNameValuePair("SOM", "rechercherPersonneParID"), new BasicNameValuePair("idPersonne", personneID.getAsInt()+""));
                    JsonObject personne = personneContainerElement.getAsJsonObject();
                    personnesList.add(personne.get("personne"));
                }
                c.remove("personnes-ID");
                c.add("personnes", personnesList);
                //clients.add(c);
                //JsonElement personneContainerElement = this.jsonHttpClient.post(this.somPersonneUrl, new BasicNameValuePair("SOM", "rechercherPersonneParID"), new BasicNameValuePair("numero", clientElement.getAsString()));
                //JsonObject personne = personneContainerElement.getAsJsonObject();
                //personnesList.add(personne.get("personne"));
            }
            this.container.add("clients", clientListe);
                    
        } catch (IOException ex) {
            throw new ServiceException("Exception in SMA rechercherClientParDeonmination", ex);
        }
    }*/
    
    public void rechercherClientParDenomination(String denomination, String ville) throws ServiceException {
        
        try {
            // 1. Obtenir la liste des Clients
            
            JsonObject clientContainer = this.jsonHttpClient.post(this.somClientUrl, new BasicNameValuePair("SOM", "rechercherClientParDenomination"), new BasicNameValuePair("denomination", denomination),new BasicNameValuePair("ville", ville));

            if (clientContainer == null) {
                throw new ServiceException("Appel impossible au Service Client::getListeClient [" + this.somClientUrl + "]");
            }

            JsonArray jsonOutputClientListe = clientContainer.getAsJsonArray("clients"); //new JsonArray();
            //System.out.println(jsonOutputClientListe);
            
            // 2. Obtenir la liste des Personnes
            /*
            JsonObject personneContainer = this.jsonHttpClient.post(this.somPersonneUrl, new BasicNameValuePair("SOM", "getListePersonne"));

            if (personneContainer == null) {
                throw new ServiceException("Appel impossible au Service Personne::getListePersonne [" + this.somPersonneUrl + "]");
            }

            
            // 3. Indexer la liste des Personnes
            
            HashMap<Integer, JsonObject> personnes = new HashMap<Integer, JsonObject>();
            
            for (JsonElement p : personneContainer.getAsJsonArray("personnes")) {

                JsonObject personne = p.getAsJsonObject();

                personnes.put(personne.get("id").getAsInt(), personne);
            }

            
            // 3. Construire la liste des Personnes pour chaque Client (directement dans le JSON)
            
            for (JsonElement clientElement : jsonOutputClientListe.getAsJsonArray()) {

                JsonObject client = clientElement.getAsJsonObject();

                JsonArray personnesID = client.get("personnes-ID").getAsJsonArray();

                JsonArray outputPersonnes = new JsonArray();

                for (JsonElement personneID : personnesID) {
                    JsonObject personne = personnes.get(personneID.getAsInt());
                    outputPersonnes.add(personne);
                }

                client.add("personnes", outputPersonnes);

            }
*/
            
            // 4. Ajouter la liste de Clients au conteneur JSON
            
            this.container.add("clients", jsonOutputClientListe);
                    
        } catch (IOException ex) {
            throw new ServiceException("Exception in SMA getListeClient", ex);
        }
    }
    
    public void getListeClient() throws ServiceException {
        try {

            // 1. Obtenir la liste des Clients
            
            JsonObject clientContainer = this.jsonHttpClient.post(this.somClientUrl, new BasicNameValuePair("SOM", "getListeClient"));

            if (clientContainer == null) {
                throw new ServiceException("Appel impossible au Service Client::getListeClient [" + this.somClientUrl + "]");
            }

            JsonArray jsonOutputClientListe = clientContainer.getAsJsonArray("clients"); //new JsonArray();

            
            // 2. Obtenir la liste des Personnes
            
            JsonObject personneContainer = this.jsonHttpClient.post(this.somPersonneUrl, new BasicNameValuePair("SOM", "getListePersonne"));

            if (personneContainer == null) {
                throw new ServiceException("Appel impossible au Service Personne::getListePersonne [" + this.somPersonneUrl + "]");
            }

            
            // 3. Indexer la liste des Personnes
            
            HashMap<Integer, JsonObject> personnes = new HashMap<Integer, JsonObject>();
            
            for (JsonElement p : personneContainer.getAsJsonArray("personnes")) {

                JsonObject personne = p.getAsJsonObject();

                personnes.put(personne.get("id").getAsInt(), personne);
            }

            
            // 3. Construire la liste des Personnes pour chaque Client (directement dans le JSON)
            
            for (JsonElement clientElement : jsonOutputClientListe.getAsJsonArray()) {

                JsonObject client = clientElement.getAsJsonObject();

                JsonArray personnesID = client.get("personnes-ID").getAsJsonArray();

                JsonArray outputPersonnes = new JsonArray();

                for (JsonElement personneID : personnesID) {
                    JsonObject personne = personnes.get(personneID.getAsInt());
                    outputPersonnes.add(personne);
                }

                client.add("personnes", outputPersonnes);

            }

            
            // 4. Ajouter la liste de Clients au conteneur JSON
            
            this.container.add("clients", jsonOutputClientListe);
                    
        } catch (IOException ex) {
            throw new ServiceException("Exception in SMA getListeClient", ex);
        }
    }

}
