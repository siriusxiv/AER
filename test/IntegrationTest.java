/*********************************************************************************
 * 
 *   Copyright 2014 BOUSSEJRA Malik Olivier, HALDEBIQUE Geoffroy, ROYER Johan
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 ********************************************************************************/

import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import models.Commune;
import models.Espece;
import models.Famille;
import models.Fiche;
import models.Groupe;
import models.Membre;
import models.Ordre;
import models.SousFamille;
import models.SousGroupe;
import models.StadeSexe;
import models.SuperFamille;

import org.junit.*;

import controllers.ajax.Listes;
import functions.credentials.Credentials;
import functions.excels.exports.TemoinsParPeriodeExcel;
import functions.mail.VerifierMail;
import play.mvc.*;
import play.test.*;
import play.libs.F.*;
import views.html.admin.gererBaseDeDonneesInsectes;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

	Map<String, Object> databaseConfiguration = new HashMap<String, Object>();

    /**
     * add your integration test here
     */
    @Test
    public void test() {
    	databaseConfiguration.put("db.default.url", "jdbc:mysql://localhost:3306/aer");
        running(testServer(3333, fakeApplication(databaseConfiguration)), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

            	long i = Calendar.getInstance().getTimeInMillis();
            	
            	//Teste que les bons départements sont trouvés par l'application
                //System.out.println(models.Departement.findDepartementsAER());
                
                //ajouteEspece();
                
                //Credentials.creeHashEtMotDePassePourToutLeMonde();
            	
            	//listeMembres();
            	
            	//listeEspecesAvecSousGroupeEtGroupe();
            	
            	//VerifierMail.envoyerMailDeVerification(Membre.find.all().get(0));
            	
            	//affichePremieresSystematiques();
            	
            	//gererBaseDeDonneesInsectes.render(Espece.findAll(), SousFamille.findSousFamillesExistantes(), Famille.findAll(), SuperFamille.findSuperFamillesExistantes(), Ordre.findAll(), SousGroupe.findAll(), Groupe.findAll());
            	//gererBaseDeDonneesInsectes.render(Espece.findAll(), SousFamille.findSousFamillesExistantesTriees(), Famille.findAllTriees(), SuperFamille.findSuperFamillesExistantesTriees(), Ordre.findAllTries(), SousGroupe.findAll(), Groupe.findAll());
            	
            	//excelTests();
            	
            	//System.out.println(StadeSexe.getStadesImagos());
            	
            	//testDoublonTemoin();
            	
            	testDoublonCommune();
            	
            	long j = Calendar.getInstance().getTimeInMillis();
            	System.out.println("Calculé en "+(j-i)+" ms");
            }
        });
    }
    
    private void ajouteEspece(){
    	Espece e = new Espece("Rasgus pipus","Malik Olivier Boussejra, 2014",54,"GA!");
    	System.out.println(e);
    	try {
			e.ajouterNouvelleEspece(true, 1);
		} catch (PersistenceException | NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	Espece es = new Espece("Rasgus pipus2","Malik Olivier Boussejra, 2014",54,"GA2!");
    	System.out.println(es);
    	try {
			es.ajouterNouvelleEspece(true, 1);
		} catch (PersistenceException | NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	Espece esp = new Espece("Rasgus pipus3","Malik Olivier Boussejra, 2014",54,"GA3!");
    	System.out.println(esp);
    	try {
			esp.ajouterNouvelleEspece(false, 1);
		} catch (PersistenceException | NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	Espece espe = new Espece("Rasgus pipus4","Malik Olivier Boussejra, 2014",54,"GA4!");
    	System.out.println(espe);
    	try {
			espe.ajouterNouvelleEspece(false, 1);
		} catch (PersistenceException | NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    private void listeMembres() throws IOException{
    	FileWriter fw = new FileWriter("listeMembres.js");
    	fw.append(Listes.listeMembres());
    	fw.flush();
    	fw.close();
    }
    
    private void listeEspecesAvecSousGroupeEtGroupe() throws IOException{
    	FileWriter fw = new FileWriter("listeEspeces");
    	List<Espece> especes = Espece.find.where().orderBy("espece_systematique").findList();
    	SousGroupe sg = null;
    	Fiche f = null;
		SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
    	for(Espece e : especes){
    		sg = e.getSousGroupe();
    		f = e.getPlusVieuxTemoignage();
    		if(f==null)
        		fw.append(e+","+sg+","+sg.sous_groupe_groupe+","+null+"\n");
    		else
    			fw.append(e+","+sg+","+sg.sous_groupe_groupe+","+date_format.format(f.fiche_date.getTime())+"\n");
    	}
    	fw.flush();
    	fw.close();
    }

	private void affichePremieresSystematiques() {
		for(Ordre ordre : Ordre.findAll()){
    		System.out.println(ordre.getSystematiquePremiereEspeceDansThis());
    	}
    	for(SuperFamille superfam : SuperFamille.findSuperFamillesExistantes()){
    		System.out.println(superfam.getSystematiquePremiereEspeceDansThis());
    	}
    	for(Famille fam : Famille.findAll()){
    		System.out.println(fam.getSystematiquePremiereEspeceDansThis());
    	}
    	for(SousFamille sousfam : SousFamille.findSousFamillesExistantes()){
    		System.out.println(sousfam.getSystematiquePremiereEspeceDansThis());
    	}
	}
	
	private void excelTests() throws IOException{
		new TemoinsParPeriodeExcel(null,null).writeToDisk();
	}
	
	private void testDoublonTemoin(){
		List<Membre> membres = Membre.find.all();
		for(Membre m : membres){
			try{
				Membre.find.where().eq("membre_nom", m.membre_nom).findUnique();
			}catch(PersistenceException e){
				System.out.println(m.membre_nom);
			}
		}
	}
	
	private void testDoublonCommune(){
		List<Commune> communes = Commune.find.all();
		for(Commune c : communes){
			try{
				Commune.find.where().eq("ville_nom_reel", c.ville_nom_reel).findUnique();
			}catch(PersistenceException e){
				System.out.println(c.ville_nom_reel);
			}
		}
	}
}
