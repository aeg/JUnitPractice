package chapter12;

import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.DataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlTable implements ITable {

    private ITableMetaData _metaData;
    private List<Map> _rows;


    public YamlTable(String tableName, List<String> rows) {

        _rows = new ArrayList<Map>();

        if (rows != null && rows.size() > 0 ) {
            _metaData = createMetaData(tableName, rows);
        } else {
            _metaData = new DefaultTableMetaData(tableName, new Column[0]);
        }
    }

    private ITableMetaData createMetaData(String tableName, List<String> rows) {

        List columnList = new ArrayList();
        for (String columnName: rows) {
            Column column;
            column = new Column(columnName, DataType.UNKNOWN);
            columnList.add(column);
        }
        Column[] columns = (Column []) columnList.toArray(new Column[0]);
        return new DefaultTableMetaData(tableName, columns);
    }

    @Override
    public ITableMetaData getTableMetaData() {
        return _metaData;
    }

    @Override
    public int getRowCount() {
        return _rows.size();
    }

    @Override
    public Object getValue(int row, String column) throws DataSetException {
        if (getRowCount() <= row) {
            throw new RowOutOfBoundsException("" + row);
        } else {
            return _rows.get(row).get(column.toUpperCase());
        }
    }

    public void addRow(Map values){
        _rows.add(convertMap(values));
    }

    Map convertMap(Map<String, Object> values){
        Map ret = new HashMap();
        for (Map.Entry<String, Object> ent: values.entrySet()){
            ret.put(ent.getKey().toUpperCase(), ent.getValue());
        }
        return ret;
    }
}
