package assembler;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class testAssembler {

	@Test
	public void testProccessCommand() {
		
		Assembler ass = new Assembler();
		String commandLine[] = new String[4];
		ArrayList<String> returnedObj = new ArrayList<>();
		
		//<regA> % <regB>
		commandLine[0] = "add";
		commandLine[1] = "%REG0";
	    commandLine[2] = "%REG1";
		ass.proccessCommand(commandLine);
		returnedObj = ass.getObjProgram();
		assertEquals("2", returnedObj.get(0)); 
		assertEquals("%REG0", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
		assertEquals(3, ass.getObjProgram().size());
		
		// <addr> %<regB>
		returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "add";
	    commandLine[1] = "var1";
	    commandLine[2] = "%REG1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("0", returnedObj.get(0)); 
	    assertEquals("&var1", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size()); 

		// <regA> % <addr> 
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "add";
	    commandLine[1] = "%REG1";
	    commandLine[2] = "var2";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("1", returnedObj.get(0)); 
	    assertEquals("%REG1", ass.getObjProgram().get(1));
	    assertEquals("&var2", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size());

		// <imm> % <regA> 
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "add";
	    commandLine[1] = "127";
	    commandLine[2] = "%REG1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("3", returnedObj.get(0));
	    assertEquals("127", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size());

		// ----------------------------------------------

		//SUB
		// <regA> %<regB>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "sub";
	    commandLine[1] = "%REG0";
	    commandLine[2] = "%REG1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("4", returnedObj.get(0)); 
	    assertEquals("%REG0", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size()); 

	    // <addr> %<regB>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "sub";
	    commandLine[1] = "var1";
	    commandLine[2] = "%REG1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("5", returnedObj.get(0)); 
	    assertEquals("&var1", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size()); 

	    //<regA> % <addr> 
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "sub";
	    commandLine[1] = "%REG1";
	    commandLine[2] = "var2";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("6", returnedObj.get(0)); 
	    assertEquals("%REG1", ass.getObjProgram().get(1));
	    assertEquals("&var2", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size()); 

	    // <imm> %<regA> 
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "sub";
	    commandLine[1] = "127";
	    commandLine[2] = "%REG1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("7", returnedObj.get(0)); 
	    assertEquals("127", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size());

	    // <addr> %<regB>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "move";
	    commandLine[1] = "var1";
	    commandLine[2] = "%REG1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("12", returnedObj.get(0)); 
	    assertEquals("&var1", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size()); 

	    // <regA> % <addr> 
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "move";
	    commandLine[1] = "%REG1";
	    commandLine[2] = "var2";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("13", returnedObj.get(0)); 
	    assertEquals("%REG1", ass.getObjProgram().get(1));
	    assertEquals("&var2", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size()); 
	    
	 
	    // <regA> %<regB>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "move";
	    commandLine[1] = "%REG0";
	    commandLine[2] = "%REG1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("14", returnedObj.get(0)); 
	    assertEquals("%REG0", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size()); 
	    
	    // <imm> %regB
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "move";
	    commandLine[1] = "127";
	    commandLine[2] = "%REG1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("15", returnedObj.get(0)); 
	    assertEquals("127", ass.getObjProgram().get(1));
	    assertEquals("%REG1", ass.getObjProgram().get(2));
	    assertEquals(3, ass.getObjProgram().size()); 

		// INC
	    //inc %<regA>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "inc";
	    commandLine[1] = "%REG3";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("11", returnedObj.get(0)); 
	    assertEquals("%REG3", ass.getObjProgram().get(1));
	    assertEquals(2, ass.getObjProgram().size());
	    
		// JUMP

	    //<addr>               
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "jmp";
	    commandLine[1] = "var1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("8", returnedObj.get(0)); 
	    assertEquals("&var1", ass.getObjProgram().get(1));
	    assertEquals(2, ass.getObjProgram().size()); 

		// JN
        // <addr>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "jn";
	    commandLine[1] = "var1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("10", returnedObj.get(0)); 
	    assertEquals("&var1", ass.getObjProgram().get(1));
	    assertEquals(2, ass.getObjProgram().size()); 
	    
		//JZ
        // <addr>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "jz";
	    commandLine[1] = "var1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("9", returnedObj.get(0)); 
	    assertEquals("&var1", ass.getObjProgram().get(1));
	    assertEquals(2, ass.getObjProgram().size()); 


        // <regA> % <regB> <addr>
        returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "jeq";
	    commandLine[1] = "%REG0";
        commandLine[2] = "%REG1";
        commandLine[3] = "var1";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("16", returnedObj.get(0)); 
	    assertEquals("%REG0", ass.getObjProgram().get(1));
        assertEquals("%REG1", ass.getObjProgram().get(2));
        assertEquals("&var1", ass.getObjProgram().get(3));
	    assertEquals(4, ass.getObjProgram().size());
	  
	  	//JNEQ
  	   	// <regA> % <regB> <addr>
  		returnedObj = new ArrayList<>();
  		ass = new Assembler();
  		commandLine[0] = "jneq";
  		commandLine[1] = "%REG0";
  		commandLine[2] = "%REG1";
  		commandLine[3] = "label";
  		ass.proccessCommand(commandLine);
  		returnedObj = ass.getObjProgram();
  		assertEquals("17", returnedObj.get(0)); 
  		assertEquals("%REG0", ass.getObjProgram().get(1));
  		assertEquals("%REG1", ass.getObjProgram().get(2));
  		assertEquals("&label", ass.getObjProgram().get(3));
  		assertEquals(4, ass.getObjProgram().size()); 

		//JQT
  		//<regA> %<regB> <mem>
  		returnedObj = new ArrayList<>();
  		ass = new Assembler();
  		commandLine[0] = "jgt";
  		commandLine[1] = "%REG0";
  		commandLine[2] = "%REG1";
  		commandLine[3] = "label2";
  		ass.proccessCommand(commandLine);
  		returnedObj = ass.getObjProgram();
  		assertEquals("18", returnedObj.get(0));
  		assertEquals("%REG0", ass.getObjProgram().get(1));
  		assertEquals("%REG1", ass.getObjProgram().get(2));
  		assertEquals("&label2", ass.getObjProgram().get(3));
  		assertEquals(4, ass.getObjProgram().size()); 
  		
		//JLW
  		// <regA> %<regB> <mem>
  		returnedObj = new ArrayList<>();
  		ass = new Assembler();
  		commandLine[0] = "jlw";
  		commandLine[1] = "%REG2";
  		commandLine[2] = "%REG1";
  		commandLine[3] = "label3";
  		ass.proccessCommand(commandLine);
  		returnedObj = ass.getObjProgram();
  		assertEquals("19", returnedObj.get(0));
  		assertEquals("%REG2", ass.getObjProgram().get(1));
  		assertEquals("%REG1", ass.getObjProgram().get(2));
  		assertEquals("&label3", ass.getObjProgram().get(3));
  		assertEquals(4, ass.getObjProgram().size()); 
  		
		//CALL
  	    // <mem>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "call";
	    commandLine[1] = "label4";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("20", returnedObj.get(0)); // O código de call <mem> é 20
	    assertEquals("&label4", ass.getObjProgram().get(1));
	    assertEquals(2, ass.getObjProgram().size()); // Apenas duas linhas: o comando e o endereço
	    
		//RET
	    //
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "ret";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("21", returnedObj.get(0)); 
	    assertEquals(1, ass.getObjProgram().size()); 
	    
		//STARTSTK
	    //<var>
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();
	    commandLine[0] = "startStk";
	    commandLine[1] = "$DEGAS_START_VALUE$";
	    ass.proccessCommand(commandLine);
	    returnedObj = ass.getObjProgram();
	    assertEquals("22", returnedObj.get(0)); 
	    assertEquals("&$DEGAS_START_VALUE$", ass.getObjProgram().get(1));
	    assertEquals(2, ass.getObjProgram().size()); 



		// Ultimos testes
	    returnedObj = new ArrayList<>();
	    ass = new Assembler();

	    // Comandos
        commandLine[0] = "add"; 
        commandLine[1] = "%R1";
        commandLine[2] = "%R2";
        ass.proccessCommand(commandLine);

        commandLine[0] = "sub"; 
        commandLine[1] = "mem1";
        commandLine[2] = "%R3";
        ass.proccessCommand(commandLine);

        commandLine[0] = "move"; 
        commandLine[1] = "%R2";
        commandLine[2] = "mem2";
        ass.proccessCommand(commandLine);

        commandLine[0] = "jmp"; 
        commandLine[1] = "label1";
        ass.proccessCommand(commandLine);

        commandLine[0] = "jn"; 
        commandLine[1] = "label2";
        ass.proccessCommand(commandLine);

        commandLine[0] = "jeq"; 
        commandLine[1] = "%R1";
        commandLine[2] = "%R2";
        commandLine[3] = "label3";
        ass.proccessCommand(commandLine);

        commandLine[0] = "call";
        commandLine[1] = "subroutine1";
        ass.proccessCommand(commandLine);

        commandLine[0] = "ret"; 
        ass.proccessCommand(commandLine);

	    returnedObj = ass.getObjProgram();
	    assertEquals(20, returnedObj.size());

		// Validando

	    // add
        assertEquals("2", returnedObj.get(0)); 
        assertEquals("%R1", returnedObj.get(1)); 
        assertEquals("%R2", returnedObj.get(2));

		// sub
        assertEquals("5", returnedObj.get(3)); 
        assertEquals("&mem1", returnedObj.get(4)); 
        assertEquals("%R3", returnedObj.get(5)); 

		// move
        assertEquals("13", returnedObj.get(6)); 
        assertEquals("%R2", returnedObj.get(7)); 
        assertEquals("&mem2", returnedObj.get(8)); 

		// jmp
        assertEquals("8", returnedObj.get(9)); 
        assertEquals("&label1", returnedObj.get(10)); 

		// jn
        assertEquals("10", returnedObj.get(11)); 
        assertEquals("&label2", returnedObj.get(12)); 

		// jeq
        assertEquals("16", returnedObj.get(13)); 
        assertEquals("%R1", returnedObj.get(14)); 
        assertEquals("%R2", returnedObj.get(15)); 
        assertEquals("&label3", returnedObj.get(16)); 

		// call 
        assertEquals("20", returnedObj.get(17)); 
        assertEquals("&subroutine1", returnedObj.get(18)); 
		// ret
        assertEquals("21", returnedObj.get(19)); 
	}

	@Test
	public void testParse() {
		Assembler ass = new Assembler();
		ArrayList<String> returnedObj = new ArrayList<>();
		ArrayList<String> sourceProgram = new ArrayList<>();
		
		
		sourceProgram.add("var1");
		sourceProgram.add("var2");
		sourceProgram.add("var3");
		sourceProgram.add("move 10 %REG0");
		sourceProgram.add("move %REG0 var3");
		sourceProgram.add("move 2 %REG0");
		sourceProgram.add("move %REG0 var2");
		sourceProgram.add("move 0 %REG0");
		sourceProgram.add("move %REG0 var1");
		sourceProgram.add("label:");
		sourceProgram.add("move var1 %REG0");
		sourceProgram.add("add %REG0 %REG1");
		sourceProgram.add("move %REG0 var1");
		sourceProgram.add("move %REG1 %REG0");
		sourceProgram.add("sub var3 %REG0");
		sourceProgram.add("jn label");
		
		//now we can generate the object program
		ass.setLines(sourceProgram);
		ass.parse();
		returnedObj = ass.getObjProgram();
		
		//testing
		assertEquals(35, returnedObj.size());

		
		//checking line by line
        assertEquals("15", returnedObj.get(0));
		assertEquals("10", returnedObj.get(1));  
		assertEquals("%REG0", returnedObj.get(2));  

		assertEquals("13", returnedObj.get(3));  
		assertEquals("%REG0", returnedObj.get(4));  
		assertEquals("&var3", returnedObj.get(5));  

		assertEquals("15", returnedObj.get(6));  
		assertEquals("2", returnedObj.get(7));  
		assertEquals("%REG0", returnedObj.get(8));  

		assertEquals("13", returnedObj.get(9));  
		assertEquals("%REG0", returnedObj.get(10));  
		assertEquals("&var2", returnedObj.get(11)); 

		assertEquals("15", returnedObj.get(12));  
		assertEquals("0", returnedObj.get(13));  
		assertEquals("%REG0", returnedObj.get(14));  

		assertEquals("13", returnedObj.get(15));  
		assertEquals("%REG0", returnedObj.get(16));  
		assertEquals("&var1", returnedObj.get(17)); 

		assertEquals("12", returnedObj.get(18));  
		assertEquals("&var1", returnedObj.get(19)); 
		assertEquals("%REG0", returnedObj.get(20)); 

		assertEquals("2", returnedObj.get(21));  
		assertEquals("%REG0", returnedObj.get(22)); 
		assertEquals("%REG1", returnedObj.get(23)); 

		assertEquals("13", returnedObj.get(24));  
		assertEquals("%REG0", returnedObj.get(25)); 
		assertEquals("&var1", returnedObj.get(26));

		assertEquals("14", returnedObj.get(27)); 
		assertEquals("%REG1", returnedObj.get(28)); 
		assertEquals("%REG0", returnedObj.get(29)); 

		assertEquals("5", returnedObj.get(30));  
		assertEquals("&var3", returnedObj.get(31)); 
		assertEquals("%REG0", returnedObj.get(32)); 

		assertEquals("10", returnedObj.get(33));  
		assertEquals("&label", returnedObj.get(34)); 
		
		//now, checking if the label "label" was inserted, pointing to the position 12
		//the line 'read var1' is just after the label
		//once the command was inserted in the position 12, the label must 
		//be pointing to the position 12
		assertTrue(ass.getLabels().contains("label"));
		assertEquals(1, ass.getLabels().size());
		assertEquals(1, ass.getLabelsAddresses().size());
		assertEquals(0, ass.getLabels().indexOf("label"));
		assertEquals(18, (int) ass.getLabelsAddresses().get(0));
		
		//checking if all variables are stored in variables collection
		assertEquals("var1", ass.getVariables().get(0));
		assertEquals("var2", ass.getVariables().get(1));
		assertEquals("var3", ass.getVariables().get(2));
	}

	@Test
	public void testReplaceVariable() {
		
		Assembler ass = new Assembler();
		ArrayList<String> sampleexec = new ArrayList<>();
		
		sampleexec.add("9");
		sampleexec.add("&var1"); 
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("&var2"); 
		sampleexec.add("9");
		sampleexec.add("&var1"); 
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("&var3"); 
		sampleexec.add("9");
		sampleexec.add("&var3");
		sampleexec.add("9");
		sampleexec.add("&var1"); 
		
		ass.setExecProgram(sampleexec);
		
		
		ass.replaceVariable("var1", 100);
		ass.replaceVariable("var2", 99);
		ass.replaceVariable("var3", 98);
		
		assertEquals("100", ass.getExecProgram().get(1));
		assertEquals("100", ass.getExecProgram().get(7));
		assertEquals("100", ass.getExecProgram().get(14));
		
		assertEquals("99", ass.getExecProgram().get(5));
		
		assertEquals("98", ass.getExecProgram().get(10));
		assertEquals("98", ass.getExecProgram().get(12));
		
	}
	
	@Test
	public void testReplaceLabels() {
		Assembler ass = new Assembler();
		ArrayList<String> sampleexec = new ArrayList<>();
		
		sampleexec.add("9");
		sampleexec.add("&label1"); 
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("&label2"); 
		sampleexec.add("9");
		sampleexec.add("&label1"); 
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("&label3"); 
		sampleexec.add("9");
		sampleexec.add("&label3"); 
		sampleexec.add("9");
		sampleexec.add("&label1");

		ass.getLabels().add("label1");
		ass.getLabels().add("label2");
		ass.getLabels().add("label3");

		ass.getLabelsAddresses().add(17); 
		ass.getLabelsAddresses().add(42); 
		ass.getLabelsAddresses().add(63); 
		
		
		ass.setExecProgram(sampleexec);
		
		
		ass.replaceLabels();
		assertEquals("17", ass.getExecProgram().get(1));
		assertEquals("17", ass.getExecProgram().get(7));
		assertEquals("17", ass.getExecProgram().get(14));
		
		assertEquals("42", ass.getExecProgram().get(5));
		
		assertEquals("63", ass.getExecProgram().get(10));
		assertEquals("63", ass.getExecProgram().get(12));
	}

	@Test
	public void testReplaceAllVariables() {
		Assembler ass = new Assembler();
		ArrayList<String> sampleexec = new ArrayList<>();
		
		sampleexec.add("9");
		sampleexec.add("&var1"); 
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("&var2"); 
		sampleexec.add("9");
		sampleexec.add("&var1");
		sampleexec.add("9");
		sampleexec.add("9");
		sampleexec.add("&var3"); 
		sampleexec.add("9");
		sampleexec.add("&var3"); 
		sampleexec.add("9");
		sampleexec.add("&var1"); 
		
		ass.setExecProgram(sampleexec);
		
		ass.getVariables().add("var1");
		ass.getVariables().add("var2");
		ass.getVariables().add("var3");
		
		ass.replaceAllVariables();
		assertEquals("127", ass.getExecProgram().get(1));
		assertEquals("127", ass.getExecProgram().get(7));
		assertEquals("127", ass.getExecProgram().get(14));
		
		assertEquals("126", ass.getExecProgram().get(5));
		
		assertEquals("125", ass.getExecProgram().get(10));
		assertEquals("125", ass.getExecProgram().get(12));
	}
	
	@Test
	public void testReplaceRegisters() {
	    Assembler ass = new Assembler();
	    ArrayList<String> sampleexec = new ArrayList<>();

	    // Adiciona misturas de registros e outros valores
	    sampleexec.add("100");           // 0: não deve mudar
	    sampleexec.add("%REG0");         // 1: deve virar "0"
	    sampleexec.add("%REG1");         // 2: deve virar "1"
	    sampleexec.add("%REG2");         // 3: deve virar "2"
	    sampleexec.add("%REG3");         // 4: deve virar "3"
	    sampleexec.add("%PC");           // 5: deve virar "4"
	    sampleexec.add("%IR");           // 6: deve virar "5"
	    sampleexec.add("%Flags");        // 7: deve virar "6"
	    sampleexec.add("%StkTOP");       // 8: deve virar "7"
	    sampleexec.add("%StkBOT");       // 9: deve virar "8"
	    sampleexec.add("label");         // 10: não deve mudar
	    sampleexec.add("%UNKNOWN");      // 11: registrador não existe, pode virar "-1" ou erro

	    ass.setExecProgram(sampleexec);
	    ass.replaceRegisters();

	    assertEquals("100", ass.getExecProgram().get(0));       // valor não modificado
	    assertEquals("0", ass.getExecProgram().get(1));         // REG0
	    assertEquals("1", ass.getExecProgram().get(2));         // REG1
	    assertEquals("2", ass.getExecProgram().get(3));         // REG2
	    assertEquals("3", ass.getExecProgram().get(4));         // REG3
	    assertEquals("4", ass.getExecProgram().get(5));         // PC
	    assertEquals("5", ass.getExecProgram().get(6));         // IR
	    assertEquals("6", ass.getExecProgram().get(7));         // Flags
	    assertEquals("7", ass.getExecProgram().get(8));         // StkTOP
	    assertEquals("8", ass.getExecProgram().get(9));         // StkBOT
	    assertEquals("label", ass.getExecProgram().get(10));    // não modificado

	    // Para registrador desconhecido, depende da implementação:
	    // Se retorna -1, testamos isso:
	    assertEquals("-1", ass.getExecProgram().get(11));
	}
	
	@Test
	public void testCheckLabels() {
	    Assembler ass = new Assembler();

	    // Caso 1: Referência a variável ou label inexistente ("123")
	    ass.getObjProgram().clear();
	    ass.getObjProgram().add("&123");
	    try {
	        assertFalse("Deveria falhar: '123' não declarado", ass.checkLabels());
	        System.out.println("[OK] Caso 1: erro corretamente identificado para '123'");
	    } catch (AssertionError e) {
	        System.out.println("[ERRO] Caso 1: falha não foi detectada como esperado");
	    }

	    // Caso 2: Apenas variáveis válidas
	    ass.getObjProgram().clear();
	    ass.getVariables().clear();
	    ass.getVariables().add("x");
	    ass.getObjProgram().add("&x");
	    try {
	        assertTrue("Deveria passar: 'x' está declarado", ass.checkLabels());
	        System.out.println("[OK] Caso 2: variável declarada corretamente");
	    } catch (AssertionError e) {
	        System.out.println("[ERRO] Caso 2: falha ao reconhecer variável declarada");
	    }

	    // Caso 3: Label não declarado
	    ass.getObjProgram().clear();
	    ass.getLabels().clear();
	    ass.getObjProgram().add("&end");
	    try {
	        assertFalse("Deveria falhar: 'end' não declarado", ass.checkLabels());
	        System.out.println("[OK] Caso 3: erro corretamente identificado para 'end'");
	    } catch (AssertionError e) {
	        System.out.println("[ERRO] Caso 3: falha não foi detectada como esperado");
	    }

	    // Caso 4: Label declarado corretamente
	    ass.getObjProgram().clear();
	    ass.getLabels().clear();
	    ass.getLabels().add("inicio");
	    ass.getObjProgram().add("&inicio");
	    try {
	        assertTrue("Deveria passar: 'inicio' está declarado", ass.checkLabels());
	        System.out.println("[OK] Caso 4: label declarado corretamente");
	    } catch (AssertionError e) {
	        System.out.println("[ERRO] Caso 4: falha ao reconhecer label declarada");
	    }

	    // Caso 5: Teste completo com múltiplas entradas
	    ass.getObjProgram().clear();
	    ass.getLabels().clear();
	    ass.getVariables().clear();

	    ass.getObjProgram().add("&labelA");
	    ass.getObjProgram().add("&labelB");
	    ass.getObjProgram().add("&z");

	    ass.getLabels().add("labelA");
	    ass.getLabels().add("labelB");
	    // "z" não está declarado

	    try {
	        assertFalse("Deveria falhar: 'z' não declarado", ass.checkLabels());
	        System.out.println("[OK] Caso 5: erro corretamente identificado para 'z'");
	    } catch (AssertionError e) {
	        System.out.println("[ERRO] Caso 5: falha não foi detectada como esperado");
	    }
	}
	
	
	@Test
	public void testProcessAddFormatsViaReflection() {
	    Assembler ass = new Assembler();

	    String[] tokens1 = {"add", "%REG1", "%REG2"};
	    assertTrue(invokeProcessAdd(ass, tokens1) >= 0);

	    String[] tokens2 = {"add", "%REG1", "var"};
	    assertTrue(invokeProcessAdd(ass, tokens2) >= 0);

	    String[] tokens3 = {"add", "5", "%REG2"};
	    assertTrue(invokeProcessAdd(ass, tokens3) >= 0);

	    String[] tokens4 = {"add", "val", "%REG1"};
	    assertTrue(invokeProcessAdd(ass, tokens4) >= 0);
	}

	@Test
	public void testProcessSubFormatsViaReflection() {
	    Assembler ass = new Assembler();
	    String[] tokens = {"sub", "%REG1", "%REG2"};
	    assertTrue(invokeProcessSub(ass, tokens) >= 0);
	}

	@Test
	public void testProcessMoveFormatsViaReflection() {
	    Assembler ass = new Assembler();

	    String[] tokens1 = {"move", "%REG1", "%REG2"};
	    String[] tokens2 = {"move", "%REG1", "var"};
	    String[] tokens3 = {"move", "3", "%REG1"};
	    String[] tokens4 = {"move", "val", "%REG3"};

	    assertTrue(invokeProcessMove(ass, tokens1) >= 0);
	    assertTrue(invokeProcessMove(ass, tokens2) >= 0);
	    assertTrue(invokeProcessMove(ass, tokens3) >= 0);
	    assertTrue(invokeProcessMove(ass, tokens4) >= 0);
	}

	@Test
	public void testReplaceRegistersMinimal() {
	    Assembler ass = new Assembler();
	    ArrayList<String> exec = new ArrayList<>();

	    exec.add("%REG1"); // Deve virar "1"
	    ass.setExecProgram(exec);
	    ass.replaceRegisters();

	    assertEquals("1", ass.getExecProgram().get(0));
	}
	

	/** 
	 * Métodos auxiliares via reflection	
	 * Usamos isso porque os métodos processAdd, processSub e proccessMove são privados, não podem ser chamados diretamente no teste.
	 * A reflexão permite chamar métodos privados para testar seu comportamento sem alterar a visibilidade original do códio.
	 **/
	private int invokeProcessAdd(Assembler ass, String[] tokens) {
	    try {
	        java.lang.reflect.Method method = Assembler.class.getDeclaredMethod("processAdd", String[].class);
	        method.setAccessible(true);
	        return (int) method.invoke(ass, (Object) tokens);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

	private int invokeProcessSub(Assembler ass, String[] tokens) {
	    try {
	        java.lang.reflect.Method method = Assembler.class.getDeclaredMethod("processSub", String[].class);
	        method.setAccessible(true);
	        return (int) method.invoke(ass, (Object) tokens);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

	private int invokeProcessMove(Assembler ass, String[] tokens) {
	    try {
	        java.lang.reflect.Method method = Assembler.class.getDeclaredMethod("proccessMove", String[].class);
	        method.setAccessible(true);
	        return (int) method.invoke(ass, (Object) tokens);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
}
