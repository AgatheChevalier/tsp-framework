package tsp;

public class Pheromones {
	
	private double rho;			//taux d'evaporation des pheromones compris entre 0 et 1
	private double[][] tau;		//matrice des pheromones sur les arcs
	private Fourmi[] fourmis; 	//liste de fourmis
	
	/** 
	 * Contructeur de la classe Pheromones.
	 * 
	 * @param tau La matrice des différentes phéromones disposées sur les arcs 
	 */
	public Pheromones(double[][] tau) {
		this.tau= tau;
	}
	
	/**
	 * Initialise la matrices de phéromones. 
	 * 
	 * @return La matrice de phéromones initialisée
	 * @throws Exception
	 */
	public double[][] depotPheromones() throws Exception{
		double[][] deltaTau = new double[this.tau.length][];	//matrice des quantites de pheromones deposes par l'ensemble des fourmis sur chaque arc
		for (int i=0; i<this.tau.length; i++) {		
			for(int j=0; j<this.tau[i].length; i++) {
				for (Fourmi f : this.fourmis) {
					//deltaTau_i_j = Somme(deltaTau_i_j_k), i.e. Somme de toutes les quantites deposees par chaque fourmi sur l'arc (i,j)
					if (f.villeSuivante(i)==j || f.villeSuivante(j)==i) { //ssi la fourmi k est passee par l'arc (i,j)
						deltaTau[i][j]=+1/(f.distance_totale(0));	//sur chaque arc, une fourmi depose un quantite 1/(distance totale d'un cycle)
					} 
				}
			}
		}
		return deltaTau;
	}
	
	
	/**
	 * Méthode de mise à jour de la matrice des phéronomnes.
	 * 
	 * @return La matrice des phéromones mise à jour
	 * @throws Exception
	 */
	public double[][] majPheromones() throws Exception{
		double[][] deltaTau = this.depotPheromones();
		for (int i=0; i<this.tau.length; i++) {
			for(int j=0; j<this.tau[i].length; i++) {
				this.tau[i][j]=(1-rho)*tau[i][j]+deltaTau[i][j];
			}
		}
		return this.tau;
	}
}
