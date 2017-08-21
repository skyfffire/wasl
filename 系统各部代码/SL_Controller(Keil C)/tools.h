#include <reg52.h>

#define ON 0
#define OFF 1

#define X_MAX 7
#define X_MIN 0
#define Y_MAX 3
#define Y_MIN 0

#ifndef TOOLS_H
#define TOOLS_H

#define ZHAN_KONG 40

sbit CHA00 = P0^0;
sbit CHA01 = P0^1;
sbit CHA02 = P0^2;
sbit CHA03 = P0^3;
sbit CHA04 = P0^4;
sbit CHA05 = P0^5;
sbit CHA06 = P0^6;
sbit CHA07 = P0^7;

sbit CHA10 = P1^0;
sbit CHA11 = P1^1;
sbit CHA12 = P1^2;
sbit CHA13 = P1^3;
sbit CHA14 = P1^4;
sbit CHA15 = P1^5;
sbit CHA16 = P1^6;
sbit CHA17 = P1^7;

sbit CHA20 = P2^0;
sbit CHA21 = P2^1;
sbit CHA22 = P2^2;
sbit CHA23 = P2^3;
sbit CHA24 = P2^4;
sbit CHA25 = P2^5;
sbit CHA26 = P2^6;
sbit CHA27 = P2^7;

sbit CHA30 = P2^0;
sbit CHA31 = P2^1;
sbit CHA32 = P2^2;
sbit CHA33 = P2^3;
sbit CHA34 = P2^4;
sbit CHA35 = P2^5;
sbit CHA36 = P2^6;
sbit CHA37 = P2^7;

unsigned int ms_cnt = 0;
unsigned int lits[4] = {0, 0, 0, 0};					// 当前路灯剩余占空比
unsigned char isLit[4] = {-1, -1, -1, -1};				// 状态标识，-1表示在常亮/暗状态，0表示需要对此灯变暗，1表示需要对此灯变亮

/* 用于快捷操作某一个口信号的函数 */
void opreation(int y, int x, unsigned char o)
{
	switch (y) 
	{
		case 0: 
		{
			switch (x) 
			{
				case 0: 
				{
					CHA00 = o;
				}; break;
				case 1: 
				{
					CHA01 = o;
				}; break;
				case 2: 
				{
					CHA02 = o;
				}; break;
				case 3: 
				{
					CHA03 = o;
				}; break;
				case 4: 
				{
					CHA04 = o;
				}; break;
				case 5: 
				{
					CHA05 = o;
				}; break;
				case 6: 
				{
					CHA06 = o;
				}; break;
				case 7: 
				{
					CHA07 = o;
				}; break;
			}
		}; break;
		case 1: 
		{
			switch (x) 
			{
				case 0: 
				{
					CHA10 = o;
				}; break;
				case 1: 
				{
					CHA11 = o;
				}; break;
				case 2: 
				{
					CHA12 = o;
				}; break;
				case 3: 
				{
					CHA13 = o;
				}; break;
				case 4: 
				{
					CHA14 = o;
				}; break;
				case 5: 
				{
					CHA15 = o;
				}; break;
				case 6: 
				{
					CHA16 = o;
				}; break;
				case 7: 
				{
					CHA17 = o;
				}; break;
			}
		}; break;
		case 2: 
		{
			switch (x) 
			{
				case 0: 
				{
					CHA20 = o;
				}; break;
				case 1: 
				{
					CHA21 = o;
				}; break;
				case 2: 
				{
					CHA22 = o;
				}; break;
				case 3: 
				{
					CHA23 = o;
				}; break;
				case 4: 
				{
					CHA24 = o;
				}; break;
				case 5: 
				{
					CHA25 = o;
				}; break;
				case 6: 
				{
					CHA26 = o;
				}; break;
				case 7: 
				{
					CHA27 = o;
				}; break;
			}
		}; break;
		case 3: 
		{
			switch (x) 
			{
				case 0: 
				{
					CHA30 = o;
				}; break;
				case 1: 
				{
					CHA31 = o;
				}; break;
				case 2: 
				{
					CHA32 = o;
				}; break;
				case 3: 
				{
					CHA33 = o;
				}; break;
				case 4: 
				{
					CHA34 = o;
				}; break;
				case 5:
				{
					CHA35 = o;
				}; break;
				case 6: 
				{
					CHA36 = o;
				}; break;
				case 7: 
				{
					CHA37 = o;
				}; break;
			}
		}; break;
	}
}

/* 获取某一个口的信号 */
unsigned char getInfo(int y, int x)
{
	unsigned char ch = 0;

	switch (y) 
	{
		case 0: 
		{
			switch (x) 
			{
				case 0: 
				{
					ch = CHA00;
				}; break;
				case 1: 
				{
					ch = CHA01;
				}; break;
				case 2: 
				{
					ch = CHA02;
				}; break;
				case 3: 
				{
					ch = CHA03;
				}; break;
				case 4: 
				{
					ch = CHA04;
				}; break;
				case 5: 
				{
					ch = CHA05;
				}; break;
				case 6: 
				{
					ch = CHA06;
				}; break;
				case 7: 
				{
					ch = CHA07;
				}; break;
			}
		}; break;
		case 1: 
		{
			switch (x) 
			{
				case 0: 
				{
					ch = CHA10;
				}; break;
				case 1: 
				{
					ch = CHA11;
				}; break;
				case 2: 
				{
					ch = CHA12;
				}; break;
				case 3: 
				{
					ch = CHA13;
				}; break;
				case 4: 
				{
					ch = CHA14;
				}; break;
				case 5: 
				{
					ch = CHA15;
				}; break;
				case 6: 
				{
					ch = CHA16;
				}; break;
				case 7: 
				{
					ch = CHA17;
				}; break;
			}
		}; break;
		case 2: 
		{
			switch (x) 
			{
				case 0: 
				{
					ch = CHA20;
				}; break;
				case 1: 
				{
					ch = CHA21;
				}; break;
				case 2: 
				{
					ch = CHA22;
				}; break;
				case 3: 
				{
					ch = CHA23;
				}; break;
				case 4: 
				{
					ch = CHA24;
				}; break;
				case 5: 
				{
					ch = CHA25;
				}; break;
				case 6: 
				{
					ch = CHA26;
				}; break;
				case 7: 
				{
					ch = CHA27;
				}; break;
			}
		}; break;
		case 3: 
		{
			switch (x) 
			{
				case 0: 
				{
					ch = CHA30;
				}; break;
				case 1: 
				{
					ch = CHA31;
				}; break;
				case 2: 
				{
					ch = CHA32;
				}; break;
				case 3: 
				{
					ch = CHA33;
				}; break;
				case 4: 
				{
					ch = CHA34;
				}; break;
				case 5:
				{
					ch = CHA35;
				}; break;
				case 6: 
				{
					ch = CHA36;
				}; break;
				case 7: 
				{
					ch = CHA37;
				}; break;
			}
		}; break;
	}

	return ch;
}

/* 延时函数 */
void delay(int s_time)
{
	ms_cnt = s_time;

	TMOD = 0X01; 
	ET0 = 1;
 
	TH0 = 0XF5; //0XDB;
	TL0 = 0XFF; //0XFE;

	TR0 = 1;
	EA = 1;

	while (ms_cnt);
}

void time0() interrupt 1
{
	unsigned char i = 0;
	unsigned int j = 0;

	// 对三盏路灯进行状态扫描
	for (i = 0; i <= 3; i++)
	{
		// 检测是否为常亮/暗状态
		if (isLit[i] != -1)
		{		
			// 处理占空比
			for (j = 1; j <= ZHAN_KONG - 1; j++)
			{
				if (j + lits[i] > ZHAN_KONG)
				{
					opreation(0, i, OFF);
				}
				else
				{
					opreation(0, i, ON);
				}
			}

			// 判断在哪种状态(变亮或变暗)
			if (isLit[i]) 
			{	
				lits[i] -= 1;

				if (!lits[i]) isLit[i] = -1;
			}
			else
			{	
				lits[i] += 1;

				if (lits[i] == ZHAN_KONG) isLit[i] = -1;
			}
		}
	}

	TH0 = 0XF5; //0XDB;
	TL0 = 0XFF; //0XFE;

	ms_cnt--;
}

#endif