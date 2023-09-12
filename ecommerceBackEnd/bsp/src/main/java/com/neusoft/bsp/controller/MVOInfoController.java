package com.neusoft.bsp.controller;

import com.neusoft.bsp.business.po.Company;
import com.neusoft.bsp.business.vo.CompanyWithManID;
import com.neusoft.bsp.business.vo.CompanyWithUserId;
import com.neusoft.bsp.common.validationGroup.InsertGroup;
import com.neusoft.bsp.common.validationGroup.UpdateGroup;
import com.neusoft.bsp.business.seller.service.CompanyService;
import com.neusoft.bsp.common.base.BaseController;
import com.neusoft.bsp.common.base.BaseModel;
import com.neusoft.bsp.common.base.BaseModelJson;
import com.neusoft.bsp.common.exception.BusinessException;
import com.neusoft.bsp.admin.user.po.User;
import com.neusoft.bsp.admin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

import static java.time.LocalDate.now;

@CrossOrigin
@RestController
@RequestMapping("/mvo")
public class MVOInfoController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;


    @PostMapping("/addCompany")
    public BaseModel addCompany(@Validated({InsertGroup.class}) @RequestBody CompanyWithUserId manu, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw BusinessException.INSERT_FAIL.newInstance(this.getErrorResponse(bindingResult),
                    new Object[]{manu.toString()});
        }else{
            BaseModel result = new BaseModel();
            Company company = new Company(manu.getName_en(),manu.getGmc_report_type(),manu.getGmc_report_url(),manu.getDescription());
            String name = userService.getById(manu.getUser_id()).getName();
            company.setCreated_by(name);
            company.setLast_update_by(name);
            company.setLast_update_date(Date.valueOf(now()));
            company.setCreation_date(Date.valueOf(now()));
            int i = companyService.insert(company);

            int j = companyService.updateUserWithLastManu(manu.getUser_id());
            if (i != 1) {
                throw BusinessException.INSERT_FAIL;
            }
            else if(j != 1){
                throw BusinessException.UPDATE_FAIL; }
            else{
                result.code = 200;
                return result;
            }
        }
    }

    @PostMapping("/getCompanyByUserID")
    public BaseModelJson<Company> getCompanyByUserID(@RequestBody User us) {
        User user = userService.getById(us.getUser_id());
        BaseModelJson<Company> result = new BaseModelJson<>();
        if(user==null){
            throw BusinessException.USERNAME_NOT_EXISTS;
        }
        int companyID = user.getMan_buyer_id();

        if(companyID != 0){
            result.setSuccess();
            result.data = companyService.getById(companyID);
        }else{
            throw BusinessException.NO_MAN_ID;
        }
        return result;
    }

    @PostMapping("/updateCompany")
    public BaseModel updateCompany(@Validated({UpdateGroup.class}) @RequestBody CompanyWithManID manu, BindingResult bindingResult) {  //bindingResult用于获得validate的反馈信息

        if (bindingResult.hasErrors()) {
            throw BusinessException.UPDATE_FAIL.newInstance(this.getErrorResponse(bindingResult),
                    new Object[]{manu.toString()});
        }else{
            BaseModel result = new BaseModel();
            Company company = new Company(manu.getMan_id(),manu.getName_en(),manu.getGmc_report_type(),manu.getGmc_report_url(),manu.getDescription());
            company.setLast_update_date(Date.valueOf(now()));
            int i = companyService.update(company);
            if(i==1){
                result.code = 200;
                return result;
            }else{
                throw BusinessException.UPDATE_FAIL;
            }
            }
    }

    @PostMapping("/deleteCompany")
    public BaseModel deleteCompany(@RequestBody int man_id) {

        int i = companyService.delete(man_id);
        int j = companyService.updateUserWithManuID(man_id);
        BaseModel result = new BaseModel();
        if(i==1 && j==1){
            result.code = 200;
            return result;
        }else{
            throw BusinessException.DELETE_FAIL;
        }
    }

}
