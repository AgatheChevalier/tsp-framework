package tsp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

// TODO: Auto-generated Javadoc
/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * The TSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following TSPSolver attributes: 
 * 	- #m_instance :  the Instance object which contains the problem data
 * 	- #m_solution : the Solution object to modify. This object will store the result of the program.
 * 	- #m_timeLimit : the maximum time limit (in seconds) given to the program.
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 * 
 */
public class TSPSolver {

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------

	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;

	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Creates an object of the class Solution for the problem data loaded in Instance.
	 *
	 * @param instance the instance of the problem
	 * @param timeLimit the time limit in seconds
	 */
	public TSPSolver(Instance instance, long timeLimit) {
		m_instance = instance;
		m_solution = new Solution(m_instance);
		m_timeLimit = timeLimit;
	}

	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/**
	 * **TODO** Modify this method to solve the problem.
	 * Do not print text on the standard output (eg. using `System.out.print()` or `System.out.println()`).
	 * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
	 * You can print using the error output (`System.err.print()` or `System.err.println()`).
	 * When your algorithm terminates, make sure the attribute #m_solution in this class points to the solution you want to return.
	 * You have to make sure that your algorithm does not take more time than the time limit #m_timeLimit.
	 * @throws Exception may return some error, in particular if some vertices index are wrong.
	 */

	public void solve() throws Exception
	{
		localSearchPPV();
		m_solution.print(System.out);
		GeneticAlgorithm();
		/**DeuxOpt();
		DeuxOpt();
		DeuxOpt();
		DeuxOpt();
		DeuxOpt();
		DeuxOpt();
		DeuxOpt();
		DeuxOpt();/*
		/*double anciencout=m_solution.evaluate();
		DeuxOpt();
		while(m_solution.evaluate()<anciencout) {
			anciencout=m_solution.evaluate();
			DeuxOpt();
		}

		Solution newm_solution=m_solution.copy();
		int nbVilles=m_instance.getNbCities();
		Random random=new Random();
		int start = 1 + random.nextInt((nbVilles/2)-1); 
		int end = (nbVilles/2) + random.nextInt((nbVilles-1) - (nbVilles/2));
		swap(start,end,newm_solution);
		m_solution=newm_solution;
		DeuxOpt();
		DeuxOpt();		DeuxOpt();
		DeuxOpt();
		DeuxOpt();
		DeuxOpt();
		DeuxOpt();
		DeuxOpt();
*/
		//System.out.println(generateSolutionRandom().isFeasible());
	
		//Solution sol = generateSolutionRandom();
		//for (int i=0; i<m_instance.getNbCities(); i++) {
		//	System.out.println(sol.contains(i));
		//}
	}
	
	// -----------------------------------
	// -- ALGORITHME PLUS PROCHE VOISIN --
	// -----------------------------------	
		
	/**
	 *  Première méthode que nous avons développée: un Local Search qui cherche de ville en ville la plus proche voisine.
	 * Appelle la méthode plusProcheVoisin(int ville_courante, boolean[] villes).
	 *
	 * @throws Exception
	 */
	public Solution localSearchPPV() throws Exception {
		Solution unePetiteSolutionDeDepart = new Solution(m_instance);
		//m_solution.print(System.err);
		long t = System.currentTimeMillis();
		long tempspasse = 0;		
		boolean[] villes = new boolean[m_instance.getNbCities()];
		int ville_courante = 0;	
		villes[ville_courante]=true;
		m_solution.setCityPosition(ville_courante, 0);
		int compteur=1;
		/* Tant que l'on a pas traité le cas de chaque ville on continue à chercher les plus proches voisins */
		while(compteur<m_instance.getNbCities() && tempspasse<m_timeLimit) {
			int ville_proche = plusProcheVoisin(ville_courante,villes);
			villes[ville_proche]=true;
			m_solution.setCityPosition(ville_proche, compteur);
			ville_courante=ville_proche;
			compteur++;
			/* On teste le temps depuis lequel le programme tourne */
			tempspasse=System.currentTimeMillis()-t;
		} 
		/* On relie la première ville avec dernière */
		m_solution.setCityPosition(0,m_instance.getNbCities());
		unePetiteSolutionDeDepart = m_solution;
		unePetiteSolutionDeDepart.evaluate();
		return unePetiteSolutionDeDepart;
	}
	

	/**
	 *  Cette méthode cherche le Plus Proche Voisin non visité de ville_courante contenu dans tableau_villes.
	 *
	 * @param ville_courante Un entier correspondant à l'indice de la ville dont on cherche le PPV.
	 * @param tableau_villes Un tableau de booléens de longueur m_instance.getNbCities().
	 * Si la ville i a déjà été visitée, ville[i]=true. Sinon, ville[i]=false.
	 * @return Un entier correspondant à l'indice de la ville la plus proche de ville_courante non visitée.
	 * @throws Exception
	 */
	public int plusProcheVoisin(int ville_courante, boolean[] tableau_villes) throws Exception {
		int index = ville_courante;
		/* Cette boucle cherche la ville la proche de ville_courante EN INDICE qui n'a pas été visitée
		 * Le but est de trouver une référence pour le minimum de distance entre ville_courante et une autre ville
		 * On stocke cees références de distance et d'indice dans minimum et ville_proche */
		while(tableau_villes[index] == true) {
			index = (index+1)%tableau_villes.length;
		}
		long minimum = m_instance.getDistances(ville_courante, index);
		int ville_proche = index;
		/* Cette boucle va tester si dans le reste des villes non visitées, on va trouver une ville plus proche que la référence
		 * Si c'est le cas, on remplace minimum et ville_proche par la distance et l'indice de la ville concernée */
		for(int i=0; i<tableau_villes.length; i++) {
			if(tableau_villes[i]==false && m_instance.getDistances(ville_courante, i)<minimum) {
				minimum = m_instance.getDistances(ville_courante, i);
				ville_proche = i;
			}
		}
		/* On a trouvé la ville plus proche voisine non visitée de ville_courante 
		 * On renvoie son indice */
		return ville_proche;
	}
	
	
	/**
	 *  Cette méthode calcule le coût d'un trajet en appelant la méthode getTour() de la classe Solution. 
	 *
	 * @return Le coût de la solution concernée par l'appel de la méthode
	 * @throws Exception
	 */
	public int getCout() throws Exception {
		m_solution.print(System.err);
		int res = 0;
		for(int i=0; i<m_solution.getTour().length-1; i++) {
			res = res + (int)(m_instance.getDistances(m_solution.getTour()[i], m_solution.getTour()[i+1]));
		}
		res = res+(int)(m_instance.getDistances(m_solution.getTour()[m_solution.getTour().length-1], m_solution.getTour()[0]));
		return res;
	}	
	
	// -----------------------------
	// ----- ALGORITHME 2-OPT ------
	// -----------------------------
	
	/**
	 *  La méthode principale qui déroule l'algorithme 2-opt; codée pour améliorer les résultats du PPV.
	 * Appelle la méthode swap(int i, int k, Solution newm_solution). 
	 *
	 * @throws Exception
	 */
	public void DeuxOpt() throws Exception {
		m_solution.print(System.err);
		long t = System.currentTimeMillis();
		long tempspasse = 0;
		int size = m_instance.getNbCities();   
		int amelioration = 0;
		while ( amelioration<100 && tempspasse<m_timeLimit) {
		        double meilleure_distance = m_solution.getCout();   
		        for ( int i = 1; i < size - 1; i++ ) {
		            for ( int k = i + 1; k < size; k++) {
		            	Solution newm_solution=m_solution.copy();
		                swap( i, k, newm_solution );		               
		                double nouvelle_distance = newm_solution.getCout();		 
		                if ( nouvelle_distance < meilleure_distance ) {
		                	// on a amélioré le trajet donc on remet la variable à 0
		                    amelioration = 0;		                                                 
		                    for (int j=0;j<size;j++) {
		                        m_solution.setCityPosition(newm_solution.getCity(j), j);
		                    }		                         
		                    meilleure_distance = nouvelle_distance;	                   
		                }		         
		           }
		        }		 
		        amelioration ++;
		        tempspasse = System.currentTimeMillis()-t;
		}	 
	}
	

		
	

	/**
	 * Méthode qui échange deux villes dans une solution selon le schéma que l'on a choisi. 
	 * Ici, on va non seulement échanger les villes i et k mais aussi inverser l'ordre des villes entre les deux indices.
	 * 
	 * @param i L'indice de la première ville
	 * @param k L'indice de la seconde ville
	 * @param newm_solution La nouvelle solution que l'on va remplir
	 * @throws Exception
	 */
	public void swap(int i, int k, Solution newm_solution) throws Exception {
	    int size = m_instance.getNbCities();
	    // 1. On recopie jusqu'à l'index i (index de la première ville qu'on veut inverser)
	    for ( int c = 0; c < i; ++c ) {
	        newm_solution.setCityPosition( m_solution.getCity( c ),c );
	    } 
	    // 2. On inverse l'ordre entre les villes d'index i et k
	    int dec = 0;
	    for ( int c = i; c < k+1; ++c ) {
	        newm_solution.setCityPosition(  m_solution.getCity( k - dec ),c );
	        dec++;
	    }
	    // 3. On rajoute les dernières villes après l'index k
	    for ( int c = k + 1; c < size; ++c ) {
	        m_solution.setCityPosition( m_solution.getCity( c ),c );
	    }
	}

	// -----------------------------
	// --- ALGORITHME GENETIQUE ----
	// -----------------------------	
	
	/**
	 *  Cette méthode génère un chemin au hasard sur l'instance choisie.
	 *  
	 * @return Une solution au problème sur l'instance choisie
	 * @throws Exception
	 */
	public Solution generateSolutionRandom() throws Exception {		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		Solution m_sol = new Solution(m_instance);
		for(int i=1; i<m_instance.getNbCities(); i++) {
			temp.add(i);
		}
		Collections.shuffle(temp);
		m_sol.setCityPosition(0, 0);
		int compt=1;
		while (compt<m_instance.getNbCities()) {
			m_sol.setCityPosition(temp.get(compt-1), compt);
			compt++;
		}
		m_sol.setCityPosition(0, m_instance.getNbCities());
		//System.out.println("Solution random : ");
		//m_solution.print(System.err);
		return m_sol;
	}
	
	/**
	 *  Cette méthode génère une population de chemins sur l'instance choisie.
	 *  
	 * @param taille_pop  La taille de population que l'on choisi
	 * @return Un tableau de solutions random ie une population random de taille que l'on a choisie
	 * @throws Exception
	 */
	public Solution[] generatePopulation(int taille_pop) throws Exception{
		Solution[] s = new Solution[taille_pop];
		s[0]=localSearchPPV(); //bonne solution au début pour converger plus vite
		for(int i=1; i<s.length; i++) {
			s[i]=generateSolutionRandom();
			/**System.out.println("individu "+i);
			for(int j=0;j<s.length;j++) {
				System.out.println(s[i]);
			}
			s[i].evaluate();
			s[i].print(System.out);*/
		}
		return s;
	}
	
	/**
	 *  Cette méthode permet de trouver le chemin avec le coût minimum (ici appelé "fitness") dans une population donnée.
	 *
	 * @param population Un tableau de solutions au problème ie une population
	 * @return L'indice dans le tableau entré en paramètre de la ville ayant le coût minimum
	 * @throws Exception
	 */
	public Solution getBestFitness(Solution[] population) throws Exception {
		double minimum = population[0].getCout();
		int indice = 0;
		for(int i=0; i<population.length; i++) {
			if(population[i].getCout()<minimum) {
				minimum = population[i].getCout();
				indice = i;
			}
		}
		return population[indice];
	}
	
	/**
	 * Méthode qui permet de faire évoluer une population selon le schéma que l'on a choisi:
	 * 1) Enregistrement du meilleur individu || 
	 * 2) Remplissage de la nouvelle population par des individus "enfants" en réalisant des tournois entre les individus "parents".
	 * POur cela on fait appel aux méthodes tournoi(Solution[] population, int tailleTournoi) et crossover(Solution individu_1, Solution individu_2) ||
	 * 3) Mutation de la nouvelle population en faisant appel à muter(Solution circuit, double tauxMutation)
	 *
	 * @param population La population de départ ie la population mère
	 * @param tailleTournoi La taille des tournois que l'on réalise à l'étape 2
	 * @param tauxMutation Le taux de mutation choisi pour l'étape 3
	 * @return Un tableau de solutions qui est la population d'arrivée ie la population enfant
	 * @throws Exception
	 */
	public Solution[] evolution(Solution[] population, int tailleTournoi, double tauxMutation) throws Exception {
		Solution[] nouvelle_pop = new Solution[population.length];
		nouvelle_pop[0] = getBestFitness(population); // On enregistre le meilleur circuit de notre ancienne population
		for(int i=1; i<nouvelle_pop.length; i++) {
			Solution parent1 = tournoi(population,tailleTournoi);
			Solution parent2 = tournoi(population,tailleTournoi);
			while (parent1==parent2) { // pour ne jamais avoir les mêmes parents
				parent2=tournoi(population,tailleTournoi);
			}
			Solution enfant = crossover(parent1,parent2);
			nouvelle_pop[i] = enfant;	
			
		} 
		for(int i=1; i<nouvelle_pop.length; i++) {
			muter(nouvelle_pop[i], tauxMutation);
		}
		/**System.out.println("la génération suivante :");
		for (int i=0; i<nouvelle_pop.length; i++) {
			System.out.println("individu"+i);
			nouvelle_pop[i].evaluate();
			nouvelle_pop[i].print(System.out);
		}*/
		return nouvelle_pop;
	}
	
	/**
	 * Cette méthode retourne l'enfant de deux individus que l'on a choisi de reproduire/croiser selon le schéma de croisement que l'on a choisi.
	 *
	 * @param individu_1 La solution "parent" n°1
	 * @param individu_2 La solution "parent" n°2
	 * @return Une solution "enfant" issue du croisement des 2 solutions "parent"
	 * @throws Exception the exception
	 */	
	public Solution crossover(Solution individu_1, Solution individu_2) throws Exception {
		Random random = new Random();
		int nbVilles = m_instance.getNbCities(); 	  		  	 	 						 	
		boolean[] villesAjoutees = new boolean[nbVilles];
		int start = 1 + random.nextInt((nbVilles/2)-1); //le +1 étant valeur minimale (on ne veut pas changer premier) pareil pour dernier
		int end = (nbVilles/2) + random.nextInt((nbVilles-1) - (nbVilles/2));
		Solution enfant = new Solution(m_instance);
		enfant.setCityPosition(0,0);
		enfant.setCityPosition(0, nbVilles);
		villesAjoutees[0]=true;
		
		// on parcout parent 1
		for(int i=start; i<end; i++) {
			enfant.setCityPosition(individu_1.getCity(i), i);
			villesAjoutees[individu_1.getCity(i)]=true;
			}
		
		// on veut parcourir parent 2
		for(int i=0; i<start; i++) {
			if (!villesAjoutees[individu_2.getCity(i)]) {
				enfant.setCityPosition(individu_2.getCity(i), i);
				villesAjoutees[individu_2.getCity(i)]=true;
			}
		}
		for(int j=end; j<nbVilles; j++) { 	  		  	 	 						 	
			if (!villesAjoutees[individu_2.getCity(j)]) { 	  		  	 	 						 	
				enfant.setCityPosition(individu_2.getCity(j), j); 	  		  	 	 						 	
				villesAjoutees[individu_2.getCity(j)]=true; 	  		  	 	 						 	
			}  	  		  	 	 						 	
		}
		
		// on complète finalement les trous, s'il y en a
		for (int k=1; k<nbVilles; k++) {
			if (enfant.getCity(k)==0) {
				int ville=0;
				while (villesAjoutees[ville] && ville<villesAjoutees.length) {
					ville++;
				}
				enfant.setCityPosition(ville, k);
				villesAjoutees[ville]=true;
			}
		}
		return enfant;		
	}
	
	/**
	 * Cette méthode fait muter un circuit selon le schéma que l'on a choisi.
	 * Si le random tiré est plus petit que tauxMutation alors on échange deux villes à des indices random.
	 * @param circuit L'individu à faire muter (ou non)
	 * @param tauxMutation La probabilité de muter
	 * @throws Exception
	 */
	public void muter(Solution circuit, double tauxMutation) throws Exception {
		int nbVilles = m_instance.getNbCities();
		for(int circuitPos1=1; circuitPos1<nbVilles; circuitPos1++) {
			double random = Math.random();
			if(random<tauxMutation) {
				int circuitPos2 = ((int)((nbVilles-1)*Math.random()))+1; //ne peut pas etre 0
				int ville_1 = circuit.getTour()[circuitPos1];
				int ville_2 = circuit.getTour()[circuitPos2];
				circuit.setCityPosition(ville_1, circuitPos2);
				circuit.setCityPosition(ville_2, circuitPos1);
			}
		}
	} 
	

	/**
	 * Cette méthode réalise un tournoi de taille choisie selon le schéma de tournoi que l'on a choisi. 
	 * 
	 * @param population La population où effectuer le tournoi
	 * @param tailleTournoi Le nombre d'individus participant au tournoi
	 * @return Le circuit avec la meilleure "fitness" ie coût du tournoi
	 * @throws Exception
	 */ 
	public Solution tournoi(Solution[] population, int tailleTournoi) throws Exception {
		Solution[] tournoi = new Solution[tailleTournoi];
		for(int i=0; i< tailleTournoi; i++) {
			int random = (int)(Math.random()*population.length);
			tournoi[i] = population[random];
		}
		Solution fittest = getBestFitness(tournoi);
		return fittest;
	}
	
	/**
	 * Méthode principale qui va dérouler l'algorithme génétique sur un certain nombre de générations.
	 * Appelle les méthodes generatePopulation(int taille_pop),
	 * evolution(Solution[] population, int tailleTournoi, double tauxMutation)
	 * et getBestFitness(Solution[] population).
	 * C'est dans cette méthode que l'on choisi tauxMutation, tailleTournoi, taille_pop et le nombre de générations.
	 * On teste également le respect de la limite de temps. 
	 *
	 * @throws Exception
	 */
	public void GeneticAlgorithm() throws Exception {
		double tauxMutation = 0.015;
		int tailleTournoi = 6;
		long t = System.currentTimeMillis();
		long tempspasse = 0;
		int compt=0;
		while(tempspasse<m_timeLimit) {
			Solution[] populationMere = generatePopulation(2000);
			for(int i=0; i<500; i++) { // correspond au nombre de générations que l'on fait
				Solution[] populationFille = evolution(populationMere, tailleTournoi, tauxMutation);
				populationMere = populationFille;
				compt++;
				
			}
			Solution meilleureSolution =getBestFitness(populationMere);
			m_solution=meilleureSolution;
			tempspasse = System.currentTimeMillis()-t;
		}
	}
	// -----------------------------
	// ----- ALGORITHME FOURMI -----
	// -----------------------------
	
	public void algoFourmi() throws Exception {
		
	}

	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/**
	 * Gets the solution.
	 *
	 * @return the problem Solution
	 */
	public Solution getSolution() {
		return m_solution;
	}

	/**
	 * Gets the single instance of TSPSolver.
	 *
	 * @return problem data
	 */
	public Instance getInstance() {
		return m_instance;
	}

	/**
	 * Gets the time limit.
	 *
	 * @return Time given to solve the problem
	 */
	public long getTimeLimit() {
		return m_timeLimit;
	}

	/**
	 * Initializes the problem solution with a new Solution object (the old one will be deleted).
	 * @param solution : new solution
	 */
	public void setSolution(Solution solution) {
		this.m_solution = solution;
	}

	/**
	 * Sets the problem data.
	 *
	 * @param instance the Instance object which contains the data.
	 */
	public void setInstance(Instance instance) {
		this.m_instance = instance;
	}

	/**
	 * Sets the time limit (in seconds).
	 * @param time time given to solve the problem
	 */
	public void setTimeLimit(long time) {
		this.m_timeLimit = time;
	}

}
