package com.lyntest.utils;

import com.lyntest.bean.CommonCase;

import java.util.Iterator;
import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/8
 */
public class CaseDataProvider implements Iterator<Object[]> {

    /** 查询结果集 */
    List<CommonCase> caseList;

    /** 总行数 */
    private int rowNum=0;
    /** 当前行数 */
    private int curRowNo=0;

    public CaseDataProvider(List<CommonCase> cases){

        this.caseList = cases;
        this.rowNum = caseList.size();

    }

    @Override
    public boolean hasNext() {
        if(rowNum==0||curRowNo>=rowNum){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public Object[] next() {
        CommonCase commonCase = caseList.get(curRowNo);
        Object[] o=new Object[1];
        o[0]= commonCase;
        this.curRowNo++;
        return o;
    }
}
