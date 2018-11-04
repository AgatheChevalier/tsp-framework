package tsp;

public class Fourmi {
	
	/*private int pos_depart;		
	//position initiale de la fourmi k
	private int position; 		
	//position à l'instant t
	private int[] memoire;		
	//liste des villes visitées par la fourmi
	private double[][] qte_pheromone;	
	//quantité de phéromones laissé par la fourmi sur chaque arc
	
	*/
	private int[] memoire ;		//liste  mémoire associée à une fourmi
	private int[] villesAVisiter ; 	//liste des villes à visiter à l'instant t
	private Instance m_instance;
	
	//paramètres en lien avec les phéromones
	private double alpha;	//paramètre relatif à  l'évaporation des phéromones
	private double beta;	//paramètre relatif à la visibilité des villes
	private double rho;		//taux d'évaporation des phéromones compris entre 0 et 1
	private double Lk; 		//Longueur de l'arc parcouru par la fourmi k lors dans l'intervalle de temps [t;t+n]
	private double[][] tau;	//matrice des phéromones sur les arcs
	
	public Fourmi(int[] memoire, int[] aVisiter) {
		super();
		this.memoire = memoire;
		this.villesAVisiter = aVisiter;
	}
	
	public double distance (int i, int j) throws Exception {	//distance entre 2 villes i et j
		return  m_instance.getDistances(i, j);
	}
	
	public double visibilite (int i, int j) throws Exception {	//visibilite entre 2 villes i et j
		return 1/distance(i, j);
	}
	
	public double probabilite(int i, int j) throws Exception { //probabilité de choisir la ville j en partant de la ville i : 
		double p = 0;
		if ( this.villesAVisiter.length == 1) {
			//si la fourmi a visité toutes les villes sauf la ville de départ :
			p = 1;
		}
		else if (this.memoire.length == 0) { 	
			//si la fourmi a déjà fait un tour complet :
			p=0;
		} else {		
			// s'il reste des villes à visiter :
			// on constrit la somme des produits tau*eta pour l'ensemble des villes restantes (i.e. produit de (qté de phéromones)*(visibilité) pour l'arc (i,l)) :
			double tau_eta = 0;
			for (int l : villesAVisiter) {
				tau_eta += Math.pow(tau[i][l], alpha)*Math.pow(visibilite(i, l), beta);
			}
			// on applique la formule de la probabilité du choix de la ville j :
			p = ( Math.pow(tau[i][j], alpha)*Math.pow(visibilite(i, j),beta) )/tau_eta;
		}
		return p;
	}
	
	public int villeSuivante(int villeDeDepart) throws Exception { // la fourmi choisit la ville de probabilité la plus élevée :
		int size = memoire.length;
		if (this.villesAVisiter.length >= 1) { //si elle n'a pas encore visité toutes les villes :
			int[] copieMemoire = new int[size+1];	// on cherche à mettre à jour la mémoire de la fourmi : on va ajouter une case pour y entrer la ville suivante
			for (int m=0; m<size; m++) {
				copieMemoire[m] = memoire[m];
			}
			if ( this.villesAVisiter.length == 1) {	//1er cas : s'il ne reste à la fourmi qu'à revenir à la ville de départ :
				memoire[size]=0;
			} else if ( this.villesAVisiter.length > 1 ) { //s'il reste plus qu'une ville à visiter : 
					double p=0;	
					int index=0;
					//on va comparer les probabilités
					for (int l : villesAVisiter) {
						if (probabilite(villeDeDepart,l)>p) {
							p=probabilite(villeDeDepart,l);
							index=l;
						} else if (probabilite(villeDeDepart,l)==p) { 
							// cas où deux villes possèdent une même probabilité, on choisit celle ou la quantite de pheromones est la plus grande
							
						}
					}
					memoire[size]=index;
			}
		}
		return memoire[size];	//on retourne la ville suivante et la liste "memoire" est actualisée
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

}
