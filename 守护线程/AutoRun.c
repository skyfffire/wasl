#include <windows.h>
#include <stdio.h>

int main()
{
	printf("Start tool time:");
	system("java PrintTime");

	while (1)
	{
		sleep(2);

		printf("\nshutdown time£º");
		system("java PrintTime");
		system("shutdown.bat");

		sleep(30);

		printf("   start Tomcat time£º");
		system("java PrintTime");
		system("startup.bat");

		sleep(60*60*6);
	}

	return 0;
}