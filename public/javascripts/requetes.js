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
function Donnees(listeUTMS,listeTemoins) {
	this.groupe = $('#groupe').val();
	this.sous_groupe = $('#sous-groupe').val();
	this.espece = $('#espece').val();
	this.stade = $('#stade').val();
	this.maille = $('#maille').val();
	this.temoin = $('#temoin').val();
	this.jour1 = $('#jour1').val();
	this.mois1 = $('#mois1').val();
	this.annee1 = $('#annee1').val();
	this.jour2 = $('#jour2').val();
	this.mois2 = $('#mois2').val();
	this.annee2 = $('#annee2').val();
	this.getInfo = function() {
		return this.groupe+' '+this.sous_groupe+' '+this.espece+' '+this.stade+' '+this.maille+' '+this.temoin+' '+this.jour1+'/'+this.mois1+'/'+this.annee1+':'+this.jour2+'/'+this.mois2+'/'+this.annee2;
	};
	this.temoinValide = function(){
		return $.inArray(this.temoin, listeMembres)>=0 || this.temoin=='';
	};
	this.datesValides = function(){
		return estEntier(this.jour1) && estEntier(this.jour2) && estEntier(this.mois1) && estEntier(this.mois2) && estEntier(this.annee1) && estEntier(this.annee2);
	};
	this.getFormData = function(){
		var formData = new FormData();
		formData.append('groupe',this.groupe);
		formData.append('sous_groupe',this.sous_groupe);
		formData.append('espece',this.espece);
		formData.append('stade',this.stade);
		formData.append('maille',this.maille);
		formData.append('temoin',this.temoin);
		formData.append('jour1',this.jour1);
		formData.append('jour2',this.jour2);
		formData.append('mois1',this.mois1);
		formData.append('mois2',this.mois2);
		formData.append('annee1',this.annee1);
		formData.append('annee2',this.annee2);
		return formData;
	};

	function estEntier(val){
		return !isNaN(val) && parseInt(val)==val;
	}
}
function temoinsParPeriode(){
	var donnees = new Donnees();
	if(isValide(donnees)){
		$('#message').html('');
		patientez();
		$.ajax({
			type : 'POST',
			url : '/ajax/temoinsParPeriode',
			data: donnees.getFormData(),
			processData: false,
			contentType: false,
			success: function (res) {
				$('#resultats').html(res);
			},
			error: function(){
				rapportDErreur();
			}
		});
	}
}
function histogrammeDesImagos(){
	var donnees = new Donnees();
	if(isValide(donnees)){
		$('#message').html('');
		patientez();
		$.ajax({
			type : 'POST',
			url : '/ajax/histogrammeDesImagos',
			data: donnees.getFormData(),
			processData: false,
			contentType: false,
			success: function (res) {
				$('#resultats').html(res);
			},
			error: function(){
				rapportDErreur();
			}
		});
	}
}
function carteSomme(){
	var donnees = new Donnees();
	if(isValide(donnees)){
		$('#message').html('');
		patientez();
		$.ajax({
			type : 'POST',
			url : '/ajax/carteSomme',
			data: donnees.getFormData(),
			processData: false,
			contentType: false,
			success: function (res) {
				$('#resultats').html(res);
			},
			error: function(){
				rapportDErreur();
			}
		});
	}
}
function carteBiodiversite(){
	var donnees = new Donnees();
	if(isValide(donnees)){
		$('#message').html('');
		patientez();
		$.ajax({
			type : 'POST',
			url : '/ajax/carteBiodiversite',
			data: donnees.getFormData(),
			processData: false,
			contentType: false,
			success: function (res) {
				$('#resultats').html(res);
			},
			error: function(){
				rapportDErreur();
			}
		});
	}
}
function carteSommeBiodiversite(){
	var donnees = new Donnees();
	if(isValide(donnees)){
		$('#message').html('');
		patientez();
		$.ajax({
			type : 'POST',
			url : '/ajax/carteSommeBiodiversite',
			data: donnees.getFormData(),
			processData: false,
			contentType: false,
			success: function (res) {
				$('#resultats').html(res);
			},
			error: function(){
				rapportDErreur();
			}
		});
	}
}
function chronologieDUnTemoin(){
	var donnees = new Donnees();
	if(mailleValide(donnees) && donnees.temoinValide() && donnees.temoin!='' && donnees.datesValides()){
		$('#message').html('');
		patientez();
		$.ajax({
			type : 'POST',
			url : '/ajax/chronologieDUnTemoin',
			data: donnees.getFormData(),
			processData: false,
			contentType: false,
			success: function (res) {
				$('#resultats').html(res);
			},
			error: function(){
				rapportDErreur();
			}
		});
	}else{
		$('#message').html('La maille est invalide, ou bien le témoin n\'existe pas.');
	}
}
function mailleValide(donnees){
	return $.inArray(donnees.maille, listeUTMS)>=0 || donnees.maille=='';
}
function isValide(donnees){
	if(!mailleValide(donnees)){
		$('#message').html('Maille entrée non valide !');
		return false;
	}else if(!donnees.temoinValide()){
		$('#message').html('Témoin entré non référencé !');
		return false;
	}else if(!donnees.datesValides()){
		$('#message').html('Les dates données ne sont pas valides !');
		return false;
	}
	return true;
}

function patientez(){
	$('#resultats').html('Calcul en cours... Veuillez patientiez');
}
function rapportDErreur(){
	$('#resultats').html('Un problème est survenu lors du calcul côté serveur...');
}