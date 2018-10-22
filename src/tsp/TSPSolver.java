package tsp;
import java.util.ArrayList;
import java.util.Collections;

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
		System.out.println(generateSolutionRandom().isFeasible());
	}
	
	// -----------------------------------
	// -- ALGORITHME PLUS PROCHE VOISIN --
	// -----------------------------------	
		
	/**
	 *  Première méthode que nous avons développée: un Local Search qui cherche de ville en ville la plus proche voisine.
	 * Appelle la méthode plusProcheVoisin(int ville_courante, boolean[] villes).
	 *
	 * @version 1 (15/10/2018)
	 * @throws Exception the exception
	 */
	public void localSearchPPV() throws Exception {
		m_solution.print(System.err);
		long t = System.currentTimeMillis();
		long tempspasse = 0;		
		boolean[] villes = new boolean[m_instance.getNbCities()];
		int ville_courante = 0;	
		villes[ville_courante]=true;
		m_solution.setCityPosition(ville_courante, 0);
		int compteur=1;
		/* Tant que l'on a pas traité le cas de chaque ville on continue à chercher les plus proches voisins */
		while(compteur<m_instance.getNbCities()) {
			int ville_proche = plusProcheVoisin(ville_courante,villes);
			villes[ville_proche]=true;
			m_solution.setCityPosition(ville_proche, compteur);
			ville_courante=ville_proche;
			compteur++;
		} 
		/* On relie la première ville avec dernière */
		m_solution.setCityPosition(0,m_instance.getNbCities());
		/* On teste le temps depuis lequel le programme tourne */
		tempspasse=System.currentTimeMillis()-t;
	}
	

	/**
	 *  Cette méthode cherche le Plus Proche Voisin non visité de ville_courante contenu dans tableau_villes.
	 *
	 * @version 1 (15/10/2018)
	 * @param ville_courante Un entier correspondant à l'indice de la ville dont on cherche le PPV.
	 * @param tableau_villes Un tableau de booléens de longueur m_instance.getNbCities().
	 * Si la ville i a déjà été visitée, ville[i]=true. Sinon, ville[i]=false.
	 * @return Un entier correspondant à l'indice de la ville la plus proche de ville_courante non visitée.
	 * @throws Exception the exception
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
	 * @version 1 (16/10/2018)
	 * @return the cout
	 * @throws Exception the exception
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
	
	public void DeuxOpt() throws Exception {
		m_solution.print(System.err);
		long t = System.currentTimeMillis();
		long tempspasse = 0;
		while(tempspasse<m_timeLimit) {
		int size = m_instance.getNbCities();   
		    // On répète jusqu'à qu'il n'y ait plus d'amélioration
		    int amelioration = 0;
		    while ( amelioration < 100 )
		    {
		        double meilleure_distance = m_solution.getCout();   
		        for ( int i = 1; i < size - 1; i++ ) 
		        {
		            for ( int k = i + 1; k < size; k++) 
		            {
		            	Solution newm_solution=m_solution.copy();
		                swap( i, k, newm_solution );		               
		                double nouvelle_distance = newm_solution.getCout();		 
		                if ( nouvelle_distance < meilleure_distance ) 
		                {
		                	// on a amélioré le trajet donc on remet la variable à 0
		                    amelioration = 0;		                                                 
		                    for (int j=0;j<size;j++)
		                    {
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
		 
	}
	
	public void swap( int i, int k, Solution newm_solution ) throws Exception {
	    int size = m_instance.getNbCities();
	    // 1. On recopie jusqu'à l'index i (index de la première ville qu'on veut inverser)
	    for ( int c = 0; c < i; ++c )
	    {
	        newm_solution.setCityPosition( m_solution.getCity( c ),c );
	    } 
	    // 2. On inverse l'ordre entre les villes d'index i et k
	    int dec = 0;
	    for ( int c = i; c < k+1; ++c )
	    {
	        newm_solution.setCityPosition(  m_solution.getCity( k - dec ),c );
	        dec++;
	    }
	    // 3. On rajoute les dernières villes après l'index k
	    for ( int c = k + 1; c < size; ++c )
	    {
	        m_solution.setCityPosition( m_solution.getCity( c ),c );
	    }
	}

	// -----------------------------
	// --- ALGORITHME GENETIQUE ----
	// -----------------------------	
	
/**
	 *  Cette méthode génère un chemin au hasard sur l'instance choisie.
	 *
	 * @version 1 (19/10/2018)
	 * @return Une solution au problème sur l'instance choisie
	 * @throws Exception the exception
	 */
	public Solution generateSolutionRandom() throws Exception {
		m_solution.print(System.err);		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i=1; i<m_instance.getNbCities(); i++) {
			temp.add(i);
		}
		Collections.shuffle(temp);
		m_solution.setCityPosition(0, 0);
		int compt=1;
		while (compt<m_instance.getNbCities()) {
			m_solution.setCityPosition(temp.get(compt-1), compt);
			compt++;
		}
		m_solution.setCityPosition(temp.get(0), m_instance.getNbCities());
		return m_solution;
		
	}
	
/**
 *  Cette méthode génère une population de chemins sur l'instance choisie.
 *
 * @version 1 (19/10/18)
 * @param taille_pop  La taille de population que l'on choisie
 * @return Un tableau de solutions random de taille que l'on a choisie
 * @throws Exception the exception
 */
	public Solution[] generatePopulation(int taille_pop) throws Exception{
		m_solution.print(System.err);
		Solution[] s = new Solution[taille_pop];
		for(int i=0; i<s.length; i++) {
			s[i]=generateSolutionRandom();
		}
		return s;
	}
	
/**
 *  Cette méthode permet de trouver le chemin avec le coût minimum dans une population .
 *
 * @version 1 (21/10/2018)
 * @param population Un tableau de solutions au problème
 * @return L'indice dans le tableau entré en paramètre de la ville ayant le coût minimum
 * @throws Exception the exception
 */
	public Object[] getBestFitness(Solution[] population) throws Exception {
		m_solution.print(System.err);
		long minimum = population[0].getCout();
		int indice = 0;
		for(int i=0; i<population.length; i++) {
			if(population[i].getCout()<minimum) {
				minimum = population[i].getCout();
				indice = i;
			}
		}
		Object[] resultat = new Object[2];
		resultat[0] = population[indice];
		resultat[1] = indice;
		return resultat;
	}
	
	/**
	 * Evolution.
	 *
	 * @param population the population
	 * @param taillepop the taillepop
	 * @param tailleTournoi the taille tournoi
	 * @param tauxMutation the taux mutation
	 * @return the solution[]
	 * @throws Exception the exception
	 */
	public Solution[] evolution(Solution[] population, int tailleTournoi, double tauxMutation) throws Exception {
		m_solution.print(System.err);
		Solution[] nouvelle_pop = generatePopulation(population.length);
		nouvelle_pop[(int)getBestFitness(population)[1]] = (Solution)getBestFitness(population)[0]; // On enregistre le meilleur circuit de notre ancienne population
		for(int i=0; i<nouvelle_pop.length; i++) {
			Solution parent1 = tournoi(population,tailleTournoi);
			Solution parent2 = tournoi(population,tailleTournoi);
			Solution enfant = crossover(parent1,parent2);
			nouvelle_pop[i] = enfant;
		}
		for(int i=0; i<nouvelle_pop.length; i++) {
			muter(nouvelle_pop[i], tauxMutation);
		}
		return nouvelle_pop;
	}
	
/**
 *  Etape 2: 
 * Cette méthode retourne l'enfant de deux individus que l'on a choisi de reproduire .
 *
 * @version 1 (21/10/2018)
 * @param individu_1 the individu 1
 * @param individu_2 the individu 2
 * @return the solution
 * @throws Exception the exception
 */	
	public Solution crossover(Solution individu_1, Solution individu_2) throws Exception {
		int start = (int)(Math.random()*individu_1.getTour().length);
		int end = (int)(Math.random()*individu_2.getTour().length);
		Solution enfant = new Solution(m_instance);
		for(int i=0; i<enfant.getTour().length; i++) {
			enfant.getTour()[i]=m_instance.getNbCities()+1;
		}
		
		for(int i=0; i<enfant.getTour().length; i++) {
			if(start<end && i>start && i<end) {
				enfant.setCityPosition(i, individu_1.getCity(i));
			} else if(start>end) {
				if (i>end && i<start) {
					enfant.setCityPosition(i, individu_1.getCity(i));
				}
			}
		}
		
		for(int i=0; i<individu_2.getTour().length; i++) {
			if(enfant.contains(individu_2.getCity(i))!=true) {
				for(int j=0; j<enfant.getTour().length; j++) {
					if(enfant.getCity(j)==m_instance.getNbCities()+1) {
						enfant.setCityPosition(j, individu_2.getCity(i));
					}
				}
			}
		}
		enfant.setCityPosition(enfant.getTour()[0], enfant.getTour().length);
		return enfant;
	}
	
	/**
	 * Muter.
	 *
	 * @param circuit the circuit
	 * @param tauxMutation the taux mutation
	 * @throws Exception the exception
	 */
	public void muter(Solution circuit, double tauxMutation) throws Exception {
		m_solution.print(System.err);
		for(int circuitPos1=0; circuitPos1<circuit.getTour().length; circuitPos1++) {
			double random = Math.random();
			if(random<tauxMutation) {
				int circuitPos2 = (int)(circuit.getTour().length*random);
				int ville_1 = circuit.getTour()[circuitPos1];
				int ville_2 = circuit.getTour()[circuitPos2];
				circuit.setCityPosition(ville_1, circuitPos2);
				circuit.setCityPosition(ville_2, circuitPos1);
			}
		}
	}
	
/**
 *  Etape 3:
 * Cette méthode retourne le meilleur circuit du tournoi.
 *
 * @version 1 (21/10/2018)
 * @param population Notre population où effectuer le tournoi
 * @param tailleTournoi Le nombre de participants au tournoi
 * @return fittest Le meilleur circuit du tournoi
 * @throws Exception the exception
 */ 
	public Solution tournoi(Solution[] population, int tailleTournoi) throws Exception {
		m_solution.print(System.err);
		Solution[] tournoi = new Solution[tailleTournoi];
		for(int i=0; i< tailleTournoi; i++) {
			int random = (int)(Math.random()*population.length);
			tournoi[i] = population[random];
		}
		Solution fittest = (Solution)getBestFitness(tournoi)[0];
		return fittest;
	}
	
	/**
	 * Genetic algorithm.
	 *
	 * @throws Exception the exception
	 */
	public void GeneticAlgorithm() throws Exception {
		m_solution.print(System.err);
/* Probabilité qu'une ville d'un circuit subisse une mutation. 
 * Cela correspond à l'inversion de la position de deux villes dans le circuit. 
 * Le taux est assez faible car la probabilité d'obtenir une distance plus faible en inversant deux ville est peu élevée */		
		double tauxMutation = 0.015;
/* Taille des poules de notre tournoi (l'une des méthodes possibles pour sélectionner les individus que nous souhaitons faire se reproduire).
La sélection par tournoi fait affronter plusieurs individus sélectionnés au hasard. Ici ce sont donc des tournois de 5 circuits et on garde le circuit avec la distance la plus faible. */
		int tailleTournoi = 5;
		
		Solution[] populationMere = generatePopulation(50);
		System.out.println("Distance initale: "+((Solution)getBestFitness(populationMere)[0]).getCout());
		for(int i=0; i<100; i++) {
			populationMere = evolution(populationMere, tailleTournoi, tauxMutation);
		}
		System.out.println("Distance finale: "+((Solution)getBestFitness(populationMere)[0]).getCout());
		Solution meilleureSolution = (Solution)getBestFitness(populationMere)[0];
		m_solution = meilleureSolution; 
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
