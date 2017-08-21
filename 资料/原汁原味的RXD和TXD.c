#include <string.h>
#include "tools.h"

/*#define INBUF_LEN 7   					// 读取数据长度
unsigned char inbuf1[INBUF_LEN];			// 读取字符串
unsigned char checksum, count3, count=0;
bit           read_flag=0;*/

void init_serialcomm(void)
{
	TMOD=0x20;		// 设置定时器1为模式2
	TH1=0xfd;		// 装初值设定波特率
	TL1=0xfd;
	TR1=1;			// 启动定时器
	SM0=0;			// 串口通信模式设置
	SM1=1;
}

//向串口发送一个字符
void send_char_com(char ch)
{
    SBUF = ch;
    while(TI == 0);
    TI = 0;
    delay(10);
}

//向串口发送一个字符串，strlen为该字符串长度
/*void send_string_com(char *str,int strlen)
{
    int k=0;

	for (k = 0; k <= strlen - 2; k++) {
        send_char_com(str[k]);
	}
}*/


//串口接收中断函数
/*void serial () interrupt 4 using 3
{
    if (RI)
    {
        unsigned char ch1;
        RI = 0;

        ch1 = SBUF;
        inbuf1[count++] = ch1;
        if(count == INBUF_LEN)
        {
        	read_flag = 1;  	// 如果串口接收的数据达到INBUF_LEN个，且校验没错，
			count = 0;      	// 就置位取数标志
        }
    }
}*/

void delay_1s()     //1s
{
    unsigned int i;
    for(i=0;i<45000;i++)
    {
    }
}

void delay_1us()     //1us
{
    unsigned int i;
    for(i=0;i<45;i++)
    {
    }
}

void main()
{
    init_serialcomm(); 			// 初始化串口
	delay_1s();
	send_char_com(112);			// 向串口发送数据
}