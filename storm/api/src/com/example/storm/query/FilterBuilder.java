package com.example.storm.query;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.example.storm.SQLiteDao;
import com.example.storm.TableHelper.Column;
import com.example.storm.api.Persistable;
import com.example.storm.types.BooleanConverter;
import com.example.storm.types.ByteConverter;
import com.example.storm.types.CharConverter;
import com.example.storm.types.IntegerConverter;
import com.example.storm.types.LongConverter;
import com.example.storm.types.ShortConverter;

public class FilterBuilder {

	private static final String TAG = FilterBuilder.class.getName();
	private SQLiteDao<Persistable> dao;
	private List<Predicate> where = new ArrayList<Predicate>();
	
	public FilterBuilder(SQLiteDao<Persistable> dao) {
		this.dao = dao;
	}

	/*
	 * Convenience methods for comparing equality of each wrapper type
	 */

	public FilterBuilder eq(Column colName, Boolean param) {
		Integer sqlValue = BooleanConverter.GET.toSql(param);
		where.add(new Equality(colName, BooleanConverter.GET.toString(sqlValue)));
		return this;
	}

	public FilterBuilder eq(Column colName, Byte param) {
		Short sqlValue = ByteConverter.GET.toSql(param);
		where.add(new Equality(colName, ByteConverter.GET.toString(sqlValue)));
		return this;
	}

	public FilterBuilder eq(Column colName, byte[] param) {
		throw new IllegalArgumentException("Exact match on type byte[] is not supported");
	}

	public FilterBuilder eq(Column colName, Character param) {
		Integer sqlValue = CharConverter.GET.toSql(param);
		where.add(new Equality(colName, CharConverter.GET.toString(sqlValue)));
		return this;
	}

	public FilterBuilder eq(Column colName, Double param) {
		throw new IllegalArgumentException("Exact match on type double is not supported");
	}

	public FilterBuilder eq(Column colName, Float param) {
		throw new IllegalArgumentException("Exact match on type float is not supported");
	}

	public FilterBuilder eq(Column colName, Integer param) {
		where.add(new Equality(colName, IntegerConverter.GET.toString(param)));
		return this;
	}
	
	public FilterBuilder eq(Column colName, Long param) {
		where.add(new Equality(colName, LongConverter.GET.toString(param)));
		return this;
	}

	public FilterBuilder eq(Column colName, Short param) {
		where.add(new Equality(colName, ShortConverter.GET.toString(param)));
		return this;
	}

	public FilterBuilder eq(Column colName, String param) {
		where.add(new Equality(colName, param));
		return this;
	}

	/**
	 * Execute the query using the attached DAO
	 * 
	 * @return Cursor result
	 */
	public Cursor exec() {
		return dao.query(where(), params());
	}
	
	/**
	 * Convert the params in each predicate to String[]
	 * used by the query methods
	 * 
	 * @return String[] parameters
	 */
	private String[] params() {
		String[] params = new String[this.where.size()];
		for (int i = 0; i < params.length; i++) {
			Predicate p = this.where.get(i);
			params[i] = p.getParam();
		}
		return params;
	}

	/**
	 * Convert the SQL conditions in each predicate by ANDing together
	 * into a single SQL WHERE clause with ? for each parameter
	 * 
	 * @return String SQL WHERE clause
	 */
	private String where() {
		StringBuilder sqlWhere = new StringBuilder();
		for (Predicate p : where) {
			sqlWhere.append(" AND ");
			sqlWhere.append(p.getSqlOp() + "?");
		}
		return sqlWhere.toString().substring(5);
	}
	
}
