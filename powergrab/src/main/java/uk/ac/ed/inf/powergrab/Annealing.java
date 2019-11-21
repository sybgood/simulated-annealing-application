package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Random;

public class Annealing {
    private int N;// Number of iteration times
    private int Tem_t;// Annealing times. 
    private float a;// Annealing weight factor.
    private float t0;// initial temperature
    private Double[][] distance; // A 2D array,shows the distance.i.e Double[i][j] is the distance between
    // the ith station and jth station.
    private int NumS; // Number of station
    //private int bestT; // Best evaluation times.
    private int[] path; // The drone will travel from coorList.get(path[0]) to coorList.get(path[max_length-1])
    private Double pathEvaluation; // Total distance the drone need to move, evaluated from path. 
    private int[] bestPath; // Path with lowest distance travel during current iteration will be considered
    // as bestPath.
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
    private void initPath() {
        // Initialise the path in random.
        int i,j;
        for(i=1;i<NumS;) {
            path[i] = rnd.nextInt(NumS);
            for(j=0;j<i;j++) {
                if(path[i]==path[j]) break; // To avoid repetition.
            }
            if(j==i) i++;
        }
    }
    // Copy data from path1 to path2.
    private void copyPath(int[] path1,int[] path2) {
        int i;
        for(i=0;i<NumS;i++) {
            path2[i] = path1[i];
        }
    }
    protected Double evaluate(int[] path) {
        Double len = 0.0;
        int i;
        for(i=1;i<NumS;i++) {
            len+=distance[path[i-1]][path[i]];
        }
        //len += distance[path[NumS - 1]][path[0]];
        return len;
    }
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
    protected ArrayList<Position> givePath(){
        int i,j;
        ArrayList<Position> path = new ArrayList<Position>();
        for(i=0;i<NumS;i++) {
            j = bestPath[i];
            path.add(coorList.get(j));
        }
        return path;
    }
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
                    //bestT = k; 
                    bestEvaluation = tempEvaluation;
                }
                r = rnd.nextDouble();
                if(tempEvaluation<pathEvaluation||Math.exp((pathEvaluation - tempEvaluation)/t)>r) {
//                    Double sss = pathEvaluation - tempEvaluation; 
//                    if(sss<0) {
//                        System.out.print(sss+"\n");
//                    }
                    copyPath(tempPath,path);
                    pathEvaluation = tempEvaluation;
                }
                n++;
            }
            t = a * t;
            k++;
        }
       
//        System.out.println("Best path length");
//        System.out.println(bestEvaluation);
//        System.out.println("Best path：");
//        for (int i = 0; i < NumS; i++) {
//            System.out.print(bestPath[i] + ",");
//            if (i % 10 == 0 && i != 0) {
//                System.out.println();
//            }
//        }
    }
    private ArrayList<Position> onlyPositive (ArrayList<Position> coorList,Map map){
        Double c;
        ArrayList<Position> cc = new ArrayList<Position>();
        for(Position p : coorList) {
            c = map.PositionCoins.get(p);
            if(c>=0) cc.add(p);
        }
        return cc;
        
    }
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
