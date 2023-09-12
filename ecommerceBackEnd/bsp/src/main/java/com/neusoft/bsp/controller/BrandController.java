package com.neusoft.bsp.controller;

import com.neusoft.bsp.admin.user.po.User;
import com.neusoft.bsp.admin.user.service.UserService;

import com.neusoft.bsp.business.vo.BrandWithBrdId;
import com.neusoft.bsp.business.vo.BrandWithUserId;
import com.neusoft.bsp.common.base.BaseController;
import com.neusoft.bsp.business.seller.service.BrandService;
import com.neusoft.bsp.common.base.BaseModel;
import com.neusoft.bsp.common.base.BaseModelJson;
import com.neusoft.bsp.common.exception.BusinessException;
import com.neusoft.bsp.common.validationGroup.InsertGroup;
import com.neusoft.bsp.common.validationGroup.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.neusoft.bsp.business.po.Brand;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import static java.time.LocalDate.now;

@CrossOrigin
@RestController
@RequestMapping("/brd")
public class BrandController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    BrandService brandService;

    @PostMapping("/getBrand")
    public BaseModelJson<List<Brand>> getBrand(@RequestBody User userget){
        int user_id = userget.getUser_id();
        User user = userService.getById(user_id);
        BaseModelJson<List<Brand>> response = new BaseModelJson();
        if(user==null){
            throw BusinessException.USERNAME_NOT_EXISTS;
        }
        int man_id = user.getMan_buyer_id();
        response.code = 200;
        List<Brand> list;
        if(man_id != 0){
            HashMap<String, Object> res = new HashMap<>();
            list = brandService.getAllById(man_id);
            response.data = list;
        }
        return response;
    }

    @PostMapping("/addBrand")
    public BaseModel addBrand(@Validated({InsertGroup.class}) @RequestBody BrandWithUserId brdu, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw BusinessException.INSERT_FAIL.newInstance(this.getErrorResponse(bindingResult),
                    new Object[]{brdu.toString()});
        }else{
            BaseModel result = new BaseModel();
            Brand brd = new Brand(brdu.getName_en(),brdu.getRemark());
            String name = userService.getById(brdu.getUser_id()).getName();
            int manid = userService.getById(brdu.getUser_id()).getMan_buyer_id();
            brd.setMan_id(manid);
            brd.setCreated_by(name);
            brd.setLast_update_by(name);
            brd.setLast_update_date(Date.valueOf(now()));
            brd.setCreation_date(Date.valueOf(now()));
            brd.setCall_cnt(0);
//            brd.setRemark();
////          brd.setSts_cd();
            int result_brd = brandService.insert(brd);
            if (result_brd != 1) {
                throw BusinessException.INSERT_FAIL;
            }
            else{
                result.code = 200;
                return result;
            }
        }
    }

    @PostMapping("/deleteBrand")
    public BaseModel deleteBrand(@RequestBody Brand brd) {
        int brd_id = brd.getBrd_id();
        int i = brandService.delete(brd_id);
        BaseModel result = new BaseModel();
        if(i!=1){
            throw BusinessException.DELETE_FAIL;
        }else{
            result.code = 200;
            return result;
        }
    }


    @PostMapping("/updateBrand")
    public BaseModel updateBrand(@Validated({UpdateGroup.class}) @RequestBody BrandWithBrdId brdu, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {    //传值错误
            throw BusinessException.UPDATE_FAIL.newInstance(this.getErrorResponse(bindingResult),
                    new Object[]{brdu.toString()});
        }else{
            BaseModel result = new BaseModel();
            Brand brand = brandService.getById(brdu.getBrd_id());
            String name = userService.getById(brdu.getUser_id()).getName();
            brand.setLast_update_date(Date.valueOf(now()));
            brand.setLast_update_by(name);
            brand.setName_en(brdu.getName_en());
            brand.setRemark(brdu.getRemark());
            int i =brandService.update(brand);
            if(i!=1){
                throw BusinessException.UPDATE_FAIL;
            }else{
                result.code = 200;
                return result;
            }
        }
    }

}