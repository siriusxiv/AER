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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List; 

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Ordre extends Model implements Comparator<Ordre>{
	@Id
	public Integer ordre_id;
	@NotNull
	public String ordre_nom;

	public static Finder<Integer,Ordre> find = new Finder<Integer,Ordre>(Integer.class, Ordre.class);

	public static List<Ordre> findAll(){
		return find.orderBy("ordre_nom").findList();
	}
	
	/**
	 * Trie les ordres
	 * @return
	 */
	public static List<Ordre> findAllTries(){
		List<Ordre> ordres = find.all();
		Collections.sort(ordres,new Ordre());
		return ordres;
	}
	private Ordre() {}
	
	/** Crée un ordre
	* @param nom
	 */
	 public Ordre(String nom){
	 	 ordre_nom=nom;
	 }
	 
	/** Ajoute un ordre à la base de données
	* @param nom
	 */
	 public static void ajouterOrdre(String nom){
	 	 Ordre ordre = new Ordre(nom);
	 	 ordre.save();
	 }
	
	
	@Override
	public String toString(){
		return ordre_nom;
	}

	/**
	 * Pour trier les listes d'ordres selon la systématique de la première espèce dedans.
	 */
	@Override
	public int compare(Ordre ordre1, Ordre ordre2) {
		int sys1 = ordre1.getSystematiquePremiereEspeceDansThis();
		int sys2 = ordre2.getSystematiquePremiereEspeceDansThis();
		return (sys1<sys2 ? -1 : (sys1==sys2 ? 0 : 1));
	}

	/**
	 * Trouve les ordres que l'on peut ajouter dans un sous-groupe
	 * @return
	 */
	public static List<Ordre> findOrdresAjoutablesDansSousGroupe(){
		List<Espece> especesSansSousGroupe = Espece.findEspecesAjoutablesDansSousGroupe();
		List<Ordre> ordres = new ArrayList<Ordre>();
		for(Espece espece : especesSansSousGroupe){
			if(!ordres.contains(espece.espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre)){
				//On trouve toutes les espèces de cette la famille de l'espèce
				List<Espece> especesDansOrdre = espece.espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre.getEspecesDansThis();
				//Si toutes les espèces de cette famille sont sans sous-groupes
				//on ajoute l'ordre
				if(especesSansSousGroupe.containsAll(especesDansOrdre)){
					ordres.add(espece.espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre);
				}
			}
		}
		return ordres;
	}

	/**
	 * Renvoie la liste des espèces dans cet ordre
	 * @return
	 */
	public List<Espece> getEspecesDansThis(){
		return Espece.find.where()
				.eq("espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre", this)
				.orderBy("espece_systematique").findList();
	}

	/**
	 * Renvoie le numéro systématique de la première espèce dans cet ordre.
	 * Utile pour trier les ordres.
	 * @return
	 */
	public int getSystematiquePremiereEspeceDansThis(){
		Espece espece = Espece.find.where()
				.eq("espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_ordre", this)
				.setMaxRows(1).orderBy("espece_systematique").findUnique();
		if(espece==null)
			return -1;
		else
			return espece.espece_systematique;
	}
	
	/**
	* Teste si un ordre contient des espèces
	*/
	public boolean estVide(){
		List<Espece> especes = this.getEspecesDansThis();
		return (especes.isEmpty());
	}
		
	/**
	* Supprime l'ordre SEULEMENT si elle ne contient pas d'espèces, 
	* et si elle contient des super-familles vides supprime celles-ci également
	* (supprime aussi les sous-familles et familles par récursion)
	*/
	public static void supprOrdre(Integer ordre_id){
		Ordre ordre = find.byId(ordre_id);
		List<SuperFamille> superfams = SuperFamille.find.where().eq("super_famille_ordre",ordre).findList();
		for (SuperFamille superfam : superfams){
			if(superfam.estVide())
				SuperFamille.supprSuperFamille(superfam.super_famille_id);
		}
		ordre.delete();
	}

}
