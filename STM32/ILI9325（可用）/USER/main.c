#include "stm32f10x.h"
#include "lcd.h"

int main(void)
{
	NVIC_Configuration(); 	 //����NVIC�жϷ���2:2λ��ռ���ȼ���2λ��Ӧ���ȼ�
 
	LCD_Init();	
	LCD_Clear(GREEN); 	

 	POINT_COLOR=RED;//��������Ϊ��ɫ 
	LCD_ShowString(60,50,300,16,16,"M3S STM32");	
	LCD_ShowString(60,70,300,16,16,"TOUCH TEST");	
	LCD_ShowString(60,90,300,16,16,"www.doflye.net");	
}




