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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import functions.cartes.Carte;
import play.Play;

/**
 * Une classe pour gérer les fichiers excels.
 * @author malik
 *
 */
public class Excel {
	
	protected static int LIGNES = 51;
	protected static String crLf = Character.toString((char)13) + Character.toString((char)10);
	
	public Workbook wb = new HSSFWorkbook();
	private String file_name; 
	
	public Excel(){
		SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		file_name=date_format.format(Calendar.getInstance().getTime())+".xls";
	}
	
	public void writeToDisk() throws IOException{
		FileOutputStream fileOut = new FileOutputStream(Play.application().configuration().getString("xls_generes.path")+file_name);
		wb.write(fileOut);
		fileOut.close();
	}
	
	public String getFileName(){
		return file_name;
	}
	
	public void collerLogo(int page) throws IOException{
		InputStream is = new FileInputStream("public/images/banniere-aer.png");
	    byte[] bytes = IOUtils.toByteArray(is);
	    int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
	    is.close();
	    CreationHelper helper = wb.getCreationHelper();
	    Sheet sheet = wb.getSheetAt(0);
	    // Create the drawing patriarch.  This is the top level container for all shapes. 
	    Drawing drawing = sheet.createDrawingPatriarch();
	    //add a picture shape
	    ClientAnchor anchor = helper.createClientAnchor();
	    //set top-left corner of the picture,
	    //subsequent call of Picture#resize() will operate relative to it
	    anchor.setCol1(0);
	    anchor.setRow1(LIGNES*page);
	    Picture pict = drawing.createPicture(anchor, pictureIdx);
	    //auto-size picture relative to its top-left corner
	    pict.resize();
	}
	
	public void pasteMap(Carte carte, int page) throws IOException{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(carte.getImage(), "png", os);
	    byte[] bytes = os.toByteArray();
	    int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
	    CreationHelper helper = wb.getCreationHelper();
	    Sheet sheet = wb.getSheetAt(0);
	    // Create the drawing patriarch.  This is the top level container for all shapes. 
	    Drawing drawing = sheet.createDrawingPatriarch();
	    //add a picture shape
	    ClientAnchor anchor = helper.createClientAnchor();
	    //set top-left corner of the picture,
	    //subsequent call of Picture#resize() will operate relative to it
	    anchor.setCol1(4);
	    anchor.setRow1(LIGNES*page+10);
	    Picture pict = drawing.createPicture(anchor, pictureIdx);
	    //auto-size picture relative to its top-left corner
	    pict.resize();
	}
}
