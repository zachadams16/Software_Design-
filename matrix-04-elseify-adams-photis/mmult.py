
N = 2
C = [ [-1,-1], [-1,-1]]
A = [[1,2],
     [3,4]]
B = [[5,6],
     [7,8]]

buff = [0 for i in range(N)]
def calculate(b, aik, row):
    for l in range(N):
        C[row][l] += (aik * b[l])

for i in range(N):
    for j in range(N):
        C[i][j] = 0
    for k in range(N):
        for l in range(N):
            buff[l] = B[k][l]
        calculate(buff, A[i][k], i)
            
"""        
        for l in range(N):
            
            C[i][l] += A[i][k] * B[k][l]
            print(C[i][l])
print(C)
"""
print(C)
    
