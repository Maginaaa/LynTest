package com.lyntest.controller;

import com.lyntest.bean.*;
import com.lyntest.service.CollectionManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 简单随风
 * @date 2019/10/25
 */
@Slf4j
@Controller
@RequestMapping(value="/collection")
public class CollectionController {

    @Autowired
    private CollectionManageService collectionManageService;

    /**
     * 查询关注集合列表
     */
    @ResponseBody
    @GetMapping(value = "/focuslist")
    public ResponseVo getFocusList(){

        return collectionManageService.getFocusCollectionList();
    }

    /**
     * 获取创建人列表
     */
    @ResponseBody
    @GetMapping(value = "/creater")
    public ResponseVo getCreaterList(){

        return collectionManageService.getCollectionCreaterList();
    }

    /**
     * 查询所有集合列表
     */
    @ResponseBody
    @PostMapping(value = "/list")
    public ResponseVo getAllList(@RequestBody CollectionList collectionList){

        return collectionManageService.getCollectionList(collectionList);
    }

    /** 创建新集合 */
    @ResponseBody
    @PostMapping(value = "/create")
    public ResponseVo createCollection(@RequestBody CollectionDetail collectionDetail){

        return  collectionManageService.createNewCollection(collectionDetail);

    }

    /** 创建新集合 */
    @ResponseBody
    @PostMapping(value = "/delete")
    public ResponseVo deleteCollection(@RequestBody Integer collectionId){

        return  collectionManageService.deleteCollection(collectionId);

    }


    /** 查询集合详情 */
    @ResponseBody
    @PostMapping(value = "/detail")
    public ResponseVo getCollectionDetailById(@RequestBody Integer collectionId){

        return collectionManageService.getCollectionDetail(collectionId);
    }

    /** 关注\取消关注 */
    @ResponseBody
    @PostMapping(value = "/focus")
    public ResponseVo getCollectionDetailById(@RequestBody CollectionFocus collectionFocus){

        return collectionManageService.collectionFocus(collectionFocus);
    }

    /** 修改集合信息 */
    @ResponseBody
    @PostMapping(value = "/edit")
    public ResponseVo editCollection(@RequestBody CollectionDetail collectionDetail){

        ResponseVo responseVo = new ResponseVo();
        if (collectionDetail.getCollectionName().isEmpty()){
            responseVo.setIsSuccess(Boolean.FALSE);
            responseVo.setMsg("集合名称不能为空");
            return responseVo;
        }

        return collectionManageService.updateCollectionInfo(collectionDetail);
    }

    /** 新增集合变量 */
    @ResponseBody
    @PostMapping(value = "/variable/insert")
    public ResponseVo insertVariable (@RequestBody Variable variable){

        ResponseVo responseVo = new ResponseVo();

        if (variable.getCollectionId() == null){
            responseVo.setIsSuccess(Boolean.FALSE);
            responseVo.setMsg("collectionId");
        }
        return collectionManageService.insertCollectionVariable(variable);
    }

    /** 删除集合变量 */
    @ResponseBody
    @PostMapping(value = "/variable/delete")
    public ResponseVo deleteVariable (@RequestBody Variable variable){

        return collectionManageService.deleteCollectionVariable(variable.getId());
    }

    /** 修改集合变量 */
    @ResponseBody
    @PostMapping(value = "/variable/update")
    public ResponseVo updateVariable (@RequestBody Variable variable){

        return collectionManageService.updateCollectionVariable(variable);
    }

    /** 修改集合的case顺序 */
    @ResponseBody
    @PostMapping(value = "/case/list")
    public ResponseVo getApiList(@RequestBody CollectionCaseList collectionCaseList){

        return collectionManageService.getCollectionCaseList(collectionCaseList.getCollectionId());
    }


    /** 修改集合的api顺序 */
    @ResponseBody
    @PostMapping(value = "/case/order")
    public ResponseVo updateApiOrder(@RequestBody CollectionCaseList collectionCaseList){

        return collectionManageService.updateCollectionCaseOrder(collectionCaseList);
    }

    /** 删除一条api */
    @ResponseBody
    @PostMapping(value = "/case/delete")
    public ResponseVo deleteApiInCollection(@RequestBody CommonCase commonCase){

        return collectionManageService.deleteCaseInCollection(commonCase);
    }

    /** 测试执行 */
    @ResponseBody
    @PostMapping(value = "/run")
    public ResponseVo collectionExcute(@RequestBody Integer collectionId){

        return collectionManageService.collectionExcute(collectionId);
    }

    /** 获取执行记录 */
    @ResponseBody
    @PostMapping(value = "/records")
    public ResponseVo getCollectionExcuteRecords(@RequestBody CollectionList collectionList){

        return collectionManageService.collectionExcuteRecords(collectionList);
    }

}
