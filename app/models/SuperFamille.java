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

import javax.naming.NamingException;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

/**
 * Il existe des familles qui ne possèdent pas de superfamilles.
 * Afin de garder la base de données consistante, si un telle famille
 * existe, alors sa super famille (dans la base de données) possède le
 * même nom avec cependant le booléen super_famille_existe valant FAUX.
 * @author malik
 *
 */
@SuppressWarnings("serial")
@Entity
public class SuperFamille extends Model implements Comparator<SuperFamille>{
	@Id
	public Integer super_famille_id;
	@NotNull
	public String super_famille_nom;
	@NotNull
	public boolean super_famille_existe;
	@ManyToOne
	@NotNull
	public Ordre super_famille_ordre;

	public static Finder<Integer,SuperFamille> find = new Finder<Integer,SuperFamille>(Integer.class, SuperFamille.class);

	@Override
	public String toString(){
		return super_famille_nom;
	}

	/**
	 * Pour trier les listes de super-familles selon la systématique de la première espèce dedans.
	 */
	@Override
	public int compare(SuperFamille sf1, SuperFamille sf2) {
		int sys1 = sf1.getSystematiquePremiereEspeceDansThis();
		int sys2 = sf2.getSystematiquePremiereEspeceDansThis();
		return (sys1<sys2 ? -1 : (sys1==sys2 ? 0 : 1));
	}
	
	public static List<SuperFamille> findSuperFamillesExistantes() {
		return find.where().eq("super_famille_existe", true).findList();
	}
	
	/**
	 * Crée une super-famille et lance une PersistenceException si plusieurs ordres portent le même nom,
	 * une NamingException si l'ordre en argument n'existe pas.
	 * @param nom
	 * @param existe
	 * @param superFamilleOuOrdreId
	 * @throws PersistenceException
	 * @throws NamingException
	 */
	public SuperFamille(String nom, boolean existe, Integer superFamilleOuOrdreId) throws PersistenceException, NamingException{
		super_famille_nom=nom;
		super_famille_existe=existe;
		super_famille_ordre=Ordre.find.byId(superFamilleOuOrdreId);
		if(super_famille_ordre==null){
			throw new NamingException("L'ordre "+superFamilleOuOrdreId+" n'existe pas !");
		}
	}
	
	/** Ajoute la sous-famille à la base de données
	* @param nom
	 * @param existe
	 * @param sousFamilleOuFamilleId
	 * @throws PersistenceException
	 * @throws NamingException
	 */
	public static void ajouterSuperFamille(String nom, boolean existe, Integer ordreId) throws NamingException, PersistenceException{
		SuperFamille superfam = new SuperFamille(nom, existe, ordreId);
		Ordre ordre = Ordre.find.byId(ordreId);
		if(ordre==null){
			throw new NamingException("L'ordre "+ordreId+" n'exsite pas!");
		} else {
		superfam.save();
		}
	}
	
	/**
	 * Trie les super-familles
	 * @return
	 */
	public static List<SuperFamille> findSuperFamillesExistantesTriees() {
		List<SuperFamille> superfamilles = find.where().eq("super_famille_existe", true).findList();
		Collections.sort(superfamilles, new SuperFamille());
		return superfamilles;
	}
	private SuperFamille() {}

	/**
	 * Trouve les super-familles que l'on peut ajouter dans un sous-groupe
	 * @return
	 */
	public static List<SuperFamille> findSuperFamillesAjoutablesDansSousGroupe(){
		List<Espece> especesSansSousGroupe = Espece.findEspecesAjoutablesDansSousGroupe();
		List<SuperFamille> super_familles = new ArrayList<SuperFamille>();
		for(Espece espece : especesSansSousGroupe){
			if(!super_familles.contains(espece.espece_sousfamille.sous_famille_famille.famille_super_famille)){
				if(espece.espece_sousfamille.sous_famille_famille.famille_super_famille.super_famille_existe){
					//On trouve toutes les espèces de cette la super-famille de l'espèce
					List<Espece> especesDansSuperFamille = espece.espece_sousfamille.sous_famille_famille.famille_super_famille.getEspecesDansThis();
					//Si toutes les espèces de cette famille sont sans sous-groupes
					//on ajoute la famille
					if(especesSansSousGroupe.containsAll(especesDansSuperFamille)){
						super_familles.add(espece.espece_sousfamille.sous_famille_famille.famille_super_famille);
					}
				}
			}
		}
		return super_familles;
	}
	
	/**
	 * Renvoie la liste des espèces dans cette super-famille
	 * @return
	 */
	public List<Espece> getEspecesDansThis(){
		return Espece.find.where()
				.eq("espece_sousfamille.sous_famille_famille.famille_super_famille", this)
				.orderBy("espece_systematique").findList();
	}
	
	/**
	 * Renvoie le numéro systématique de la première espèce dans cette super-famille.
	 * Utile pour trier les super-familles.
	 * @return
	 */
	public int getSystematiquePremiereEspeceDansThis(){
		Espece espece = Espece.find.where()
				.eq("espece_sousfamille.sous_famille_famille.famille_super_famille", this)
				.setMaxRows(1).orderBy("espece_systematique").findUnique();
		if(espece==null)
			return -1;
		else
			return espece.espece_systematique;
	}
}
