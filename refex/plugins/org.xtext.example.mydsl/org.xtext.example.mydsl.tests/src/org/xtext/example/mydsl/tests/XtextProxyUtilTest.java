package org.xtext.example.mydsl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtext.example.mydsl.MyDslInjectorProvider;
import org.xtext.example.mydsl.myDsl.Greeting;
import org.xtext.example.mydsl.myDsl.Model;

import com.temenos.ds.op.xtext.util.XtextProxyUtil;

/**
 * Integration Test illustrating the use of XtextProxyUtil.
 *  
 * @author Michael Vorburger
 */
@RunWith(XtextRunner.class)
@InjectWith(MyDslInjectorProvider.class)
public class XtextProxyUtilTest {

	@Inject XtextProxyUtil proxyUtil;
	@Inject ParseHelper<Model> ph;
	
	@Test
	public void testGetProxyCrossRefAsString() throws Exception {
		Model m = ph.parse("Hello world! REF world REF nada");
		Greeting worldGreeting = m.getGreetings().get(0);
		assertEquals("world", worldGreeting.getName());
		
		assertEquals(worldGreeting, m.getGreetings().get(1).getGreetingRef());
		
		Greeting proxyRef = m.getGreetings().get(2).getGreetingRef();
		assertTrue(proxyRef.eIsProxy());
		String text = proxyUtil.getProxyCrossRefAsString(proxyRef);
		assertEquals("some.thing.non.existant", text);
	}

}
