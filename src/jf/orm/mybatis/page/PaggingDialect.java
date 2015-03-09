package jf.orm.mybatis.page;


/*
 * 文件名称: PaggingDialect.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-6-29
 * 修改内容: 
 */

/**
 * 数据库查询分页方�?? 
 * MyBatis 默认采用基于数据库游标的内存分页，效率不高，并且当数据量非常庞大的时候，
 * 会�?成内存溢出问题�?
 * 实现此接口可提供物理分页�?
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public interface PaggingDialect {

    /**
     * 根据传入的查询SQL语句以及�?��获取的分页信息，�?��返回分页SQL语句�?
     * 
     * @param querySql �?��分页查询的SQL语句
     * @param pageNo 请求�?
     * @param pageSize 每页记录�?
     * @return 分页查询SQL
     * @author Allen.Hu / 2012-6-29
     * @since SkyMarket 1.0
     */
    public String getPaggingSql(String querySql, int pageNo, int pageSize);

    /**
     * 获得总数的SQL语句�?
     * 
     * @param querySql
     * @return
     * @author Allen.Hu / 2012-6-29
     * @since SkyMarket 1.0
     */
    public String getCountSql(String querySql);
}
