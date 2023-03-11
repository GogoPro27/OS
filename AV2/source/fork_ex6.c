#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
 
int main(void)
{
    for(int i=0;i<5;i++) 
    {
        // loop will run n times (n=5)
        if(fork() == 0)
        {
            sleep(5);
            printf("[son] pid %d from [parent] pid %d\n",getpid(),getppid());
            exit(0);
        }
    }
    for(int i=0;i<5;i++) 
    {
        // loop will run n times (n=5)
        wait(NULL);
    }
     
}