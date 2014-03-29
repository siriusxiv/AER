package espece;

import java.io.FileWriter;
import java.io.IOException;

public class ErrorFile {
	int compteurDErreur = 0;
	int dateMaxError = 0;
	int communeNomError = 0;
	int especeError = 0;
	int membreError = 0;
	String errorFileName;
	FileWriter fw;

	public ErrorFile(String fileName) throws IOException{
		errorFileName=fileName;
		fw = new FileWriter(fileName);
	}

	public void write(String s) throws IOException{
		if(s.startsWith("Date max non spécifiée :"))
			dateMaxError++;
		else if(s.startsWith("Nom de commune introuvable:"))
			communeNomError++;
		String ss = s.replaceAll("µ", ";");
		System.out.println(ss);
		fw.append(ss+"\n");
		compteurDErreur++;
	}
	
	public void flush() throws IOException{
		fw.flush();
	}

	public void getStatus() {
		System.out.println(compteurDErreur+" errors, "+communeNomError+" pour des noms de communes ; "+dateMaxError+" pour la date max ; "+especeError+" pour les espèces ; "+membreError+" pour les membres");
	}

	public void erreurEspece(String line_origine) throws IOException {
		especeError++;
		String ss = "Espèce introuvable : "+line_origine.replaceAll("µ", ";");
		System.out.println(ss);
		fw.append(ss+"\n");
		compteurDErreur++;
	}

	public void erreurMembre(String string) throws IOException {
		String ss = string.replaceAll("µ", ";");
		System.out.println(ss);
		fw.append(ss+"\n");
		compteurDErreur++;
		membreError++;
	}
}
