/*									
*		P0.0-P0.3	:	路灯信号控制
*		P0.4-P0.7	:	红外信号反馈
*		P1.0-P1.3	:	由本控制终端发送到下一个路灯的信号
*		P1.4-P1.7	:	由上一个终端发送到本控制终端的信号
*		P2.0-P2.3	:	是否自动模式
*		P2.4-P2.7	:	由Web获取的控制信号
*
*		Create by skyfffire@outlook.com in 2017-6-1
*/

#include "tools.h"

#define COUNT 30
#define TIME 50

unsigned char litTime[4] = {0, 0, 0, 0};

void main()
{
	int i = 0;
	unsigned char isAuto = 0;
	unsigned char isLine = 0;
	unsigned char isHeadLit = 0;
			   
    P1 = 0XFF;

	while (1)
	{
		// 遍历四个路灯
		for (i = X_MIN; i <= 3; i++)
		{
			// 获取串口通讯端发来的自动控制信号
			isAuto = getInfo(2, i);

			// 自动模式判断，此时为手动模式
			if (!isAuto)
			{
				opreation(0, i, getInfo(2, i + 4));
			}
			else
			{	 
				// 多端信号检测，表示前方路灯发来的信号
				if ((!getInfo(1, i + 4) || !getInfo(1, i))
				&& !isHeadLit)
				{	
					if (!litTime[i])
					{
						lits[i] = ZHAN_KONG;
						isLit[i] = 1;
					}
				   	litTime[i] = COUNT;
				}

				// 红外信号检测，此处表示红外被阻挡
				if (!getInfo(0, i + 4))
				{
					// 让他与周围的灯再亮COUNT单位的TIME 

					// 上两盏
					if (i - 2 <= -1) 
					{
						isLine = 1;
						isHeadLit = 1;
						opreation(1, 8 + (i - 2), 0);
					}
					else
					{	 
						if (!litTime[i - 2])
						{
							lits[i - 2] = ZHAN_KONG;
							isLit[i - 2] = 1;
						}
						litTime[i - 2] = COUNT;
					}

					// 上一盏
					if (i - 1 <= -1)
					{
						isLine = 1;
						isHeadLit = 1;
						opreation(1, 8 + (i - 1), 0);
					} 
					else
					{	
						if (!litTime[i - 1])
						{
							lits[i - 1] = ZHAN_KONG;
							isLit[i - 1] = 1;
						}
						litTime[i - 1] = COUNT;
					}

					// 红外所在盏处理
					if (!litTime[i])
					{
						lits[i] = ZHAN_KONG;
						isLit[i] = 1;
					}
					litTime[i] = COUNT;

					// 下一盏的处理，判断是否需要多端通讯
					if (i + 1 >= 4)
					{
						isLine = 1;
						opreation(1, (i + 1) % 4, 0);
					}
					else
					{	
						if (!litTime[i + 1])
						{
							lits[i + 1] = ZHAN_KONG;
							isLit[i + 1] = 1;
						}

						litTime[i + 1] = COUNT;
					}

					// 下一盏的下一盏的处理，判断是否需要多端通讯
					if (i + 2 >= 4)
					{
						isLine = 1;

						opreation(1, (i + 2) % 4, 0);
					}
					else
					{	
						if (!litTime[i + 2])
						{
							lits[i + 2] = ZHAN_KONG;
							isLit[i + 2] = 1;
						}

						litTime[i + 2] = COUNT;
					}
				}
	
				// 如果不做这个判断，会出现：litTime[i] == 255，也就是数据下溢
				if (litTime[i]) 
				{
					litTime[i] -= 1;

					if (!litTime[i]) 
					{
						lits[i] = 0;
						isLit[i] = 0;
					}
				}
	
				opreation(0, i, litTime[i] == 0);
			}
		}  

		delay(TIME); 

		if (isLine) 
		{
			P1 = 0XFF;

			isLine = 0;
			isHeadLit = 0;
		}
	} 
}