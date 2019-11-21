import sys
import os
import matplotlib.pyplot as plt

NUM_ARGS = 1
NEW_LINE = "\n"

def main(argv):
    if(len(argv) != NUM_ARGS):
        print("usage: python plot.py [flist]")
        return False    
    opened, flist = open_file(argv[1])
    if not opened:
        print("invalid file: %s", flist)
        return False
    files = [fname for fname in flist.read().split(NEW_LINE) if fname]
    plots = {}
    flist.close()
    X = None
    for index, ofile in enumerate(files):
        nnodes = os.path.basename(ofile).split("-")[1]
        opened, fp = open_file(ofile)
        if not opened:
            continue
        if index == 0:
            X, plots[index] = get_data_points(fp.read().split(NEW_LINE))
            continue
        _, plots[nnodes] = get_data_points(fp.read().split(NEW_LINE))
        fp.close()
    print(plots)
    return True

def get_data_points(content):
    X = []
    Y = []
    for line in content:
        tokenized = line.split()
        if len(tokenized) != 3:
            continue
        X.append(int(tokenized[1]))
        Y.append(float(tokenized[2]))
    return [X,Y]

def open_file(fname):
    try:
        fp = open(fname, "r")
        return [True, fp]
    except IOError as e:
        return [False, e.strerror]

if __name__ == '__main__':
    main(sys.argv[1:])
