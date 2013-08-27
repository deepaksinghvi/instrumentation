package com.deepak.instrumentation;

import java.lang.instrument.Instrumentation;

/**
 * @author dsinghvi
 *
 */
public class InstrumentationAgent {
	public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new InstrumentationClassFileTransformer());
	}
}
