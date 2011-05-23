import java.net.InetAddress;
import org.lb.plc.*;
import org.lb.plc.tpy.*;

public class Main implements VariableObserver {
	@Override
	public void variableChanged(String variable, Object newValue) {
		System.out.println("Variable " + variable + " has a new value: "
				+ newValue);
	}

	public static void main(String args[]) throws Exception {
		//final VariableLocator locator = new VariableLocator(
		//		new VariableExpander(new TpyFile("Test.tpy")).getVariables());

		// final InetAddress destIp = InetAddress.getByName("192.168.1.2");
		// final String destNetId = "1.2.3.4.1.1";
		// final String sourceNetId = "192.168.1.1.1.1";
		//
		// final SimplePlcInterface conn = new SimplePlcInterface(destIp,
		// sourceNetId, 32768, destNetId, 801, 1000, locator, new Main());
		// System.out.println("Connection established");
		//
		// System.out.println("PLC device version: " + conn.getDeviceVersion());
		// System.out.println("Reading a variable: " + conn.getVariableAsInteger(".variable.name"));
		//
		// conn.close();
	}
}
