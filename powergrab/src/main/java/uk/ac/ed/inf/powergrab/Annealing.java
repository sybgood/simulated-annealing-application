package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Random;

public class Annealing {
    private Map map;
    private int N;// Number of iteration times
    private int Tem_t;// Annealing times. 
    private float a;// Annealing weight factor.
    private float t0;// initial temperature
     
    private Double[][] distance;
    private int NumS;
    private int bestT;
    private int[] Path;
    private Double PathEvaluation;
    private int[] bestPath;
    private Double bestEvaluation;
    private int[] tempPath;
    private Double tempEvaluation;
    private Random rnd;
    private ArrayList<Position> coorList;
    public Annealing(Map map,int n, int t, float t0, float aa,Random rnd,Position startPoint) {
        this.map = map;
        this.N = n;
        this.Tem_t = t;
        this.t0 = t0;
        this.a = aa;
        this.rnd = rnd;
        coorList = map.getCoorList();
        coorList = onlyPositive(coorList);
        NumS = coorList.size()+1; // for start position.
        coorList.add(0,startPoint);
        calculateStationDistance(coorList);
        Path = new int[NumS];
        Path[0] = 0 ;
        bestPath = new int[NumS];
        bestEvaluation = Double.MAX_VALUE;
        tempPath = new int[NumS];
        tempEvaluation = Double.MAX_VALUE;
        bestT = 0;
    }
    private void initGroup() {
        int i,j;
       // Path[0] = rnd.nextInt(NumS);
        for(i=1;i<NumS;) {
            Path[i] = rnd.nextInt(NumS);
            for(j=0;j<i;j++) {
                if(Path[i]==Path[j]) break; // To avoid repetition.
            }
            if(j==i) i++;
        }
    }
    // Copy data from Path1 to Path2.
    private void copyPath(int[] Path1,int[] Path2) {
        int i;
        for(i=0;i<NumS;i++) {
            Path2[i] = Path1[i];
        }
    }
    public Double evaluate(int[] Path) {
        Double len = 0.0;
        int i;
        for(i=1;i<NumS;i++) {
            len+=distance[Path[i-1]][Path[i]];
        }
        //len += distance[Path[NumS - 1]][Path[0]];
        return len;
    }
    private void newPath(int[] Path, int[] tempPath) {
        int i,temp,ran1,ran2;
        copyPath(Path,tempPath);
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
    public ArrayList<Position> givePath(){
        int i,j;
        ArrayList<Position> path = new ArrayList<Position>();
        for(i=0;i<NumS;i++) {
            j = bestPath[i];
            path.add(coorList.get(j));
        }
        return path;
    }
    public void solve() {
        initGroup();
        copyPath(Path,bestPath);
        bestEvaluation = evaluate(Path);
        PathEvaluation = bestEvaluation;
        int k = 0;
        int n;
        float t = t0;
        Double r= 0.0;
        
        while(k<Tem_t) {
            n=0;
            while(n<N) {
                newPath(Path,tempPath);
                tempEvaluation = evaluate(tempPath);
                if (tempEvaluation < bestEvaluation) {
                    copyPath(tempPath, bestPath);
                    bestT = k;
                    bestEvaluation = tempEvaluation;
                }
                r = rnd.nextDouble();
                if(tempEvaluation<PathEvaluation||Math.exp((PathEvaluation - tempEvaluation)/t)>r) {
//                    Double sss = PathEvaluation - tempEvaluation; 
//                    if(sss<0) {
//                        System.out.print(sss+"\n");
//                    }
                    copyPath(tempPath,Path);
                    PathEvaluation = tempEvaluation;
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
    private ArrayList<Position> onlyPositive (ArrayList<Position> coorList){
        String id;
        Double c;
        ArrayList<Position> cc = new ArrayList<Position>();
        for(Position p : coorList) {
            id = map.CoordinateId.get(p);
            c = map.IDcoins.get(id);
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
                dis = drone.CalDistance(p1, p2)*200000;
                distance[i][j] = dis;
                distance[j][i] = dis;
                
            }
        }
        distance[NumS-1][NumS-1] = 0.0;
    }
}
