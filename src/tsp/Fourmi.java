package tsp;

public class Fourmi {
	
	private int[] memoire ;		//liste  memoire associee a  une fourmi
	private int[] villesAVisiter ; 	//liste des villes a  visiter a  l'instant t
	private Instance m_instance;
	
	//parametres en lien avec les pheromones
	private double alpha;	//parametre relatif Ã   l'evaporation des pheromones
	private double beta;	//parametre relatif Ã  la visibilite des villes
	private double rho;		//taux d'evaporation des pheromones compris entre 0 et 1
	private double Lk; 		//Longueur de l'arc parcouru par la fourmi k lors dans l'intervalle de temps [t;t+n]
	private double[][] tau;	//matrice des pheromones sur les arcs
	
	public Fourmi(int[] memoire, int[] aVisiter) { //constructeur
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
	
	public double probabilite(int i, int j) throws Exception { //probabilite de choisir la ville j en partant de la ville i : 
		double p = 0;
		if ( this.villesAVisiter.length == 1) {
			//si la fourmi a visite toutes les villes sauf la ville de depart :
			p = 1;
		}
		else if (this.memoire.length == 0) { 	
			//si la fourmi a deja  fait un tour complet :
			p=0;
		} else {		
			// s'il reste des villes a visiter :
			// on constrit la somme des produits tau*eta pour l'ensemble des villes restantes (i.e. produit de (qte de pheromones)*(visibilite) pour l'arc (i,l)) :
			double tau_eta = 0;
			for (int l : this.villesAVisiter) {
				tau_eta += Math.pow(tau[i][l], alpha)*Math.pow(visibilite(i, l), beta);
			}
			// on applique la formule de la probabilitÃ© du choix de la ville j :
			p = ( Math.pow(tau[i][j], alpha)*Math.pow(visibilite(i, j),beta) )/tau_eta;
		}
		return p;
	}
	
	public int villeSuivante(int villeDeDepart) throws Exception { // la fourmi choisit la ville de probabilite la plus elevee :
		int size = this.memoire.length;
		if (this.villesAVisiter.length >= 1) { //si elle n'a pas encore visite toutes les villes :
			int[] copieMemoire = new int[size+1];	// on cherche a  mettre a  jour la memoire de la fourmi : on va ajouter une case pour y entrer la ville suivante
			for (int m=0; m<size; m++) {
				copieMemoire[m] = this.memoire[m];
			}
			if ( this.villesAVisiter.length == 1) {	//1er cas : s'il ne reste a  la fourmi qu'a  revenir a  la ville de depart :
				copieMemoire[size]=0;
			} else if ( this.villesAVisiter.length > 1 ) { //s'il reste plus qu'une ville a  visiter : 
					double p=0;	
					int index=0;
					//on va comparer les probabilites
					for (int v : this.villesAVisiter) {
						if (probabilite(villeDeDepart,v)>p) {
							p=probabilite(villeDeDepart,v);
							index=v;
						} else if (probabilite(villeDeDepart,v)==p) { 
							// cas ou deux villes possedent une meme probabilite, on choisit celle ou la quantite de pheromones est la plus grande
							
						}
					}
					copieMemoire[size]=index;
			}
			this.memoire=copieMemoire;
		}
		return this.memoire[size];	//on retourne la ville suivante et la liste "memoire" est actualisee
	}
	
	public double distance_totale (int position_init) throws Exception {
		int i = 0;
		double d_tot = 0;
		while (i<this.memoire.length) {
			d_tot += distance(this.memoire[i], this.memoire[i+1]);
			i++;
		}
		return d_tot;
	}
}
