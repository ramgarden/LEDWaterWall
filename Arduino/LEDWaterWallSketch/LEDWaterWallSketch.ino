/*
 * LED Water Wall
 *
 * The arduino will be getting analog wetness values from
 * each LED board then sending them as a byte array over
 * USB serial to a laptop running the main interface
 * program for music and other bonus output.
 *
 * https://256.makerslocal.org/wiki/LED_Water_Wall
 */

int AnalogPin1 = 0;
int AnalogPin2 = 1;
int AnalogPin3 = 2;
int AnalogPin4 = 3;
int AnalogPin5 = 4;
int AnalogPin6 = 5;
int SerialBaudRate = 115200;
int PollingSeconds = 5; //num seconds to wait between checks
int const TotalBoards = 40; //the total number of LED board modules
int wetnessValues[TotalBoards]; //all the analog values from each board

//setup all the pin modes for reading in the wetness from the 
//LED wall boards as well as getting the USB serial set up
//to send byte arrays to a laptop running the main interface
//program for music and other bonus output.
void setup()
{
  Serial.begin(SerialBaudRate);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  for (int i = 0; i < TotalBoards; i++)
  {
    wetnessValues[i] = 0; 
  }
  Serial.println("LEDWaterWall arduino serial online and ready.");
}

//gets each wetness value from the analog inputs and returns it
// as an array of bytes.
void getWetnessValues(int wetnessValues[])
{
  //loop through each board
  
  //read analog in for that board
  //analogRead();
  
  //add that value as a byte to the return array
    
  //end loop
  
  //for now just return test values
  incrementValues(wetnessValues); 
  
}

//randomly increment random values in the given array 
//by a random amount and return the array. 
void incrementValues(int wetVals[])
{  
  int index = 0;
  int increment = 0;
  int wetVal = 0;
  while (index < TotalBoards)
  {
    //First pick the random index
    index += random(5);
    
    //then choose how much to increment by.
    increment = random(500);
    
    //get the value at that index (bounds check!)
    if(index >= TotalBoards)
    {
      index = TotalBoards - 1;
    }
    
    wetVal = wetVals[index];
    
    //first time through will be all zeros so set them to something
    if (!wetVal)
    {
      wetVal = 0; 
    }
    
    //Serial.print("got index: ");
    //Serial.println(index);
    //Serial.print("got increment: ");
    //Serial.println(increment);
    //Serial.print("got wetVal: ");
    //Serial.println(wetVal);
    
    //check if that value would go over 1024 if incremented
    wetVal += increment;
    if (wetVal > 1024)
    {
      //if so then just set it to 1024
      wetVal = 1024;
    }
    
    wetVals[index] = wetVal;
    
    if(index == TotalBoards - 1)
    {
       index = TotalBoards; 
    }
  }
}

void loop()
{
  //read the values from the wall
  getWetnessValues(wetnessValues);

  //print the values to serial with commas to separate
  int i = 0;
  for(i = 0; i < TotalBoards - 1; i++)
  {
    Serial.print(wetnessValues[i]);
    Serial.print(",");
  }
  Serial.println(wetnessValues[i]);
  
  delay(PollingSeconds * 1000); //wait a few seconds.
}


