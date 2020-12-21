package com.zfw.core.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.Validate;
import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.DictData;
import com.zfw.core.sys.service.IDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
/**
* @Author: zfw
* @Date: 2020-07-28
* @Content: 字典类型控制器
*/
@Api(tags = "字典类型相关接口")
@Controller
public class DictDataController extends BaseController {
    @Autowired
    private IDictDataService iDictDataService;

    @ApiOperation(value = "字典类型列表", notes = "返回字典类型列表信息")
    @GetMapping("dictDatas")
    public JSONObject dictDatas(){
        return success(iDictDataService.findAll(Sort.Order.asc("sort")));
    }

    @ApiOperation(value = "通过id获取单个字典类型", notes = "返回单个字典类型信息")
    @GetMapping("dictData/typeName/{typeName}")
    @Validate
    public JSONObject getDictData(@PathVariable("typeName") String typeName){
        return success(iDictDataService.findByTypeNameOrderBySortAsc(typeName));
    }

    @ApiOperation(value = "通过id获取单个字典类型", notes = "返回单个字典类型信息")
    @GetMapping("dictData/{id}")
    @Validate
    public JSONObject getDictDataById(@PathVariable("id") Integer id){
        if(id==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        return success(iDictDataService.getById(id));
    }

    @ApiOperation(value = "创建字典类型", notes = "返回创建字典类型信息")
    @PostMapping("dictData")
    @Validate
    public JSONObject createDictData(DictData dictData){
        if (iDictDataService.existsByValueAndTypeName(dictData.getValue(),dictData.getTypeName(),dictData.getId())){
            throw new GlobalException(dictData.getTypeName()+"此字典类型下已经有值为："+dictData.getValue());
        }
        return success(iDictDataService.save(dictData));
    }



    @ApiOperation(value = "修改字典类型", notes = "返回修改字典类型信息")
    @PutMapping("dictData")
    public JSONObject updateDictData(DictData dictData){
        if (iDictDataService.existsByValueAndTypeName(dictData.getValue(),dictData.getTypeName(),dictData.getId())){
            throw new GlobalException(dictData.getTypeName()+"此字典类型下已经有值为："+dictData.getValue());
        }
        if(dictData.getId()==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }

        return success(iDictDataService.save(dictData));
    }

    @ApiOperation(value = "通过id删除字典类型", notes = "删除字典类型信息")
    @DeleteMapping("dictData/{id}")
    public JSONObject deleteDictData(@PathVariable("id") Integer id){
        DictData dictData = iDictDataService.getById(id);
        if (dictData.getIsSys()==0){
            throw new GlobalException("系统级数据，禁止删除");
        }

        if(id==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        iDictDataService.deleteById(id);
        return success();
    }

}
