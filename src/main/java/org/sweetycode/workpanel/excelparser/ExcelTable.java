package org.sweetycode.workpanel.excelparser;

import java.util.ArrayList;

public class ExcelTable {
    private String createTableText = "";
    private String tableName = "";
    private String tableComment = "";
    private ArrayList<String> columnList = new ArrayList();
    private ArrayList<String> columnTypeList = new ArrayList();
    private ArrayList<String> columnCommentList = new ArrayList();

    public String getCreateTableText() {
        return createTableText;
    }

    public void setCreateTableText(String createTableText) {
        this.createTableText = createTableText;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public ArrayList<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(ArrayList<String> columnList) {
        this.columnList = columnList;
    }

    public ArrayList<String> getColumnTypeList() {
        return columnTypeList;
    }

    public void setColumnTypeList(ArrayList<String> columnTypeList) {
        this.columnTypeList = columnTypeList;
    }

    public ArrayList<String> getColumnCommentList() {
        return columnCommentList;
    }

    public void setColumnCommentList(ArrayList<String> columnCommentList) {
        this.columnCommentList = columnCommentList;
    }
}
