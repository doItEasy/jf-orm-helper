package jf.orm.mybatis;

/*
 * 文件名称: DataBaseTypes.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-6-29
 * 修改内容: 
 */

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;


/**
 * 数据库类型常量定义�?
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class DataBaseTypes {

    public static final String ORACLE = "oracle";

    public static final String POSTGRESQL = "postgresql"; 
    
    /** 不知道的数据库类�?*/
    public static final String UNKNOWDB = "UnKnowDBType";
    
    /**
     * 根据数据源驱动获得数据库类型�?
     * 
     * @param driver 驱动�?
     * @return
     * @author Allen.Hu / 2012-6-29 
     * @since SkyMarket 1.0
     */
    public static String getTypeByDriverClassName(String driver) {
        if (driver.toUpperCase().indexOf("ORACLE") >= 0) 
            return DataBaseTypes.ORACLE;
        else if (driver.toUpperCase().indexOf("POSTGRESQL") >= 0)
            return DataBaseTypes.POSTGRESQL;
        else
            return DataBaseTypes.UNKNOWDB;
    }
    
    /**
     * 根据数据源对象获得该数据库的类型�?
     * 
     * @param dataSource 数据�?
     * @return 数据库类型的字符串枚举，请参考本类各数据库常量�?�?
     */
    public final static String getDataSourceType(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            String driverName = meta.getDriverName();
            String dsType = getTypeByDriverClassName(driverName);
            return dsType.toLowerCase();
        }
        catch (SQLException e) {
        }
        finally {
        	try {
				conn.close();
			} catch (SQLException e) {
			}
        }
        return "";
    }
}
