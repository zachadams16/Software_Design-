#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/times.h>
#define min(x, y) ((x)<(y)?(x):(y))

double* gen_matrix(int n, int m);
int mmult(double *c, double *a, int aRows, int aCols, double *b, int bRows, int bCols);
int mmult_omp(double *c, double *a, int aRows, int aCols, double *b, int bRows, int bCols);
void compare_matrices(double *a, double *b, int nRows, int nCols);
void print_matrix(double *matrix, int nrows, int ncols);

/** 
    Program to multiply a matrix times a matrix using both
    mpi to distribute the computation among nodes and omp
    to distribute the computation among threads.
*/

int main(int argc, char* argv[])
{
  int nrows, ncols, row;
  double *aa;	/* the A matrix */
  double *bb;	/* the B matrix */
  double *buffer;
  double *cc1;	/* A x B computed using the omp-mpi code you write */
  double *cc2;	/* A x B computed using the conventional algorithm */
  int myid, numprocs;
  int master;
  int sender;
  int numsent;
  double starttime, endtime;
  double *response;
  MPI_Status status;
  MPI_Init(&argc, &argv);
  MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
  MPI_Comm_rank(MPI_COMM_WORLD, &myid);
  if (argc > 1) {
    nrows = atoi(argv[1]);
    ncols = nrows;
    master = 0;
    aa = malloc(sizeof(double) * nrows * ncols);
    bb = malloc(sizeof(double) * nrows * ncols);
    cc1 = malloc(sizeof(double) * nrows * nrows);
    buffer = (double*)malloc(sizeof(double) * ncols);
    response = (double*)malloc(sizeof(double) * nrows);    
    if (myid == master) {
      aa = gen_matrix(nrows, ncols);
      bb = gen_matrix(ncols, nrows);
      starttime = MPI_Wtime();
      numsent = 0;
      //possible solution for master
      MPI_Bcast(bb, nrows * ncols,MPI_DOUBLE, master, MPI_COMM_WORLD);
      for(int i = 0; i < min(numprocs - 1, nrows);i++){
	for(int j= 0;j<ncols; j++){
	  cc1[j*ncols+j] = 0.0;
	  buffer[j] = aa[i*ncols + j];
	}
	MPI_Send(buffer, ncols, MPI_DOUBLE, i+1, ++numsent, MPI_COMM_WORLD);
      }
      for(int i = 0; i < nrows; i++){
	MPI_Recv(response, nrows, MPI_DOUBLE, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);	
	sender = status.MPI_SOURCE;
	row = status.MPI_TAG;
	for(int l=0; l < ncols; l++){
	  cc1[row*nrows + l] = response[l];
	}
        if(numsent < nrows){
	  for(int l =0; l < nrows; l++){
	    buffer[l] = aa[numsent*nrows  + l];
	  }
	  MPI_Send(buffer, ncols, MPI_DOUBLE, sender, ++numsent, MPI_COMM_WORLD);
        } else{
      	  MPI_Send(MPI_BOTTOM, 0 , MPI_DOUBLE , sender, 0, MPI_COMM_WORLD);
        }	
      }
      endtime = MPI_Wtime();
      printf("TIME: %f\n",(endtime - starttime));
      cc2  = malloc(sizeof(double) * nrows * nrows);
      mmult(cc2, aa, nrows, ncols, bb, ncols, nrows);
      compare_matrices(cc2, cc1, nrows, nrows);   
    } else {
     MPI_Bcast(bb, nrows * ncols, MPI_DOUBLE,master, MPI_COMM_WORLD);
     if(myid <= nrows){
      while(1){
	MPI_Recv(buffer, ncols, MPI_DOUBLE, master, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
        if(status.MPI_TAG == 0){
	  break;
        }
	row = status.MPI_TAG - 1;
	mmult_omp(response, buffer, 1, ncols, bb, nrows, ncols);
	MPI_Send(response, nrows, MPI_DOUBLE, master, row, MPI_COMM_WORLD);
      }
     }
    }
  } else {
    fprintf(stderr, "Usage matrix_times_vector <size>\n");
  }

  MPI_Finalize();

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
