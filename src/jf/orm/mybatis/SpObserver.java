package jf.orm.mybatis;


/*
 * 文件名称: SpObserver1.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-7-2
 * 修改内容: 
 */


/**
 * 线程�?��变量模拟请求上下文�?
 * 
 * @author Allen.Hu / 2012-7-2
 * @since SkyMarket 1.0
 */
public class SpObserver {

    /** 线程�?��变量模拟请求上下�? 用于多数据源切换时映射的数据源标�? */
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
    
    /**
     * 添加数据源标识�?
     * 
     * @param dsFlag
     * @author Allen.Hu / 2012-7-2 
     * @since SkyMarket 1.0
     */
    public static void putDsFlag(String dsFlag) {
        threadLocal.set(dsFlag);
    }
    
    /**
     * 获得数据源标识�?
     * 
     * @return
     * @author Allen.Hu / 2012-7-2 
     * @since SkyMarket 1.0
     */
    public static String getDsFlag() {
        return threadLocal.get();
    }
    
    /**
     * 移除数据源标识�?
     * 
     * @author Allen.Hu / 2012-7-2 
     * @since SkyMarket 1.0
     */
    public static void clearDsFlag() {
        threadLocal.remove();
    }
}
