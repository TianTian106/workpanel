package org.sweetycode.workpanel;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.sweetycode.workpanel.datapreview.HbaseService;
import org.sweetycode.workpanel.excelparser.ExcelToSql;

import java.io.IOException;

public class WorkController {
    //  <!--这里的Button对象有需要加@FXML注解，然后变量的名称为你刚才在FXML文件中声明的Button的id属性-->
    @FXML
    private TextArea textExcel;
    @FXML
    private TextArea textSqlOracle;
    @FXML
    private TextArea textSqlHive;
    @FXML
    private TextArea textXml;
    @FXML
    private CheckBox agg1;
    @FXML
    private CheckBox agg2;
    @FXML
    private CheckBox agg3;
    @FXML
    private CheckBox agg4;
    @FXML
    private CheckBox agg5;
    @FXML
    private CheckBox agg6;
    @FXML
    private CheckBox allselect;
    //  <!--这里的handleButtonAction方法为我们在FXML文件中声明的onAction的处理函数-->
    @FXML
    private TextField textHbaseSql;
    @FXML
    private TextArea textHbase;

    @FXML
    protected void readExcel(ActionEvent event) {
        String inputText = textExcel.getText();
        ExcelToSql excelToSql = new ExcelToSql(inputText) ;
        textSqlOracle.setText(excelToSql.excelTextToCreateTable("Oracle"));
        textSqlHive.setText(excelToSql.excelTextToCreateTable("Hive"));
        textXml.setText(excelToSql.excelTextToXml(agg1,agg2,agg3,agg4,agg5,agg6,allselect));
    }

    @FXML
    protected void searchHbase(ActionEvent event) {
        try {
            String result =
                    HbaseService.searchHbase(textHbaseSql.getText());
            textHbase.setText(result);
        } catch (IOException e) {
            textHbase.setText("IOException" + e);
        }

//        JsonParser jsonParser = new JsonParser();
//        JsonElement tt  = jsonParser.parse(test);
//        ObservableList data = FXCollections.observableArrayList(tt);
//        JsonConverter ds1 = new JsonConverter(test);
//        tableHbase.setItems(test);
//        DataSourceReader dsr1 = new FileReader("foo.xml");

    }
}