package assembler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.hamcrest.core.IsNull;

import components.Register;

import architecture.Architecture;

public class Assembler {
	
	private ArrayList<String> lines;
	private ArrayList<String> objProgram;
	private ArrayList<String> execProgram;
	private Architecture arch;
	private ArrayList<String>commands;	
	private ArrayList<String>labels;
	private ArrayList<Integer> labelsAdresses;
	private ArrayList<String>variables;
	
	
	public Assembler() {
		lines = new ArrayList<>();
		labels = new ArrayList<>();
		labelsAdresses = new ArrayList<>();
		variables = new ArrayList<>();
		objProgram = new ArrayList<>();
		execProgram = new ArrayList<>();
		arch = new Architecture();
		commands = arch.getCommandsList();	
	}
	
	//getters
	
	public ArrayList<String> getObjProgram() {
		return objProgram;
	}
	
	
	protected ArrayList<String> getLabels() {
		return labels;
	}
	
	protected ArrayList<Integer> getLabelsAddresses() {
		return labelsAdresses;
	}
	
	protected ArrayList<String> getVariables() {
		return variables;
	}
	
	protected ArrayList<String> getExecProgram() {
		return execProgram;
	}
	
	protected void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}	

	protected void setExecProgram(ArrayList<String> lines) {
		this.execProgram = lines;
	}	
	
	
	/**
	 * This method reads an entire file in assembly 
	 * @param filename
	 * @throws IOException 
	 */
	public void read(String filename) throws IOException {
		   BufferedReader br = new BufferedReader(new		 
		   FileReader(filename+".dsf"));
		   String linha;
		   lines.add("startStk $DEGAS_START_VALUE$");
		   while ((linha = br.readLine()) != null) {
			     lines.add(linha);
			}
			br.close();	
	}
	

	/**
	 * This method scans the strings in lines
	 * generating, for each one, the corresponding machine code
	 * @param lines
	 */
	public void parse() {
		for (String s:lines) {
			String tokens[] = s.split(" ");
			if (findCommandNumber(tokens)>=0) { //the line is a command
				proccessCommand(tokens);
			}
			else { //the line is not a command: so, it can be a variable or a label
				if (tokens[0].endsWith(":")){ //if it ends with : it is a label
					String label = tokens[0].substring(0, tokens[0].length()-1); //removing the last character
					labels.add(label);
					labelsAdresses.add(objProgram.size());
				}
				else //otherwise, it must be a variable
					variables.add(tokens[0]);
			}
		}
		variables.add("$DEGAS_START_VALUE$");
	}



	/**
	 * This method processes a command, putting it and its parameters (if they have)
	 * into the final array
	 * @param tokens
	 */
	protected void proccessCommand(String[] tokens) {
		String command = tokens[0];
		String parameter1 ="";
		String parameter2 = "";
		String parameter3 = "";

		int commandNumber = findCommandNumber(tokens);
	
		if (commandNumber == 0) { //addMemReg 0
			parameter1 = tokens[1];
			parameter1 = "&" + parameter1;
			parameter2 = tokens[2];
		}
		if (commandNumber == 1) { //addRegMem 1
			parameter1 = tokens[1];
			parameter2 = tokens[2];
			parameter2 = "&" + parameter2;
		}
			if (commandNumber == 2) { //addRegARegB 2
			parameter1 = tokens[1];
			parameter2 = tokens[2];
		}
		if (commandNumber == 3) { //addImmReg 3
			parameter1 = tokens[1];
			parameter2 = tokens[2];
		}
		if (commandNumber == 4) { //subRegARegB 4
			parameter1 = tokens[1];
			parameter2 = tokens[2];
		}
		if (commandNumber == 5) { //subMemReg 5
			parameter1 = tokens[1];
			parameter1 = "&" + parameter1;
			parameter2 = tokens[2];
		}
		if (commandNumber == 6) { //subRegMem 6
			parameter1 = tokens[1];
			parameter2 = tokens[2];
			parameter2 = "&" + parameter2;
		}
		if (commandNumber == 7) { //subImmReg 7
			parameter1 = tokens[1];
			parameter2 = tokens[2];
		}
		if (commandNumber == 8) { //jmp 8
			parameter1 = tokens[1];
			parameter1 = "&"+parameter1;//this is a flag to indicate that is a position in memory
		}
		if (commandNumber == 9) { //jz 9
			parameter1 = tokens[1];
			parameter1 = "&"+parameter1;//this is a flag to indicate that is a position in memory
		}
			if (commandNumber == 10) { //jn 10
			parameter1 = tokens[1];
			parameter1 = "&"+parameter1;//this is a flag to indicate that is a position in memory
		}
		if (commandNumber == 11) { //inc 11
			parameter1 = tokens[1];
		}
		if (commandNumber == 12) { //moveMemReg 12
			parameter1 = tokens[1];
			parameter1 = "&" + parameter1;
			parameter2 = tokens[2];
		}
		if (commandNumber == 13) { //moveRegMem 13
			parameter1 = tokens[1];
			parameter2 = tokens[2];
			parameter2 = "&" + parameter2;
		}
		if (commandNumber == 14) { //moveRegReg 14
			parameter1 = tokens[1];
			parameter2 = tokens[2];
		}
		if (commandNumber == 15) { //moveImmReg	15
			parameter1 = tokens[1];
			parameter2 = tokens[2];
		}
		if (commandNumber == 16) { //jeq 16
			parameter1 = tokens[1];
			parameter2 = tokens[2];
			parameter3 = tokens[3];
			parameter3 = "&"+parameter3; //this is a flag to indicate that is a position in memory
		}
		if (commandNumber == 17) { //jneq 17
			parameter1 = tokens[1];
			parameter2 = tokens[2];
			parameter3 = tokens[3];
			parameter3 = "&"+parameter3; //this is a flag to indicate that is a position in memory
		}
		if (commandNumber == 18) { //jgt 18
			parameter1 = tokens[1];
			parameter2 = tokens[2];
			parameter3 = tokens[3];
			parameter3 = "&"+parameter3; //this is a flag to indicate that is a position in memory
		}
		if (commandNumber == 19) { //jlw 19	
			parameter1 = tokens[1];
			parameter2 = tokens[2];
			parameter3 = tokens[3];
			parameter3 = "&"+parameter3; //this is a flag to indicate that is a position in memory
		}
		if (commandNumber == 20) { //call 20
			parameter1 = tokens[1];
			parameter1 = "&"+parameter1;//this is a flag to indicate that is a position in memory
		}
		if (commandNumber == 21) { //ret 21
			//não tem parametros
		}
		if(commandNumber == 22) { // startStk 22
			parameter1 = tokens[1];
			parameter1 = "&"+parameter1;
		}
		objProgram.add(Integer.toString(commandNumber));
		if (!parameter1.isEmpty()) {
			objProgram.add(parameter1);
		}
		if (!parameter2.isEmpty()) {
			objProgram.add(parameter2);
		}
		if (!parameter3.isEmpty()) {
			objProgram.add(parameter3);
		}
	}
	
	
	/**
	 * This method uses the tokens to search a command
	 * in the commands list and returns its id.
	 * Some commands (as move) can have multiple formats (reg reg, mem reg, reg mem) and
	 * multiple ids, one for each format.
	 * @param tokens
	 * @return
	 */
	private int findCommandNumber(String[] tokens) {
		int p = commands.indexOf(tokens[0]);
		if (p<0){ //the command isn't in the list. So it must have multiple formats
			if ("move".equals(tokens[0])) //the command is a move
				p = proccessMove(tokens);
				else if ("add".equals(tokens[0]))
				p = processAdd(tokens);
			else if ("sub".equals(tokens[0]))
				p = processSub(tokens);	
		}
		return p;
	}

	/**
	 * This method proccess a move command.
	 * It must have differents formats, meaning differents internal commands
	 * @param tokens
	 * @return
	 */
	private int proccessMove(String[] tokens) {
		String p1 = tokens[1];
		String p2 = tokens[2];
		int p=-1;
		if ((p1.startsWith("%"))&&(p2.startsWith("%"))) { //this is a moveRegReg comand
			p = commands.indexOf("moveRegReg");
			// ====================================
		}else if((p1.startsWith("%"))) { 
			p = commands.indexOf("moveRegMem");
		}
		else if(p1.matches("-?\\d+") && p2.startsWith("%")){	
			p = commands.indexOf("moveImmReg");
		}
		else if((p2.startsWith("%"))) { 
			p = commands.indexOf("moveMemReg");
		}
		return p;
	}


	/**
	 * This method processes an 'add' command.
	 * Depending on the format of its parameters, it returns the corresponding internal command index.
	 * Valid formats:
	 * - add %<regA> %<regB>         → addRegARegB
	 * - add %<reg> <memory>         → addRegMem
	 * - add <immediate> %<reg>      → addImmReg
	 * - add <memory> %<reg>         → addMemReg
	 * 
	 * @param tokens An array of strings representing the parts of the add instruction
	 * @return Index of the corresponding internal command in the commands list
	 */
	private int processAdd(String[] tokens) {
		String p1 = tokens[1];
		String p2 = tokens[2];
		int p=-1;
		
		if((p1.startsWith("%"))&&(p2.startsWith("%"))) { 
			p = commands.indexOf("addRegARegB");
		}
		else if((p1.startsWith("%"))) { 
			p = commands.indexOf("addRegMem");
		}
		else if(p1.matches("-?\\d+") && p2.startsWith("%")){
			p = commands.indexOf("addImmReg");
		}
		else if((p2.startsWith("%"))) { 
			p = commands.indexOf("addMemReg");
		}
		return p;
	}
	
	
	/**
	 * This method processes a 'sub' command.
	 * Depending on the format of its parameters, it returns the corresponding internal command index.
	 * Valid formats:
	 * - sub %<regA> %<regB>         → subRegARegB
	 * - sub %<reg> <memory>         → subRegMem
	 * - sub <immediate> %<reg>      → subImmReg
	 * - sub <memory> %<reg>         → subMemReg
	 * 
	 * @param tokens An array of strings representing the parts of the sub instruction
	 * @return Index of the corresponding internal command in the commands list
	 */
	private int processSub(String[] tokens) {
		String p1 = tokens[1];
		String p2 = tokens[2];
		int p=-1;
		
		if((p1.startsWith("%"))&&(p2.startsWith("%"))) { 
			p = commands.indexOf("subRegARegB");
		}
		else if((p1.startsWith("%"))) { 
			p = commands.indexOf("subRegMem");
		}
		else if(p1.matches("-?\\d+") && p2.startsWith("%")){	
			p = commands.indexOf("subImmReg");
		}
		else if((p2.startsWith("%"))) { 	
			p = commands.indexOf("subMemReg");
		}
		return p;
	}


	/**
	 * This method creates the executable program from the object program
	 * Step 1: check if all variables and labels mentioned in the object 
	 * program are declared in the source program
	 * Step 2: allocate memory addresses (space), from the end to the begin (stack)
	 * to store variables
	 * Step 3: identify memory positions to the labels
	 * Step 4: make the executable by replacing the labels and the variables by the
	 * corresponding memory addresses 
	 * @param filename 
	 * @throws IOException 
	 */
	public void makeExecutable(String filename) throws IOException {
		if (!checkLabels())
			return;
		execProgram = (ArrayList<String>) objProgram.clone();
		replaceAllVariables();
		replaceLabels(); //replacing all labels by the address they refer to
		replaceRegisters(); //replacing all registers by the register id they refer to
		saveExecFile(filename);
		System.out.println("Finished");
	}

	/**
	 * This method replaces all the registers names by its correspondings ids.
	 * registers names must be prefixed by %
	 */
	protected void replaceRegisters() {
		int p=0;
		for (String line:execProgram) {
			if (line.startsWith("%")){ //this line is a register
				line = line.substring(1, line.length());
				int regId = searchRegisterId(line, arch.getRegistersList());
				String newLine = Integer.toString(regId);
				execProgram.set(p, newLine);
			}
			p++;
		}
		
	}

	/**
	 * This method replaces all variables by their addresses.
	 * The addresses o0f the variables startes in the end of the memory
	 * and decreases (creating a stack)
	 */
	protected void replaceAllVariables() {
		int position = arch.getMemorySize()-1; //starting from the end of the memory
		for (String var : this.variables) { //scanning all variables
			replaceVariable(var, position);
			position --;
		}
	}

	/**
	 * This method saves the execFile collection into the output file
	 * @param filename
	 * @throws IOException 
	 */
	private void saveExecFile(String filename) throws IOException {
		File file = new File(filename+".dxf");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (String l : execProgram)
			writer.write(l+"\n");
		writer.write("-1"); //-1 is a flag indicating that the program is finished
		writer.close();
		
	}

	/**
	 * This method replaces all labels in the execprogram by the corresponding
	 * address they refer to
	 */
	protected void replaceLabels() {
		int i=0;
		for (String label : labels) { //searching all labels
			label = "&"+label;
			int labelPointTo = labelsAdresses.get(i);
			int lineNumber = 0;
			for (String l : execProgram) {
				if (l.equals(label)) {//this label must be replaced by the address
					String newLine = Integer.toString(labelPointTo); // the address
					execProgram.set(lineNumber, newLine);
				}
				lineNumber++;
			}
			i++;
		}
		
	}

	/**
	 * This method replaces all occurences of a variable
	 * name found in the object program by his address
	 * in the executable program
	 * @param var
	 * @param position
	 */
	protected void replaceVariable(String var, int position) {
		var = "&"+var;
		int i=0;
		for (String s:execProgram) {
			if (s.equals(var)) {
				s = Integer.toString(position);
				execProgram.set(i, s);
			}
			i++;
		}
	}

	/**
	 * This method checks if all labels and variables in the object program were in the source
	 * program.
	 * The labels and the variables collection are used for this
	 */
	protected boolean checkLabels() {
		System.out.println("Checking labels and variables");
		for (String line:objProgram) {
			boolean found = false;
			if (line.startsWith("&")) { //if starts with "&", it is a label or a variable
				line = line.substring(1, line.length());
				if (labels.contains(line))
					found = true;
				if (variables.contains(line))
					found = true;
				if (!found) {
					System.out.println("FATAL ERROR! Variable or label "+line+" not declared!");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This method searches for a register in the architecture register list
	 * by the register name
	 * @param line
	 * @param registersList
	 * @return
	 */
	private int searchRegisterId(String line, ArrayList<Register> registersList) {
		int i=0;
		for (Register r:registersList) {
			if (line.equals(r.getRegisterName())) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public static void main(String[] args) throws IOException {
		 // Verifica se exatamente um argumento foi passado
	    if (args.length != 1) {
	        System.err.println("Usage: Assembler <INPUT>");
	        System.err.println("INPUT must be the name of a .dsf file, without the extension");
	        System.exit(2);
	    }

	    String filename = args[0];
	    File file = new File(filename + ".dsf");

	    // Verifica se o arquivo .dsf existe
	    if (!file.exists()) {
	        System.err.println("ERROR: File \"" + filename + ".dsf\" not found.");
	        System.exit(1);
	    }

	    Assembler assembler = new Assembler();
	    System.out.println("Reading source assembler file: " + filename + ".dsf");
	    assembler.read(filename);
	    System.out.println("Generating the object " + filename);
	    assembler.parse();
	    System.out.println("Generating executable: " + filename + ".dxf");
	    assembler.makeExecutable(filename);
	}
		
}
