#include <stdio.h>
#include <stdlib.h>

int main(void) 
{
    char command[1024];
    printf("Enter commands,one per line:\n");

    while (gets(command) != NULL)
    {
        system(command);
    }
    
    exit(0);
}