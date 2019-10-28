package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Random;

public class Annealing {
    private Map map;
    private int N;// Number of iteration times
    private int Tem_t;// Temperature decrease times 
    private float a;// weight factor.
    private float t0;// initial temperature
     
    private Double[][] distance;
    private int NumS;
    private int bestT;
    private int[] Ghh;
    private Double GhhEvaluation;
    private int[] bestGh;
    private Double bestEvaluation;
    private int[] tempGhh;
    private Double tempEvaluation;
    private Random rnd;
    private ArrayList<Position> coorList;
    public Annealing(Map map,int n, int t, float t0, float aa,int seed) {
        this.map = map;
        this.N = n;
        this.Tem_t = t;
        this.t0 = t0;
        this.a = aa;
        rnd = new Random(seed);
        coorList = map.getCoorList();
        coorList = onlyPositive(coorList);
        NumS = coorList.size();
        calculateStationDistance(coorList);
        Ghh = new int[NumS];
        bestGh = new int[NumS];
        bestEvaluation = Double.MAX_VALUE;
        tempGhh = new int[NumS];
        tempEvaluation = Double.MAX_VALUE;
        bestT = 0;
    }
    private void initGroup() {
        int i,j;
        Ghh[0] = rnd.nextInt(NumS);
        for(i=0;i<NumS;) {
            Ghh[i] = rnd.nextInt(NumS);
            for(j=0;j<i;j++) {
                if(Ghh[i]==Ghh[j]) break; // To avoid repetition.
            }
            if(j==i) i++;
        }
    }
    // Copy data from gh1 to gh2.
    private void copyGh(int[] gh1,int[] gh2) {
        int i;
        for(i=0;i<NumS;i++) {
            gh2[i] = gh1[i];
        }
    }
    public Double evaluate(int[] ghh) {
        Double len = 0.0;
        int i;
        for(i=1;i<NumS;i++) {
            len+=distance[ghh[i-1]][ghh[i]];
        }
        len += distance[ghh[NumS - 1]][ghh[0]];
        return len;
    }
    private void neighbour(int[] Gh, int[] tempGh) {
        int i,temp,ran1,ran2;
        for(i=0;i<NumS;i++) {
            tempGh[i] = Gh[i];
        }
        ran1 = rnd.nextInt(NumS);
        ran2 = rnd.nextInt(NumS);
        while (ran1 == ran2) {
            ran2 = rnd.nextInt(NumS);
        }
        temp = tempGh[ran1];
        tempGh[ran1] = tempGh[ran2];
        tempGh[ran2] = temp;
    }
    public ArrayList<Position> givePath(){
        int i,j;
        ArrayList<Position> path = new ArrayList<Position>();
        for(i=0;i<NumS;i++) {
            j = bestGh[i];
            path.add(coorList.get(j));
        }
        return path;
    }
    public void solve() {
        initGroup();
        copyGh(Ghh,bestGh);
        bestEvaluation = evaluate(Ghh);
        GhhEvaluation = bestEvaluation;
        int k = 0;
        int n;
        float t = t0;
        Double r= 0.0;
        
        while(k<Tem_t) {
            n=0;
            while(n<N) {
                neighbour(Ghh,tempGhh);
                tempEvaluation = evaluate(tempGhh);
                if (tempEvaluation < bestEvaluation) {
                    copyGh(tempGhh, bestGh);
                    bestT = k;
                    bestEvaluation = tempEvaluation;
                }
                r = rnd.nextDouble();
                if(tempEvaluation<GhhEvaluation||Math.exp((GhhEvaluation - tempEvaluation)/t)>r) {
                    Double sss = GhhEvaluation - tempEvaluation; 
                    if(sss<0) {
                        System.out.print(sss+"\n");
                    }
                    copyGh(tempGhh,Ghh);
                    GhhEvaluation = tempEvaluation;
                }
                n++;
            }
            t = a * t;
            k++;
        }
        System.out.println("最佳长度出现代数：");
        System.out.println(bestT);
        System.out.println("最佳长度");
        System.out.println(bestEvaluation);
        System.out.println("最佳路径：");
        for (int i = 0; i < NumS; i++) {
            System.out.print(bestGh[i] + ",");
            if (i % 10 == 0 && i != 0) {
                System.out.println();
            }
        }
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
                dis = drone.CalDistance(p1, p2)*100000;
                distance[i][j] = dis;
                distance[j][i] = dis;
                
            }
        }
        distance[NumS-1][NumS-1] = 0.0;
    }
}
