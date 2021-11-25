package semantico;

public abstract class Simbolo {
	
	protected String nome;
	
	public Simbolo(String nome) {
		this.nome = nome;		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Simbolo [nome=" + nome + "]";
	}

}
