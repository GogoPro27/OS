#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>

int main(void)
{
    pid_t pid;
    pid = fork();
    switch(pid){
        case -1:
            fprintf(stderr,"fork failed\n");
            exit(2);
        case 0:
            sleep(2);
            printf("I'm the child\n");
            printf("My parrent is %d\n", getppid());
            printf("My PID (child) is %d\n", getpid());
            exit(0);
        default:
            printf("I'm the parrent\n");
            printf("My PID (parent) is %d\n", getpid() );
            printf("My child's PID is %d\n", pid);
            printf("My parent process ID is %d", getppid());
    }
    return 0;
}