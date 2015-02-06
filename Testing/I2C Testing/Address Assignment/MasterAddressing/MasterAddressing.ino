// Master Module Address Assignment template

#include <Wire.h>

#define DECODER_1 A0
#define DECODER_2 A1
#define DECODER_3 A2
#define DECODER_4 A3
#define DATA 12
#define CLOCK 13

byte decoder[16][4] = {
 {0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 0, 1, 1}, 
 {0, 1, 0, 0}, {0, 1, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1},  
 {1, 0, 0, 0}, {1, 0, 0, 1}, {1, 0, 1, 0}, {1, 0, 1, 1}, 
 {1, 1, 0, 0}, {1, 1, 0, 1}, {1, 1, 1, 0}, {1, 1, 1, 1}
};

// branch wise structuring {slave1address, slave2address, slave3address}
byte slaveStructure[15][4] = {
 {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, 
 {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},  
 {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, 
 {0, 0, 0}, {0, 0, 0}, {0, 0, 0}
};

// {starting address, terminal rank - starting from 1}
// 7 slaves addresses for each branch
byte branchDetails[15][2] = {
  {8,0}, {15,0}, {22,0}, {29,0}, {36,0},
  {43,0}, {50,0}, {57,0}, {64,0}, {71,0},
  {78,0}, {85,0}, {82,0}, {99,0}, {106,0}
};

byte address_to_assign = 0;

void setup()
{
  pinMode(DECODER_1, OUTPUT);
  pinMode(DECODER_2, OUTPUT);
  pinMode(DECODER_3, OUTPUT);
  pinMode(DECODER_4, OUTPUT);
  
  Wire.begin();
}

void loop()
{

}

/* Functions Related to Address Allocation */

// address allocation for the whole slave system
void addressAllocation(){
  
  for (int i = 0; i < 15; i++) {
    int address = branchDetails[i][0];
    boolean hasSlave = false;
    int j =0;                    // slave position along the path i
    decode(i);                  // activate select line of first module of the branch
    delay(10);                  //change accordingly
    resetDecoder();             // set low - select pin
    
    Wire.beginTransmission(11);  // check if the module is present
    if(Wire.endTransmission()==0){
      hasSlave = true;
    }
    else{
      hasSlave = false;
    }
     
    while(hasSlave){
      Wire.beginTransmission(11); // set device to address setting mode
      Wire.write('A'); 
      Wire.endTransmission();
      
      Wire.beginTransmission(11); // send address to set
      Wire.write(address); 
      Wire.endTransmission(); 
      
      Wire.beginTransmission(address); // activate the select line of next module
      Wire.write('B'); 
      Wire.endTransmission();
      
      delay(10);                  //change accordingly      
      
      Wire.beginTransmission(address); // set low - select line
      Wire.write('C'); 
      Wire.endTransmission(); 
      
      Wire.beginTransmission(11);  // check if the module is present
      if(Wire.endTransmission()==0){
        hasSlave = true;
      }
      
      slaveStructure[i][j] = address;
      branchDetails[i][1] = j+1;
      address ++;      
      j++;
    }
  }
  resetDecoder();
}

//NO NEED TO RESET AS ADDRESSING IS DONE BRANCH WISE
// reset all the slaves in the system to default addresses
//void resetAllSlaves(){
//  for (int i = 0; i < 15; i++) {
//    for (int i = 0; i < 3; i++) {
//      if(slaveStructure[i][j] != 0){
//        Wire.beginTransmission(slaveStructure[i][j]);
//        Wire.write('R'); // command to reset to default address
//        Wire.endTransmission();
//        slaveStructure[i][j] = 0;
//      }
//    }
//  }
//}

// check whether there are new slaves added in run time
boolean checkForNewSlaves(){
  boolean newSlave = false;
  Wire.beginTransmission(11);  // check if a new module is present
  if(Wire.endTransmission()==0){
    newSlave = true;
  }
  return newSlave;
}

// update structure allong with address allocation to changed structure during run time
void updateAddressAllocation(){
  
  for (int i = 0; i < 15; i++) {
    int address = branchDetails[i][0];
    boolean hasSlave = false;
    boolean newSlave = false;
    int j =0;                    // slave position along the path i
    decode(i);                  // activate select line of first module of the branch
    delay(10);                  //change accordingly
    resetDecoder();             // set low - select pin  
    
    Wire.beginTransmission(11);  // check if a new module is present
    if(Wire.endTransmission()==0){
      hasSlave = true;
      newSlave = true;
    }
    else{
      hasSlave = false;
    }
    
    if(!newSlave){
      Wire.beginTransmission(address);  // check if the module is present
      if(Wire.endTransmission()==0){
          hasSlave = true;
      }
    }
     
    while(hasSlave){
      
      if(newSlave){
        Wire.beginTransmission(11); // set device to address setting mode
        Wire.write('A'); 
        Wire.endTransmission();
        
        Wire.beginTransmission(11); // send address to set
        Wire.write(address); 
        Wire.endTransmission(); 
      }
      
      Wire.beginTransmission(address); // activate the select line of next module
      Wire.write('B'); 
      Wire.endTransmission();
      
      delay(10);                  //change accordingly      
      
      Wire.beginTransmission(address); // set low - select line
      Wire.write('C'); 
      Wire.endTransmission(); 
      
      newSlave = false;
      
      Wire.beginTransmission(11);  // check if a new module is present
      if(Wire.endTransmission()==0){
        hasSlave = true;
        newSlave = true;
      }
      else{
        hasSlave = false;
      }
      
      if(!newSlave){
        Wire.beginTransmission(address);  // check if the module is present
        if(Wire.endTransmission()==0){
          hasSlave = true;
        }
      }     
      
      slaveStructure[i][j] = address;
      branchDetails[i][1] = j+1;
      address ++;      
      j++;
    }
    
    for(int k=j; j<sizeof(slaveStructure[i]); j++){
      slaveStructure[i][j] = 0;
    }
  }
  resetDecoder();
}

/* Functions related to Decoder */

// select lines through decoder
void decode(int line)
{
  selectDecoderLines(decoder[line][3],decoder[line][2],decoder[line][1],decoder[line][0]);
}

// reset the decoder
void resetDecoder()
{
  selectDecoderLines(decoder[15][3],decoder[15][2],decoder[15][1],decoder[15][0]);
}

// select a line through decoder given the binary sequence
void selectDecoderLines(byte line1, byte line2, byte line3, byte line4){
  digitalWrite(DECODER_1, line1);
  digitalWrite(DECODER_2, line2); 
  digitalWrite(DECODER_3, line3);
  digitalWrite(DECODER_4, line4); 
}


