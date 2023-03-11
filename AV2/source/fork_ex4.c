#include <stdio.h>
#include <sys/types.h>

int main(void)
{
    pid_t pid;
    pid = fork();
    
    if(pid > 0)
    {
        pid = fork(); 
        // startuvame nov proces samo ako nie sme roditelot. Inaku ke vlezeme vo rekurzivno startuvanje na procesi​
    }
    
    if(pid > 0)
    {
        pid = fork(); 
        // startuvame nov proces samo ako nie sme roditelot. Inaku ke vlezeme vo rekurzivno startuvanje na procesi​
    }

    printf("Startuvan e eden proces od programata od primer 4. Sekoj proces ja pechati ovaa poraka. Mojot PID e %d Mojot roditel e %d \n",getpid(), getppid());


    if (pid > 0) { 
        printf("Ovaa poraka e ispechatena od procesot tatko\n");
    }    
    
    if (pid == 0){
        printf("Ovaa poraka e ispechatena od procesot dete\n");
        exit(0);
    }

    return 0;
}