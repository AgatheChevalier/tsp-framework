package tsp;

public class Fourmi {
	
	private int pos_depart;		//position initiale de la fourmi k
	private int[] memoire;		//liste des villes visitées par la fourmi
	private double[][] qte_pheromone;	//quantité de phéromones laissé par la fourmi sur chaque arc
	private Instance m_instance;
	
	public Fourmi(Instance instance, int position_init, int[] villes, double[][] pheromones) {
		this.m_instance = instance;
		this.pos_depart = position_init;
		this.memoire = villes;
		this.qte_pheromone = pheromones;
	}
	
	public long distance (int ville_1, int ville_2) throws Exception {	//distance entre 2 villes
		return  m_instance.getDistances(ville_1, ville_2);
	}
	
	public long distance_totale (int position_init) throws Exception {
		int i = 0;
		long d_tot = 0;
		while (i<this.memoire.length) {
			d_tot += d_tot + distance(memoire[i], memoire[i+1]);
			i++;
		}
		if (i==this.memoire.length) {
			d_tot += d_tot + distance(memoire[i], memoire[0]);
		}
		return d_tot;
	}
	
	public void depot_pheromones() {
		
	}
	
	public void choixDeVilleSuivante () {
		// probabilité de choisir une ville proportionnelle à la quantité de phéromones présents sur le chemin qui mène à cette ville
	}
	
	public double visibilite (int villeA, int villeB) throws Exception {
		return 1/distance(villeA, villeB);
	}

}
