import java.lang.System.*;;

public class matrix_mult {
    
    public static void main(String args[]){
        int nrows = Integer.valueOf(args[0]);
        int ncols = nrows;
        double aa[][] = new double[nrows][ncols];
        double bb[][] = new double[nrows][ncols];
        double cc[][] = new double[nrows][ncols];
        
//        System.out.println(nrows);
        aa = gen_matrix(nrows,ncols, 1);
        bb = gen_matrix(nrows,ncols, nrows*nrows + 1);
        long start = System.currentTimeMillis();
        for(int i = 0; i < nrows; i++){
            for(int j = 0;j<nrows;j++){
                cc[i][j] = 0.0;
            }
            for(int k = 0; k < nrows; k++){
                for(int l = 0; l<nrows; l++){
                    cc[i][l] += aa[i][k] * bb[k][l];
                }
            }
        }
        
        long end = System.currentTimeMillis();
        float sec =(end-start) / 1000F;
	System.out.println("time: " + sec + " seconds");
//        print_matrices(aa,ncols, "aa");
//        print_matrices(bb,ncols,"bb");
//        print_matrices(cc,ncols, "aa x bb Result");


    }
    
    public static double[][] gen_matrix(int nrows, int ncols, int num){
        double a[][] = new double[nrows][ncols];
        
        for(int i = 0; i< ncols; i++){
            for(int j = 0; j< nrows; j++){
                a[i][j] = num;
                num++;
            }
        }
        
        return a;
    }
    public static void print_matrices(double a[][], int ncols, String name){
        
        System.out.println(name+":");
         for(int i = 0; i< ncols; i++){
            System.out.print("[");
            for(int j = 0; j< ncols; j++){
                System.out.print(a[i][j]);
                System.out.print(",");
            }
            System.out.println("]");
        }
         System.out.println("");
    }
        
        
    
    
}
