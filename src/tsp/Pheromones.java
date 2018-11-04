package tsp;

public class Pheromones {
	
	private double alpha;	//paramètre relatif à  l'évaporation des phéromones
	private double beta;	//paramètre relatif à la visibilité des villes
	private double rho;		//taux d'évaporation des phéromones compris entre 0 et 1
	private double Lk; 		//Longueur de l'arc parcouru par la fourmi k lors dans l'intervalle de temps [t;t+n]
	private double[][] tau;	//matrice des phéromones sur les arcs
	
	public Pheromones(double a, double b, double p, double Lk, double[][] qte_pheromone, double[][] tau) {
		this.alpha = a;
		this.beta = b;
		this.rho = p;
		this.Lk = Lk;
		this.tau= tau;
	}

	public double[][] majPheromones(){
		double[][] tauMisAJour = new double[this.tau.length][];
		for (int i=0; i<this.tau.length; i++) {
			for(int j=0; j<this.tau[i].length; i++) {
				//tauMisAJour[i][j]=rho*tau[i][j]+deltaTau[i][j];
			}
		}
		return tauMisAJour;
	}
}
