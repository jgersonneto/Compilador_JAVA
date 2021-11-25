package arvoreGeraCodigo;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import semantico.TabelaSimbolo;

public class ProgGeradorCod {
	
	private TabelaSimbolo vartable;
	private ArrayList<AbstractComando> comandos;
	private String nomeProg;
	
	public void generateTarget() {
		StringBuilder str = new StringBuilder();
		str.append("BEGIN \n");
		for(AbstractComando comando : this.comandos) {
			str.append("	" + comando.gerarCodInterm()+"\n");
		}
		str.append("END");
		
		try {
			FileWriter fr = new FileWriter(new File("C:\\Users\\Neto\\eclipse-workspace\\Compilador\\src\\CodIntermediario.net"));
			fr.write(str.toString());
			fr.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public TabelaSimbolo getVartable() {
		return vartable;
	}

	public void setVartable(TabelaSimbolo vartable) {
		this.vartable = vartable;
	}

	public ArrayList<AbstractComando> getComandos() {
		return comandos;
	}

	public void setComandos(ArrayList<AbstractComando> comandos) {
		this.comandos = comandos;
	}

	public String getNomeProg() {
		return nomeProg;
	}

	public void setNomeProg(String nomeProg) {
		this.nomeProg = nomeProg;
	}

}
