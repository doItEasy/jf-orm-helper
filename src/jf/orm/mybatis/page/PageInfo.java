package jf.orm.mybatis.page;


/*
 
* 文件名称: PageInfo.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-6-29
 * 修改内容: 
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;


/**
 * 分页信息对象�?
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class PageInfo<T> extends RowBounds {

    public static final int DEFAULT_PAGE_NO = 1;

    public static final int DEFAULT_PAGE_SIZE = 3;
    public static final String COUNT_COLUMN_ALIAS = "ROW_COUNT_ASDFG_ASDFG_";
    /** 当前�?*/
    private int pageNo = DEFAULT_PAGE_NO;

    /** 每页记录条数 */
    private int pageSize = DEFAULT_PAGE_SIZE;

    /** 总页�?*/
    private int totalPage = 0;
    
    /** 总页�?*/
    private int totalCount = 0;

    /**
     * 当前页中存放的记�?类型�?��为List.
     */
    protected List<T> result =new ArrayList<T>();

    /** 分页导航�?*/
    private String navigation;

    /** 默认构�?函数 */
    public PageInfo() {
        super();
    }

    /** 构�?函数 */
    public PageInfo(int pageNo, int pageSize) {
        super(pageNo, pageSize);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    
    
    public void initNavigation(){
        if(null == result||0 == result.size())
            return;
        StringBuffer pathfind = new StringBuffer("");

        pathfind.append("<ul class=\"pagination\">");
        if(isHasPre()){
            pathfind.append("<li ><a href=\"javascript:jumpPage("+ this.getPrePage() + ");\">«</a></li>");
        }else {
            pathfind.append("<li  class=\"disabled\"><a>«</a></li>");
        }


        //固定显示页码
        int baseNum = 7;
        int betweenNum = 3;
        //加载页码
        int m=1;
        int n=(int) this.getTotalPage();
        if(n>=baseNum){
            if(this.getPageNo()-betweenNum<=0){
                m=1;
                n=baseNum;
            }else{
                m=this.getPageNo()-betweenNum;
                n=this.getPageNo()+betweenNum;
                if(n>=this.getTotalPage()){
                    m = (int) (m - (n - this.getTotalPage()));
                    n=(int) this.getTotalPage();
                }
            }
        }

        if(m==2){
            pathfind.append("<li><a class=\"active\" href=\"javascript:jumpPage(1);\">1</a></li>");

        }
        if(m>2){
            pathfind.append("<li><a href=\"javascript:jumpPage(1);\">1</a></li>" +
                    "<li><a>...</a></li>");
        }
        for(int i=m;i<=n;i++){
            if(i==this.getPageNo()){
                pathfind.append("<li class=\"active\"><a>"+i+"</a></li>");
            }else{
                pathfind.append("<li><a href=\"javascript:jumpPage("+i+");\" >"+i+"</a></li>");
            }
        }
        if(this.getPageNo()!=this.getTotalPage() && this.getTotalPage()>baseNum && (this.getTotalPage()-this.getPageNo())>betweenNum){
            if(this.getTotalPage()==(this.getPageNo()+betweenNum+1)){
                pathfind.append("<li ><a  href=\"jumpPage("+this.getTotalPage()+");\" >"+this.getTotalPage()+"</a></li>");
            }else{
                pathfind.append("<li><a>...</a></li>"+"<li > <a href=\"javascript:jumpPage("+this.getTotalPage()+");\" >"+this.getTotalPage()+"</a></li>");
            }
        }
        if(isHasNext()){
            pathfind.append("<li><a href=\"javascript:jumpPage("+ this.getNextPage() + ");\">»</a></li>");
        }else{
            pathfind.append("<li class=\"disabled\"><a>»</a></li>");
        }
        pathfind.append("</ul>");
        navigation = pathfind.toString();
        //initViewid();
    }
    
    /**
     * 初始化导航条
     */
    public void initNavigation2() {
        if (null == result || 0 == result.size())
            return;
        StringBuffer pathfind = new StringBuffer("");

        pathfind.append("<table width=\"100%\" border=0 cellspacing=0 cellpadding=0>");
        pathfind.append("<tr>");
        pathfind.append("<td align='left'><div align=\"left\"><span class=\"pageStyle\">&nbsp;&nbsp;&nbsp;&nbsp;共有<strong> "
                        + totalCount
                        + "</strong> 条记录，当前�?input onkeydown=\"if(event.keyCode==13)jumpToPage(this)\" onKeyup=\"value=value.replace(/[^\\d]/g,'')\" onbeforepaste=\"clipboardData.setData('text',clipboardData.getData('text').replace(/[^\\d]/g,''))\" type='text' id='toPageNum' style='width: 25px;' value=' "
                        + getPageNo()
                        + "'/>  页，�?<strong id='totalPage'>"
                        + getTotalPage()
                        + "</strong> �?/span>"
                        + "</div></td>");
        pathfind.append("<td align='right'><table width=\"200\" border=\"0\" align=\"right\" cellpadding=\"0\" cellspacing=\"0\">");
        pathfind.append("<tr>");

        if (getTotalPage() == 1 || getPageNo() == 1) {
            pathfind.append("<td width=\"42\"><div align=\"center\"><img src=\"" 
                    + "/images/first_dis.gif\" width=\"40\" height=\"15\"/></div></td>");
        }
        else {
            pathfind.append("<td width=\"42\"><div align=\"center\"><img src=\"" 
                    + "/images/first.gif\" width=\"40\" height=\"15\" onclick=\"javascript:jumpPage(" + 1
                    + ")\" style=\"cursor:hand\"/></div></td>");
        }

        if (isHasPre()) {
            pathfind.append("<td width=\"42\"><div align=\"center\"><img src=\"" 
                    + "/images/pre.gif\" width=\"45\" height=\"15\" onclick=\"javascript:jumpPage(" + this.getPrePage()
                    + ")\" style=\"cursor:hand\" /></div></td>");
        }
        else {
            pathfind.append("<td width=\"42\"><div align=\"center\"><img src=\"" 
                    + "/images/pre_dis.gif\" width=\"45\" height=\"15\"  /></div></td>");
        }
        if (isHasNext()) {
            pathfind.append("<td width=\"42\"><div align=\"center\"><img src=\"" 
                    + "/images/next.gif\" width=\"45\" height=\"15\" onclick=\"javascript:jumpPage("
                    + this.getNextPage() + ")\" style=\"cursor:hand\"/></div></td>");
        }
        else {
            pathfind.append("<td width=\"42\"><div align=\"center\"><img src=\"" 
                    + "/images/next_dis.gif\" width=\"45\" height=\"15\" /></div></td>");
        }

        if (getTotalPage() <= 1 || getTotalPage() == getPageNo()) {
            pathfind.append("<td width=\"42\"><div align=\"center\"><img src=\"" 
                    + "/images/last_dis.gif\" width=\"40\" height=\"15\" /></div></td>");
        }
        else {
            pathfind.append("<td width=\"42\"><div align=\"center\"><img src=\"" 
                    + "/images/last.gif\" width=\"40\" height=\"15\" onclick=\"javascript:jumpPage(" + getTotalPage()
                    + ")\" style=\"cursor:hand\"/></div></td>");
        }

        pathfind.append("</tr>");
        pathfind.append("</table></td>");
        pathfind.append("</tr>");
        pathfind.append("</table>");

        this.navigation = pathfind.toString();
    }

    /**
     * 是否还有上一�?
     */
    private boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     * 是否还有下一�?
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPage());
    }

    /**
     * 取得上页的页�? 序号�?�?��. 当前页为首页时返回首页序�?
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        }
        else {
            return pageNo;
        }
    }

    /**
     * 取得下页的页�? 序号�?�?��. 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        }
        else {
            return pageNo;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("PageInfo{").append("  pageNo=").append(pageNo).append(", pageSize=").append(pageSize).append(
                ", totalCount=").append(totalCount).append('}');
        return sb.toString();
    }
 

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        reCalcTotalCount(totalCount, pageSize);
    }

    private void reCalcTotalCount(int totalCount, int pageSize) {
        if (pageSize != 0) {
            totalPage = totalCount / pageSize;
            if (totalCount % pageSize > 0) {
                totalPage++;
            }
        }
    }

    // -------------------------------- 以下为Getter/Setter方法 -------------------------------- //

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo > 0) {
            this.pageNo = pageNo;
        }
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
            reCalcTotalCount(totalCount, pageSize);
        }
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public int getOffset() {
		return pageNo;
	}

	@Override
	public int getLimit() {
		return pageSize;
	}

    
    
}
