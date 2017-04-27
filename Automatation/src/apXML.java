import java.io.File;

public class apXML {
	public static void main(String[] args) {
		args = new String[]{"C:\\Users\\s24267\\Downloads\\installation_instruction.xml"};
		new Approver(new File(String.join(" ",args).replace("\\", "\\\\")));
	}
}
