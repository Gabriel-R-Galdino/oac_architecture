package architecture;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import components.Memory;

public class TestArchitecture {
	
	//uncomment the anotation below to run the architecture showing components status
	//@Test
	public void testShowComponentes() {

		ArchitectureD3 arch = new ArchitectureD3(true);
		arch.getMemory().getDataList()[0]=7;
		arch.getMemory().getDataList()[1]=2;
		arch.getMemory().getDataList()[2]=6;
		arch.getMemory().getDataList()[3]=40;
		arch.getMemory().getDataList()[4]=7;
		arch.getMemory().getDataList()[5]=-4;
		arch.getMemory().getDataList()[6]=6;
		arch.getMemory().getDataList()[7]=41;
		arch.getMemory().getDataList()[8]=5;
		arch.getMemory().getDataList()[9]=40;
		arch.getMemory().getDataList()[10]=0;
		arch.getMemory().getDataList()[11]=40;
		arch.getMemory().getDataList()[12]=6;
		arch.getMemory().getDataList()[13]=40;
		arch.getMemory().getDataList()[14]=5;
		arch.getMemory().getDataList()[15]=41;
		arch.getMemory().getDataList()[16]=8;
		arch.getMemory().getDataList()[17]=4;
		arch.getMemory().getDataList()[18]=6;
		arch.getMemory().getDataList()[19]=-1;
		arch.getMemory().getDataList()[40]=0;
		arch.getMemory().getDataList()[41]=0;
		
		arch.controlUnitEexec();
		
	}

	@Test
	public void testAddRegReg() {
	    ArchitectureD3 arch = new ArchitectureD3();

	    arch.getMemory().getDataList()[31] = 0;
	    arch.getMemory().getDataList()[32] = 1;

	    arch.getExtbus1().put(30);
	    arch.getPC().store();

	    arch.getIntbus1().put(1);
	    arch.getRegistersList().get(0).store(); 
	    arch.getIntbus1().put(2);
	    arch.getRegistersList().get(1).store(); 

	    arch.addRegARegB();

	    arch.getRegistersList().get(0).read();
	    assertEquals(1, arch.getIntbus1().get());
	    arch.getRegistersList().get(1).read();
	    assertEquals(3, arch.getIntbus1().get());

	    arch.getPC().read();
	    assertEquals(33, arch.getExtbus1().get());

	    assertEquals(0, arch.getFlags().getBit(0));
	    assertEquals(0, arch.getFlags().getBit(1));
	}

	@Test
	public void testAddMemReg() {
	    ArchitectureD3 arch = new ArchitectureD3();

	    arch.getMemory().getDataList()[40] = 5;

	    arch.getMemory().getDataList()[11] = 40;
	    arch.getMemory().getDataList()[12] = 0;

	    arch.getExtbus1().put(10);
	    arch.getPC().store();

	    arch.getIntbus1().put(10);
	    arch.getRegistersList().get(0).store(); 

	    arch.addMemReg();

	    arch.getRegistersList().get(0).read();
	    assertEquals(15, arch.getIntbus1().get());

	    arch.getExtbus1().put(40);
	    arch.getMemory().read();
	    assertEquals(5, arch.getExtbus1().get());

	    arch.getPC().read();
	    assertEquals(13, arch.getExtbus1().get());

	    assertEquals(0, arch.getFlags().getBit(0));
	    assertEquals(0, arch.getFlags().getBit(1));
	}

	@Test
	public void testAddRegMem() {
	    ArchitectureD3 arch = new ArchitectureD3();

	    arch.getMemory().getDataList()[40] = 5;

	    arch.getMemory().getDataList()[11] = 0;

	    arch.getMemory().getDataList()[12] = 40;

	    arch.getExtbus1().put(10);
	    arch.getPC().store();

	    arch.getIntbus1().put(10);
	    arch.getRegistersList().get(0).store(); 

	    arch.addRegMem();

	    arch.getRegistersList().get(0).read();
	    assertEquals(10, arch.getIntbus1().get());

	    arch.getExtbus1().put(40);
	    arch.getMemory().read();
	    assertEquals(15, arch.getExtbus1().get());

	    arch.getPC().read();
	    assertEquals(13, arch.getExtbus1().get());

	    assertEquals(0, arch.getFlags().getBit(0));
	    assertEquals(0, arch.getFlags().getBit(1));
	}

	@Test
	public void testAddImmReg() {
	    ArchitectureD3 arch = new ArchitectureD3();

	    arch.getMemory().getDataList()[11] = 5;

	    arch.getMemory().getDataList()[12] = 0;

	    arch.getExtbus1().put(10);
	    arch.getPC().store();


	    arch.getIntbus1().put(10);
	    arch.getRegistersList().get(0).store(); 


	    arch.addImmReg();

	    arch.getRegistersList().get(0).read();
	    assertEquals(15, arch.getIntbus1().get());


	    arch.getPC().read();
	    assertEquals(13, arch.getExtbus1().get());

	    // os bits de flag 0 e 1 devem ser 0
	    assertEquals(0, arch.getFlags().getBit(0));
	    assertEquals(0, arch.getFlags().getBit(1));
	}

	
	@Test
	public void testSubRegReg() {
	    ArchitectureD3 arch = new ArchitectureD3();

	    arch.getMemory().getDataList()[31] = 0;

	    arch.getMemory().getDataList()[32] = 1;

	    arch.getExtbus1().put(30);
	    arch.getPC().store();

	    arch.getIntbus1().put(1);
	    arch.getRegistersList().get(0).store(); 
	    arch.getIntbus1().put(2);
	    arch.getRegistersList().get(1).store(); 

	    arch.subRegARegB();

	    arch.getRegistersList().get(0).read();
	    assertEquals(1, arch.getIntbus1().get());
	    arch.getRegistersList().get(1).read();
	    assertEquals(-1, arch.getIntbus1().get());


	    arch.getPC().read();
	    assertEquals(33, arch.getExtbus1().get());


	    assertEquals(0, arch.getFlags().getBit(0));
	    assertEquals(1, arch.getFlags().getBit(1));
	}

	@Test
	public void testSubMemReg() {
	    ArchitectureD3 arch = new ArchitectureD3();

	    arch.getMemory().getDataList()[40] = 5;

	    arch.getMemory().getDataList()[11] = 40;

	    arch.getMemory().getDataList()[12] = 0;

	    arch.getExtbus1().put(10);
	    arch.getPC().store();


	    arch.getIntbus1().put(10);
	    arch.getRegistersList().get(0).store(); 

	    arch.subMemReg();

	    arch.getRegistersList().get(0).read();
	    assertEquals(-5, arch.getIntbus1().get());

	    arch.getExtbus1().put(40);
	    arch.getMemory().read();
	    assertEquals(5, arch.getExtbus1().get());

	    arch.getPC().read();
	    assertEquals(13, arch.getExtbus1().get());

	    assertEquals(0, arch.getFlags().getBit(0));
	    assertEquals(1, arch.getFlags().getBit(1));
	}

	@Test
	public void testSubRegMem() {
	    ArchitectureD3 arch = new ArchitectureD3();

	    arch.getMemory().getDataList()[40] = 5;

	    arch.getMemory().getDataList()[11] = 0;

	    arch.getMemory().getDataList()[12] = 40;


	    arch.getExtbus1().put(10);
	    arch.getPC().store();

	    arch.getIntbus1().put(10);
	    arch.getRegistersList().get(0).store();

	    arch.subRegMem();


	    arch.getRegistersList().get(0).read();
	    assertEquals(10, arch.getIntbus1().get());

	    arch.getExtbus1().put(40);
	    arch.getMemory().read();
	    assertEquals(5, arch.getExtbus1().get());


	    arch.getPC().read();
	    assertEquals(13, arch.getExtbus1().get());


	    assertEquals(0, arch.getFlags().getBit(0));
	    assertEquals(0, arch.getFlags().getBit(1));
	}

	@Test
	public void testSubImmReg() {
	    ArchitectureD3 arch = new ArchitectureD3();

	    arch.getMemory().getDataList()[11] = 5;

	    arch.getMemory().getDataList()[12] = 0;

	    arch.getExtbus1().put(10);
	    arch.getPC().store();

	    arch.getIntbus1().put(10);
	    arch.getRegistersList().get(0).store(); 

	    arch.subImmReg();

	    arch.getRegistersList().get(0).read();
	    assertEquals(-5, arch.getIntbus1().get());


	    arch.getPC().read();
	    assertEquals(13, arch.getExtbus1().get());

	    assertEquals(0, arch.getFlags().getBit(0));
	    assertEquals(1, arch.getFlags().getBit(1));
	}
	
	
	@Test
	public void testMoveMemReg() {
		ArchitectureD3 arch = new ArchitectureD3();

		arch.getMemory().getDataList()[31] = 37;

		arch.getMemory().getDataList()[32] = 0;

		arch.getMemory().getDataList()[37] = 1;

		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(45);
		arch.getRegistersList().get(0).store(); 

		arch.moveMemReg();


		arch.getRegistersList().get(0).read();
		assertEquals(1, arch.getIntbus1().get());


		arch.getPC().read();
		assertEquals(33, arch.getExtbus1().get());
	}

	@Test
	public void testMoveRegMem() {
		ArchitectureD3 arch = new ArchitectureD3();

		arch.getMemory().getDataList()[31] = 0;

		arch.getMemory().getDataList()[32] = 37;

		arch.getMemory().getDataList()[37] = 15;

		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(1);
		arch.getRegistersList().get(0).store(); 


		arch.moveRegMem();


		arch.getExtbus1().put(37);
	    arch.getMemory().read();
	    assertEquals(1, arch.getExtbus1().get());


		arch.getPC().read();
		assertEquals(33, arch.getExtbus1().get());
	}
	
	@Test
	public void testMoveRegReg() {
		ArchitectureD3 arch = new ArchitectureD3();

		arch.getMemory().getDataList()[31] = 1;
		arch.getMemory().getDataList()[32] = 0;

		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(30);
		arch.getRegistersList().get(0).store(); 
		arch.getIntbus1().put(10);
		arch.getRegistersList().get(1).store(); 


		arch.moveRegARegB();

		arch.getRegistersList().get(0).read();
		assertEquals(10, arch.getIntbus1().get());
		arch.getRegistersList().get(1).read();
		assertEquals(10, arch.getIntbus1().get());

		arch.getPC().read();
		assertEquals(33, arch.getExtbus1().get());
	}
	
	@Test
	public void testMoveImmReg() {
		ArchitectureD3 arch = new ArchitectureD3();

		arch.getMemory().getDataList()[31] = 1;

		arch.getMemory().getDataList()[32] = 0;

		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(31);
		arch.getRegistersList().get(0).store();

		arch.moveImmReg();

		arch.getExtbus1().put(31);
	    arch.getMemory().read();
	    assertEquals(1, arch.getExtbus1().get());
	    

	    arch.getRegistersList().get(0).read();
		assertEquals(1, arch.getIntbus1().get());
	    

		arch.getPC().read();
		assertEquals(33, arch.getExtbus1().get());
	}
	
	@Test
	public void testInc() {
		
		ArchitectureD3 arch = new ArchitectureD3();

		arch.getMemory().getDataList()[31] = 0;

		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(3);
		arch.getRegistersList().get(0).store(); 

		arch.inc();

		arch.getRegistersList().get(0).read();
		assertEquals(4, arch.getIntbus1().get());

		arch.getPC().read();
		assertEquals(32, arch.getExtbus1().get());
	}
	
	@Test
	public void testJmp() {
		ArchitectureD3 arch = new ArchitectureD3();
		arch.getIntbus2().put(10);
		arch.getPC().internalStore();

		arch.getExtbus1().put(11); 
		arch.getMemory().store();
		arch.getExtbus1().put(15);
		arch.getMemory().store();

        
		
		arch.getPC().read();
		assertEquals(10, arch.getExtbus1().get());
		
		arch.jmp();
		arch.getPC().internalRead();;
		assertEquals(15, arch.getIntbus2().get());

	}
	
	@Test
	public void testJz() {
		ArchitectureD3 arch = new ArchitectureD3();
		
		arch.getIntbus2().put(10);
		arch.getPC().internalStore();
		
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(15);
		arch.getMemory().store();

        arch.getExtbus1().put(15);
		arch.getMemory().store();
		arch.getExtbus1().put(17);
		arch.getMemory().store();


		arch.getFlags().setBit(0, 1);
		
		arch.getPC().read();
		assertEquals(10, arch.getExtbus1().get());		

		arch.jz();
		
		arch.getPC().internalRead();
		assertEquals(15, arch.getIntbus2().get());
		
		arch.getFlags().setBit(0, 0);

		arch.getExtbus1().put(10);
		arch.getPC().store();

		arch.getExtbus1().put(0);


		arch.getPC().read();
		assertEquals(10, arch.getExtbus1().get());	
		
		arch.jz();
		
		arch.getPC().internalRead();
		assertEquals(12, arch.getIntbus2().get());
	}
	
	@Test
	public void testJn() {
		ArchitectureD3 arch = new ArchitectureD3();
		
		arch.getIntbus2().put(10);
		arch.getPC().internalStore();
		
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(15);
		arch.getMemory().store();
		


		arch.getFlags().setBit(1, 1);
		

		arch.getPC().read();
		assertEquals(10, arch.getExtbus1().get());		

		arch.jn();
		
		
		
		arch.getFlags().setBit(1, 0);
		arch.getExtbus1().put(10);
		arch.getPC().store();
		arch.getExtbus1().put(0);
		
		arch.jn();

		arch.getPC().read();
		assertEquals(12, arch.getExtbus1().get());	
		
		
	}
	
    @Test
    public void testeJeq(){
        ArchitectureD3 arch = new ArchitectureD3();
		arch.getMemory().getDataList()[11]=0;

		arch.getMemory().getDataList()[12]=1;

		arch.getIntbus2().put(10);
		arch.getPC().internalStore();
		
		
		arch.getExtbus1().put(13);
		arch.getMemory().store();
		arch.getExtbus1().put(15);
		arch.getMemory().store();
		
        
		arch.getIntbus1().put(45);
		arch.getRegistersList().get(0).store(); 
		arch.getIntbus1().put(99);
		arch.getRegistersList().get(1).store(); 

		arch.getPC().read();
		assertEquals(10, arch.getExtbus1().get());
        arch.jeq();
            

		arch.getPC().internalRead();
		assertEquals(14, arch.getIntbus2().get());

		arch.getIntbus2().put(10);
		arch.getPC().internalStore();
		
        arch.getIntbus1().put(45);
		arch.getRegistersList().get(0).store(); 
		arch.getRegistersList().get(1).store(); 

        arch.getPC().read();
		assertEquals(10, arch.getExtbus1().get());
        arch.jeq();
        
		arch.getPC().internalRead();
		assertEquals(15, arch.getIntbus2().get());

     }
	
	@Test
	public void testJneq() {
		ArchitectureD3 arch = new ArchitectureD3(); 


		arch.getMemory().getDataList()[31] = 0;  
		arch.getMemory().getDataList()[32] = 1;  
		arch.getMemory().getDataList()[33] = 100;  
	

		arch.getExtbus1().put(30);
		arch.getPC().store();
	

		arch.getIntbus1().put(1);
		arch.getRegistersList().get(0).store(); 
		arch.getIntbus1().put(2);
		arch.getRegistersList().get(1).store();
	
		arch.jneq();
	

		arch.getRegistersList().get(0).read();
		assertEquals(1, arch.getIntbus1().get());
		arch.getRegistersList().get(1).read();
		assertEquals(2, arch.getIntbus1().get());
	

		arch.getPC().read();
		assertEquals(100, arch.getExtbus1().get());
	


		arch.getExtbus1().put(30);
		arch.getPC().store();
	

		arch.getIntbus1().put(5);
		arch.getRegistersList().get(0).store();
		arch.getRegistersList().get(1).store(); 
	
		arch.jneq();
	

		arch.getRegistersList().get(0).read();
		assertEquals(5, arch.getIntbus1().get());
		arch.getRegistersList().get(1).read();
		assertEquals(5, arch.getIntbus1().get());
	

		arch.getPC().read();
		assertEquals(34, arch.getExtbus1().get());
	

		assertEquals(1, arch.getFlags().getBit(0)); 
		assertEquals(0, arch.getFlags().getBit(1)); 
	}
	
	@Test
	public void testJgt() {
		ArchitectureD3 arch = new ArchitectureD3();

		arch.getMemory().getDataList()[31] = 0;  
		arch.getMemory().getDataList()[32] = 1;  
		arch.getMemory().getDataList()[33] = 100;  


		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(1);
		arch.getRegistersList().get(0).store(); 
		arch.getIntbus1().put(2); 
		arch.getRegistersList().get(1).store(); 

		arch.jgt();

		arch.getRegistersList().get(0).read();
		assertEquals(1, arch.getIntbus1().get());
		arch.getRegistersList().get(1).read();
		assertEquals(2, arch.getIntbus1().get());


		arch.getPC().read();
		assertEquals(34, arch.getExtbus1().get());

		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(2);
		arch.getRegistersList().get(0).store();
		arch.getIntbus1().put(1);
		arch.getRegistersList().get(1).store(); 

		arch.jgt();

		arch.getPC().read();
		assertEquals(100, arch.getExtbus1().get());


		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(5);
		arch.getRegistersList().get(0).store(); 
		arch.getRegistersList().get(1).store(); 
		
		arch.jgt();

		arch.getPC().read(); 
		assertEquals(34, arch.getExtbus1().get());
	}
	
	@Test
	public void testJlw() {
		ArchitectureD3 arch = new ArchitectureD3();


		arch.getMemory().getDataList()[31] = 0;  
		arch.getMemory().getDataList()[32] = 1;  
		arch.getMemory().getDataList()[33] = 100;  


		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(1);
		arch.getRegistersList().get(0).store(); 
		arch.getIntbus1().put(2);
		arch.getRegistersList().get(1).store(); 

		arch.jlw();

		arch.getRegistersList().get(0).read();
		assertEquals(1, arch.getIntbus1().get());
		arch.getRegistersList().get(1).read();
		assertEquals(2, arch.getIntbus1().get());

		arch.getPC().read();
		assertEquals(100, arch.getExtbus1().get());

		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(7);
		arch.getRegistersList().get(0).store();
		arch.getIntbus1().put(3);
		arch.getRegistersList().get(1).store(); 

		arch.jlw();

		arch.getPC().read();
		assertEquals(34, arch.getExtbus1().get());

		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getIntbus1().put(5);
		arch.getRegistersList().get(0).store(); 
		arch.getRegistersList().get(1).store(); 

		arch.jlw();

		arch.getPC().read();
		assertEquals(34, arch.getExtbus1().get());
	}
	
	
	@Test
	public void  testCall() {
		
		ArchitectureD3 arch = new ArchitectureD3();

		
		arch.getMemory().getDataList()[31] = 100; 
			
		arch.getExtbus1().put(30);
		arch.getPC().store();

	
		arch.getExtbus1().put(125);
		arch.getRegistersList().get(7).store();
		arch.getRegistersList().get(8).store();
	
	arch.call();
	
		arch.getPC().read();
		assertEquals(100, arch.getExtbus1().get());

		 arch.getRegistersList().get(8).read();
		 assertEquals(125, arch.getExtbus1().get());

		 arch.getRegistersList().get(7).read();
		 assertEquals(124, arch.getExtbus1().get());
			

	arch.getExtbus1().put(125);
    arch.getMemory().read();
    assertEquals(32, arch.getExtbus1().get());
			
	}
	
	@Test
	public void testRet() {
		
		ArchitectureD3 arch = new ArchitectureD3();

		
		arch.getMemory().getDataList()[125] = 32;  

		arch.getExtbus1().put(124);
		arch.getRegistersList().get(7).store(); // StkTOP ← 125

		arch.getExtbus1().put(125);
		arch.getRegistersList().get(8).store(); // StkBOT ← 124

		arch.getExtbus1().put(100);
		arch.getPC().store(); // PC ← 100

		arch.ret();

		arch.getPC().read();
		assertEquals(32, arch.getExtbus1().get()); // espera o valor 32

		arch.getRegistersList().get(8).read();
		assertEquals(125, arch.getExtbus1().get()); 

		arch.getRegistersList().get(7).read();
		assertEquals(125, arch.getExtbus1().get());
	}
	
	@Test
	public void teststartStk() {
		ArchitectureD3 arch = new ArchitectureD3();

		arch.getMemory().getDataList()[31] = 125;
		
		arch.getExtbus1().put(30);
		arch.getPC().store();

		arch.getExtbus1().put(31);
	    arch.getRegistersList().get(7).store();
	    arch.getRegistersList().get(8).store();

		arch.startStk();

	    arch.getRegistersList().get(7).read();
	    assertEquals(125, arch.getExtbus1().get());
	    arch.getRegistersList().get(8).read();
	    assertEquals(125, arch.getExtbus1().get());


		arch.getPC().read();
		assertEquals(32, arch.getExtbus1().get());
	}
}
