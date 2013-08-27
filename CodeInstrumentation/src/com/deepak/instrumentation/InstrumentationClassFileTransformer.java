package com.deepak.instrumentation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;

/**
 * @author dsinghvi
 *
 */
public class InstrumentationClassFileTransformer implements ClassFileTransformer {
	private static final List<String> list = new ArrayList<String>();
	static {
		String configLocation = System.getProperty("configuration");
		if (null != configLocation) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(configLocation + "conf/classinstrumentation.properties"));
				String readLine = br.readLine();
				while (readLine != null) {
					list.add(readLine);
					readLine = br.readLine();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] byteCode = new byte[0];
		if (list.contains(className)) {
			byteCode = classfileBuffer;
			try {
				ClassPool classPoolRef = ClassPool.getDefault();
				CtClass ctClazz = classPoolRef.get(className.replace('/', '.'));
				long counter = 0;
				CtMethod[] declaredMethods = ctClazz.getDeclaredMethods();
				for (CtMethod ctMethod : declaredMethods) {
					// do not instrument static methods
					if ((ctMethod.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
						continue;
					}
					String counterName = ctMethod.getName() + "counter";
					CtField counterfield = new CtField(CtClass.longType, counterName, ctClazz);
					counterfield.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
					ctClazz.addField(counterfield, CtField.Initializer.constant(counter));
					ctMethod.addLocalVariable("startTime", CtClass.longType);
					ctMethod.addLocalVariable("totalTime", CtClass.longType);
					ctMethod.insertBefore("startTime = System.currentTimeMillis();");
					ctMethod.insertAfter("" + 
							"{totalTime = System.currentTimeMillis() - startTime;" + 
							"System.out.println(\"Total execution time: \" + totalTime);" + 
							"java.lang.Class clazz = this.getClass(); " + 
							"String counterMethodName = Thread.currentThread().getStackTrace()[1].getMethodName() +\"counter\";" + 
							"java.lang.reflect.Field field = this.getClass().getField(counterMethodName); " + 
							"field.setLong(this,field.getLong(this)+1l);" + 
							"System.out.println(\"Total execution count: \"+field.getLong(this));" + "System.out.println(\"******************\");}");
				}
				byteCode = ctClazz.toBytecode();
				ctClazz.detach();
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		return byteCode;
	}
}
