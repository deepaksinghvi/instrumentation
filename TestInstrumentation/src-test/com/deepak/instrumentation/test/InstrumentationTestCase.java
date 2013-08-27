package com.deepak.instrumentation.test;

import com.deepak.instrumentation.modal.TestMyInstrumentation;

import junit.framework.TestCase;

/**
 * @author dsinghvi
 *
 */
public class InstrumentationTestCase extends TestCase {
	public void testMethod1() throws Exception {
		TestMyInstrumentation testMyInstrumentation = new TestMyInstrumentation();
		try {
			testMyInstrumentation.method1();
			testMyInstrumentation.method2();
			testMyInstrumentation.method1();
			testMyInstrumentation.method1();
			TestMyInstrumentation.method3();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

