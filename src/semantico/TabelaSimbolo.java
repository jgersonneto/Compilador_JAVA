package semantico;

import java.util.HashMap;

public class TabelaSimbolo {
	
	private HashMap<String, Simbolo> map;
	
	public TabelaSimbolo() {
		map = new HashMap<String, Simbolo>();
	}
	
	public void add(Simbolo simbolo) {
		map.put(simbolo.getNome(), simbolo);		
	}
	
	public boolean existe(String nome) {
		return map.get(nome) != null;
	}
	
	public Simbolo getSimbolo(String nome) {
		return map.get(nome);
	}

}
