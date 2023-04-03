package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> allEsami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.allEsami = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		migliore = new HashSet<>();  // lo inizializzo qua per essere sicuro che sia vuota, è temporanea, esiste solo quando chiamo il metodo
		mediaMigliore = 0.0;
		
		Set<Esame> parziale = new HashSet<>();
		
		cercaMeglio(parziale, 0, numeroCrediti);
		
		return migliore;	
	}

	private void cercaMeglio(Set<Esame> parziale, int L, int numeroCrediti) {
		
		int sommaCrediti = sommaCrediti(parziale);
		
		if(sommaCrediti > numeroCrediti) {
			return;
		}
		if(sommaCrediti == numeroCrediti) {   //qui potrei avere una soluzione
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				mediaMigliore = mediaVoti;
				migliore = new HashSet<>(parziale);
			}
			return;
		}
		
		if(L == allEsami.size()) {
			return;
		}
		
		// provo ad aggiungere il prossimo elemento
		// L = 0 {e1} / { }
		// L = 1 {e1, e2} - {e1} / {e2} - { }
		parziale.add(allEsami.get(L));
		cercaMeglio(parziale, L+1, numeroCrediti);
		parziale.remove(allEsami.get(L));
		
		// provo a non aggiungere l'elemento
		cercaMeglio(parziale, L+1, numeroCrediti);
		
	}
	
	private void cerca(Set<Esame> parziale, int L, int numeroCrediti) {
		
		int sommaCrediti = sommaCrediti(parziale);
		
		if(sommaCrediti > numeroCrediti) {
			return;
		}
		if(sommaCrediti == numeroCrediti) {   //qui potrei avere una soluzione
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				mediaMigliore = mediaVoti;
				migliore = new HashSet<>(parziale);
			}
			return;
		}
		
		if(L == allEsami.size()) {
			return;
		}
		
		for(Esame e : allEsami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale, L+1, numeroCrediti);
				parziale.remove(e);
			}
		}
		
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
