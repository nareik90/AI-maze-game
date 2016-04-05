package AI;

public class HeuristicCalculator {
	/* The heuristic function computes a score by biasing the actual distance using the levels of terrain
	 * and danger as components of the function. In this case the A* heuristic f*(n) + g*(n) + h*(n) is:
	 * 
	 * 	f*(n) = actual distance + (approximate goal distance adjusted for terrain & danger)
	 */
	public static float getHeuristicValue(int distanceTravelled, int approxGoalDistance){
		return distanceTravelled + approxGoalDistance;
	}
}
