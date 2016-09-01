 #include"led.h"

void LED_GPIO_Config(void)
{
     GPIO_InitTypeDef GPIO_InitStructure;//����һ��GPIO_InitTypeDef���͵Ľṹ��


	 RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB,ENABLE);//��������ʱ��

	 GPIO_InitStructure.GPIO_Pin=GPIO_Pin_6 | GPIO_Pin_7 | GPIO_Pin_8 | GPIO_Pin_9;

	 GPIO_InitStructure.GPIO_Mode=GPIO_Mode_Out_PP;//����Ϊͨ���������ģʽ

	 GPIO_InitStructure.GPIO_Speed=GPIO_Speed_50MHz;//������������Ϊ50MHz

	 GPIO_Init(GPIOB,&GPIO_InitStructure);//����GPIO_Init��������ʼ��GPIOC

	 GPIO_SetBits(GPIOB,GPIO_Pin_6 | GPIO_Pin_8); 
	 GPIO_ResetBits(GPIOB,GPIO_Pin_7 | GPIO_Pin_9);
}


