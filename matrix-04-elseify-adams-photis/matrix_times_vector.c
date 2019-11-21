#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define min(x, y) ((x)<(y)?(x):(y))
#define MULTIPLIER 2
#define INITIAL_SIZE 5

int main(int argc, char* argv[])
{

  /* number of rows and columns */
  /*                            */
  int size, nrows, ncols;


  double *aa, *b, *c;

  /* array buffers to send/recv data */
  /*                                 */
  double *buffer, ans;

  /* array of times */
  /*                */
  double *times;
  double total_times;
  int run_index;
  int nruns;

  /* keep track of different id of nodes */
  /*                                     */
  int myid, master, numprocs;
  double starttime, endtime;
  MPI_Status status;
  int i, j, numsent, sender;
  int anstype, row;

  /* seed the rng */
  /*              */
  srand(time(0));
  MPI_Init(&argc, &argv);

  /* size is number of nodes */
  /*                         */
  MPI_Comm_size(MPI_COMM_WORLD, &numprocs);

  /* rank is the ID of the current node */
  /*                                    */
  MPI_Comm_rank(MPI_COMM_WORLD, &myid);
  if (argc > 1) {
    nruns = atoi(argv[1]);
    nrows = INITIAL_SIZE;
    for(run_index = 1; run_index <= nruns; run_index++){

      nrows *= MULTIPLIER;
      ncols = nrows;
      
      /* allocate the matrices */
      /*                       */
      aa = (double*)malloc(sizeof(double) * nrows * ncols);
      b = (double*)malloc(sizeof(double) * ncols);
      c = (double*)malloc(sizeof(double) * nrows);
      
      /* buffer is equal to one row */
      /*                            */
      buffer = (double*)malloc(sizeof(double) * ncols);
      
      /* master is always id 0 */
      /*                       */
      master = 0;
      if (myid == master) {
	// Master Code goes here
	
	/* initialize array with random doubles */
	/*                                      */
	for (i = 0; i < nrows; i++) {
	  b[i] = (double)rand()/RAND_MAX;
	  for (j = 0; j < ncols; j++) {
	    aa[i*ncols + j] = (double)rand()/RAND_MAX;
	  }
	}
	
	starttime = MPI_Wtime();
	numsent = 0;
	MPI_Bcast(b, ncols, MPI_DOUBLE, master, MPI_COMM_WORLD);
	for (i = 0; i < min(numprocs-1, nrows); i++) {
	  for (j = 0; j < ncols; j++) {
	    buffer[j] = aa[i * ncols + j];
	  }  
	  MPI_Send(buffer, ncols, MPI_DOUBLE, i+1, i+1, MPI_COMM_WORLD);
	  numsent++;
	}
	for (i = 0; i < nrows; i++) {
	  MPI_Recv(&ans, 1, MPI_DOUBLE, MPI_ANY_SOURCE, MPI_ANY_TAG, 
		   MPI_COMM_WORLD, &status);
	  sender = status.MPI_SOURCE;
	  anstype = status.MPI_TAG;
	  c[anstype-1] = ans;
	  if (numsent < nrows) {
	    for (j = 0; j < ncols; j++) {
	      buffer[j] = aa[numsent*ncols + j];
	    }  
	    MPI_Send(buffer, ncols, MPI_DOUBLE, sender, numsent, 
		     MPI_COMM_WORLD);
	    numsent++;
	  } else {
	    MPI_Send(MPI_BOTTOM, 0, MPI_DOUBLE, sender, 0, MPI_COMM_WORLD);
	  }
	} 
	endtime = MPI_Wtime();
	printf("size %d %f\n", nrows, (endtime - starttime));
      } else {
	// Slave Code goes here
	MPI_Bcast(b, ncols, MPI_DOUBLE, master, MPI_COMM_WORLD);
	if (myid <= nrows) {
	  while(1) {
	    MPI_Recv(buffer, ncols, MPI_DOUBLE, master, MPI_ANY_TAG, 
		     MPI_COMM_WORLD, &status);
	    if (status.MPI_TAG == 0){
	      break;
	    }
	    row = status.MPI_TAG;
	    ans = 0.0;
	    for (j = 0; j < ncols; j++) {
	      ans += buffer[j] * b[j];
	    }
	    MPI_Send(&ans, 1, MPI_DOUBLE, master, row, MPI_COMM_WORLD);
	  }
	}
      }
    }
  } else {
    fprintf(stderr, "Usage matrix_times_vector <size>\n");
  }
  MPI_Finalize();
  return 0;
}
