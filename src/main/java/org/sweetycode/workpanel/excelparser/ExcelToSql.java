package org.sweetycode.workpanel.excelparser;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;

public class ExcelToSql {
    private ArrayList<ExcelTable> excelTables = new ArrayList<ExcelTable>();

    public ExcelToSql(String excelText) {
        excelText = excelText.trim();

        ExcelTable excelTable = new ExcelTable();
        ArrayList<String> columnList = new ArrayList();
        ArrayList<String> columnTypeList = new ArrayList();
        ArrayList<String> columnCommentList = new ArrayList();

        String[] lines = excelText.split("\\n");
        for ( String line: lines ) {
            if (line.equals("\t\t\t\t")) {
                excelTable.setColumnList(columnList);
                excelTable.setColumnTypeList(columnTypeList);
                excelTable.setColumnCommentList(columnCommentList);
                excelTables.add(excelTable);
                excelTable = new ExcelTable();
                columnList = new ArrayList();
                columnTypeList = new ArrayList();
                columnCommentList = new ArrayList();
                continue;
            }
            String[] cells = line.split("\\t");
            if (cells[0].equals("") && cells[1].equals("")) {
                columnList.add(cells[2]);
                columnTypeList.add(cells[3]);
                columnCommentList.add(cells[4]);
            } else {
                excelTable.setTableName(cells[0]);
                excelTable.setTableComment(cells[1]);
                columnList.add(cells[2]);
                columnTypeList.add(cells[3]);
                columnCommentList.add(cells[4]);
            }
        }
        excelTable.setColumnList(columnList);
        excelTable.setColumnTypeList(columnTypeList);
        excelTable.setColumnCommentList(columnCommentList);
        excelTables.add(excelTable);
    }

    public String excelTextToXml (CheckBox agg1,CheckBox agg2,CheckBox agg3,CheckBox agg4,CheckBox agg5,CheckBox agg6,CheckBox allselect) {

        String actionXml="";
        for (ExcelTable excelTable : excelTables) {
            ArrayList<String> columnList = excelTable.getColumnList();
            columnList.remove("CHANNEL_NAME");
            if (allselect.isIndeterminate()) {
                ArrayList<String> var1 = new ArrayList<String>();
                var1.add("AREA_ID");var1.add("GROUP_ID");var1.add("CHANNEL_ID");var1.add("SUB_CHANNEL_ID");var1.add("PLATFORM");
                if(agg1.isSelected()) {
                    var1.remove("PLATFORM"); var1.remove("CHANNEL_ID");
                }
                if(agg2.isSelected()) {
                    var1.remove("PLATFORM"); var1.remove("AREA_ID");
                }
                if(agg3.isSelected()) {
                    var1.remove("PLATFORM");
                }
                if(agg4.isSelected()) {
                    var1.remove("AREA_ID");
                }
                if(agg5.isSelected()) {
                    var1.remove("AREA_ID"); var1.remove("GROUP_ID");
                }
                if(agg6.isSelected()) {
                    var1.remove("PLATFORM"); var1.remove("CHANNEL_ID"); var1.remove("SUB_CHANNEL_ID");
                }
                for (String item : var1) {
                    columnList.remove(item);
                }
            } else {
                if (!allselect.isSelected()) {
                    columnList.remove("AREA_ID");
                    columnList.remove("GROUP_ID");
                    columnList.remove("CHANNEL_ID");
                    columnList.remove("SUB_CHANNEL_ID");
                    columnList.remove("PLATFORM");
                }
            }

            actionXml = actionXml + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "\n" +
                    "<command> \n" +
                    "  <job-scheduler>10.1.1.176:10008</job-scheduler>  \n" +
                    "  <name>/app/hadoop/dw-report/bin/dw_report_start.sh</name>  \n" +
                    "  <arg>#{dw.action.schedule.id}</arg>  \n" +
                    "  <arg>#{dw.action.app.id}</arg>  \n" +
                    "  <arg>#{dw.action.schedule.data.date}</arg>  \n" +
                    "  <arg>hive_dw</arg>  \n" +
                    "  <arg><![CDATA[\n" +
                    "]]></arg>  \n" +
                    "  <arg>oracle_rpt</arg>  \n" +
                    "  <arg>" + excelTable.getTableName() + columnList.toString().replaceAll(" ","").replace("[","(").replace("]",")") + "</arg>  \n" +
                    "  <arg>oracle_rpt</arg>  \n" +
                    "  <arg><![CDATA[\n" +
                    "update " + excelTable.getTableName() + " set PLATFORM='h5' where stat_date=to_date('${date}','YYYY-MM-DD') and PLATFORM='3';\n" +
                    "update " + excelTable.getTableName() + " set PLATFORM='ios' where stat_date=to_date('${date}','YYYY-MM-DD') and PLATFORM='2';\n" +
                    "update " + excelTable.getTableName() + " set PLATFORM='android' where stat_date=to_date('${date}','YYYY-MM-DD') and PLATFORM='1' ;\n" +
                    "]]></arg> \n" +
                    "</command>\n\n\n"  ;
        }

        return  actionXml;

    }

    public String excelTextToCreateTable (String type) {
        String createTableText="";
        for (ExcelTable excelTable : excelTables) {
            createTableText = createTableText + generateSql(excelTable, type) ;
        }
        return createTableText;
    }


    private String generateSql (ExcelTable excelTable, String type) {
        StringBuilder sb = new StringBuilder();

        if(type == "Oracle") {
            sb.append("CREATE TABLE ").append(excelTable.getTableName()).append("(").append("\n");
            int i;
            for (i = 0; i < excelTable.getColumnList().size() - 1; ++i) {
                sb.append((String) excelTable.getColumnList().get(i)).append("\t").append((String) excelTable.getColumnTypeList().get(i)).append(",").append("\n");
            }
            sb.append((String) excelTable.getColumnList().get(excelTable.getColumnList().size() - 1)).append("\t").append((String) excelTable.getColumnTypeList().get(excelTable.getColumnTypeList().size() - 1)).append("\n");
            sb.append(");").append("\n");
            sb.append("COMMENT ON TABLE ").append(excelTable.getTableName()).append(" IS '").append(excelTable.getTableComment()).append("';").append("\n");
            for (i = 0; i < excelTable.getColumnList().size(); ++i) {
                sb.append("COMMENT ON COLUMN ").append(excelTable.getTableName()).append(".").append((String) excelTable.getColumnList().get(i)).append(" IS '").append((String) excelTable.getColumnCommentList().get(i)).append("';").append("\n");
            }

            sb.append("grant select on " + excelTable.getTableName() + " to gdata_view; \n");
            sb.append("grant select on " + excelTable.getTableName() + " to rpt_viewer; \n\n");

        } else if (type == "Hive") {

            sb.append("create table `").append(excelTable.getTableName()).append("`(").append("\n");
            int i;
            for (i = 0; i < excelTable.getColumnList().size() - 1; ++i) {
                sb.append("`").append((String) excelTable.getColumnList().get(i)).append("` ").append((String) excelTable.getColumnTypeList().get(i)).append(" comment '").append((String) excelTable.getColumnCommentList().get(i)).append("',").append("\n");
            }
            sb.append("`").append((String) excelTable.getColumnList().get(excelTable.getColumnList().size() - 1)).append("`").append(" ").append((String) excelTable.getColumnTypeList().get(excelTable.getColumnTypeList().size() - 1)).append(" comment '").append((String) excelTable.getColumnCommentList().get(excelTable.getColumnCommentList().size()-1)).append("'").append("\n");
            sb.append(")\n").append("comment '").append(excelTable.getTableComment()).append("'\n").append("PARTITIONED BY(part_date STRING) STORED AS ORC TBLPROPERTIES('orc.compress'='SNAPPY');\n\n");

//                    .append("PARTITIONED BY (\n" +
//                    "`part_date` string)\n" +
//                    "ROW FORMAT SERDE\n" +
//                    "'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'\n" +
//                    "WITH SERDEPROPERTIES (\n" +
//                    "'field.delim'='\\t',\n" +
//                    "'serialization.format'='\\t')\n" +
//                    "STORED AS INPUTFORMAT\n" +
//                    "'org.apache.hadoop.mapred.SequenceFileInputFormat'\n" +
//                    "OUTPUTFORMAT\n" +
//                    "'org.apache.hadoop.mapred.SequenceFileOutputFormat'; \n\n");
        }

        return sb.toString();
    }

}

