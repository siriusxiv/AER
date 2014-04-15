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

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Une classe pour gérer les fichiers excels.
 * @author malik
 *
 */
public class Excel {
	Workbook wb = new HSSFWorkbook();
	private String file_name; 
	
	public Excel(){
		SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		file_name=date_format.format(Calendar.getInstance().getTime())+".xls";
	}
	
	public void writeToDisk() throws IOException{
		FileOutputStream fileOut = new FileOutputStream(file_name);
		wb.write(fileOut);
		fileOut.close();
	}
	
	public String getFileName(){
		return file_name;
	}
}
