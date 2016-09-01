#include "stm32f10x.h"
#include "lcd.h"

int main(void)
{
	NVIC_Configuration(); 	 //设置NVIC中断分组2:2位抢占优先级，2位响应优先级
 
	LCD_Init();	
	LCD_Clear(GREEN); 	

 	POINT_COLOR=RED;//设置字体为红色 
	LCD_ShowString(60,50,300,16,16,"M3S STM32");	
	LCD_ShowString(60,70,300,16,16,"TOUCH TEST");	
	LCD_ShowString(60,90,300,16,16,"www.doflye.net");	
}




