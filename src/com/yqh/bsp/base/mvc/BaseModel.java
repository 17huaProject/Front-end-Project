package com.yqh.bsp.base.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

public abstract class BaseModel<M extends Model<M>> extends Model<M> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6752322255000744526L;

	/**
	 * 获取表映射对象
	 * 
	 * @return
	 */
	protected Table getTable() {
		return TableMapping.me().getTable(getClass());
	}

	/**
	 * 获取表名称
	 * 
	 * @return
	 */
	protected String getTableName() {
		return getTable().getName();
	}

	/**
	 * 获取主键名称
	 * @return
	 */
	public String[] getPKNameArr(){
		return getTable().getPrimaryKey();
	}

	/**
	 * 获取主键名称
	 * @return
	 */
	public String getPKNameStr(){
		String[] pkArr = getPKNameArr();
		if(pkArr.length == 1){
			return pkArr[0];
		}else{
			String pk = "";
			for (String pkName : pkArr) {
				pk += pkName + ",";
			}
			return pk;
		}
		
	}

	/**
	 * 获取主键值：非复合主键
	 * @return
	 */
	public String getPKValue(){
		String[] pkNameArr = getTable().getPrimaryKey();
		if(pkNameArr.length == 1){
			if (pkNameArr[0] instanceof String) {
				return this.getStr(pkNameArr[0]);
			} else {
				return this.getLong(pkNameArr[0]).toString();
			}
		}else{
			String pk = "";
			for (String pkName : pkNameArr) {
				pk += this.get(pkName) + ",";
			}
			return pk;
		}
	}

	/**
	 * 获取主键值：复合主键
	 * @return
	 */
	public List<Object> getPKValueList(){
		String[] pkNameArr = getTable().getPrimaryKey();
		
		List<Object> pkValueList = new ArrayList<Object>();
		for (String pkName : pkNameArr) {
			pkValueList.add(this.get(pkName));
		}
		
		return pkValueList;
	}
	
	
	
	/**
	 * 返回获取数据Map对象
	 * @return
	 */
	public Map<String ,Object> getMap() {
		return this.getAttrs();
	}
	

}
