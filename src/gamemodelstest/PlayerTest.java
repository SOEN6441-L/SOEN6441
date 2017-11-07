package gameelementstest;

/*import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	private Player player;
	@Before
	public void set(){
		player = new Player("A",null);
		System.out.println("Begin testing");
	}
	
	@Test
	public void testAddCard() {
		player.addCard(0);
		int [] cards = new int [3];
		player.getCards(cards);
		int [] expect = {1,0,0};
		boolean result = Arrays.equals(expect,cards);
		assertTrue(result);
		System.out.println("add cards success");
	}
	
	@Test
	public void testifForceExchange(){
		for(int i = 0; i < 5; i++){
			player.addCard(0);
		}
		boolean result = player.ifForceExchange();
		assertTrue(result);
		System.out.println("if force Exchange sucess");	
	}
	
	
	@Test
	public void testCanExchange(){
		for(int i = 0; i < 5; i++){
			player.addCard(0);
		}
		int [] cards = new int [3];
		player.getCards(cards);
		boolean result = player.canExchange(cards);
		assertTrue(result);
		System.out.println("if test can exchange sucess");	
	}
	
}
*/