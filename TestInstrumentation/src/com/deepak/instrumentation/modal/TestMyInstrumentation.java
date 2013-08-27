package com.deepak.instrumentation.modal;

/**
 * @author dsinghvi
 *
 */
public class TestMyInstrumentation {
	public void method1() {
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("In TestMyInstrumentation for method1");
	}

	public void method2() {
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("In TestMyInstrumentation for method2");
	}

	public static void method3() {
		try {
			Thread.sleep(2500);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("In TestMyInstrumentation for static method3");
	}
}
