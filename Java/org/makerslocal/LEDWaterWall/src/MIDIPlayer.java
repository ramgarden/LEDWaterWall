import java.io.*;
import javax.sound.midi.*;
import java.util.Scanner;
import java.util.*;

public class MIDIPlayer{
  public static void main(String arg[]){
  	//int noteNumber = 0;
  	//int presetNum = 0;
	//the total number of board modules in the LED wall.
	int TOTAL_NUM_BOARDS = 40; 
	int MAX_WETNESS_VALUE = 1024;
	int activationPoint = 0;
  	try
  	{
		//Get the "activation" point of the wetness (halfway)
		activationPoint = MAX_WETNESS_VALUE / 2;
		
		Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		Instrument[] instrument = synthesizer.getDefaultSoundbank().getInstruments();
		//show all instruments in the default sound bank.
		//for (Instrument currInstrument : instrument) 
		//{
		//	System.out.println(currInstrument.toString());
		//}
		
		//load a midi file to play.
		Sequence sequence = MidiSystem.getSequence(new File("C:\\Users\\Josh\\Documents\\GitHub\\LEDWaterWall\\Java\\org\\makerslocal\\LEDWaterWall\\src\\test.midi"));
		
		// Create a sequencer for the sequence
		Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.open();
		sequencer.setSequence(sequence);
		
		int trackNumber = 0;
		Track[] tracks = sequence.getTracks();
        for (Track track :  tracks) 
		{
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
			//set all the tracks to mute at start.
			//control change 7 is volume.
			//channel.controlChange(7, value);
			sequencer.setTrackMute(trackNumber, true);
		}	

		// Start playing
		sequencer.start();
		System.out.println("MIDI started playing!");
		
		//get a scanner to grab input from the user
		Scanner in = new Scanner(System.in);
		
		//OPTION 1: read in sets of numbers between 0 and 1024.
		//loop through 3 different test sets of numbers
		int[] valueArray = new int[TOTAL_NUM_BOARDS];
		Random generator = new Random();
		int boardDivision = 0;
		
		//Get the number of tracks in the midi file and divide it evenly among the
		// board values.
		Track[] trackArray = sequence.getTracks();
		int numTracks = trackArray.length;
		int extraBoards = 0; //holds number of extra boards than we have tracks.
		if(numTracks == TOTAL_NUM_BOARDS)
		{
			boardDivision = 1;
		}
		else
		{
			boardDivision = TOTAL_NUM_BOARDS / numTracks;
			extraBoards = TOTAL_NUM_BOARDS % numTracks;
			if( extraBoards > 0)
			{
				//then we need to ignore the last set of boards?
				//or rope the last set of boards into the same channel 
				// as the last board chunk?
			}
		}
		
		System.out.println("total num boards: " + TOTAL_NUM_BOARDS);
		System.out.println("num tracks: " + numTracks);
		System.out.println("board division: " + boardDivision);
		int valueArrayIndex = 0;
		
		//Loop until exit value
		//for(int mainLoopIndex = 0; mainLoopIndex<30;mainLoopIndex++)
		//get an instance of the serial port reader class
		SerialTest serialTest = new SerialTest();
		boolean readyToExit = false;
		while(!readyToExit)
		{		
			// //create a list of 40 random numbers between 0 and 1024.			
			// for(int i=0;i<TOTAL_NUM_BOARDS;i++)
			// {
				// valueArray[i] = generator.nextInt(MAX_WETNESS_VALUE);
			// }
			
			//get the string of wetness values from the serial			
			serialTest.initialize();
			Thread.sleep(5000); //pause for a few seconds
			String valueString = serialTest.getSerialLine();
			String[] valueArrayString = valueString.split(",");
			for (int i=0; i < valueArrayString.length; i++) {
				valueArray[i] = Integer.parseInt(valueArrayString[i]);
			}
			
			System.out.println(Arrays.toString(valueArray));
			
			trackNumber = 0;
			valueArrayIndex = 0;
			for (Track track :  tracks) 
			{
				trackNumber++;
				System.out.println("Checking wetness for track #" + trackNumber);
				//grab a boardDivision chunk of the value array and average it.
				//each chunk is a channel and if the average value is over half
				// then that track will be unmuted.
				int[] valueChunk = Arrays.copyOfRange(valueArray, valueArrayIndex, valueArrayIndex + boardDivision);
				valueArrayIndex += boardDivision;
				
				System.out.println("This track's boards values" + Arrays.toString(valueChunk));
				
				double valueTotal = 0.00;
				for(int i=0; i < valueChunk.length; i++){
					valueTotal += valueChunk[i];
				}
				double avg = (valueTotal/valueChunk.length);
				System.out.print("average for this chunk: " + avg);
				
				//if the average wetness is greater than the activation point then unmute it
				// else mute it.
				if(avg > activationPoint)
				{
					System.out.println("turning on this track!");
					sequencer.setTrackMute(trackNumber, false);
				}
				else
				{
					System.out.println("muting this track.");
					sequencer.setTrackMute(trackNumber, true);
				}
			}	
			
			//close the port
			//serialTest.close();
			
			//presetNum = in.nextInt();
			
			//if(presetNum == 1)
			//{
				//readyToExit = true;	
			//}
			
			//Thread.sleep(5000); //pause for a few seconds
		}
		
		
		
		//Loop forever until quit flag is raised.
		// while(presetNum != -1)
		// {
			// //now ask the user for an instrument ID
			// System.out.println("Enter an instrument preset #:");
			// presetNum = in.nextInt();
			// System.out.println("You entered: " + presetNum);
			
			// if(presetNum == -1)
			// {
				// break;	
			// }
			
			// //now ask the user for a note to play
			// System.out.println("Enter a note number to play (20 - 80):");
			// noteNumber = in.nextInt();
			// System.out.println("You entered note: " + noteNumber);
			
			// //now play the note on the selected instrument!
			// synthesizer.loadInstrument(instrument[presetNum]); //was 29
			// MidiChannel[] channels = synthesizer.getChannels();
			// MidiChannel channel = channels[1];
			// channel.programChange(29);
			// channel.noteOn(noteNumber, 127);
			// Thread.sleep(2000); //play the note for 2 seconds
			// channel.noteOff(noteNumber);
		// }
	}
	catch (Exception e)
	{
            System.out.println(e.toString());
	}
  }
}



