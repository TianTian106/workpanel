package org.sweetycode.workpanel.datapreview;

import org.sweetycode.workpanel.util.HttpClientUtil;
import org.sweetycode.workpanel.util.JsonUtil;
import org.sweetycode.workpanel.util.MyFileReader;

import java.io.IOException;
import java.net.URLEncoder;

public class HbaseService {

    public static String searchHbase(String sql) throws IOException {
        sql = URLEncoder.encode(sql,"UTF-8");
        String subject = HttpClientUtil
                .doGet(MyFileReader.readConf("etc/file.properties","HBASE_PREFIX") + sql, null);
        return JsonUtil.formatJson(subject);
    }
}
