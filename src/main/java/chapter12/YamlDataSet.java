package chapter12;

import org.dbunit.dataset.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlDataSet extends AbstractDataSet {

    private OrderedTableNameMap _tables;

//    public YamlDataSet(File file) throws IOException, DataSetException {
//        this(new FileInputStream(file));
//    }

    public YamlDataSet(InputStream in) throws IOException, DataSetException {
        _tables = super.createTableNameMap();

        Yaml yaml = new Yaml();

        Map map = (HashMap) yaml.load(in);
        for (Object ent : map.entrySet()) {
            Map.Entry<List, Object> entry = (Map.Entry<List, Object>) ent;
            String tableName = String.valueOf(entry.getKey());
            List<Map> rows = (List<Map>) entry.getValue();
            if (rows == null) {
                rows = new ArrayList<Map>();
            }
            ITable table = createTable(tableName, rows);

            _tables.add(table.getTableMetaData().getTableName(), table);

        }
    }

//    public YamlDataSet(String fileName) throws IOException, DataSetException {
//        this(getClass().getResourceAsStream(fileName));
//        this(new FileInputStream(fileName));
//    }

    YamlTable createTable(String tableName, List<Map> rows) {
        YamlTable table = new YamlTable(tableName, rows.size() > 0 ?
                new ArrayList(rows.get(0).keySet()) : null);
        for (Map values : rows)
            table.addRow(values);
        return table;
    }

    @Override
    protected ITableIterator createIterator(boolean reversed) throws DataSetException {
        ITable[] tables = (ITable[]) _tables.orderedValues().toArray(new ITable[0]);
        return new DefaultTableIterator(tables, reversed);
    }
}


