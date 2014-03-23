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

package models;

import java.util.List;

import javax.naming.NamingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

/**
 * Source des données :
 * http://sql.sh/1879-base-donnees-departements-francais
 * @author malik
 *
 */
@SuppressWarnings("serial")
@Entity
public class Espece extends Model {
	@Id
	public Integer espece_id;
	@NotNull
	public String espece_nom;
	@NotNull
	public String espece_auteur;
	@NotNull
	public Integer espece_systematique;
	public String espece_photo;
	@NotNull
	@ManyToOne
	public SousFamille espece_sousfamille;
	@Column(columnDefinition="TEXT")
	public String espece_commentaires;

	public static Finder<Integer,Espece> find = new Finder<Integer,Espece>(Integer.class, Espece.class);

	@Override
	public String toString(){
		return espece_systematique+". "+espece_nom+"-"+espece_auteur;
	}

	/**
	 * Créé une nouvelle espèce. Attention, on ne peux pas enregistrer cette espèce dans la base
	 * de données tout de suite avec la méthode save() ! Il faut utiliser ajouterNouvelleEspece().
	 * @param nom
	 * @param auteur
	 * @param systematique
	 * @param commentaires
	 */
	public Espece(String nom, String auteur, Integer systematique, String commentaires){
		espece_nom=nom;
		espece_auteur=auteur;
		espece_systematique=systematique;
		espece_commentaires=commentaires;
		espece_sousfamille=null;
	}

	/**
	 * Ajoute l'espèce en réordonnant toutes les espèces qui suivent.
	 * @param avecSousFamille
	 * @param sousFamilleOuFamille
	 * @throws NamingException
	 * @throws PersistenceException
	 */
	public void ajouterNouvelleEspece(boolean avecSousFamille, String sousFamilleOuFamille) throws NamingException, PersistenceException{
		if(avecSousFamille){
			this.espece_sousfamille=SousFamille.find.where().eq("sous_famille_nom", sousFamilleOuFamille).findUnique();
			if(espece_sousfamille!=null){
				this.save();
			}else{
				throw new NamingException("La sous-famille "+sousFamilleOuFamille+" n'existe pas !");
			}
		}
		else{
			this.espece_sousfamille=new SousFamille(this.espece_nom,false,sousFamilleOuFamille);
			this.espece_sousfamille.save();
			this.save();
		}
		List<Espece> especes = Espece.find.where().ge("espece_systematique",this.espece_systematique).findList();
		for(Espece e : especes){
			if(!e.espece_nom.equals(this.espece_nom)){
				e.espece_systematique++;
				e.save();
			}
		}
	}
}
