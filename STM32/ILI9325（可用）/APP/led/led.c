 #include"led.h"

void LED_GPIO_Config(void)
{
     GPIO_InitTypeDef GPIO_InitStructure;//定义一个GPIO_InitTypeDef类型的结构体


	 RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB,ENABLE);//开启外设时钟

	 GPIO_InitStructure.GPIO_Pin=GPIO_Pin_6 | GPIO_Pin_7 | GPIO_Pin_8 | GPIO_Pin_9;

	 GPIO_InitStructure.GPIO_Mode=GPIO_Mode_Out_PP;//设置为通用推挽输出模式

	 GPIO_InitStructure.GPIO_Speed=GPIO_Speed_50MHz;//设置外设速率为50MHz

	 GPIO_Init(GPIOB,&GPIO_InitStructure);//调用GPIO_Init函数，初始化GPIOC

	 GPIO_SetBits(GPIOB,GPIO_Pin_6 | GPIO_Pin_8); 
	 GPIO_ResetBits(GPIOB,GPIO_Pin_7 | GPIO_Pin_9);
}


