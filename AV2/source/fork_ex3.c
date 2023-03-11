#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>

int main(int argc, char *argv[])
{
    pid_t pid;
    if ((pid = fork()) == 0)
    { 
        /* child */
        printf("sleeping...\n");
        sleep(5);
        printf("waking ... and exiting\n");
    }
    else 
    {
        if (pid > 0)
        {
            printf("waiting for child\n");
            // wait(NULL);
            printf("child woke up\n");
        }
    }

    return 0;
}
