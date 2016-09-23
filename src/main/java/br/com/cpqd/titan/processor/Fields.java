package br.com.cpqd.titan.processor;

/**
 * 
 * @author glabbate
 *
 */
public class Fields
{
	private int size;
	private String function;
	private String value;
	private String name;
	private String variable;

	public final String getVariable() {
		return variable;
	}

	public final void setVariable(String variable) {
		this.variable = variable;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 */
	public Fields() {
		super();
	}

	public final int getSize() {
		return size;
	}

	public final void setSize(int size) {
		this.size = size;
	}

	public final String getFunction() {
		return function;
	}

	public final void setFunction(String function) {
		this.function = function;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}

}
