/*
*		P0^0 - P0^3 : 路灯方红外信号检测接口，用于检测路灯状态
*		P0^4 - P0^7 : 路灯方光敏信号检测接口，用于检测路灯状态
*		
*		P1^0 - P1^3	: 报警信号
*		P1^5		: 报警反馈器
*		P1^6		: 光敏信号反馈
*		P1^7		: 测试光敏用
*
*		P2^0 - P2^3	: 自动模式与非自动模式
*		P2^4 - P2^7 : 对路灯的实时开关信号		
*
*		Create by skyfffire@outlook.com in 2017-6-1
*/

#include "tools.h"

unsigned char sl[5] = {-1, 0, 64, 128, 192};
unsigned char ch = 0;	   
unsigned char isAuto[4] = {1, 1, 1, 1};
unsigned char o[4] = {1, 1, 1, 1};
unsigned char j = 0;
unsigned int times[4] = {0, 0, 0, 0};

char mustLit(unsigned char num);												// 检测某一盏路灯在该亮的时候亮没亮

void main()
{
	init_serialcomm();
	delay_1s();	  
	  
	P1 = 0XFF;

	while (1)
	{
		// 检测是否有报警信号, 有就触发报警器
		opreation(1, 5, !(times[0] || times[1] || times[2] || times[3]));

		// 报警信号持久化
		if (!getInfo(1, 0)) times[0] = 4000;
		if (!getInfo(1, 1)) times[1] = 4000;
		if (!getInfo(1, 2)) times[2] = 4000;
		if (!getInfo(1, 3)) times[3] = 4000;

		// 报警信号还在
		if (times[0] || times[1] || times[2] || times[3])
		{
			if (times[0]) times[0]--;
			if (times[1]) times[1]--;
			if (times[2]) times[2]--;
			if (times[3]) times[3]--;

			// 发送具体报警位置到Java
			send_char_com(((times[3] > 0) << 3) + ((times[2] > 0) << 2) 
			+ ((times[1] > 0) << 1) + ((times[0] > 0)));
		}

		// 手动中断
		if (RI)
		{
			ch = SBUF;

			isAuto[ch >> 6] = ((ch & 2) == 2);

			// 管理员控制优先级高于光敏控制优先级，故非自动模式不启用光敏信号
			// 自动模式判断
			if (isAuto[ch >> 6])
			{
				// 获取光敏感应信号是否不是夜间、雨天、雾天等能见度底的天气
				if (!getInfo(1, 6) && !getInfo(1, 7))
				{
					opreation(2, ch >> 6, 0);
	
					opreation(2, ch >> 6 + 4, OFF);
				}
				else
				{
					opreation(2, ch >> 6, isAuto[ch >> 6]);
				}
			}
			else
			{
				// 非自动模式下
				// 声明：服务端即将控制此路灯
				opreation(2, ch >> 6, isAuto[ch >> 6]);
				// 发送服务端控制信号
				o[ch >> 6] = !(ch & 1);
				opreation(2, (ch >> 6) + 4, o[ch >> 6]); 
			}

			// 重新标识指令
			RI = 0;
		}
		else
		{
			// 对四盏路灯的控制信号进行遍历
			for (j = 0; j <= 3; j++)
			{
				if (isAuto[j])
				{	
					// 获取光敏感应信号是否不是夜间、雨天、雾天等能见度底的天气
					if (!getInfo(1, 6) && !getInfo(1, 7))
					{
						opreation(2, j, 0);
								
						opreation(2, j + 4, OFF);
					}
					else
					{
						opreation(2, j, isAuto[j]);
					}
				}
				else
				{
					opreation(2, j, isAuto[j]);

					opreation(2, j + 4, o[j]);
				}
			}
		}

		
		// 1111-0000为前面的240，标识路灯检测信号
		// 后面分别左移路灯号对应位数，方便Java进行数据处理
		send_char_com(240 + (mustLit(3) << 3) + (mustLit(2) << 2) 
		+ (mustLit(1) << 1) + (mustLit(0)));

		delay_1us();
	}
}

char mustLit(unsigned char num)
{
	char j = 0;
	// 服务端控制时
	if (!isAuto[num] && !o[num] && getInfo(0, 4 + num))
	{
		return 0;
	}

	// 本地控制时
	if (!getInfo(0, num) && getInfo(0, 4 + num))
	{
		return 0;
	}

	return 1;
}
