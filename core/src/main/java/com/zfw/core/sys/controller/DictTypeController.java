package com.zfw.core.sys.controller;


import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.Validate;
import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.entity.DictType;
import com.zfw.core.sys.service.IDictDataService;
import com.zfw.core.sys.service.IDictTypeService;
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
public class DictTypeController extends BaseController {
    @Autowired
    private IDictTypeService iDictTypeService;
    @Autowired
    private IDictDataService iDictDataService;

    @ApiOperation(value = "字典类型列表", notes = "返回字典类型列表信息")
    @GetMapping("dictTypes")
    public JSONObject dictTypes(){
        return success(iDictTypeService.findAll(Sort.Order.asc("sort")));
    }

    @ApiOperation(value = "通过id获取单个字典类型", notes = "返回单个字典类型信息")
    @GetMapping("dictType/{id}")
    @Validate
    public JSONObject getDictType(@PathVariable("id") Integer id){
        if(id==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        return success(iDictTypeService.getById(id));
    }

    @ApiOperation(value = "创建字典类型", notes = "返回创建字典类型信息")
    @PostMapping("dictType")
    @Validate
    public JSONObject createDictType(DictType dictType){
        if (iDictTypeService.existsByTypeName(dictType.getTypeName())){
            throw new GlobalException("此字典类型已存在，请勿重复添加");
        }
        return success(iDictTypeService.save(dictType));
    }



    @ApiOperation(value = "修改字典类型", notes = "返回修改字典类型信息")
    @PutMapping("dictType")
    public JSONObject updateDictType(DictType dictType){
        if(dictType.getId()==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        return success(iDictTypeService.save(dictType));
    }


    @ApiOperation(value = "通过id删除字典类型", notes = "删除字典类型信息")
    @DeleteMapping("dictType/{id}")
    public JSONObject deleteDictType(@PathVariable("id") Integer id){
        if(id==null){
            throw new GlobalException(Constant.NOT_NULL_ID);
        }
        DictType dictType = iDictTypeService.getById(id);
        if (iDictDataService.existsByTypeName(dictType.getTypeName())){
            throw new GlobalException("当前字典下有数据，禁止删除");
        }
        iDictTypeService.deleteById(id);
        return success();
    }

}
