package tsp;

public class Pheromones {
	
	private double alpha;
	private double beta;
	private double rho;
	//taux d'évaporation des phéromones
	private double Lk; 
	//Longueur de l'arc parcouru par la fourmi k lors dans l'intervalle de temps [t;t+n]
	private double[][] qte_pheromone;
	private double[][] qte_init;
	
	public Pheromones(double a, double b, double p, double Lk, double[][] qte_pheromone, double[][] qte_init) {
		this.alpha = a;
		this.beta = b;
		this.rho = p;
		this.Lk = Lk;
		this.qte_pheromone = qte_pheromone;
		this.qte_init = qte_init;
	}
	
	public double quantite_arc(int i, int j) {			//il faut faire en sorte que la quantité de phéromones sur l'arc (i,j) se mette à jour à chaque itération
		double qte = this.qte_init[i][j];
		//coprs du programme
		return qte;
	}
	
	public double depot() {
		return Double.NaN;
	}
}
