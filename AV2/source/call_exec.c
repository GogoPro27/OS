#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>

int main()
{
        printf("Hello from main");
        
        // Niza od pokazhuvaci koja zavrshuva so NULL
        char *args[]={"./exec_example", NULL};
        execvp(args[0],args);
     
        /*
            Site naredbi po execvp se ignorirani bidejki 
            e zamenet process image-ot
        */
        printf("Ending-----");
     
    return 0;
}