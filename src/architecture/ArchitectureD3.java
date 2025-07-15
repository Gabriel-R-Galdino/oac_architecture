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
	private Register REG0, REG1, REG2, REG3; 
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
		
		StkTOP = new Register("StkTOP", extbus1, null);//descobrir para que serve isso
		StkBOT = new Register("StkBOT", extbus1, null);//descobrir para que serve isso
		
		PC = new Register("PC", extbus1, intbus2);
		IR = new Register("IR", extbus1, intbus2);
		Flags = new Register(2, intbus2);

		
		fillRegistersList();
		ula = new Ula(intbus1, intbus2);
		 
		// Inicializa os valores da pilha
		extbus1.put(100);         // coloca o valor 100 no barramento externo
	    StkTOP.store();
	    extbus1.put(100);         // mesmo processo para StackBottom
	    StkBOT.store();
		
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
		commandsList.add("startStk"); //22


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
	    PC.internalRead();
	    ula.internalStore(1);
	    ula.inc();
	    ula.internalRead(1);
	    PC.internalStore();
	    PC.read();
	    memory.read();
	    memory.read();
	    IR.store();
	    PC.internalRead();
	    ula.internalStore(1);
	    ula.inc();
	    ula.internalRead(1);
	    PC.internalStore();
	    PC.read();
	    memory.read();
	    demux.setValue(extbus1.get());
	    registersInternalRead();
	    ula.internalStore(1);
	    IR.internalRead();
	    ula.internalStore(0);
	    ula.add();
	    ula.internalRead(1);
	    setStatusFlags(intbus2.get());
	    registersInternalStore();
	    PC.internalRead();
	    ula.internalStore(1);
	    ula.inc();
	    ula.internalRead(1);
	    PC.internalStore();
    }

    public void addRegARegB() {
    	  PC.internalRead();
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore(); 
  	    PC.read();
  	    memory.read(); 
  	    PC.internalRead();
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore(); 
  	    demux.setValue(extbus1.get()); 
  	    registersInternalRead(); 
  	    ula.internalStore(0);
  	    PC.read();
  	    memory.read(); 
  	    demux.setValue(extbus1.get()); 
  	    registersInternalRead();
  	    ula.internalStore(1);
  	    ula.add();
  	    ula.internalRead(1);
  	    setStatusFlags(intbus2.get()); 
  	    registersInternalStore();
  	    PC.internalRead(); 
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore();
    }

	public void addRegMem() {
		    PC.internalRead();
		    ula.internalStore(1);
		    ula.inc();
		    ula.internalRead(1);
		    PC.internalStore();
		    PC.read();
		    memory.read();
		    demux.setValue(extbus1.get());
		    PC.internalRead();
		    ula.internalStore(1);
		    ula.inc();
		    ula.internalRead(1);
		    PC.internalStore();
		    PC.read();
		    memory.read();
		    memory.read();
		    IR.store();
		    IR.internalRead();
		    ula.internalStore(1);
		    registersInternalRead();
		    ula.internalStore(0);
		    ula.add();
		    ula.internalRead(1);
		    setStatusFlags(intbus2.get());
		    IR.internalStore();
		    PC.read();
		    memory.read();
		    memory.store();
		    IR.read();
		    memory.store();
		    PC.internalRead();
		    ula.internalStore(1);
		    ula.inc();
		    ula.internalRead(1);
		    PC.internalStore();
	}
	
    public void addImmReg() {
    	  PC.internalRead();
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore();
  	    PC.read();
  	    memory.read();
  	    IR.store();
  	    PC.internalRead();
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore();
  	    PC.read();
  	    memory.read();
  	    demux.setValue(extbus1.get());
  	    registersInternalRead();
  	    ula.internalStore(1);
  	    IR.internalRead();
  	    ula.internalStore(0);
  	    ula.add();
  	    ula.internalRead(1);
  	    setStatusFlags(intbus2.get());
  	    registersInternalStore();
  	    PC.internalRead();
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore();
    }

	public void subRegARegB()  {	
		  PC.internalRead();
		    ula.internalStore(1);
		    ula.inc();
		    ula.internalRead(1);
		    PC.internalStore();
		    PC.read();
		    memory.read();
		    PC.internalRead();
		    ula.internalStore(1);
		    ula.inc();
		    ula.internalRead(1);
		    PC.internalStore(); 
		    demux.setValue(extbus1.get()); 
		    registersInternalRead(); 
		    ula.internalStore(0);
		    PC.read();
		    memory.read(); 
		    demux.setValue(extbus1.get()); 
		    registersInternalRead(); 
		    ula.internalStore(1);
		    ula.sub();
		    ula.internalRead(1);
		    setStatusFlags(intbus2.get()); 
		    registersInternalStore();
		    PC.internalRead(); 
		    ula.internalStore(1);
		    ula.inc();
		    ula.internalRead(1);
		    PC.internalStore(); //
	}
	
    public void subMemReg() {	
    	  PC.internalRead();
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore();
  	    PC.read();
  	    memory.read();
  	    memory.read();
  	    IR.store();
  	    PC.internalRead();
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore();
  	    PC.read();
  	    memory.read();
  	    demux.setValue(extbus1.get());
  	    registersInternalRead();
  	    ula.internalStore(1);
  	    IR.internalRead();
  	    ula.internalStore(0);
  	    ula.sub();
  	    ula.internalRead(1);
  	    setStatusFlags(intbus2.get());
  	    registersInternalStore();
  	    PC.internalRead();
  	    ula.internalStore(1);
  	    ula.inc();
  	    ula.internalRead(1);
  	    PC.internalStore();
    }

    public void subRegMem() {
    	 PC.internalRead();
 	    ula.internalStore(1);
 	    ula.inc();
 	    ula.internalRead(1);
 	    PC.internalStore();
 	    PC.read();
 	    memory.read();
 	    demux.setValue(extbus1.get());
 	    PC.internalRead();
 	    ula.internalStore(1);
 	    ula.inc();
 	    ula.internalRead(1);
 	    PC.internalStore();
 	    PC.read();
 	    memory.read();
 	    memory.read();
 	    IR.store();
 	    IR.internalRead();
 	    ula.internalStore(1);
 	    registersInternalRead();
 	    ula.internalStore(0);
 	    ula.sub();
 	    ula.internalRead(1);
 	    setStatusFlags(intbus2.get());
 	    IR.internalStore();
 	    PC.read();
 	    memory.read();
 	    memory.store();
 	    IR.read();
 	    memory.store();
 	    PC.internalRead();
 	    ula.internalStore(1);
 	    ula.inc();
 	    ula.internalRead(1);
 	    PC.internalStore();
    }

    public void subImmReg() {
    	 PC.internalRead();
 	    ula.internalStore(1);
 	    ula.inc();
 	    ula.internalRead(1);
 	    PC.internalStore();
 	    PC.read();
 	    memory.read();
 	    IR.store();
 	    PC.internalRead();
 	    ula.internalStore(1);
 	    ula.inc();
 	    ula.internalRead(1);
 	    PC.internalStore();
 	    PC.read();
 	    memory.read();
 	    demux.setValue(extbus1.get());
 	    registersInternalRead();
 	    ula.internalStore(1);
 	    IR.internalRead();
 	    ula.internalStore(0);
 	    ula.sub();
 	    ula.internalRead(1);
 	    setStatusFlags(intbus2.get());
 	    registersInternalStore();
 	    PC.internalRead();
 	    ula.internalStore(1);
 	    ula.inc();
 	    ula.internalRead(1);
 	    PC.internalStore();
    }   
	
	/*
	
	*  ----------- Como Funciona o JUMP --------------
	   ------ PC ++ -----
	*  1. pc -> intus2    |        | PC.internalRead();
	*  2. ula <- intbus2  |        | ula.internalStore();
	*  3. ula.incs		  |		   | ula.inc();
	*  4. ula -> intbus2  |        | ula.internalRead();
	*  5. pc <- intbus2   |        | PC.internalStore();
	   ------------------
	*  6. pc -> extbus1            | PC.internalRead();
	*  7. memory.read() 		   | memory.read();
	*  8. PC.store()               | PC.store();
	//-------------- Fim do JUMP ----------------------

	 * 
	 *  O JUMP é o comando mais simples, ele apenas faz com que o PC aponte para o endereço
	 *  que está no próximo endereço da memória. 
	 *  O PC já aponta para o próximo endereço, então não é necessário incrementar o PC.
	 *  A ULA é usada para ler o endereço do PC e armazená-lo na memória.
	  
	*/

	public void jmp() {	
		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
		PC.read();
		memory.read();
		PC.store();
	}

	/*
	
	*  ----------- Como Funciona o JZ --------------
	   ------ PC ++ -----
	*  1. pc -> intus2    |         | PC.internalRead();
	*  2. ula <- intbus2  |         | ula.internalStore();
	*  3. ula.incs		  |		    | ula.inc();
	*  4. ula -> intbus2  |         | ula.internalRead();
	*  5. pc <- intbus2   |         | PC.internalStore();
	   ------------------
	*  6. pc -> extbus1             | PC.internalRead();
	*  7. memory.read() 		    | memory.read();
	*  8. CI: stn(1) ← extbus1      | statusMemory.storeIn1();           
	*  9. ula.inc                   | ula.inc();
	* 10. ula -> intbus2            | ula.internalRead();
	* 11. pc <- intbus2             | PC.internalStore();
	* 12. pc -> extbus              | PC.internalRead();
	* 13. CI: stn(0) ← extbus1      | statusMemory.storeIn0();
	* 14. extbus <- flags           | extbus1.put(flags.getBit(1));
	* 15. statusMemory -> extbus    | statusMemory.read();
	* 16. pc <- extbus              | PC.internalStore();
	//-------------- Fim do JZ ----------------------
	* 
	 *  O JZ é o comando que verifica se o bit zero está ativo, se sim, ele faz com que o PC aponte
	 *  para o endereço que está no próximo endereço da memória. 
	 *  O PC já aponta para o próximo endereço, então não é necessário incrementar o PC.
	 *  A ULA é usada para ler o endereço do PC e armazená-lo na memória.
	 * 
	*/

	public void jz() {	
		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
		PC.read();
		memory.read();
		statusMemory.storeIn1();
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
		PC.read();
		statusMemory.storeIn0();
		extbus1.put(Flags.getBit(1));
		statusMemory.read();
		PC.store();
	}

	/*
	
	*  ----------- Como Funciona o JN --------------
	   ------ PC ++ -----
	*  1. pc -> intus2    |         | PC.internalRead();
	*  2. ula <- intbus2  |         | ula.internalStore();
	*  3. ula.incs		  |		    | ula.inc();
	*  4. ula -> intbus2  |         | ula.internalRead();
	*  5. pc <- intbus2   |         | PC.internalStore();
	   ------------------
	*  6. pc -> extbus1             | PC.internalRead();
	*  7. memory.read() 		    | memory.read();
	*  8. CI: stn(1) ← extbus1      | statusMemory.storeIn1();
	*  9. ula.inc                   | ula.inc();
	* 10. ula -> intbus2            | ula.internalRead();
	* 11. pc <- intbus2             | PC.internalStore();
	* 12. pc -> extbus              | PC.internalRead();
	* 13. CI: stn(0) ← extbus1      | statusMemory.storeIn0();
	* 14. extbus <- flags           | extbus1.put(Flags.getBit(1));
	* 15. statusMemory -> extbus    | statusMemory.read();
	* 16. pc <- extbus              | PC.internalStore();
	//-------------- Fim do JN ----------------------
	 * 
	 *  O JN é o comando que verifica se o bit negativo está ativo, se sim, ele faz com que o PC aponte
	 *  para o endereço que está no próximo endereço da memória. 
	 *  O PC já aponta para o próximo endereço, então não é necessário incrementar o PC.
	 *  A ULA é usada para ler o endereço do PC e armazená-lo na memória.
	 * 
	*/
	
	public void jn() {
		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
		PC.read();
		memory.read();
		statusMemory.storeIn1();
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
		PC.read();
		statusMemory.storeIn0();
		extbus1.put(Flags.getBit(1));
		statusMemory.read();
		PC.store();
	}
	
	public void inc() {
				PC.internalRead(); 
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();

				PC.read();
				memory.read(); 
				demux.setValue(extbus1.get());
				registersInternalRead(); 
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				setStatusFlags(intbus2.get());
				registersInternalStore();

				PC.internalRead(); 
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore(); 
	}
	
	public void moveRegARegB() {
		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();

		PC.read(); 
		memory.read(); 

		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore(); 
		
		demux.setValue(extbus1.get()); 
		registersInternalRead();

		PC.read();
		memory.read(); 
		demux.setValue(extbus1.get());
		registersInternalStore(); 

		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
	}

    public void moveMemReg() {

		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();

		PC.read(); 
		memory.read(); 
		memory.read(); 
		IR.store(); 

		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
		IR.internalRead(); 
		PC.read();
		memory.read(); 
		demux.setValue(extbus1.get()); 
		registersInternalStore(); 

		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
    }
	
    public void moveRegMem() {
    
       			PC.internalRead();
    			ula.internalStore(1);
    			ula.inc();
    			ula.internalRead(1);
    			PC.internalStore();

    			PC.read(); 
    			memory.read(); 
    			demux.setValue(extbus1.get()); 

    			PC.internalRead();
    			ula.internalStore(1);
    			ula.inc();
    			ula.internalRead(1);
    			PC.internalStore();
    			registersInternalRead(); 
    			IR.internalStore();

    			PC.read();
    			memory.read(); 
    			memory.store(); 
    			IR.read();
    			memory.store();

    			PC.internalRead();
    			ula.internalStore(1);
    			ula.inc();
    			ula.internalRead(1);
    			PC.internalStore();
    }

    public void moveImmReg() {
    			PC.internalRead();
    			ula.internalStore(1);
    			ula.inc();
    			ula.internalRead(1);
    			PC.internalStore();

    			PC.read(); 
    			memory.read(); 
    			IR.store();

    			PC.internalRead();
    			ula.internalStore(1);
    			ula.inc();
    			ula.internalRead(1);
    			PC.internalStore();

    			PC.read();
    			memory.read();
    			demux.setValue(extbus1.get());
    			IR.internalRead(); 
    			registersInternalStore();

    			PC.internalRead();
    			ula.internalStore(1);
    			ula.inc();
    			ula.internalRead(1);
    			PC.internalStore();
    }

	/*
	
	*  ----------- Como Funciona o JEQ --------------
	   ------ PC ++ -----
	*  1. pc -> intbus2   |         | PC.internalRead();
	*  2. ula <- intbus2  |         | ula.internalStore();
	*  3. ula.inc		  |		    | ula.inc();
	*  4. ula -> intbus2  |         | ula.internalRead();
	*  5. pc <- intbus2   |         | PC.internalStore();
	   ------------------
	*  6. pc -> extbus1             | PC.read();
	*  7. memory.read() 		    | memory.read();
	*  8. demux <- extbus1          | demux.setValue(extbus1.get());
	*  9. registers -> intbus2      | registersInternalRead();
	* 10. ula <- intbus2            | ula.internalStore(0);

	* 11. ula.inc                   | ula.inc();
	* 12. ula -> intbus2            | ula.internalRead();
	* 13. pc <- intbus2             | PC.internalStore();
	* 14. pc -> extbus1             | PC.read();
	* 15. memory.read()             | memory.read();
	* 16. demux <- extbus1          | demux.setValue(extbus1.get());
	* 17. registers -> intbus2      | registersInternalRead();
	* 18. ula <- intbus2            | ula.internalStore(1);	

	* 19. ula.sub                   | ula.sub();
	* 20. ula -> intbus2            | ula.internalRead();
	* 21. flags <- intbus2          | setStatusFlags(intbus2.get());

	* 22. pc -> intbus2             | PC.internalRead();
	* 23. ula <- intbus2            | ula.internalStore(1);
	* 24. ula.inc                   | ula.inc();
	* 25. ula -> intbus2            | ula.internalRead();
	* 26. pc <- intbus2             | PC.internalStore();
	* 27. pc -> extbus1             | PC.read();
	* 28. memory.read()             | memory.read();
	* 29. CI: stn(1) ← extbus1      | statusMemory.storeIn1();
	* 30. ula.inc                   | ula.inc();
	* 31. ula -> intbus2            | ula.internalRead();
	* 32. pc <- intbus2             | PC.internalStore();
	* 33. pc -> extbus1             | PC.read();
	* 34. CI: stn(0) ← extbus1      | statusMemory.storeIn0();
	* 35. extbus1 <- flags          | extbus1.put(Flags.getBit(0));
	
	* 36. statusMemory -> extbus1   | statusMemory.read();
	* 37. pc <- extbus1             | PC.store();
	//-------------- Fim do JEQ ----------------------
	 * 
	 *  O JEQ é o comando que faz com que se regA for igual a reg B, o PC aponte para o endereço
	 *  que está em um endereço de memória.
	*/
	
	public void jeq() {	
        PC.internalRead(); 
        ula.internalStore(1); 
        ula.inc();              
        ula.internalRead(1); 
        PC.internalStore();     
        PC.read();       
        memory.read(); 
        demux.setValue(extbus1.get());
        registersInternalRead(); 
        ula.internalStore(0);   


        ula.inc(); 
        ula.internalRead(1); 
        PC.internalStore(); 
        PC.read(); 
        memory.read(); //
        demux.setValue(extbus1.get());
        registersInternalRead(); 
        ula.internalStore(1); 


        ula.sub(); 
        ula.internalRead(1); 
        setStatusFlags(intbus2.get());


        PC.internalRead(); 
        ula.internalStore(1); 
        ula.inc(); 
        ula.internalRead(1);
        PC.internalStore(); 
        PC.read(); 
        memory.read(); 
        statusMemory.storeIn1(); 
        ula.inc(); 
        ula.internalRead(1); 
        PC.internalStore(); 
        PC.read(); 
        statusMemory.storeIn0(); 
        extbus1.put(Flags.getBit(0)); 


        statusMemory.read(); 
        PC.store(); 
    }
	
	/*
	
	*  ----------- Como Funciona o JNEQ --------------
	   ------ PC ++ -----
	*  1. pc -> intbus2   |         | PC.internalRead();
	*  2. ula <- intbus2  |         | ula.internalStore();
	*  3. ula.inc   	  |		    | ula.inc();
	*  4. ula -> intbus2  |         | ula.internalRead();
	*  5. pc <- intbus2   |         | PC.internalStore();
	   ------------------
	*  6. pc -> extbus1             | PC.read();
	*  7. memory.read() 		    | memory.read();
	*  8. demux <- extbus1          | demux.setValue(extbus1.get());
	*  9. registers -> intbus2      | registersInternalRead();
	* 10. ula <- intbus2            | ula.internalStore(0);

	* 11. PC -> intbus2             | PC.internalRead();
	* 12. ula <- intbus2            | ula.internalStore(1);
	* 13. ula.inc                   | ula.inc();
	* 14. ula -> intbus2            | ula.internalRead();
	* 15. pc <- intbus2             | PC.internalStore();

	* 16. pc -> extbus1             | PC.read();
	* 17. memory.read()             | memory.read();
	* 18. demux <- extbus1          | demux.setValue(extbus1.get());
	* 19. registers -> intbus2      | registersInternalRead();
	* 20. ula <- intbus2            | ula.internalStore(1);

	* 21. ula.sub                   | ula.sub();
	* 22. ula -> intbus2            | ula.internalRead();
	* 23. flags <- intbus2          | setStatusFlags(intbus2.get());

	* 24. pc -> intbus2             | PC.internalRead();
	* 25. ula <- intbus2            | ula.internalStore(1);
	* 26. ula.inc                   | ula.inc();
	* 27. ula -> intbus2            | ula.internalRead();
	* 28. pc <- intbus2             | PC.internalStore();

	* 29. pc -> extbus1             | PC.read();
	* 30. memory.read()             | memory.read();
	* 31. CI: stn(0) ← extbus1      | statusMemory.storeIn0();

	* 32. PC -> intbus2             | PC.internalRead();
	* 33. ula <- intbus2            | ula.internalStore(1);
	* 34. ula.inc                   | ula.inc();
	* 35. ula -> intbus2            | ula.internalRead();
	* 36. pc <- intbus2             | PC.internalStore();
	* 37. pc -> extbus1             | PC.read();
	* 38. CI: stn(1) ← extbus1      | statusMemory.storeIn1();

	* 39. extbus1 <- flags          | extbus1.put(Flags.getBit(0));
	* 40. statusMemory -> extbus1   | statusMemory.read();
	* 41. pc <- extbus1             | PC.store();
	//-------------- Fim do JNEQ ----------------------
	 * 
	 *  O JNEQ é o comando que faz com que se regA for diferente de reg B, o PC aponte para o endereço
	 *  que está em um endereço de memória.
	*/

	public void jneq() {	
				PC.internalRead(); 
				ula.internalStore(1); 
				ula.inc();
				ula.internalRead(1); 
				PC.internalStore();
				
				PC.read(); 
				memory.read(); 
				demux.setValue(extbus1.get()); 
				registersInternalRead(); 
				ula.internalStore(0);


				PC.internalRead(); 
				ula.internalStore(1);
				ula.inc(); 
				ula.internalRead(1);
				PC.internalStore(); 
				
				PC.read();
				memory.read(); 
				demux.setValue(extbus1.get()); 
				registersInternalRead(); 
				ula.internalStore(1);

				ula.sub();
				ula.internalRead(1); 
				setStatusFlags(intbus2.get()); 

				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore(); 
				
				PC.read();
				memory.read(); 
				statusMemory.storeIn0();


				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore(); 
				PC.read(); 
				statusMemory.storeIn1(); 
				
				extbus1.put(Flags.getBit(0)); 
				statusMemory.read();
				PC.store();
	}
	
	/*
	
	*  ----------- Como Funciona o JGT --------------
	   ------ PC ++ -----
	*  1. pc -> intbus2   |         | PC.internalRead();
	*  2. ula <- intbus2  |         | ula.internalStore();
	*  3. ula.inc		  |		    | ula.inc();
	*  4. ula -> intbus2  |         | ula.internalRead();
	*  5. pc <- intbus2   |         | PC.internalStore();
	   ------------------
	*  6. pc -> extbus1             | PC.read();
	*  7. memory.read() 		    | memory.read();
	*  8. demux <- extbus1          | demux.setValue(extbus1.get());
	*  9. registers -> intbus2      | registersInternalRead();
	* 10. ula <- intbus2            | ula.internalStore(0);

	* 11. PC -> intbus2             | PC.internalRead();
	* 12. ula <- intbus2            | ula.internalStore(1);
	* 13. ula.inc                   | ula.inc();
	* 14. ula -> intbus2            | ula.internalRead();
	* 15. pc <- intbus2             | PC.internalStore();

	* 16. pc -> extbus1             | PC.read();
	* 17. memory.read()             | memory.read();
	* 18. demux <- extbus1          | demux.setValue(extbus1.get());
	* 19. registers -> intbus2      | registersInternalRead();
	* 20. ula <- intbus2            | ula.internalStore(1);

	* 21. ula.sub                   | ula.sub();
	* 22. ula -> intbus2            | ula.internalRead();
	* 23. flags <- intbus2          | setStatusFlags(intbus2.get());

	* 24. pc -> intbus2             | PC.internalRead();
	* 25. ula <- intbus2            | ula.internalStore(1);
	* 26. ula.inc                   | ula.inc();
	* 27. ula -> intbus2            | ula.internalRead();
	* 28. pc <- intbus2             | PC.internalStore();
	* 29. pc -> extbus1             | PC.read();
	* 30. memory.read()             | memory.read();
	* 31. CI: stn(0) ← extbus1      | statusMemory.storeIn0();

	* 32. PC -> intbus2             | PC.internalRead();
	* 33. ula <- intbus2            | ula.internalStore(1);
	* 34. ula.inc                   | ula.inc();
	* 35. ula -> intbus2            | ula.internalRead();
	* 36. pc <- intbus2             | PC.internalStore();
	* 37. pc -> extbus1             | PC.read();
	* 38. CI: stn(1) ← extbus1      | statusMemory.storeIn1();

	* 39. extbus1 <- flags          | extbus1.put(Flags.getBit(1));
	* 40. IR <- extbus1             | IR.store();
	* 41. IR -> intbus2             | IR.internalRead();
	* 42. ula <- intbus2            | ula.internalStore(1);
	* 43. extbus1 <- flags          | extbus1.put(Flags.getBit(0));
	* 44. IR <- extbus1             | IR.store();
	* 45. IR -> intbus2             | IR.internalRead();
	* 46. ula <- intbus2            | ula.internalStore(0);
	* 47. ula.add                   | ula.add();
	* 48. ula -> intbus2            | ula.internalRead(1);
	* 49. IR <- intbus2             | IR.internalStore();
	* 50. IR -> extbus1             | IR.read();

	* 51. statusMemory -> extbus1   | statusMemory.read();
	* 52. pc <- extbus1             | PC.store();
	//-------------- Fim do JGT ----------------------
	 * 
	 *  O JGT é o comando que faz com que se regA for maior que reg B, o PC aponte para o endereço
	 *  que está em um endereço de memória.
	*/

	public void jgt() {
				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				
				PC.read();
				memory.read();
				demux.setValue(extbus1.get());
				registersInternalRead();
				ula.internalStore(0);

				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				
				PC.read();
				memory.read();
				demux.setValue(extbus1.get());
				registersInternalRead();
				ula.internalStore(1);

				ula.sub();
				ula.internalRead(1);
				setStatusFlags(intbus2.get());

				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				PC.read();
				memory.read();
				statusMemory.storeIn0();

				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				PC.read();
				statusMemory.storeIn1();

				extbus1.put(Flags.getBit(1)); 
				IR.store(); 
				IR.internalRead(); 
				ula.internalStore(1); 
				extbus1.put(Flags.getBit(0)); 
				IR.store(); 
				IR.internalRead(); 
				ula.internalStore(0); 
				ula.add(); 
				ula.internalRead(1); 
				IR.internalStore();
				IR.read(); 
				
				statusMemory.read();
				PC.store();
	}
	

	/*
	
	*  ----------- Como Funciona o JLW --------------
	   ------ PC ++ -----
	*  1. pc -> intbus2   |         | PC.internalRead();
	*  2. ula <- intbus2  |         | ula.internalStore();
	*  3. ula.inc 		  |		    | ula.inc();
	*  4. ula -> intbus2  |         | ula.internalRead();
	*  5. pc <- intbus2   |         | PC.internalStore();
	   ------------------
	*  6. pc -> extbus1             | PC.read();
	*  7. memory.read() 		    | memory.read();
	*  8. demux <- extbus1          | demux.setValue(extbus1.get());
	*  9. registers -> intbus2      | registersInternalRead();
	* 10. ula <- intbus2            | ula.internalStore(0);

	* 11. PC -> intbus2             | PC.internalRead();
	* 12. ula <- intbus2            | ula.internalStore(1);
	* 13. ula.inc                   | ula.inc();
	* 14. ula -> intbus2            | ula.internalRead();
	* 15. pc <- intbus2             | PC.internalStore();

	* 16. pc -> extbus1             | PC.read();
	* 17. memory.read()             | memory.read();
	* 18. demux <- extbus1          | demux.setValue(extbus1.get());
	* 19. registers -> intbus2      | registersInternalRead();
	* 20. ula <- intbus2            | ula.internalStore(1);

	* 21. ula.sub                   | ula.sub();
	* 22. ula -> intbus2            | ula.internalRead();
	* 23. flags <- intbus2          | setStatusFlags(intbus2.get());

	* 24. pc -> intbus2             | PC.internalRead();
	* 25. ula <- intbus2            | ula.internalStore(1);
	* 26. ula.inc                   | ula.inc();
	* 27. ula -> intbus2            | ula.internalRead();
	* 28. pc <- intbus2             | PC.internalStore();
	* 29. pc -> extbus1             | PC.read();
	* 30. memory.read()             | memory.read();
	* 31. CI: stn(0) ← extbus1      | statusMemory.storeIn0();

	* 32. PC -> intbus2             | PC.internalRead();
	* 33. ula <- intbus2            | ula.internalStore(1);
	* 34. ula.inc                   | ula.inc();
	* 35. ula -> intbus2            | ula.internalRead();
	* 36. pc <- intbus2             | PC.internalStore();
	* 37. pc -> extbus1             | PC.read();
	* 38. CI: stn(1) ← extbus1      | statusMemory.storeIn0();

	* 39. extbus1 <- flags          | extbus1.put(Flags.getBit(1));
	* 40. statusMemory -> extbus1   | statusMemory.read();
	* 41. pc <- extbus1             | PC.store();
	//-------------- Fim do JLW ----------------------
	 * 
	 *  O JLW é o comando que faz com que se regA for menor que reg B, o PC aponte para o endereço
	 *  que está em um endereço de memória.
	*/
	
	public void jlw() {
				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				
				PC.read();
				memory.read();
				demux.setValue(extbus1.get());
				registersInternalRead();
				ula.internalStore(0);

				PC.internalRead();
				ula.internalStore(1); 
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				
				PC.read();
				memory.read();
				demux.setValue(extbus1.get());
				registersInternalRead();
				ula.internalStore(1);

				ula.sub();
				ula.internalRead(1);
				setStatusFlags(intbus2.get());

				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				PC.read();
				memory.read();
				statusMemory.storeIn1();

				PC.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				PC.read();
				statusMemory.storeIn0();

				extbus1.put(Flags.getBit(1));
				statusMemory.read();
				PC.store();
	}
	
	
	/*
	
	*  ----------- Como Funciona o CALL --------------
	   ------ PC ++ -----
	*  1. PC -> intbus2  |          | PC.internalRead();
	*  2. ula <- intbus2 |          | ula.internalStore(1);
	*  3. ula.inc        |          | ula.inc();
	*  4. ula -> intbus2 |          | ula.internalRead(1);
	*  5. PC <- intbus2  |          | PC.internalStore();
	   ------------------
	*  6. PC -> extbus1             | PC.read();
	*  7. memory.read() 		    | memory.read();
	*  8. PC <- extbus1             | PC.store();

	*  9. ula.inc                   | ula.inc();
	* 10. ula -> intbus2            | ula.internalRead(1);
	* 11. IR <- intbus2             | IR.internalStore();
	* 12. StkTOP -> extbus1         | StkTOP.read();
	* 13. memory.store()            | memory.store();
	* 14. IR -> extbus1             | IR.read();
	* 15. memory.store()            | memory.store();

	* 16. StkTOP -> intbus2         | StkTOP.read();
	* 17. IR <- extbus1             | IR.store();
	* 18. IR -> intbus2             | IR.internalRead();
	* 19. ula <- intbus2            | ula.internalStore(1);
	* 20. ula.inc                   | ula.inc();
	* 21. ula <- intbus2            | ula.internalStore(0);
	* 22. ula.sub                   | ula.sub();
	* 23. ula.add                   | ula.add();
	* 24. ula -> intbus2            | ula.internalRead(1);
	* 25. IR <- intbus2             | IR.internalStore();
	* 26. IR -> extbus1             | IR.read();
	* 27. StkTOP -> extbus1         | StkTOP.store();

	//-------------- Fim do JN ----------------------
	 * 
	 *  O JN é o comando que verifica se o bit negativo está ativo, se sim, ele faz com que o PC aponte
	 *  para o endereço que está no próximo endereço da memória. 
	 *  O PC já aponta para o próximo endereço, então não é necessário incrementar o PC.
	 *  A ULA é usada para ler o endereço do PC e armazená-lo na memória.
	 * 
	*/
	
	public void call() {
		
				PC.internalRead(); 
				ula.internalStore(1); 
				ula.inc();
				ula.internalRead(1);
				PC.internalStore();
				PC.read(); 
				memory.read();
				PC.store();

				ula.inc(); 
				ula.internalRead(1); 
				IR.internalStore();
				StkTOP.read();
				memory.store();
				IR.read();
				memory.store();

				StkTOP.read(); // coloca o valor no barramento
				IR.store();
				IR.internalRead();
				ula.internalStore(1);
				ula.inc();
				ula.internalStore(0);
				ula.sub(); // 1 <- 0 - 1
				ula.add();
				ula.internalRead(1);
				IR.internalStore();
				IR.read();
				StkTOP.store();		
	}

	/*
	
	*  ----------- Como Funciona o RET --------------
	   ------ PC ++ -----
	*  1. stkTOP -> extbus1         | StkTOP.read();
	*  2. IR <- extbus1             | IR.store();
	*  3. IR -> intbus2             | IR.internalRead();
	*  4. ula <- intbus2            | ula.internalStore(1);
	*  5. ula.inc                   | ula.inc();
	*  6. ula -> intbus2            | ula.internalRead(1);
	*  7. IR <- intbus2             | IR.internalStore();
	*  8. IR -> extbus1             | IR.read();
	*  9. stkTOP <- extbus1         | StkTOP.store();
	* 10. memory.read()             | memory.read();
	* 11. pc <- extbus1             | PC.store();
	//-------------- Fim do RET ----------------------
	 * 
	 *  O RET é o comando que retorna de uma chamada de função, ou seja, ele lê o endereço de retorno
	 * 
	*/

	public void ret() {

		StkTOP.read();
		IR.store();	
		IR.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		IR.internalStore();
		IR.read();
		StkTOP.store();
		memory.read();
		PC.store();
	}
	
	
	public void startStk(){
		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();

		PC.read();
		memory.read();
		StkBOT.store();
		StkTOP.store();

		PC.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		PC.internalStore();
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
			moveMemReg();
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
		case 22:
			startStk();
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
		int para1,para2,para3;
		para1 = para2 = para3 =0;
		
		for (Register r:registersList) {
			System.out.println(r.getRegisterName()+": "+r.getData());
		}
		if (command !=-1)
			instruction = commandsList.get(command);
		else
			instruction = "END";
		
		int operands = hasOperands(instruction);
		
		switch (operands) {
		case 1:
			para1 = memory.getDataList()[PC.getData()+1];
			System.out.println("Instruction: "+instruction+" "+para1);
			break;
		case 2:
			para1 = memory.getDataList()[PC.getData()+1];
			para2 = memory.getDataList()[PC.getData()+2];
			System.out.println("Instruction: "+instruction+" "+para1+ " "+ para2);
			break;
		case 3:
		    para1 = memory.getDataList()[PC.getData()+1]; // FALTAVA ISSO
		    para2 = memory.getDataList()[PC.getData()+2];
		    para3 = memory.getDataList()[PC.getData()+3];
		    System.out.println("Instruction: "+instruction+" "+para1+ " "+ para2 + " " + para3);
		    break;

		default:
			System.out.println("Instruction: "+instruction);
			if ("read".equals(instruction))
				System.out.println("memory["+para1+"]="+memory.getDataList()[para1]);
			break;
		}
		
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
		
		int ValBotton =  memory.getDataList()[1];
		int i = memorySize -1;
		while(i>ValBotton) {
			System.out.println(" Mem["+ i + "]: "+ memory.getDataList()[i]);
			i--;
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
	private int hasOperands(String instruction) {
		if ("ret".equals(instruction)) 
			return 0;
		else if("jlw".equals(instruction)
				|| "jgt".equals(instruction) 
				|| "jneq".equals(instruction) 
				|| "jeq".equals(instruction))
			return 3;
		else if("jn".equals(instruction) 
				|| "jz".equals(instruction) 
				|| "jmp".equals(instruction) 
				|| "inc".equals(instruction) 
				|| "call".equals(instruction)
				|| "startStk".equals(instruction))
			return 1;
		else
			return 2; // todos os outros
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
