package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Random;

public class Annealing {
    private final int N;
    private final int Tem_n;
    private final float a;
    private final float t0;
    private Double[][] distance; 
    private int NumS; 
    private int[] path; 
    private Double pathEvaluation; 
    private int[] bestPath; 
    protected Double bestEvaluation; 
    private int[] tempPath;
    private Double tempEvaluation; 
    private Random rnd; 
    private ArrayList<Position> coorList; 
    protected Annealing(Map map,int n, int t, float t0, float aa,Random rnd,Position startPoint) {
        this.N = n;
        this.Tem_n = t;
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
        initPath();
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
     * Copy the path from path1 to path2.
     * @param path1 int[]
     * @param path2 int[]
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
        copyPath(path,bestPath);
        bestEvaluation = evaluate(path);
        pathEvaluation = bestEvaluation;
        int k = 0;
        int n;
        float t = t0;
        Double r= 0.0;
        while(k<Tem_n) {
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
     * @param coorList List of station's position
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
    protected void heatAgain() {
        this.copyPath(bestPath,path);
        int k = rnd.nextInt();
        rnd = new Random(k);
        this.solve();
    }
}
