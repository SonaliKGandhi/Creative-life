/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bayesianclassifier;
import bayesianclassifier.*;
import static bayesianclassifier.Bayesianclassifier.A_i;
import static bayesianclassifier.Bayesianclassifier.pop_size;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author DC
 */
import weka.classifiers.bayes.net.search.SearchAlgorithm;
//import weka.classifiers.SearchAlgorithm;
import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;
import java.util.Random;
import java.util.Stack;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.net.ParentSet;

public class bat3 extends SearchAlgorithm {
    static final long serialVersionUID = 6164792240778525312L;
    static int parent;
    static int edge;
                        int jnew;

  boolean  hasFound;
    
  //  protected int m_nMaxNrOfParents = 3;

   
    protected boolean m_bInitAsNaiveBayes = true;
    
    static double [][] frequency=new double[5][5];
    static int numberOfNodes;
    int maxedge;
    static int sum;
    int maxparent;
    int id;
    int[] A_i;
   
   

    public void setId(int id) {
        this.id = id;
    }
    
    public bat3() {
        
        this.frequency=Bayesianclassifier.frequency;
    } 

     public boolean hasCycles(BayesNet bayesNet) {
      int numNodes =  bayesNet.getNrOfNodes();
      List[] adj = new ArrayList[numNodes];
      for(int i= 0; i<numNodes; i++){
          adj[i] = new ArrayList<>();
      }
      //create the adj list
      for(int i= 0; i<numNodes; i++){
        ParentSet parents = bayesNet.getParentSet(i);
        int[] parentList = parents.getParents();
        int parent_count = parents.getNrOfParents();
        for(int j=0; j<parent_count; j++){
            //printing for debugging
//           System.out.println(" Parents of node " + i + " : " + parentList[j]);
           adj[parentList[j]].add(i);
        }
      }
      
//      //print the adj. list for debugging.
//      System.out.println("printing the adjancey list.");
//      for (int i=0; i<numNodes; i++){
//          String s="Adj. List of node " + i + ": ";
//          for (int j =0; j<adj[i].size(); j++){
//              s = s + " " + adj[i].get(j);
//          }
//          System.out.println(s);
//      }
      
      // do a dfs search to identify a cycle.
      //0=white, 1= gray, 2=black
      int[] colorNode = new int[numNodes];
      for (int i=0; i<numNodes; i++){
          colorNode[i]=0;
      }
      hasFound=false;
      for (int i=0; i<numNodes; i++){
          if(colorNode[i]==0){
              dfsVisit(adj, i, colorNode);
              if(hasFound==true)return true;
          }
          if(hasFound==true)return true;
      }
      
      return false;
  }
    private void dfsVisit(List[] adj, int i, int[] colorNode) {
 //       System.out.println("Calling dfsVisit function for node: " + i);
        colorNode[i]=1;  // set the node color to gray
        List nodeAdjList = adj[i];
        for (int j=0; j<nodeAdjList.size(); j++){
            int k=(int)adj[i].get(j);
            if (colorNode[k]==1){
    //            System.out.println(" Found a back edge of color gray: " + i + " --> " + k);
                hasFound=true;
                return;
            }
            if(colorNode[k]==0){
                dfsVisit(adj, k, colorNode);
            }
        }
        colorNode[i]=2;
    }
    
    
    /*protected boolean hashcycles(BayesNet bayesNet,Instances instances, int j,int k)
    {
      if (j== k)
      {
           return false;
      }
      
      if(isArc(bayesNet,j,k))           
      {
          return false;
      }
      int nNodes = instances.numAttributes();
       // System.out.println("node is"+nNodes);
      boolean[] bDone = new boolean[nNodes];

      for (int iNode = 0; iNode < nNodes; iNode++)      //declare all attributes to false
      {
        bDone[iNode] = false;
      }

      bayesNet.getParentSet(j).addParent(k, instances);

        for (int iNode = 0; iNode < nNodes; iNode++) 
        {

            
            boolean bFound = false;                     //new variable set to false

            for (int iNode2 = 0; !bFound && iNode2 < nNodes; iNode2++)
            {
                if (!bDone[iNode2]) 
                {
                    boolean bHasNoParents = true;

                    for (int iParent = 0; iParent < bayesNet.getParentSet(iNode2).getNrOfParents(); iParent++)
                    {
                        if (!bDone[bayesNet.getParentSet(iNode2).getParent(iParent)])
                        {
                            bHasNoParents = false;
                        }
                    }

                    if (bHasNoParents) {
                        bDone[iNode2] = true;
                        bFound = true;
                    }
                }
            }

            if (!bFound) {

                bayesNet.getParentSet(j).deleteLastParent(instances);

                return false;
            }
        }

        bayesNet.getParentSet(j).deleteLastParent(instances);

        return true;
    } 
   protected boolean reversearc(
        BayesNet bayesNet,
        Instances instances,
        int j,
        int k) {
      
        if (j == k) {
            return false;
        }

        
        if (!isArc(bayesNet, j, k)) {
            return false;
        }

       
        int nNodes = instances.numAttributes();
      //  System.out.println("node 1:"+nNodes);
        boolean[] bDone = new boolean[nNodes];

        for (int iNode = 0; iNode < nNodes; iNode++) {
            bDone[iNode] = false;
        }

        
		bayesNet.getParentSet(k).addParent(j, instances);

        for (int iNode = 0; iNode < nNodes; iNode++) {

            
            boolean bFound = false;

            for (int iNode2 = 0; !bFound && iNode2 < nNodes; iNode2++) {
                if (!bDone[iNode2]) {
                	ParentSet parentSet = bayesNet.getParentSet(iNode2);
                    boolean bHasNoParents = true;
                    for (int iParent = 0; iParent < parentSet.getNrOfParents(); iParent++) {
                        if (!bDone[parentSet.getParent(iParent)]) {
                        	
                            
                            if (!(iNode2 == j && parentSet.getParent(iParent) == k)) {
                                bHasNoParents = false;
                            }
                        }
                    }

                    if (bHasNoParents) {
                        bDone[iNode2] = true;
                        bFound = true;
                    }
                }
            }

            if (!bFound) {
                bayesNet.getParentSet(j).deleteLastParent(instances);           //hnjbjkjk j
                return false;
            }
        }

        bayesNet.getParentSet(j).deleteLastParent(instances);
        return true;
    } 
    
    
    @Override
    protected boolean isArc(BayesNet bayesNet, int k, int j) {
        for (int iParent = 0; iParent < bayesNet.getParentSet(j).getNrOfParents(); iParent++) {
            if (bayesNet.getParentSet(j).getParent(iParent) == k) {
                return true;
            }
        }

        return false;
    } 
    */
    
   /* public static void initialization()throws Exception
    {
          frequency=Bayesianclassifier.frequency;
    
         for(int i=0; i<5;i++)
         {
             for(int j=0;j<5;j++)
             {
                 System.out.println("main matrix:::"+i+j+"   "+frequency[i][j]);
             }
         }
 
    
    }*/
    
    
    
     public static double[] frequencyValue(int Edge,int numberOfNodes)throws Exception
    {
        int n=numberOfNodes;
        double[][] maxfrequency=new double[n][n]; 
        int ed=Edge;
        
        int position[]=new int [2];
        int a=0;
        double positionNumber;
        double maxfreq[]=new double[Edge+Edge];
    
       for(int i=0; i<n;i++)                    //copy frequency array to the maxfrequency
         {
             for(int j=0;j<n;j++)
             {
                 maxfrequency[i][j]=frequency[i][j];
             }
         }
        
        for(int e=0;e<ed;e++)                   //itrate till maximum edge
        {   
            double max=maxfrequency[0][0];
            for(int i1=0;i1<n;i1++)
            {
               for(int j1=0;j1<n;j1++)
               {
                   if(maxfrequency[i1][j1] > max)
                    {
                               max=maxfrequency[i1][j1];
                               position[0]=i1;
                               position[1]=j1;                               
                    }
                          
                }
            }
            
           maxfrequency[position[0]][position[1]]=0;
           positionNumber=position[0]*10+position[1];
           maxfreq[a++]=max;
           maxfreq[a++]=positionNumber;
             
        } 
      /* for(int i1=0; i1<5;i1++)
         {
             for(int j1=0;j1<5;j1++)
             {
                 System.out.println("matrix.fre............"+i1+j1+"   "+frequency[i1][j1]);
             }
         }
        for(int i1=0; i1<5;i1++)
         {
             for(int j1=0;j1<5;j1++)
             {
                 System.out.println("matrix.oooooofre............"+i1+j1+"   "+ofre[i1][j1]);
             }
         }*/
       // for(int z=0;z<maxfreq.length;z++)
     //   System.out.println("......................................................................."+maxfreq[z]);
           
        return maxfreq;
   
   }
     
     
     
   public void setmaxedges(int Edge)throws Exception
    {
       
       edge=Edge;
        
    }
     
    
  public void setmaxparents(int Parent)throws Exception
    {
        //set maximum possible parents
       // System.out.println("......second file "+ parent);
        parent = Parent;
        //maxparent=parent-1;
        //return maxparent;
       
    }
 
  
  
  
    @Override
    public void buildStructure(BayesNet bayesNet, Instances instances) throws Exception {
      
    super.buildStructure(bayesNet,instances);
  
    Random ran = new Random();
    int numberOfNodes=bayesNet.getNrOfNodes();
 
    double[] maxf=frequencyValue(edge,numberOfNodes);    //get maximum number of frequency of edges and their edges
    
         /*   for(int c=0;c<maxf.length;c++)
             {
                 System.out.println("maxfrequency:::    "+maxf[c]);
            }
          */   
    double[] edgeweight=new double[edge];
    double[] pos=new double[edge];
    int s=0;
             
   for(int d=0;d<maxf.length;d++)                           //get only maximum number of edges from maxf array
   {
                   
      edgeweight[s++]=maxf[d];
      d++;
   }
    /*
  for(int c=0;c<edgeweight.length;c++)
   {          
      System.out.println("("+c+")" +" maxfrequency:    "+edgeweight[c]+"\n");
    
   }
    */
   s=0;
               
   for(int d=1;d<maxf.length;d++)                           //get only position of edges to be added from maxf array
   {
       pos[s++]=maxf[d];
       d++;
                   
   }
      
   // for(int c=0;c<pos.length;c++)
   //System.out.println("pos:    "+pos[c]);
            
        int x[] = new int[edge];                            
        int y[] = new int[edge];                            
                 
            for(int z=0;z<pos.length;z++)                   //get one node from pos array
            {
                 x[z]=(int) (pos[z]/10);
                 
            }
        
     //       for(int c=0;c<pos.length;c++)
      //      System.out.println("j is:    "+x[c]);
                 
            for(int z=0;z<pos.length;z++)                   //get another node from pos array 
            {
                  y[z]=(int) (pos[z]%10);
                 
            }
               
        //    for(int c=0;c<pos.length;c++)
         //   System.out.println("k is:    "+y[c]);
                 
   Stack stc=new Stack();
   Stack stp=new Stack();

  
    //int v=0;
    for(int p=0;p<edge;p++)                                  //untill number of edges
   {                    

       
      int j=x[p];
      int k=y[p];
        
      
      if(stp.search(k)!=-1 && stc.search(j)!=-1)  
     
        continue;
     
       
     
        stp.push(k);
        stc.push(j);
        
        bayesNet.getParentSet((int) j).addParent((int) k, instances); 
           
         //if(hashcycles(bayesNet,instances,j,k))
          // bayesNet.getParentSet((int) j).deleteParent((int) k, instances); 
           
          //reversearc(bayesNet,instances,j,k);
                 //bayesNet.getParentSet(j).deleteParent(k, instances);

         if(hasCycles(bayesNet)) 
         {
             bayesNet.getParentSet(j).deleteParent(k, instances);
         }
         // v++;
      
    }
   // System.out.println("parents are:"+stp);
     //  System.out.println("child are:"+stc);
    //      System.out.println("new   child are:"+jnew);
    

    }   

    
    
}
        
        
     

    
    
