package tsp;
import java.util.ArrayList;
import java.util.Collections;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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
	 * Creates an object of the class Solution for the problem data loaded in Instance
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
		generateSolutionRandom();
		System.out.println(m_solution.getCout());
	}
		
	/** Première méthode que nous avons développée: un Local Search qui cherche de ville en ville la plus proche voisine.
	 * Appelle la méthode plusProcheVoisin(int ville_courante, boolean[] villes).
	 * @throws Exception
	 * @version 1 (15/10/2018)
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
	
	/** Un Local Search qui cherche de ville en ville la plus proche voisine, en partant de ville_depart.
	 * Appelle la méthode plusProcheVoisin(int ville_courante, boolean[] villes).
	 * @param ville_depart Entier correspondant à l'indice de la ville de départ
	 * @throws Exception
	 * @version 1 (16/10/2018)
	 * */
	public void localSearchPPV(int ville_depart) throws Exception {
		m_solution.print(System.err);
		long t = System.currentTimeMillis();
		long tempspasse = 0;	
		boolean[] villes = new boolean[m_instance.getNbCities()];
		int ville_courante = ville_depart;
		villes[ville_courante]=true;
		m_solution.setCityPosition(ville_courante, 0);
		int compteur=1;
		while(compteur<m_instance.getNbCities()) {
			int ville_proche = plusProcheVoisin(ville_courante,villes);
			villes[ville_proche]=true;
			m_solution.setCityPosition(ville_proche, compteur);
			ville_courante=ville_proche;
			compteur++;
		}
		m_solution.setCityPosition(ville_depart, m_instance.getNbCities());
		tempspasse=System.currentTimeMillis()-t;
	}

	/** Cette méthode cherche le Plus Proche Voisin non visité de ville_courante contenu dans tableau_villes.
	 * @param ville_courante Un entier correspondant à l'indice de la ville dont on cherche le PPV.
	 * @param tableau_villes Un tableau de booléens de longueur m_instance.getNbCities(). 
	 * Si la ville i a déjà été visitée, ville[i]=true. Sinon, ville[i]=false.
	 * @return Un entier correspondant à l'indice de la ville la plus proche de ville_courante non visitée.
	 * @throws Exception
	 * @version 1 (15/10/2018)
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
	
	/** Cette méthode calcule le coût d'un trajet en appelant la méthode getTour() de la classe Solution. 
	 * @throws Exception
	 * @version 1 (16/10/2018)
	 * */
	public int getCout() throws Exception {
		m_solution.print(System.err);
		int res = 0;
		for(int i=0; i<m_solution.getTour().length-1; i++) {
			res = res + (int)(m_instance.getDistances(m_solution.getTour()[i], m_solution.getTour()[i+1]));
		}
		res = res+(int)(m_instance.getDistances(m_solution.getTour()[m_solution.getTour().length-1], m_solution.getTour()[0]));
		return res;
	}	

	
	public Solution generateSolutionRandom() throws Exception {
		m_solution.print(System.err);
		ArrayList<Integer> position = new ArrayList<Integer>();
		for (int i=0; i<m_instance.getNbCities(); i++) {
			position.add(i);
		}
		Collections.shuffle(position);
		int compt=0;
		while (compt<m_instance.getNbCities()) {
			m_solution.setCityPosition(position.get(compt), compt);
			compt++;
		}
		m_solution.setCityPosition(position.get(0), m_instance.getNbCities());
		return m_solution;
		
	}
	
	public Solution[] generatePopulation(int taille_pop) throws Exception{
		m_solution.print(System.err);
		Solution[] s = new Solution[taille_pop];
		for(int i=0; i<s.length; i++) {
			s[i]=generateSolutionRandom();
		}
		return s;
	}
	
	

	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/** @return the problem Solution */
	public Solution getSolution() {
		return m_solution;
	}

	/** @return problem data */
	public Instance getInstance() {
		return m_instance;
	}

	/** @return Time given to solve the problem */
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
	 * Sets the problem data
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
