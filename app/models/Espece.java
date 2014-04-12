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
	//Duplication des données de SousGroupe pour améliorer la vitesse de la base
	@ManyToOne
	public SousGroupe espece_sous_groupe;
	@NotNull
	@ManyToOne
	public SousFamille espece_sousfamille;
	@Column(columnDefinition="TEXT")
	public String espece_commentaires;

	public static Finder<Integer,Espece> find = new Finder<Integer,Espece>(Integer.class, Espece.class);

	public static Espece findBySystematique(Integer syst) {
		return find.where().eq("espece_systemtique", syst).findUnique();
	}
	
	public static List<Espece> findAll(){
		return find.orderBy("espece_systematique").findList();
	}
	
	//Fonctions de filtres de la liste des insectes
	/*************************************************/
	public static List<Espece> selectEspecesSousFamille(SousFamille sousfam){
		return find.where().eq("espece_sousfamille", sousfam).orderBy("espece_systematique").findList();
	}
	public static List<Espece> selectEspecesFamille(Famille fam){
		return find.where().eq("espece_sousfamille.sous_famille_famille", fam).orderBy("espece_systematique").findList();
	}
	public static List<Espece> selectEspecesSuperFamille(SuperFamille superfam){
		return find.where().eq("espece_sousfamille.sous_famille_famille.famille_super_famille", superfam).orderBy("espece_systematique").findList();
	}
	public static List<Espece> selectEspecesOrdre(Ordre ordre){
		return find.where().eq("espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre", ordre).orderBy("espece_systematique").findList();
	}
	public static List<Espece> selectEspecesSousGroupe(SousGroupe sousg){
		return find.where().eq("espece_sous_groupe", sousg).orderBy("espece_systematique").findList();
	}
	public static List<Espece> selectEspecesGroupe(Groupe groupe){
		return find.where().eq("espece_sous_groupe.sous_groupe_groupe",groupe).orderBy("espece_systematique").findList();
	}

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
	 * Ajoute une espèce au milieu ou début. S'applique sur l'espèce juste avant.
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

	/**
	 * Renvoie le sous-groupe auquel appartient l'espèce.
	 * Si aucun sous-groupe n'est défini, renvoie null.
	 * Attention, si les sous-groupes ont été définis de telle
	 * sorte qu'une espèce est dans plusieurs sous-groupes (ce
	 * qui est une erreur), la fonction de renvoiera qu'un seul
	 * sous-groupe
	 * @return SousGroupe s'il existe, null sinon
	 */
	public SousGroupe getSousGroupe(){
		SousGroupe resultat = null;
		EspeceHasSousGroupe ehsg =
				EspeceHasSousGroupe.find.where()
				.eq("espece",this)
				.findUnique();
		if(ehsg==null){
			SousFamilleHasSousGroupe sofhsg =
					SousFamilleHasSousGroupe.find.where()
					.eq("sous_famille",this.getSousFamille())
					.findUnique();
			if(sofhsg==null){
				FamilleHasSousGroupe fhsg =
						FamilleHasSousGroupe.find.where()
						.eq("famille",this.getFamille())
						.findUnique();
				if(fhsg==null){
					SuperFamilleHasSousGroupe sufhsg =
							SuperFamilleHasSousGroupe.find.where()
							.eq("super_famille",this.getSuperFamille())
							.findUnique();
					if(sufhsg==null){
						OrdreHasSousGroupe ohsg =
								OrdreHasSousGroupe.find.where()
								.eq("ordre",this.getOrdre())
								.findUnique();
						if(ohsg!=null)
							resultat = ohsg.sous_groupe;
					}else
						resultat = sufhsg.sous_groupe; 
				}else
					resultat = fhsg.sous_groupe;
			}else
				resultat = sofhsg.sous_groupe;
		}else
			resultat = ehsg.sous_groupe;
		return resultat;
	}

	/**
	 * Renvoie le groupe auquel appartient l'espèce.
	 * Si aucun groupe n'est défini, renvoie null.
	 * Attention, si les groupes ont été définis de telle
	 * sorte qu'une espèce est dans plusieurs groupes (ce
	 * qui est une erreur), la fonction de renvoiera qu'un seul
	 * groupe
	 * @return Groupe s'il existe, null sinon
	 */
	public Groupe getGroupe(){
		SousGroupe sg = this.getSousGroupe();
		if(sg!=null)
			return sg.sous_groupe_groupe;
		else
			return null;
	}

	/**
	 * Renvoie la sous-famille de l'espèce
	 * @return
	 */
	public SousFamille getSousFamille(){
		return this.espece_sousfamille;
	}
	/**
	 * Renvoie la famille de l'espèce
	 * @return
	 */
	public Famille getFamille(){
		return this.espece_sousfamille.sous_famille_famille;
	}
	/**
	 * Renvoie la super-famille de l'espèce
	 * @return
	 */
	public SuperFamille getSuperFamille(){
		return this.espece_sousfamille.sous_famille_famille.famille_super_famille;
	}
	/**
	 * Renvoie l'ordre de l'espèce
	 * @return
	 */
	public Ordre getOrdre(){
		return this.espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre;
	}
	
	/**
	 * Renvoie la plus vieille fiche contenant un témoignage de l'espèce en question
	 * Renvoie null si l'espèce n'a jamais été observée.
	 * @return
	 */
	public Fiche getPlusVieuxTemoignage(){
		Observation o = Observation.find.where()
								.eq("observation_espece",this)
								.eq("observation_validee",Observation.VALIDEE)
							.setMaxRows(1).orderBy("observation_fiche.fiche_date").findUnique();
		if(o==null)
			return null;
		else
			return o.getFiche();
	}
	
	/**
	 * Met a jour le sous-groupe de l'espèce en question.
	 * (Duplication des données des sous-groupes pour améliorer la vitesse des requêtes.)
	 */
	public void metAJourSousGroupes(){
		SousGroupe sg=this.getSousGroupe();
		this.espece_sous_groupe= sg;
		this.save();
	}
	
	/**
	 * Renvoie la liste des espèces sans sous-groupe.
	 * @return
	 */
	public static List<Espece> findEspecesSansSousGroupe(){
		return find.where().eq("espece_sous_groupe", null).findList();
	}
	
}
