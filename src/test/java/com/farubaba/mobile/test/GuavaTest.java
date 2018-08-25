package com.farubaba.mobile.test;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.farubaba.mobile.demo.GuavaDemo;

public class GuavaTest {
	private GuavaDemo guavaDemo;
	@Before
	public void setUp(){
		guavaDemo = new GuavaDemo();
	}
	
	@Test
	public void testListMultiMap(){
		List<String> list = guavaDemo.testListMultiMap();
		for(String s : list){
			System.out.println(s);
		}
	}
	
	@Test
	public void testSetMultiMap(){
		Set<String> set = guavaDemo.tesSetMultiMap();
		for(String s : set){
			System.out.println(s);
		}
	}
}
