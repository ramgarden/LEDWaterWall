import javax.sound.midi.*;
import java.util.Scanner;

public class MIDIPlayer{
  public static void main(String arg[]){
  	int noteNumber = 0;
  	int presetNum = 0;
  	try
  	{
		Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		Instrument[] instrument = synthesizer.getDefaultSoundbank().getInstruments();
		//show all instruments in the default sound bank.
		for (Instrument currInstrument : instrument) {
			System.out.println(currInstrument.toString());
		}
		
		Scanner in = new Scanner(System.in);
		
		//Loop forever until quit flag is raised.
		while(presetNum != -1)
		{
			//now ask the user for an instrument ID
			System.out.println("Enter an instrument preset #:");
			presetNum = in.nextInt();
			System.out.println("You entered: " + presetNum);
			
			if(presetNum == -1)
			{
				break;	
			}
			
			//now ask the user for a note to play
			System.out.println("Enter a note number to play (20 - 80):");
			noteNumber = in.nextInt();
			System.out.println("You entered note: " + noteNumber);
			
			//now play the note on the selected instrument!
			synthesizer.loadInstrument(instrument[presetNum]); //was 29
			MidiChannel[] channels = synthesizer.getChannels();
			MidiChannel channel = channels[1];
			channel.programChange(29);
			channel.noteOn(noteNumber, 127);
			Thread.sleep(2000); //play the note for 2 seconds
			channel.noteOff(noteNumber);
		}
	}
	catch (Exception e)
	{
		
	}
  }
}



