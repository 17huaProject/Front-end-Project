package com.yqh.bsp.base.mvc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.ModelBuilder;

public class BaseModelBuilder extends ModelBuilder {
	
	@SuppressWarnings({"rawtypes"})
	public static final List<Map<String,Object>> buildMap(ResultSet rs, Class<? extends BaseModel> modelClass) throws SQLException, InstantiationException, IllegalAccessException {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		String[] labelNames = new String[columnCount + 1];
		int[] types = new int[columnCount + 1];
		buildAttrNamesAndTypes(rsmd, labelNames, types);
		while (rs.next()) {
			BaseModel<?> ar = modelClass.newInstance();
			Map<String, Object> attrs = ar.getMap();
			for (int i=1; i<=columnCount; i++) {
				Object value;
				if (types[i] < Types.BLOB)
					value = rs.getObject(i);
				else if (types[i] == Types.CLOB)
					value = handleClob(rs.getClob(i));
				else if (types[i] == Types.NCLOB)
					value = handleClob(rs.getNClob(i));
				else if (types[i] == Types.BLOB)
					value = handleBlob(rs.getBlob(i));
				else
					value = rs.getObject(i);
				
				attrs.put(labelNames[i], value);
			}
			result.add(attrs);
		}
		return result;
	}
	
	private static final void buildAttrNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
		for (int i=1; i<labelNames.length; i++) {
			labelNames[i] = rsmd.getColumnLabel(i);
			types[i] = rsmd.getColumnType(i);
		}
	}

}
