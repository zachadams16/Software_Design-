#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/times.h>
#include <math.h>

double* gen_matrix(int n, int m);
int mmult2(double *c, double *a, double *b, int aRows, int aCols, int bRows, int bCols);
int mmult(double *c, double *a, int aRows, int aCols, double *b, int bRows, int bCols);
void print_matrix(double *matrix, int nrows, int ncols);
void compare_matrices(double* a, double* b, int nRows, int nCols);

int main(int argc, char* argv[]){

	int nrows;
	int ncols;
	double* aa;
	double* bb;
	double* cc;
	double *cc2;

	if(argc < 2){
	  printf("Pass a matrix size greater than 1/n");
	  exit(0);
	}
	nrows = atoi(argv[1]);
	ncols = nrows;

	cc = malloc(sizeof(double)  * nrows * nrows);
	cc2 = malloc(sizeof(double) * nrows * ncols);
	aa = gen_matrix(nrows, ncols);
	bb = gen_matrix(ncols, nrows);

	clock_t start = clock();
	mmult2(cc, aa, bb, nrows, ncols, ncols, nrows);
	clock_t finish = clock();
	double timeTaken = (double)(finish - start) / CLOCKS_PER_SEC;

	clock_t start2 = clock();
	mmult(cc2, aa, nrows, ncols, bb, ncols, nrows);
	clock_t finish2 = clock();
	double timeTaken2 = (double)(finish2 - start2) / CLOCKS_PER_SEC;

	/*
	print_matrix(aa, nrows, ncols);
	print_matrix(bb, nrows, ncols);
	print_matrix(cc, nrows, ncols);
	*/
	compare_matrices(cc, cc2, nrows, ncols);
	printf("Time taken mmult2: %f Time Taken mmult: %f\n", timeTaken, timeTaken2);

}

//without parallelization
int mmult2(double *c,
           double *a, double *b, int aRows, 
           int aCols, int bRows, int bCols){

  int i;
  int j;
  int k;
  
  for(i = 0; i < aRows; i++){
    for(j = 0; j < bCols; j++){
      c[i*bCols + j] = 0;
      for(k = 0; k < aCols; k++){
	c[i*bCols + j] += a[i*aCols + k] * b[k*bCols + j];
      }
    }
  }
  return 0;
}


void print_matrix(double *matrix, int nrows, int ncols){
  for(int i=0; i < nrows; i++){
    printf("[");
    for(int j=0; j < ncols; j++){
      printf("%lf", matrix[i*nrows + j]);
      if(j + 1 != ncols){
        printf(" ");
      }
    }
    printf("]\n");
  }
  puts("");
}

