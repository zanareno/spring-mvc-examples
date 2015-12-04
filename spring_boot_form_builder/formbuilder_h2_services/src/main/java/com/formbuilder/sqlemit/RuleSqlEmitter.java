package com.formbuilder.sqlemit;

public class RuleSqlEmitter extends SqlEmitter {

	@Override
	public void emit(String appName, String tableName, String[] column, String[] relationship, int orderBy, StringBuffer ddlScripts,
			StringBuffer dmlScripts) {
		ddlScripts.append(String.format("CREATE Table %s (", tableName) + System.lineSeparator());
		String nameColumn = "name";
		for (String col : column) {
			String[] st = col.split(":");
			if (st.length == 2) {
				nameColumn = st[1];
			}
		}

		int i = 0;
		for (String rel : relationship) {
			String[] st = rel.split(":");
			String relName = st.length == 1 ? st[0] : st[1] + "__" + st[0];
			if (i > 0) {
				ddlScripts.append(String.format(relIdColumn, relName));
			} else {
				ddlScripts.append(String.format(relIdColumn, relName) + " Not Null");
			}
			if (i != relationship.length - 1) {
				ddlScripts.append(",");
			}
			ddlScripts.append(System.lineSeparator());
			i++;
		}

		ddlScripts.append(closeCreate + System.lineSeparator());

		// Insert Script
		insertUiForm(dmlScripts, tableName, nameColumn, orderBy, "Rule");
	}
}