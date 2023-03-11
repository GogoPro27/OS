#include <stdio.h>
#include <sys/types.h>

int main(void)
{
    pid_t pid;
    pid = fork();

    if(pid == 0) 
    {
        printf("Dete proces na prvo nivo. Ke startuvam svoj dete proces.\n");
        pid = fork(); // startuvame nov proces samo ako nie sme deteto​
    }

    if(pid == 0) 
    {
        printf("Dete proces na vtoro nivo. Ke startuvam svoj dete proces.\n");
        pid = fork(); // startuvame nov proces samo ako nie sme deteto​
    }

    if(pid == 0) 
    {
        printf("Dete proces na treto nivo. Nema da startuvam svoj dete proces.\n");
    }

    printf("Startuvan e eden proces od programata od primer 5. Sekoj proces ja pechati ovaa poraka. Mojot PID e %d Mojot roditel e %d \n",getpid(),getppid());

    if (pid > 0) 
    {
        printf("Ovaa poraka e ispechatena od procesot tatko\n");
        wait(NULL); //mora da go pocekame naseto dete da zavrsi, pred da mu javima na roditelot deka sme zavrsile. ​

        //bez ova programata ne zavrsuva​
        exit(0);
    }
    
    if (pid == 0) 
    {
        printf("Ovaa poraka e ispechatena od procesot dete\n");
        exit(0); // dete na najnisko nivo. Ne mora da cheka​
    }
}
