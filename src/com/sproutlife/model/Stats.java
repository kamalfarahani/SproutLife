/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.sproutlife.model.echosystem.Echosystem;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;

public class Stats {
    GameModel gameModel;
    
    public double avgAge;
    public double avgLifespan;
    public int avgMaxLifespan;
    
    double avgSize = 0;
    double avgMaxSize = 0;
    double avgTerritory = 0;
    int[] maxTerriroty = new int[100];
    int sumMaxTerritory = 0;
    
    double avgTotalMutations = 0;
    double avgRecentMutations = 0;
    
    public int c1, c2, c3, c4;
    
    public int born, die1, die2, stayed;
    public int gridSize;
    
    public int mutationCount;
    public int mutationMiss;
    
    public Mutation freqMutation;
    public int freqMuteFreq =0;
    
    public int infectedCount =0;
    
    public int[] childEnergy = new int[100];
    public int[] sproutNumber = new int[100];
    
    
    public Stats(GameModel gameModel) {
        this.gameModel = gameModel;
    }
    
    private Echosystem getEchosystem() {
        return gameModel.getEchosystem();
    }
    
    private int getTime() {
        return gameModel.getTime();
    }
    
    public String getDisplayText() {
        String text ="";
        text += "Game Time: "+getTime();
        text += "\n";
        text += "\n";
        text += "Population (Organism #): "+getEchosystem().getOrganisms().size();
        text += "\n";
        text += "\n";
        text += "Average Age: "+String.format("%.1f", avgAge);
        text += "\n";
        text += "Average Lifespan: "+String.format("%.1f", avgLifespan);
        text += "\n";
        text += "Average Max Lifespan: "+String.format("%.1f", avgMaxLifespan/10.0+1);
        text += "\n";
        text += "\n";
        text += "Average Cell #: "+String.format("%.1f", avgSize);
        text += "\n";
        text += "Average Territory: "+String.format("%.1f", avgTerritory);
        text += "\n";       
        text += "Average Max Cell#: "+String.format("%.0f", avgMaxSize);
        text += "\n";
        text += "Average Max Territory: "+(sumMaxTerritory/ getEchosystem().getOrganisms().size());
        text += "\n";
        text += "\n";
        text += "Average Mutation #: "+String.format("%.1f", avgTotalMutations);
        
        return text;
        
    }
    
    public void update() {
        updateAvgSize();
        updateAvgLifespan();
        updateAvgMutations();
    }
    
    public void printInfected() {
        System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.print(" Avg Life  " + avgMaxLifespan);
        System.out.println(" Infected born "+infectedCount);
        infectedCount = 0;
    }
    
    public void printHistogram() {
        int cellCount = 0;

        
        for (Organism o : getEchosystem().getOrganisms()) {
            int oc = o.size();
            cellCount += oc;
        }
        
        int[] countAtAge = new int[100];
        int[] countAtSize = new int[100];
        int[] countAtLifespan = new int[200];
        int[] maxSizeAtLifespan = new int[200];
        int[] maxAgeAtLifespan = new int[200];
        int[] ageHalfLifespan = new int[200];
        int[] sizeAtAge = new int[100];
        int[] mutationCount = new int[100];
        
        

        for (Organism o : getEchosystem().getOrganisms()) {
            int oc = o.size();
            cellCount += oc;

            countAtSize[o.size()/5]+=1;            
            countAtAge[o.getAge()/5]+=1;
            countAtLifespan[o.lifespan/2]+=1;
            //int mc = o.getGenome().getRecentMutations(0,getEchosystem().getTime(),o.lifespan).size();
            //mutationCount[mc/3]++;
            if (oc>maxSizeAtLifespan[o.lifespan/2]) {
                maxSizeAtLifespan[o.lifespan/2]=oc;
            }
            if (o.getAge()>maxAgeAtLifespan[o.lifespan/2]) {
                maxAgeAtLifespan[o.lifespan/2]=oc;
            }
            if (o.getAge()>=o.lifespan/2) {
                ageHalfLifespan[o.lifespan/2]++;;
            }
            
            
            sizeAtAge[o.getAge()/5]+=o.size();
        }               
        
        System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.print(" Cell count " + cellCount);
        System.out.print(" Avg cells " + cellCount*10/getEchosystem().getOrganisms().size());
        /*
        System.out.print(" Count at age: ");
        for (int i=0;i<10;i++) {
            System.out.print(countAtAge[i]+" ");
        }
        */
        /*
        System.out.print(" Count at size: ");
        for (int i=0;i<10;i++) {
            System.out.print(countAtSize[i]+" ");
        }
        */
        
        System.out.print(" Count at lifespan: ");
        for (int i=6;i<50;i++) {
            System.out.print(countAtLifespan[i]+" ");
        }
        
        System.out.print(" AMC: "+sumMaxTerritory*10/getEchosystem().getOrganisms().size());
        /*
        System.out.print(" Max cells: ");               
        for (int i=0;i<50;i++) {
            System.out.print(maxCells[i]+" ");
        }
        */
        /*
        System.out.print(" MC: ");
        for (int i=0;i<50;i++) {
            System.out.print(mutationCount[i]+" ");
        }
        */
        /*
        System.out.print(" Size at age: ");
        for (int i=0;i<10;i++) {
            if (countAtAge[i]>0) {
                System.out.print(sizeAtAge[i]*10/countAtAge[i]+" ");
            }
            else {
                System.out.print("0 ");
            }
        }
        */
    }
    
    public void printChildEnergy() {
        printHistogram();
       
        System.out.print(" Avg Life " + avgMaxLifespan);        
        int allEnergy = 0;                     
        int childSum = 0;
        for (int i=0;i<5;i++) {
        	childSum +=sproutNumber[i];
        }
        if (childSum>0) {
        	System.out.print(" AVC: "+avgMaxLifespan*getEchosystem().getOrganisms().size()/childSum);
        }
        System.out.print(" RM count: "+getRecentMutationCount(getEchosystem().getTime(),5000));
        System.out.print(" CE:");
        for (int i=0;i<4;i++) {
            if (sproutNumber[i]== 0 ) {
                System.out.print(" 0");
                continue;
            }
            
            int e = childEnergy[i]*10/sproutNumber[i];
            if (i>0 && sproutNumber[i-1]>0 ) {
                e-=(childEnergy[i-1]*10/sproutNumber[i-1]);
            }
            if(i==0) {
            	allEnergy+=e;
            }
            else {
            	if (sproutNumber[0]>0) {
            		allEnergy+=e*sproutNumber[i]/sproutNumber[0];
            	}
            }
            //childEnergy[0]*10/sproutNumber[0];
            //childEnergy[1]*10/sproutNumber[0]
            //childEnergy[0]*10*sproutNumber[1]/sproutNumber[0]/sproutNumber[0];
            
            //childEnergy[0]*10;
            //childEnergy[1]*10]
            //childEnergy[0]*10*sproutNumber[1]/sproutNumber[0];

            
            System.out.print(" "+e);
        }
        
        System.out.print(" All Energy2: "+allEnergy);

        System.out.print(" Ratios: ");
        if (sproutNumber[0]>0) {
            System.out.print(" "+sproutNumber[1]*1000/sproutNumber[0]);
            System.out.print(" "+sproutNumber[2]*1000/sproutNumber[0]);
            System.out.print(" "+sproutNumber[3]*1000/sproutNumber[0]);
        }        

        System.out.println();
        childEnergy = new int[20];
        sproutNumber = new int[20];
    }    
    
    public void printMutations() {              
        
        System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.print(" Avg Life  " + avgMaxLifespan);               
        System.out.print(" RM count: "+getRecentMutationCount(10000,1000));
        System.out.print(" Mutations: " + mutationCount +" Hit: "+(mutationCount-mutationMiss) + " Percent "+(int) (mutationMiss*100/(mutationCount+0.1)));        
        if (freqMutation!=null) {
            System.out.print(" MaxFreq "+freqMuteFreq+" x "+freqMutation.getLocation().x+" y "+freqMutation.getLocation().y+" time "+freqMutation.getOrganismAge());
        }
        System.out.println();
        //else {
          
        //}
        mutationCount=0;
        mutationMiss=0;
        freqMutation = null;
        freqMuteFreq = 0;

    }
    
    private int getRecentMutationCount(int fromAge, int toAge) {
    	HashSet<Mutation> recentMutations = new HashSet<Mutation>();
    	HashMap<Mutation,Integer> totalRM = new HashMap<Mutation,Integer>();
    	
    	for (Organism o: getEchosystem().getOrganisms()) {
    	    int fromTime = getEchosystem().getTime()-fromAge;
    	    int toTime = getEchosystem().getTime()-toAge;
    	    for(Mutation m: o.getGenome().getRecentMutations(fromTime, toTime, o.lifespan)) {
    	        recentMutations.add(m);
    	        Integer mCount = totalRM.get(m);
    	        if (mCount==null) {
    	            mCount = 0;
    	        }
    	        mCount++;
    	        totalRM.put(m, mCount);
    	    }
    	}
    	int totalCount = 0;
    	for (Integer mc : totalRM.values()) {
    	    totalCount +=mc;
    	}
        return totalCount;//recentMutations.size();
    }
    
    
    public void printBDS() {
        System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
        System.out.println(" Grid: "+gridSize+" Born: "+born+" Die1: "+die1+" Die2: "+die2+" Stayed: "+stayed);
    }
    
    public void printGenerations() {
        HashSet<Organism> generation = new HashSet(getEchosystem().getOrganisms());
        
        System.out.print(getTime() + " Org count "+getEchosystem().getOrganisms().size());
           
        for (int i=1;i<60;i++) {
            HashSet<Organism> parents = new HashSet<Organism>();
            HashSet<Organism> remove = new HashSet<Organism>();
            for (Organism o: generation) {

                if(o.getAge() < i*115 ) {
                    remove.add(o);
                    Organism p = o.getParent();
                    while (p!=null && (p.getAge() < i*115)) {
                        p=p.getParent();
                    }
                    
                    if (p!=null) {                            
                        parents.add(p);
                    }
                }
            }
            generation.removeAll(remove);
            generation.addAll(parents);
            System.out.print(" "+remove.size());
        }
        System.out.println(); 
    }
    
    private void updateAvgSize() {
        int maxSizeSum = 0;
        int sizeSum = 0;
        int territorySum = 0;
        sumMaxTerritory = 0;
        for (Organism o : getEchosystem().getOrganisms()) {

            sizeSum += o.size();
            territorySum +=o.getAttributes().getTerritorySize();
            if(o.getParent()!=null) {
                int ms = o.getParent().getAttributes().maxCells;
                if (o.getParent().getParent()!=null) {
                    ms = Math.max(ms, o.getParent().getAttributes().maxCells);
                }

                maxSizeSum+=ms;
            }
            
            if(o.getParent()!=null) {
                int ts = o.getParent().getAttributes().getTerritorySize();
                if (o.getParent().getParent()!=null) {
                    ts = Math.max(ts, o.getParent().getParent().getAttributes().getTerritorySize());
                }
                if (o.getParent().getAttributes().getTerritorySize()/3<100) {
                    maxTerriroty[ts/10]++;
                }
                sumMaxTerritory+=ts;
            }
        }
        
        this.avgSize = sizeSum / (double) getEchosystem().getOrganisms().size();
        this.avgTerritory = territorySum / (double) getEchosystem().getOrganisms().size();
        this.avgMaxSize = maxSizeSum/ (double) getEchosystem().getOrganisms().size();
    }
    
    private void updateAvgLifespan() {
        int ageSum = 0;
        for (Organism o : getEchosystem().getOrganisms()) {
            ageSum +=o.getAge();
        }
        if (getEchosystem().getOrganisms().size()>0) {
            this.avgAge = ageSum / (double) getEchosystem().getOrganisms().size();
        }
        else {
            this.avgAge = 0;
        }
        
        int lifespanSum = 0;
        for (Organism o : getEchosystem().getRetiredOrganisms()) {
            lifespanSum +=o.getAge();
        }
        if (getEchosystem().getRetiredOrganisms().size()>0) {
            this.avgLifespan = lifespanSum / (double) getEchosystem().getRetiredOrganisms().size();
        }
        else {
            this.avgLifespan = 0;
        }
        
        int maxLifespanSum = 0;

        for (Organism o : getEchosystem().getOrganisms()) {
            maxLifespanSum +=o.lifespan;
        }
        this.avgMaxLifespan = maxLifespanSum*10/ getEchosystem().getOrganisms().size();

    }
    
    public void updateAvgMutations() {
        int mutationSum=0;
        int recentMutationSum=0;
        for (Organism o : getEchosystem().getOrganisms()) {
            int fromTime = getEchosystem().getTime()-2000;
            int toTime = getEchosystem().getTime();
            //recentMutationSum+=o.getGenome().getRecentMutations(fromTime, toTime, o.lifespan).size();
            mutationSum +=o.getGenome().getRecentMutations(0, toTime, o.lifespan).size();     
        }
        avgTotalMutations = mutationSum / (double) getEchosystem().getOrganisms().size();
        //avgRecentMutations = recentMutationSum / (double) getEchosystem().getOrganisms().size();
        
        
        
    }

}
