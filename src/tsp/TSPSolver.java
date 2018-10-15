package tsp;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * 
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * 
 * The TSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following TSPSolver attributes: 
 * 	- #m_instance :  the Instance object which contains the problem data
 * 	- #m_solution : the Solution object to modify. This object will store the result of the program.
 * 	- #m_timeLimit : the maximum time limit (in seconds) given to the program.
 *  
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
	 * 
	 * Do not print text on the standard output (eg. using `System.out.print()` or `System.out.println()`).
	 * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
	 * 
	 * You can print using the error output (`System.err.print()` or `System.err.println()`).
	 * 
	 * When your algorithm terminates, make sure the attribute #m_solution in this class points to the solution you want to return.
	 * 
	 * You have to make sure that your algorithm does not take more time than the time limit #m_timeLimit.
	 * 
	 * @throws Exception may return some error, in particular if some vertices index are wrong.
	 */

	public void solve() throws Exception
	{
		m_solution.print(System.err);
		
		// Example of a time loop
		long t = System.currentTimeMillis();
		long tempspasse = 0;
/*		boolean[] visite = new boolean[m_instance.getNbCities()];
		visite[0] = true; 
		
		while(tempspasse < m_timeLimit*100) {
			m_solution.setCityPosition(1, 0);
			int compteur=0;
			int ville = 1;
			while(compteur<m_instance.getNbCities()) {
				int ville_suiv=plusProcheVoisin(ville,visite);
				visite[ville_suiv]=true;
				m_solution.setCityPosition(ville_suiv, compteur);
				compteur++;
				ville=ville_suiv;
				tempspasse = System.currentTimeMillis()-t;
				if (compteur==m_instance.getNbCities()) { // si toutes les villes ont ete parcourues, retour a la ville 0
					ville_suiv=0;
				}
			}
		} */
		
		boolean[] villes = new boolean[m_instance.getNbCities()];
		int ville_courante = 0;	
		villes[ville_courante]=true;
		m_solution.setCityPosition(ville_courante, 0);
		int compteur=1;
		while(compteur<m_instance.getNbCities()) {
			int index = ville_courante;
			while(villes[index]==true) {
				index = (index+1)%villes.length;
			}
			long minimum = m_instance.getDistances(ville_courante, index);
				int ville_proche = index;
				for(int i=0; i<villes.length; i++) {
					if(villes[i]==false && m_instance.getDistances(ville_courante,i)<minimum) {
						minimum=m_instance.getDistances(ville_courante,i);
						ville_proche=i;
					}
				}
				villes[ville_proche]=true;
				m_solution.setCityPosition(ville_proche, compteur);
				ville_courante=ville_proche;
				compteur++;
		}
		m_solution.setCityPosition(0,m_instance.getNbCities());

	}
	
	
	public int plusProcheVoisin(int ville,boolean[] lesVilles) throws Exception {
		int villePlusProche=ville+1;
		long minimum= m_instance.getDistances(ville, villePlusProche);
		for (int i=0; i<m_instance.getNbCities(); i++) {
			if (m_instance.getDistances(ville-1, i)<minimum && lesVilles[i]==false) {
				minimum=m_instance.getDistances(ville-1,i);
				villePlusProche=i;
			}
		}
		return villePlusProche;
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
