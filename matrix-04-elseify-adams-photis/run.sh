#!/bin/bash
NUM_RUNS=$1
RUN=0
COUNTER=0
SIZE_MATRIX=5
NUM_NODES=0
MAX_NODES=12
AVG_RUNS=10
REGEX="[0-9]+([.][0-9]+)?+"
PROGRAM=$2

# we must be supplied two arguments
#
if [ -z "$2" ]
then
    echo "usage: run.sh [NUM_RUNS] [PROGRAM]"
    exit 1
fi


# while we have more runs
#
while [ $RUN -lt $NUM_RUNS ]; do    

    # while we have more nodes to run
    #
    while [ $NUM_NODES -lt $MAX_NODES ]; do

	# increment the number of nodes by 2
	#
	let NUM_NODES=NUM_NODES+2

	# average time per num nodes is initially 0
	#
	AVG_TIME=0

	# run for 10 times to guage an average
	#
	while [  $COUNTER -lt $AVG_RUNS ]; do
	    
	    # collect the time to run this one round
	    #
	    TIME=$(mpiexec -f ~/hosts -n $NUM_NODES ./$PROGRAM $SIZE_MATRIX | grep -Eo $REGEX)

	    # increment the counter
	    #
	    let COUNTER=COUNTER+1

	    # add to the average
	    #
	    AVG_TIME=$(python -c "print($TIME + $AVG_TIME)")
	done

	# reset the counter
	#
	let COUNTER=0

	# divide the average time by 10 and save to file while writing to stdout
	#
	echo $(python -c "print($AVG_TIME/$AVG_RUNS)") | tee run-$SIZE_MATRIX-$NUM_NODES
    done

    # reset the number of nodes to 0
    #
    let NUM_NODES=0

    # double the size of the matrix
    #
    SIZE_MATRIX=$(python -c "print($SIZE_MATRIX * 2)")

    # increment the run variable
    #
    let RUN=RUN+1
    echo $RUN
done

# copy to my directory
#
scp -pr ./run-*-* tug35668@cis-linux2.temple.edu:/home/TU/tug35668/results/mmult_mpi_omp_non_optimized/

# clean up
#
rm run-*-*

#
# end of file
