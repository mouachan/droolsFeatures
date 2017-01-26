package com.redhat.droolsfeature.model;

public class BzrFlightMatchDeclare {
	private String name;
	private int lom;
	private BzrFlightDeclare parent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLom() {
		return lom;
	}

	public void setLom(int lom) {
		this.lom = lom;
	}

	/**
	 * @return the parent
	 */
	public BzrFlightDeclare getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(BzrFlightDeclare parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BzrFlightMatchDeclare [name=");
		builder.append(name);
		builder.append(", lom=");
		builder.append(lom);
		builder.append("]");
		return builder.toString();
	}
}
