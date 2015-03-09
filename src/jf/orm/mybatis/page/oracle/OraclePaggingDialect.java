package jf.orm.mybatis.page.oracle;

import jf.orm.mybatis.page.DefaultPaggingDialect;




public class OraclePaggingDialect extends DefaultPaggingDialect {

    /**
     * (non-Javadoc)
     * 
     * @see com.skymobi.commons.dao.page.PaggingDialect#getPaggingSql(java.lang.String, int, int)
     */
    @Override
    public String getPaggingSql(String querySql, int pageNo, int pageSize) {
        int myPageNo = (pageNo > 0 ? pageNo : 1);
        int myPageSize = (pageSize > 0 ? pageSize : 10);
        int begin = (myPageNo - 1) * myPageSize + 1;
        int end = begin + myPageSize;

        querySql = querySql.trim();
        boolean isForUpdate = false;
        if (querySql.toLowerCase().endsWith(" for update")) {
            querySql = querySql.substring(0, querySql.length() - 11);
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer(querySql.length() + 100);
        pagingSelect.append("SELECT * FROM ( SELECT row_.*, rownum rownum_ FROM ( ");
        pagingSelect.append(querySql);
        pagingSelect.append(" ) row_ WHERE rownum < " + end + ") WHERE rownum_ >= " + begin);

        if (isForUpdate) {
            pagingSelect.append(" for update");
        }

        return pagingSelect.toString();
    }

}
