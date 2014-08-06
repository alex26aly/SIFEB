#include <main.h>
char buffer[3];
int8 state; 

#INT_SSP
void  SSP_isr(void) 
{

      state = i2c_isr_state();

      if((state== 0 ) || (state== 0x80))

        i2c_read();

      if(state >= 0x80)

         i2c_write(buffer[0]);

      else if(state > 0)

         buffer[state - 1] = i2c_read();
         /*
         if(buffer[state - 1] == 'H')
         {
            output_high(PIN_A0);
         }
         if(buffer[state - 1] == 'L')
         {
            output_low(PIN_A0);
         }
         */

}

void main()
{

   enable_interrupts(INT_SSP);
   enable_interrupts(GLOBAL);
   SET_TRIS_D(0);
   SET_TRIS_A(0);
   setup_timer_3(T3_DISABLED|T3_DIV_BY_1);
   
while(true)
{
   printf("hello\n");
   delay_ms(1000);
   printf("value is %s\n",buffer);
}

   

}
