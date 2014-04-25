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
package functions.excels;

import java.util.Calendar;

import org.apache.poi.ss.usermodel.Row;

public class RowCheckEdit {
	private Row row;
	private int rowNumber;
	private StringBuilder errorReport;
	private boolean noError;
	private Calendar creationTime;
	/**
	 * Instancie la classe pour charger les informations dans une Row
	 * @param row
	 * @param rowNumber
	 * @param errorReport
	 */
	public RowCheckEdit(Row row, int rowNumber, StringBuilder errorReport, Calendar creationTime){
		this.row = row;
		this.rowNumber=rowNumber;
		this.errorReport=errorReport;
		this.creationTime=creationTime;
	}
	
	/**
	 * Vérifie que la ligne donnée est juste.
	 */
	public void checkRow(){
		
	}
	
	/**
	 * Ajoute une erreur dans la liste des erreurs
	 * @param s
	 */
	public void addError(String s){
		noError=false;
		errorReport.append("Ligne "+(rowNumber+1)+": ");
		errorReport.append(s+"<br>");
	}

	public boolean noError(){
		return noError;
	}
	public String getErrors(){
		return errorReport.toString();
	}

	/**
	 * Sauvegarde la Row dans la base de données.
	 */
	public void saveToDatabase() {
	}
}
