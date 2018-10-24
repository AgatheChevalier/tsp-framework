package tsp;

public class Fourmi extends Pheromones{
	
	private int pos_depart;		
	//position initiale de la fourmi k
	private int position; 		
	//position à l'instant t
	private int[] memoire;		
	//liste des villes visitées par la fourmi
	private double[][] qte_pheromone;	
	//quantité de phéromones laissé par la fourmi sur chaque arc
	
	private Instance m_instance;
	
	
	public Fourmi(Instance instance, int position_init, int position, int[] villes, double[][] pheromones,double a, double b, double p, double Lk, double[][] qte_pheromone, double[][] qte_init) {
		super(a,b,p,Lk,qte_pheromone,qte_init);
		this.m_instance = instance;
		this.pos_depart = position_init;
		this.position = position;
		this.memoire = villes;
		this.qte_pheromone = pheromones;
	}
	
	public long distance (int i, int j) throws Exception {	//distance entre 2 villes
		return  m_instance.getDistances(i, j);
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
		//à remplir
	}
	
	public double probabilite(int i, int j) {
		double p = 0;
		if (this.position == this.memoire[this.memoire.length-1]) {
			//si la fourmi a visité toutes les villes sauf la ville de départ :
			p = 1;
		}
		else if (this.position == this.memoire[this.memoire.length]) { 
			//si la fourmi a fait un tour complet :
			p=0;
		} else {
			
		}
		return p;
	}
	
	public void choixDeVilleSuivante () {
		// probabilité de choisir une ville proportionnelle à 
		//la quantité de phéromones présents sur le chemin qui mène à cette ville
	}
	
	public double visibilite (int villeA, int villeB) throws Exception {
		return 1/distance(villeA, villeB);
	}

}
