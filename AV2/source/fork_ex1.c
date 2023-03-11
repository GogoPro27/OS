#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>

int main(void)
{
    pid_t pid;
    pid = fork();
    
    if (pid == -1) 
    { 
        // Rezultat -1 znaci deka ne uspealo kreiranjeto na nov proces​
        fprintf(stderr, "fork failed\n");
        exit(1);
    }
    if (pid == 0) 
    { 
        // Ovoj kod go izvrsuva procesot-dete​
        printf("I'm the child\n");
        exit(0);
    }
    if (pid > 0) 
    {
        // Ovoj kod go izvrsuva procesot-tatko​
        printf("I'm the parent with child %d\n", pid);
        exit(0);

    }

    return 0;
}