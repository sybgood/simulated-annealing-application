package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Random;

public class Annealing {
    private int N;// iteration times for each temperature.
    private int Tem_t;// annealing iteration times. 
    private float a;// annealing cooling factor. Current temperature t1 = a * t0
    private float t0;// initial temperature
    private Double[][] distance; /* A 2D array,shows the distance between two positions. i.e Double[i][j] is the distance between
                                    the ith station and jth station.*/
    private int NumS; // Number of nodes, should be # Positive station + 1 (start point)
    
    private int[] path; // The drone will travel from coorList.get(path[0]) to coorList.get(path[max_length-1])
    private Double pathEvaluation; // Total distance the drone need to move, evaluated from path. 
    private int[] bestPath; /* Path with lowest distance travel during current iteration will be considered
                               as bestPath.*/
    private Double bestEvaluation; // Total distance the drone need to move, evaluated from bestpath. 
    private int[] tempPath; // temporal path.
    private Double tempEvaluation; //Total distance from the drone need to move, evaluated from temppath.
    private Random rnd; // random seed.
    private ArrayList<Position> coorList; // List of position of charge station with positive coins.
    protected Annealing(Map map,int n, int t, float t0, float aa,Random rnd,Position startPoint) {
        this.N = n;
        this.Tem_t = t;
        this.t0 = t0;
        this.a = aa;
        this.rnd = rnd;
        coorList = map.getCoorList();
        coorList = onlyPositive(coorList,map); // Find all stations with positive coins and record its position. 
        NumS = coorList.size()+1; // +1 for start position.
        coorList.add(0,startPoint);
        calculateStationDistance(coorList);
        path = new int[NumS];
        path[0] = 0 ;
        bestPath = new int[NumS];
        bestEvaluation = Double.MAX_VALUE;
        tempPath = new int[NumS];
        tempEvaluation = Double.MAX_VALUE;
        //bestT = 0;
    }
    /**
     * Initialise the path.
     */
    private void initPath() {
        int i,j;
        for(i=1;i<NumS;) {
            path[i] = rnd.nextInt(NumS);
            for(j=0;j<i;j++) {
                if(path[i]==path[j]) break; // To avoid repetition.
            }
            if(j==i) i++;
        }
    }
    /**
     * 
     * @param path1
     * @param path2
     * Copy the path from path1 to path2.
     */
    private void copyPath(int[] path1,int[] path2) {
        int i;
        for(i=0;i<NumS;i++) {
            path2[i] = path1[i];
        }
    }
    /**
     * 
     * @param path
     * @return return the score for path, that is the total distance travel for current path.
     */
    protected Double evaluate(int[] path) {
        Double len = 0.0;
        int i;
        for(i=1;i<NumS;i++) {
            len+=distance[path[i-1]][path[i]];
        }
        //len += distance[path[NumS - 1]][path[0]];
        return len;
    }
    /**
     * @param path
     * @param tempPath
     * First copy current path to tempPath, then random switch two nodes in tempPath.
     */
    private void newPath(int[] path, int[] tempPath) {
        int temp,ran1,ran2;
        copyPath(path,tempPath);
        ran1 = 0;
        ran2 = 0;
        while (ran1==0||ran1 == ran2||ran2==0) {
            ran1 = rnd.nextInt(NumS);
            ran2 = rnd.nextInt(NumS);
        }
        temp = tempPath[ran1];
        tempPath[ran1] = tempPath[ran2];
        tempPath[ran2] = temp;
    }
    /**
     * @return the travelling sequence of the best path we got. 
     */
    protected ArrayList<Position> givePath(){
        int i,j;
        ArrayList<Position> path = new ArrayList<Position>();
        for(i=0;i<NumS;i++) {
            j = bestPath[i];
            path.add(coorList.get(j));
        }
        return path;
    }
    /**
     * Find the best path.
     */
    protected void solve() {
        initPath();
        copyPath(path,bestPath);
        bestEvaluation = evaluate(path);
        pathEvaluation = bestEvaluation;
        int k = 0;
        int n;
        float t = t0;
        Double r= 0.0;
        while(k<Tem_t) {
            n=0;
            while(n<N) {
                newPath(path,tempPath);
                tempEvaluation = evaluate(tempPath);
                if (tempEvaluation < bestEvaluation) {
                    copyPath(tempPath, bestPath);
                    bestEvaluation = tempEvaluation;
                }
                r = rnd.nextDouble();
                if(tempEvaluation<pathEvaluation||Math.exp((pathEvaluation - tempEvaluation)/t)>r) {
                    copyPath(tempPath,path);
                    pathEvaluation = tempEvaluation;
                }
                n++;
            }
            t = a * t;
            k++;
        }

    }
    /**
     * @param coorList
     * @param map
     * @return the list of position of charge stations which contains positive coins and powers.
     */
    private ArrayList<Position> onlyPositive (ArrayList<Position> coorList,Map map){
        Double c;
        ArrayList<Position> cc = new ArrayList<Position>();
        for(Position p : coorList) {
            c = map.PositionCoins.get(p);
            if(c>=0) cc.add(p);
        }
        return cc;
        
    }
    /**
     * @param coorList
     * Calculate distance between positions in coorList. Store it in distance[][];
     */
    private void calculateStationDistance(ArrayList<Position> coorList) {
        int i,j;
        Double dis;
        Position p1;
        Position p2;
        distance = new Double[NumS][NumS];
        for (i=0;i<NumS-1;i++) {
            distance[i][i] = 0.0;
            for(j=i+1;j<NumS;j++) {
                p1 = coorList.get(i);
                p2 = coorList.get(j);
                dis = drone.calDistance(p1, p2)*200000;
                distance[i][j] = dis;
                distance[j][i] = dis;
                
            }
        }
        distance[NumS-1][NumS-1] = 0.0;
    }
}
