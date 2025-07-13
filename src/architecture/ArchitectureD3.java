package architecture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import components.Bus;
import components.Demux;
import components.Memory;
import components.Register;
import components.Ula;

public class ArchitectureD3 {
	
	private boolean simulation; //this boolean indicates if the execution is done in simulation mode.
								//simulation mode shows the components' status after each instruction
	
	private boolean halt;
	private Bus extbus1;
	private Bus intbus1;
	private Bus intbus2;
	private Memory memory;
	private Memory statusMemory;
	private int memorySize;
	private Register PC;
	private Register IR;
	private Register REG0, REG1, REG2, REG3; // Ainda não usei esses
	private Register Flags;
	private Register StkTOP, StkBOT;
	private Ula ula;
	private Demux demux; //only for multiple register purposes
	
	private ArrayList<String> commandsList;
	private ArrayList<Register> registersList;
	
	

	/**
	 * Instanciates all components in this architecture
	 */
	private void componentsInstances() {
		//don't forget the instantiation order
		//buses -> registers -> ula -> memory
		extbus1 = new Bus();
		intbus1 = new Bus();
		intbus2 = new Bus();
		
		REG0 = new Register("REG0", intbus1, intbus2);
		REG1 = new Register("REG1", intbus1, intbus2);
		REG2 = new Register("REG2", intbus1, intbus2);
		REG3 = new Register("REG3", intbus1, intbus2);
		
		StkTOP = new Register("StkTOP", extbus1, intbus2);//descobrir para que serve isso
		StkBOT = new Register("StkBOT", extbus1, intbus2);//descobrir para que serve isso
		
		PC = new Register("PC", extbus1, intbus2);
		IR = new Register("IR", extbus1, intbus2);
		Flags = new Register(2, intbus2);

		
		fillRegistersList();
		 
		// Inicializa os valores da pilha
		extbus1.put(100);         // coloca o valor 100 no barramento externo
	    StkTOP.store();
	    extbus1.put(100);         // mesmo processo para StackBottom
	    StkBOT.store();
		
		ula = new Ula(intbus1, intbus2);
		statusMemory = new Memory(2, extbus1);
		memorySize = 128;
		memory = new Memory(memorySize, extbus1);
		demux = new Demux(); //this bus is used only for multiple register operations
		

		fillCommandsList();
	}

	/**
	 * This method fills the registers list inserting into them all the registers we have.
	 * IMPORTANT!
	 * The first register to be inserted must be the default RPG
	 */
	private void fillRegistersList() {
		registersList = new ArrayList<Register>();
		registersList.add(REG0);
		registersList.add(REG1);
		registersList.add(REG2);
		registersList.add(REG3);
		registersList.add(PC);
		registersList.add(IR);
		registersList.add(Flags);
		registersList.add(StkTOP);
		registersList.add(StkBOT);
	}

	/**
	 * Constructor that instanciates all components according the architecture diagram
	 */
	public ArchitectureD3() {
		componentsInstances();
		
		//by default, the execution method is never simulation mode
		simulation = false;
	}

	
	public ArchitectureD3(boolean sim) {
		componentsInstances();
		
		//in this constructor we can set the simoualtion mode on or off
		simulation = sim;
	}



	//getters
	
	protected Bus getExtbus1() {
		return extbus1;
	}

	protected Bus getIntbus1() {
		return intbus1;
	}

	protected Bus getIntbus2() {
		return intbus2;
	}

	protected Memory getMemory() {
		return memory;
	}

	protected Register getPC() {
		return PC;
	}

	protected Register getIR() {
		return IR;
	}
	
	protected Register getRPG() {
		return REG0;
	}

	protected Register getFlags() {
		return Flags;
	}

	protected Ula getUla() {
		return ula;
	}

	public ArrayList<String> getCommandsList() {
		return commandsList;
	}



	//Todos os microprogramas tem que estar implementado aqui
	//the instructions table is
	/*
	 *
			add addr (rpg <- rpg + addr)
			sub addr (rpg <- rpg - addr)
			jmp addr (pc <- addr)
			jz addr  (se bitZero pc <- addr)
			jn addr  (se bitneg pc <- addr)
			read addr (rpg <- addr)
			store addr  (addr <- rpg)
			ldi x    (rpg <- x. x must be an integer)
			inc    (rpg++)
			move regA regB (regA <- regB)
	 */
	
	/**
	 * This method fills the commands list arraylist with all commands used in this architecture
	 */
	protected void fillCommandsList() {
		commandsList = new ArrayList<String>();
		commandsList.add("addMemReg"); //0
        commandsList.add("addRegMem"); //1
        commandsList.add("addRegARegB"); //2
        commandsList.add("addImmReg");   //3

		commandsList.add("subRegARegB");   //4
        commandsList.add("subMemReg");   //5
        commandsList.add("subRegMem");   //6
        commandsList.add("subImmReg");   //7

		commandsList.add("jmp");   //8
		commandsList.add("jz");    //9
		commandsList.add("jn");    //10
		commandsList.add("inc");   //11

		commandsList.add("moveMemReg"); //12
		commandsList.add("moveRegMem"); //13
		commandsList.add("moveRegReg"); //14
		commandsList.add("moveImmReg"); //15

		commandsList.add("jeq");       // 16
		commandsList.add("jneq");      // 17
		commandsList.add("jgt");       // 18
		commandsList.add("jlw");       // 19
		commandsList.add("call");      // 20
		commandsList.add("ret");       // 21


	}

	
	/**
	 * This method is used after some ULA operations, setting the flags bits according the result.
	 * @param result is the result of the operation
	 * NOT TESTED!!!!!!!
	 */
	private void setStatusFlags(int result) {
		Flags.setBit(0, 0);
		Flags.setBit(1, 0);
		if (result==0) { //bit 0 in flags must be 1 in this case
			Flags.setBit(0,1);
		}
		if (result<0) { //bit 1 in flags must be 1 in this case
			Flags.setBit(1,1);
		}
	}

	/**
	 * ADIONAR OS MICROPROGRAMAS EM FORMATO DE COMENTARIO ANTES DE TRANSPOR EM FORMATO DE CODIGO
	 * DA MANEIRA QUE SE ENCONTRA ABAIXO:
	 * 
	 * 	 
	 * 1. pc -> intbus2 //pc.read()
	 * 2. ula <-  intbus2 //ula.store()
	 * 3. ula incs
	 * 4. ula -> intbus2 //ula.read()
	 * 5. pc <- intbus2 //pc.store() now pc points to the parameter
	 * 6. rpg -> intbus1 //rpg.read() the current rpg value must go to the ula 
	 * 7. ula <- intbus1 //ula.store()
	 * 8. pc -> extbus (pc.read())
	 * 9. memory reads from extbus //this forces memory to write the data position in the extbus
	 * 10. memory reads from extbus //this forces memory to write the data value in the extbus
	 * 11. rpg <- extbus (rpg.store())
	 * 12. rpg -> intbus1 (rpg.read())
	 * 13. ula  <- intbus1 //ula.store()
	 * 14. Flags <- zero //the status flags are reset
	 * 15. ula adds
	 * 16. ula -> intbus1 //ula.read()
	 * 17. ChangeFlags //informations about flags are set according the result 
	 * 18. rpg <- intbus1 //rpg.store() - the add is complete.
	 * 19. pc -> intbus2 //pc.read() now pc must point the next instruction address
	 * 20. ula <- intbus2 //ula.store()
	 * 21. ula incs
	 * 22. ula -> intbus2 //ula.read()
	 * 23. pc <- intbus2 //pc.store() 
	 * end
	 * @param address
	 */

	public void addMemReg()
    {

    }

    public void addRegARegB() {

    }

	public void addRegMem() {
		
	}
	
    public void addImmReg() {
    }

	public void subRegReg()  {	
	
	}
	
    public void subMemReg() {	
    
    }

    public void subRegMem() {
    
    }

    public void subImmReg() {
    
    }   
	
	public void jmp() {	
	
	}
	
	public void jz() {	
	
	}
	
	public void jn() {
	
	}
	
	public void inc() {
	
	}
	
	public void moveRegReg() {
	
	}

    public void moveMemReg() {
        
    }
	
    public void moveRegMem() {
    
    }

    public void moveImmReg() {
    
    }
	
	public void jeq() {	
	
	}
	
	public void jneq() {	
	
	}
	
	public void jqt() {
	
	}
	
	public void jlw() {
	
	}
	
	
	
		// NÃO TENHO CERTEZA, CERIFICA AQUI TONHO OS CALL E RET
	
	public void call() {
	    // 1. Avança PC para pegar o parâmetro (endereço destino)
	    PC.internalRead();             // -> intbus2
	    ula.internalStore(1);          // ULA recebe PC
	    ula.inc();                     // PC + 1
	    ula.internalRead(1);
	    PC.internalStore();           // PC = PC + 1 (aponta pro endereço do destino)

	    // 2. Salvar retorno na pilha (PC agora é addr, precisamos salvar retorno como PC - 1)
	    // Decrementa StackTop
	    StkTOP.internalRead();       // pega StackTop
	    ula.store(1);
	    extbus1.put(1);                // immediate = 1
	    ula.store(0);                  // 1 na ULA
	    ula.sub();                     // StackTop - 1
	    ula.internalRead(1);
	    StkTOP.internalStore();      // StackTop--

	    // Salva PC (já está com endereço de retorno) na mem[StackTop]
	    StkTOP.read();               // Endereço da pilha
	    memory.store();                // Salva endereço (do retorno)
	    PC.read();                     // valor PC
	    memory.store();                // Salva valor PC na posição de pilha

	    // 3. Leitura do destino
	    PC.read();                     // pega endereço do destino
	    memory.read();                 // obtém valor de destino
	    PC.store();                    // PC <- addr
	}

	
	public void ret() {
	    // 1. Incrementa StackTop
		StkTOP.internalRead();
	    ula.store(1);
	    extbus1.put(1);                // Immediate 1
	    ula.store(0);
	    ula.add();                     // StackTop + 1
	    ula.internalRead(1);
	    StkTOP.internalStore();      // StackTop++

	    // 2. PC <- mem[StackTop]
	    StkTOP.read();
	    memory.read();                 // Mem[StackTop]
	    PC.store();                    // PC <- mem[StackTop]
	}

	
	public ArrayList<Register> getRegistersList() {
		return registersList;
	}

	/**
	 * This method performs an (external) read from a register into the register list.
	 * The register id must be in the demux bus
	 */
	private void registersRead() {
		registersList.get(demux.getValue()).read();
	}
	
	/**
	 * This method performs an (internal) read from a register into the register list.
	 * The register id must be in the demux bus
	 */
	private void registersInternalRead() {
		registersList.get(demux.getValue()).internalRead();;
	}
	
	/**
	 * This method performs an (external) store toa register into the register list.
	 * The register id must be in the demux bus
	 */
	private void registersStore() {
		registersList.get(demux.getValue()).store();
	}
	
	/**
	 * This method performs an (internal) store toa register into the register list.
	 * The register id must be in the demux bus
	 */
	private void registersInternalStore() {
		registersList.get(demux.getValue()).internalStore();;
	}



	/**
	 * This method reads an entire file in machine code and
	 * stores it into the memory
	 * NOT TESTED
	 * @param filename
	 * @throws IOException 
	 */
	public void readExec(String filename) throws IOException {
		   BufferedReader br = new BufferedReader(new		 
		   FileReader(filename+".dxf"));
		   String linha;
		   int i=0;
		   while ((linha = br.readLine()) != null) {
			     extbus1.put(i);
			     memory.store();
			   	 extbus1.put(Integer.parseInt(linha));
			     memory.store();
			     i++;
			}
			br.close();
	}
	
	/**
	 * This method executes a program that is stored in the memory
	 */
	public void controlUnitEexec() {
		halt = false;
		while (!halt) {
			fetch();
			decodeExecute();
		}

	}
	

	/**
	 * This method implements The decode proccess,
	 * that is to find the correct operation do be executed
	 * according the command.
	 * And the execute proccess, that is the execution itself of the command
	 */
	private void decodeExecute() {
		IR.internalRead(); //the instruction is in the internalbus2
		int command = intbus2.get();
		simulationDecodeExecuteBefore(command);
		switch (command) {
		case 0:
			addMemReg();
			break;
		case 1:
			addRegMem();
			break;
		case 2:
			addRegARegB();
			break;
		case 3:
			addImmReg();
			break;
		case 4:
			subRegARegB();
			break;
		case 5:
			subMemReg();
			break;
		case 6:
			subRegMem();
			break;
		case 7:
			subImmReg();
			break;
		case 8:
			jmp();
			break;
		case 9:
			jz();
			break;
		case 10:
			jn();
			break;
		case 11:
		    call();
		    break;
		case 12:
		    ret();
		    break;
		case 13:
			inc();
			break;
		case 14:
			moveRegReg();
			break;
		case 15:
			moveRegMem();
			break;
		case 16:
			moveRegARegB();
			break;
		case 17:
			moveImmReg();
			break;
		case 18:
			jeq();
			break;
		case 19:
			jneq();
			break;
		case 20:
			jgt();
			break;
		case 21:
			jlw();
			break;
		default:
			halt = true;
			break;
		}
		if (simulation)
			simulationDecodeExecuteAfter();
	}

	/**
	 * This method is used to show the components status in simulation conditions
	 * NOT TESTED
	 * @param command 
	 */
	private void simulationDecodeExecuteBefore(int command) {
		System.out.println("----------BEFORE Decode and Execute phases--------------");
		String instruction;
		int parameter = 0;
		for (Register r:registersList) {
			System.out.println(r.getRegisterName()+": "+r.getData());
		}
		if (command !=-1)
			instruction = commandsList.get(command);
		else
			instruction = "END";
		if (hasOperands(instruction)) {
			parameter = memory.getDataList()[PC.getData()+1];
			System.out.println("Instruction: "+instruction+" "+parameter);
		}
		else
			System.out.println("Instruction: "+instruction);
		if ("read".equals(instruction))
			System.out.println("memory["+parameter+"]="+memory.getDataList()[parameter]);
		
	}

	/**
	 * This method is used to show the components status in simulation conditions
	 * NOT TESTED 
	 */
	private void simulationDecodeExecuteAfter() {
		String instruction;
		System.out.println("-----------AFTER Decode and Execute phases--------------");
		System.out.println("Internal Bus 1: "+intbus1.get());
		System.out.println("Internal Bus 2: "+intbus2.get());
		System.out.println("External Bus 1: "+extbus1.get());
		for (Register r:registersList) {
			System.out.println(r.getRegisterName()+": "+r.getData());
		}
		Scanner entrada = new Scanner(System.in);
		System.out.println("Press <Enter>");
		String mensagem = entrada.nextLine();
	}

	/**
	 * This method uses PC to find, in the memory,
	 * the command code that must be executed.
	 * This command must be stored in IR
	 * NOT TESTED!
	 */
	private void fetch() {
		PC.read();
		memory.read();
		IR.store();
		simulationFetch();
	}

	/**
	 * This method is used to show the components status in simulation conditions
	 * NOT TESTED!!!!!!!!!
	 */
	private void simulationFetch() {
		if (simulation) {
			System.out.println("-------Fetch Phase------");
			System.out.println("PC: "+PC.getData());
			System.out.println("IR: "+IR.getData());
		}
	}

	/**
	 * This method is used to show in a correct way the operands (if there is any) of instruction,
	 * when in simulation mode
	 * NOT TESTED!!!!!
	 * @param instruction 
	 * @return
	 */
	private boolean hasOperands(String instruction) {
		if ("inc".equals(instruction)) //inc is the only one instruction having no operands
			return false;
		else
			return true;
	}

	/**
	 * This method returns the amount of positions allowed in the memory
	 * of this architecture
	 * NOT TESTED!!!!!!!
	 * @return
	 */
	public int getMemorySize() {
		return memorySize;
	}
	
	public static void main(String[] args) throws IOException {
		ArchitectureD3 arch = new ArchitectureD3(true);
		arch.readExec("program");
		arch.controlUnitEexec();
	}
	

}
