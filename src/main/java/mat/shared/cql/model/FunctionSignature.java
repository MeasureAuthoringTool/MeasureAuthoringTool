package mat.shared.cql.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class FunctionSignature implements IsSerializable {

	private String name;
	private String returnType;
	private List<FunctionArgument> arguments;
	private String documentation;
	
	public FunctionSignature() {
		this.setName("");
		this.setReturnType("");
		this.setArguments(new ArrayList<>());
		this.documentation = "";
	}
	
	public FunctionSignature(String name, String returnType, List<FunctionArgument> arguments, String documentation) {
		this.setName(name);
		this.setReturnType(returnType);
		this.setArguments(arguments);
		this.setDocumentation(documentation);
	}
	
	public String getSignature() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.name);
		builder.append("(");
		
		for(FunctionArgument argument : this.arguments) {
			builder.append(", ");
			builder.append(argument.getReturnType());
		}
		
		builder.append(")");
		return builder.toString().replaceFirst(", ", "");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<FunctionArgument> getArguments() {
		return arguments;
	}

	public void setArguments(List<FunctionArgument> arguments) {
		this.arguments = arguments;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentaion) {
		this.documentation = documentaion;
	}
}
