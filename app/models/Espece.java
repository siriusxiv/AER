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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
}
